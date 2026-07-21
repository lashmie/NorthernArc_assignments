import pandas as pd
import numpy as np

# ======================================================
# PART 1 - READ DATA
# ======================================================

try:
    funds = pd.read_csv("funds.csv")
    investors = pd.read_csv("investors.csv")
    transactions = pd.read_csv("transactions.csv")
    nav = pd.read_csv("nav_history.csv")

    print("All files loaded successfully.")

except FileNotFoundError as e:
    print("File not found:", e)
    exit()

except Exception as e:
    print(e)
    exit()


# ======================================================
# PART 2 - DATA CLEANING
# ======================================================

# Remove duplicates
funds.drop_duplicates(inplace=True)
investors.drop_duplicates(inplace=True)
transactions.drop_duplicates(inplace=True)
nav.drop_duplicates(inplace=True)

# Missing values
print("\nMissing Values")
print(funds.isnull().sum())
print(investors.isnull().sum())
print(transactions.isnull().sum())
print(nav.isnull().sum())

# Fill missing NAV
# normalize column names and ensure LatestNAV is present
nav.columns = nav.columns.str.strip()
if "LatestNAV" not in nav.columns:
    # try to find a close match
    cand = [c for c in nav.columns if "nav" in c.lower()]
    if cand:
        nav = nav.rename(columns={cand[0]: "LatestNAV"})

nav["LatestNAV"] = nav["LatestNAV"].ffill()

# Replace InvestorType
investors["InvestorType"] = investors["InvestorType"].fillna("Retail")

# Remove negative NAV
nav = nav[nav["LatestNAV"] >= 0]

# Date conversion
transactions["PurchaseDate"] = pd.to_datetime(
    transactions["PurchaseDate"],
    errors="coerce"
)

nav["Date"] = pd.to_datetime(
    nav["Date"],
    errors="coerce"
)


# ======================================================
# PART 3 - MERGE DATA
# ======================================================

# Latest NAV per Fund: sort by FundID then Date so group tail(1) selects latest per fund
latest_nav = nav.sort_values(["FundID", "Date"]).groupby("FundID").tail(1)

merged = transactions.merge(
    investors,
    on="InvestorID",
    how="left"
)

merged = merged.merge(
    funds,
    on="FundID",
    how="left"
)

merged = merged.merge(
    latest_nav[["FundID","LatestNAV"]],
    on="FundID",
    how="left"
)

print("\nMerged Data")
print(merged.head())


# ======================================================
# PART 4 - NEW COLUMNS
# ======================================================

merged["InvestmentAmount"] = (
    merged["UnitsPurchased"] *
    merged["PurchaseNAV"]
)

merged["CurrentValue"] = (
    merged["UnitsPurchased"] *
    merged["LatestNAV"]
)

merged["Profit"] = (
    merged["CurrentValue"] -
    merged["InvestmentAmount"]
)

merged["ROI"] = (
    (
        merged["CurrentValue"] -
        merged["InvestmentAmount"]
    )
    /
    merged["InvestmentAmount"]
) * 100


print("\nCalculated Columns")
print(merged.head())


# ======================================================
# PART 5 - NUMPY ANALYSIS
# ======================================================

nav_values = merged["LatestNAV"].to_numpy()

print("\nNumPy Analysis")

print("Average NAV :", np.mean(nav_values))
print("Maximum NAV :", np.max(nav_values))
print("Minimum NAV :", np.min(nav_values))
print("Variance :", np.var(nav_values))
print("Standard Deviation :", np.std(nav_values))

rolling_avg = pd.Series(nav_values).rolling(5).mean()

print("\nRolling Average")
print(rolling_avg)


# ======================================================
# PART 6 - PANDAS ANALYSIS
# ======================================================

print("\nTop 5 Investors")

top_investors = (
    merged.groupby("InvestorName")["InvestmentAmount"]
    .sum()
    .sort_values(ascending=False)
    .head(5)
)

print(top_investors)

print("\nTop 5 Profitable Funds")

top_funds = (
    merged.groupby("FundName")["Profit"]
    .sum()
    .sort_values(ascending=False)
    .head(5)
)

print(top_funds)

print("\nWorst Performing Fund")

worst = (
    merged.groupby("FundName")["Profit"]
    .sum()
    .sort_values()
    .head(1)
)

print(worst)

print("\nHighest NAV Fund")

highest_nav = (
    merged.groupby("FundName")["LatestNAV"]
    .max()
    .sort_values(ascending=False)
    .head(1)
)

print(highest_nav)

print("\nLowest NAV Fund")

lowest_nav = (
    merged.groupby("FundName")["LatestNAV"]
    .min()
    .sort_values()
    .head(1)
)

print(lowest_nav)


# ======================================================
# PART 7 - GROUP BY
# ======================================================

print("\nCategory Summary")

category_summary = merged.groupby("Category").agg(

    AverageROI=("ROI","mean"),
    AverageNAV=("LatestNAV","mean"),
    TotalInvestment=("InvestmentAmount","sum")

)

print(category_summary)

print("\nAMC Summary")

amc_summary = merged.groupby("AMC").agg(

    NumberOfFunds=("FundID","nunique"),
    AverageNAV=("LatestNAV","mean"),
    TotalInvestment=("InvestmentAmount","sum")

)

print(amc_summary)

print("\nState Summary")

state_summary = merged.groupby("State").agg(

    NumberOfInvestors=("InvestorID","nunique"),
    TotalInvestment=("InvestmentAmount","sum"),
    AverageROI=("ROI","mean")

)

print(state_summary)

print("\nInvestor Type Summary")

investor_summary = merged.groupby("InvestorType").agg(

    TotalInvestment=("InvestmentAmount","sum"),
    AverageProfit=("Profit","mean")

)

print(investor_summary)


# ======================================================
# PART 8 - ISSUE DETECTION
# ======================================================

print("\nDuplicate NAV Records")

duplicate_nav = nav[
    nav.duplicated(
        subset=["FundID","Date"],
        keep=False
    )
]

print(duplicate_nav)

print("\nNegative NAV")

negative_nav = nav[
    nav["LatestNAV"] < 0
]

print(negative_nav)

print("\nFuture Dates")

future_dates = nav[
    nav["Date"] >
    pd.Timestamp.today()
]

print(future_dates)

print("\nMissing Fund IDs")

print(
    transactions[
        ~transactions["FundID"].isin(funds["FundID"])
    ]
)

print("\nMissing Investor IDs")

print(
    transactions[
        ~transactions["InvestorID"].isin(
            investors["InvestorID"]
        )
    ]
)

print("\nInvalid Purchase NAV")

print(
    transactions[
        transactions["PurchaseNAV"] < 0
    ]
)


# ======================================================
# PART 9 - FINANCE METRICS
# ======================================================

merged["AbsoluteReturn"] = (
    merged["CurrentValue"] -
    merged["InvestmentAmount"]
)

merged["AnnualReturn"] = merged["ROI"]

volatility = np.std(
    merged["LatestNAV"]
)

print("\nVolatility :", volatility)

risk_free_rate = 6

average_return = merged["ROI"].mean()

sharpe_ratio = (
    average_return -
    risk_free_rate
) / volatility

print("Sharpe Ratio :", sharpe_ratio)


# ======================================================
# PART 10 - EXPORT REPORTS
# ======================================================

top_funds.to_excel(
    "TopFunds.xlsx"
)

investor_summary.to_excel(
    "InvestorSummary.xlsx"
)

category_summary.to_csv(
    "CategorySummary.csv"
)

print("\nReports Generated Successfully.")