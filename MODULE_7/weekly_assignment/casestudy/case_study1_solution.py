"""
Case Study 1: Credit Risk & Loan Portfolio Analysis

Creates merged dataset from customers, loans and credit score files,
cleans data, computes portfolio metrics, identifies high-risk customers
and writes `risk_report.xlsx`, `high_risk_customers.csv`, and `summary.json`.

Usage:
    python casestudy/case_study1_solution.py

Adjust file paths at top if your CSV filenames differ.
"""
import json
from dataclasses import dataclass
from typing import Optional

import numpy as np
import pandas as pd


# --------------------------- Configuration ---------------------------
BASE_DIR = "./casestudy"  # relative to workspace root
CUSTOMERS_CSV = f"{BASE_DIR}/customers.csv"
LOANS_CSV = f"{BASE_DIR}/loans.csv"
CREDIT_CSV = f"{BASE_DIR}/credit_scores.csv"

OUTPUT_RISK_XLSX = f"{BASE_DIR}/risk_report.xlsx"
OUTPUT_HIGH_RISK = f"{BASE_DIR}/high_risk_customers.csv"
OUTPUT_SUMMARY = f"{BASE_DIR}/summary.json"


def read_csv_safe(path: str) -> Optional[pd.DataFrame]:
    """Read CSV with basic corrupted-file handling."""
    try:
        # use modern on_bad_lines to skip malformed rows if needed
        df = pd.read_csv(path, on_bad_lines='skip')
        return df
    except pd.errors.EmptyDataError:
        print(f"Warning: {path} is empty.")
        return None
    except pd.errors.ParserError:
        print(f"Warning: {path} appears corrupted — attempting fallback read with engine='python'.")
        try:
            return pd.read_csv(path, engine="python", on_bad_lines='skip')
        except Exception as e:
            print(f"Failed fallback read for {path}: {e}")
            return None
    except FileNotFoundError:
        print(f"Warning: {path} not found.")
        return None
    except Exception as e:
        print(f"Failed reading {path}: {e}")
        return None


@dataclass
class Loan:
    loan_id: Optional[int]
    customer_id: Optional[int]
    loan_amount: float
    interest_rate: float  # annual in percent
    tenure_months: int = 60

    def monthly_rate(self) -> float:
        return self.interest_rate / 100.0 / 12.0

    def emi(self) -> float:
        r = self.monthly_rate()
        n = max(1, int(self.tenure_months))
        if r == 0:
            return self.loan_amount / n
        return self.loan_amount * r * (1 + r) ** n / ((1 + r) ** n - 1)


def standardize_columns(df: pd.DataFrame) -> pd.DataFrame:
    """Lowercase column names and strip spaces for easier heuristics."""
    df = df.copy()
    df.columns = [c.strip().lower().replace(" ", "_") for c in df.columns]
    return df


def find_common_key(dfs):
    # find an intersection column name to join on
    common = None
    cols_sets = [set(df.columns) for df in dfs if df is not None]
    if not cols_sets:
        return None
    inter = set.intersection(*cols_sets)
    # prefer typical names
    for candidate in ["customer_id", "cust_id", "id", "customerid", "custid"]:
        if candidate in inter:
            return candidate
    # fallback to any column that appears in all
    return next(iter(inter)) if inter else None


def load_and_merge():
    c_df = read_csv_safe(CUSTOMERS_CSV)
    l_df = read_csv_safe(LOANS_CSV)
    cr_df = read_csv_safe(CREDIT_CSV)

    if c_df is None and l_df is None and cr_df is None:
        raise RuntimeError("No input data available.")

    # standardize column names early so heuristics work reliably
    c_df = standardize_columns(c_df) if c_df is not None else None
    l_df = standardize_columns(l_df) if l_df is not None else None
    cr_df = standardize_columns(cr_df) if cr_df is not None else None

    # pick join key
    dfs = [df for df in (c_df, l_df, cr_df) if df is not None]
    key = find_common_key(dfs)
    if key is None:
        raise RuntimeError("Could not determine a common join key to merge files.")

    merged = None
    # merge progressively
    if c_df is not None:
        merged = c_df.copy()
        merged_cols = set(merged.columns)
    if l_df is not None:
        if merged is None:
            merged = l_df.copy()
        else:
            merged = pd.merge(merged, l_df, on=key, how="inner")
    if cr_df is not None:
        merged = pd.merge(merged, cr_df, on=key, how="left")

    # try to map handful of standard columns if present
    # standard names used below: customer_id, loan_amount, salary, credit_score, interest_rate, default_flag, tenure_months
    col_map = {}
    for c in merged.columns:
        if c in ("customer_id", "cust_id", "id"):
            col_map[c] = "customer_id"
        if c in ("loan_amount", "loanamt", "amount"):
            col_map[c] = "loan_amount"
        if c in ("salary", "annual_salary", "income"):
            col_map[c] = "salary"
        if c in ("credit_score", "creditscore", "score"):
            col_map[c] = "credit_score"
        if c in ("interest_rate", "interest", "rate"):
            col_map[c] = "interest_rate"
        if c in ("default_flag", "default", "is_default"):
            col_map[c] = "default_flag"
        if c in ("tenure_months", "tenure", "loan_tenure"):
            col_map[c] = "tenure_months"
    merged = merged.rename(columns=col_map)

    return merged


def clean_and_impute(df: pd.DataFrame) -> pd.DataFrame:
    df = df.copy()
    # Salary -> median
    if "salary" in df.columns:
        median_salary = df["salary"].median(skipna=True)
        df["salary"] = df["salary"].fillna(median_salary)
    # CreditScore -> mean
    if "credit_score" in df.columns:
        mean_cs = df["credit_score"].mean(skipna=True)
        df["credit_score"] = df["credit_score"].fillna(mean_cs)
    # Interest Rate -> previous value (ffill)
    if "interest_rate" in df.columns:
        df["interest_rate"] = df["interest_rate"].fillna(method="ffill").fillna(method="bfill")

    # Ensure numeric types
    for col in ["loan_amount", "salary", "credit_score", "interest_rate"]:
        if col in df.columns:
            df[col] = pd.to_numeric(df[col], errors="coerce")

    # Drop rows still missing essential numeric values
    essential = [c for c in ["loan_amount", "salary"] if c in df.columns]
    if essential:
        df = df.dropna(subset=essential)

    return df


def remove_outliers(df: pd.DataFrame) -> pd.DataFrame:
    df = df.copy()
    if "loan_amount" in df.columns:
        thresh = df["loan_amount"].quantile(0.99)
        df = df[df["loan_amount"] <= thresh]
    return df


def compute_numpy_metrics(df: pd.DataFrame) -> dict:
    res = {}
    arr = df
    if "loan_amount" in arr.columns:
        res["mean_loan_amount"] = float(np.mean(arr["loan_amount"]))
        try:
            res["std_loan_amount"] = float(np.std(arr["loan_amount"], ddof=1))
        except Exception:
            res["std_loan_amount"] = None
    if "salary" in arr.columns:
        res["median_salary"] = float(np.median(arr["salary"]))
        try:
            res["std_salary"] = float(np.std(arr["salary"], ddof=1))
        except Exception:
            res["std_salary"] = None
    if "interest_rate" in arr.columns:
        res["percentile_interest_rate_90"] = float(np.percentile(arr["interest_rate"].dropna(), 90))
    if set(["salary", "loan_amount"]).issubset(arr.columns):
        try:
            if len(arr) >= 2:
                res["corr_salary_loanamount"] = float(np.corrcoef(arr["salary"], arr["loan_amount"])[0, 1])
            else:
                res["corr_salary_loanamount"] = None
        except Exception:
            res["corr_salary_loanamount"] = None
    return res


def compute_finance_metrics(df: pd.DataFrame) -> pd.DataFrame:
    df = df.copy()
    # Debt-to-Income Ratio: total loan_amount / salary
    if "salary" in df.columns and "loan_amount" in df.columns:
        df["dti"] = df["loan_amount"] / df["salary"]
    else:
        df["dti"] = np.nan

    # LoanUtilization: without sanctioned amount, assume utilization = 1.0 or compute if both present
    if "sanctioned_amount" in df.columns:
        df["loan_utilization"] = df["loan_amount"] / df["sanctioned_amount"]
    else:
        df["loan_utilization"] = 1.0

    # Default % and NPA% (interpret default_flag==1 as default)
    if "default_flag" in df.columns:
        default_pct = float(df["default_flag"].mean())
    else:
        default_pct = float((df.get("credit_score", pd.Series([])) < 650).mean())

    # Average EMI: need tenure and interest_rate
    def calc_emi_row(r):
        try:
            tenure = int(r.get("tenure_months", 60))
        except Exception:
            tenure = 60
        loan = Loan(None, None, float(r.get("loan_amount", 0)), float(r.get("interest_rate", 0)), tenure)
        return loan.emi()

    df["emi"] = df.apply(calc_emi_row, axis=1)

    # Expected Loss: estimate PD by credit score buckets, LGD assume 0.45, EAD = loan_amount
    def pd_from_score(score):
        if pd.isna(score):
            return 0.02
        score = float(score)
        if score < 550:
            return 0.2
        if score < 600:
            return 0.1
        if score < 650:
            return 0.05
        if score < 700:
            return 0.02
        return 0.01

    lgd = 0.45
    df["pd"] = df.get("credit_score", pd.Series(np.nan)).apply(pd_from_score)
    df["expected_loss"] = df["pd"] * lgd * df.get("loan_amount", 0)

    metrics = {
        "default_pct": default_pct,
        "npa_pct": default_pct,  # simplified - using default as NPA for this exercise
        "average_emi": float(df["emi"].mean()) if "emi" in df.columns else None,
        "total_expected_loss": float(df["expected_loss"].sum()),
    }
    return df, metrics


def find_high_risk(df: pd.DataFrame, top_n: int = 20) -> pd.DataFrame:
    # define a risk score combining PD, dti, loan_amount, and default flag
    df = df.copy()
    df["risk_score"] = 0
    if "pd" in df.columns:
        df["risk_score"] += df["pd"] * 100
    if "dti" in df.columns:
        df["risk_score"] += df["dti"] * 10
    if "loan_amount" in df.columns:
        # normalize by 99th percentile to keep scale
        p99 = df["loan_amount"].quantile(0.99)
        p99 = p99 if p99 > 0 else 1
        df["risk_score"] += df["loan_amount"] / p99 * 20
    if "default_flag" in df.columns:
        df["risk_score"] += df["default_flag"] * 50

    top = df.sort_values("risk_score", ascending=False).head(top_n)
    return top


def generate_reports(df: pd.DataFrame, numpy_metrics: dict, finance_metrics: dict):
    # high risk customers
    high_risk = find_high_risk(df, 20)
    # write outputs
    high_risk.to_csv(OUTPUT_HIGH_RISK, index=False)

    # risk report as excel with multiple sheets
    with pd.ExcelWriter(OUTPUT_RISK_XLSX) as writer:
        df.head(1000).to_excel(writer, sheet_name="sample_data", index=False)
        high_risk.to_excel(writer, sheet_name="high_risk_customers", index=False)
        pd.DataFrame([numpy_metrics]).to_excel(writer, sheet_name="numpy_metrics", index=False)
        pd.DataFrame([finance_metrics]).to_excel(writer, sheet_name="finance_metrics", index=False)

    # summary json
    summary = {**numpy_metrics, **finance_metrics}
    with open(OUTPUT_SUMMARY, "w") as f:
        json.dump(summary, f, indent=2)

    print(f"Wrote {OUTPUT_HIGH_RISK}, {OUTPUT_RISK_XLSX}, {OUTPUT_SUMMARY}")


def main():
    merged = load_and_merge()
    merged = clean_and_impute(merged)
    merged = remove_outliers(merged)
    numpy_metrics = compute_numpy_metrics(merged)
    merged, finance_metrics = compute_finance_metrics(merged)
    # top 20 risky customers
    high_risk = find_high_risk(merged, 20)
    # also find customers matching filters
    filters = {}
    if "credit_score" in merged.columns:
        filters["credit_score_lt_650"] = merged[merged["credit_score"] < 650]
    if "salary" in merged.columns:
        filters["salary_lt_60000"] = merged[merged["salary"] < 60000]
    if "loan_amount" in merged.columns:
        filters["loan_gt_1000000"] = merged[merged["loan_amount"] > 1000000]
    if "default_flag" in merged.columns:
        filters["defaults"] = merged[merged["default_flag"] == 1]

    # generate outputs
    generate_reports(merged, numpy_metrics, finance_metrics)

    print("Top 20 risky customers: ")
    print(high_risk[[c for c in ["customer_id", "loan_amount", "credit_score", "dti", "risk_score"] if c in high_risk.columns]])


if __name__ == "__main__":
    main()
