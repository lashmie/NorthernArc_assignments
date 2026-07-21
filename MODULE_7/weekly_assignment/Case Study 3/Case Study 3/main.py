import pandas as pd
import numpy as np

# =====================================================
# PART 1 - READ DATA
# =====================================================

try:
    customers = pd.read_csv("customers.csv")
    loans = pd.read_csv("loan_application.csv")
    payments = pd.read_csv("loan_payments.csv")

    print("CSV Files Loaded Successfully")

except FileNotFoundError as e:
    print("File Not Found:", e)
    exit()

except Exception as e:
    print(e)
    exit()

# =====================================================
# PART 2 - DATA CLEANING
# =====================================================

customers.drop_duplicates(inplace=True)
loans.drop_duplicates(inplace=True)
payments.drop_duplicates(inplace=True)

# Remove duplicate Loan IDs
loans.drop_duplicates(subset="LoanID", inplace=True)

print("\nMissing Values")
print(customers.isnull().sum())
print(loans.isnull().sum())
print(payments.isnull().sum())

# Replace missing Salary with Median (if present)
if "Salary" in customers.columns:
    customers["Salary"] = customers["Salary"].fillna(customers["Salary"].median())

# Replace missing Credit Score with Mean (if present)
if "CreditScore" in customers.columns:
    customers["CreditScore"] = customers["CreditScore"].fillna(customers["CreditScore"].mean())

# Convert dates
loans["ApplicationDate"] = pd.to_datetime(
    loans["ApplicationDate"],
    errors="coerce"
)

payments["PaymentDate"] = pd.to_datetime(
    payments["PaymentDate"],
    errors="coerce"
)

# Remove negative Loan Amount
loans = loans[loans["LoanAmount"] >= 0]

# Remove invalid EMI Amount (if column exists)
if "EMIAmount" in payments.columns:
    payments = payments[payments["EMIAmount"].fillna(0) > 0]

# Remove future payment dates
payments = payments[
    payments["PaymentDate"] <= pd.Timestamp.today()
]

print("\nData Cleaning Completed")

# =====================================================
# PART 3 - MERGE DATASETS
# =====================================================

merged = customers.merge(
    loans,
    on="CustomerID",
    how="inner"
)

merged = merged.merge(
    payments,
    on="LoanID",
    how="left"
)

print("\nMerged Data")
print(merged.head())

# =====================================================
# PART 4 - CREATE NEW COLUMNS
# =====================================================

# Create new columns safely (check presence of each column first)
if "Salary" in merged.columns:
    merged["MonthlyIncome"] = merged["Salary"] / 12
else:
    merged["MonthlyIncome"] = np.nan

if "LoanAmount" in merged.columns and "Salary" in merged.columns:
    # avoid division by zero
    merged["DebtToIncomeRatio"] = merged.apply(lambda r: (r["LoanAmount"] / r["Salary"]) if r["Salary"] and r["Salary"] != 0 else np.nan, axis=1)
else:
    merged["DebtToIncomeRatio"] = np.nan

# EMI due and payment completion — treat missing values as 0 for arithmetic
emi_amt = merged["EMIAmount"] if "EMIAmount" in merged.columns else pd.Series(0, index=merged.index)
amt_paid = merged["AmountPaid"] if "AmountPaid" in merged.columns else pd.Series(0, index=merged.index)
merged["EMIDue"] = emi_amt.fillna(0) - amt_paid.fillna(0)
merged["PaymentCompletion"] = np.where(emi_amt.fillna(0) > 0, (amt_paid.fillna(0) / emi_amt.fillna(0)) * 100, np.nan)

print("\nCalculated Columns Added")

# =====================================================
# PART 5 - NUMPY ANALYSIS
# =====================================================

loan_amount = merged["LoanAmount"].to_numpy()

print("\nNumPy Statistics")

print("Average Loan:", np.mean(loan_amount))
print("Median Loan:", np.median(loan_amount))
print("Maximum Loan:", np.max(loan_amount))
print("Minimum Loan:", np.min(loan_amount))
print("Standard Deviation:", np.std(loan_amount))
print("Variance:", np.var(loan_amount))
print("25 Percentile:", np.percentile(loan_amount,25))
print("75 Percentile:", np.percentile(loan_amount,75))

# =====================================================
# PART 6 - PANDAS ANALYSIS
# =====================================================

print("\nTop 10 Loan Customers")

top10loan = merged.nlargest(
    10,
    "LoanAmount"
)[["CustomerName","LoanAmount"]]

print(top10loan)

print("\nTop 10 Salary")

top10salary = merged.nlargest(
    10,
    "Salary"
)[["CustomerName","Salary"]]

print(top10salary)

print("\nCredit Score Below 650")

low_credit = merged[
    merged["CreditScore"] < 650
]

print(low_credit)

print("\nLoan Amount > 20 Lakhs")

highloan = merged[
    merged["LoanAmount"] > 2000000
]

print(highloan)

print("\nPending Payments")


if "PaymentStatus" in merged.columns:
    pending = merged[merged["PaymentStatus"] == "Pending"]
else:
    pending = pd.DataFrame(columns=merged.columns)

print(pending)

print("\nFully Paid Loans")

paid = merged[
    merged["PaymentStatus"] == "Paid"
]

print(paid)

# =====================================================
# PART 7 - GROUP BY
# =====================================================

print("\nCity Summary")

city_summary = merged.groupby("City").agg(

    NumberOfCustomers=("CustomerID","nunique"),
    AverageSalary=("Salary","mean"),
    TotalLoanAmount=("LoanAmount","sum")

)

print(city_summary)

print("\nLoan Type Summary")

loan_summary = merged.groupby("LoanType").agg(

    NumberOfLoans=("LoanID","count"),
    AverageLoanAmount=("LoanAmount","mean"),
    TotalLoanAmount=("LoanAmount","sum")

)

print(loan_summary)

print("\nLoan Status Summary")

status_summary = merged.groupby("LoanStatus").agg(

    NumberOfLoans=("LoanID","count")

)

print(status_summary)

print("\nPayment Status Summary")

payment_summary = merged.groupby("PaymentStatus").agg(

    Count=("LoanID","count"),
    TotalAmountPaid=("AmountPaid","sum")

)

print(payment_summary)

# =====================================================
# PART 8 - BUSINESS RULES
# =====================================================

merged["HighLoanFlag"] = merged["LoanAmount"] > 3000000

merged["LowCreditFlag"] = merged["CreditScore"] < 650

merged["LowSalaryFlag"] = merged["Salary"] < 30000

merged["HighDTIFlag"] = (
    merged["DebtToIncomeRatio"] > 5
)

merged["HighEMIDueFlag"] = (
    merged["EMIDue"] > 10000
)

merged["PendingPaymentFlag"] = (
    merged["PaymentStatus"] == "Pending"
)

merged["RejectedLoanFlag"] = (
    merged["LoanStatus"] == "Rejected"
)

print("\nBusiness Rules Applied")

# =====================================================
# PART 9 - FINANCE METRICS
# =====================================================

total_loan = merged["LoanAmount"].sum()

total_collection = merged["AmountPaid"].sum()

outstanding = (
    merged["LoanAmount"] -
    merged["AmountPaid"]
).sum()

loan_recovery = (
    total_collection /
    total_loan
) * 100

pending_loans = merged[
    merged["PaymentStatus"] == "Pending"
].shape[0]

total_loans = merged.shape[0]

default_percent = (
    pending_loans /
    total_loans
) * 100

average_emi = merged["EMIAmount"].mean()

average_credit = merged["CreditScore"].mean()

print("\nFinance Metrics")

print("Total Loan Portfolio :", total_loan)

print("Total Amount Collected :", total_collection)

print("Outstanding Amount :", outstanding)

print("Loan Recovery % :", loan_recovery)

print("Default % :", default_percent)

print("Average EMI :", average_emi)

print("Average Credit Score :", average_credit)

# =====================================================
# PART 10 - EXPORT REPORTS
# =====================================================

loan_summary.to_excel(
    "LoanSummary.xlsx"
)

merged.to_excel(
    "CustomerLoanReport.xlsx",
    index=False
)

pending.to_csv(
    "PendingPayments.csv",
    index=False
)

print("\nReports Generated Successfully")

print("\n======== EXPECTED OUTPUTS ========")

print("\nTop 10 Loan Customers")
print(top10loan)

print("\nCustomers with Low Credit Score")
print(low_credit)

print("\nPending Loan Payments")
print(pending)

print("\nCity-wise Loan Summary")
print(city_summary)

print("\nLoan Type Summary")
print(loan_summary)

print("\nLoan Recovery Report")
print("Recovery Percentage :", loan_recovery)