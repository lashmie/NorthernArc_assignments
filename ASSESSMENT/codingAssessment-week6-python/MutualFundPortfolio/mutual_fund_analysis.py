# =====================================================
# MUTUAL FUND PORTFOLIO PERFORMANCE & RISK ANALYSIS
# =====================================================
# Column names used (from the CSV files):
# investors.csv    -> InvestorID, InvestorName, Age, City, AnnualIncome, RiskProfile
# funds.csv        -> FundID, FundName, Category, FundManager, ExpenseRatio, Benchmark
# transactions.csv -> TransactionID, InvestorID, FundID, TransactionDate, TransactionType, Units, NAV, Amount
# nav_history.csv  -> FundID, Date, NAV
#
# Note: funds.csv has no AUM column, so AUM is taken as the total
# amount invested in each fund.


import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import logging
import os


# Make the logs folder if it is missing.
if not os.path.exists("logs"):
    os.makedirs("logs")

# Send all log messages to logs/portfolio.log
logging.basicConfig(
    filename="logs/portfolio.log",
    level=logging.INFO,
    format="%(asctime)s - %(message)s",
    filemode="w"
)

logging.info("Program Started")


# =====================================================
# SECTION 3 : EXCEPTION HANDLING FOR CSV READING
# =====================================================
def read_csv_file(filename):
    # Read a CSV safely. Return empty table if the file is bad.
    try:
        return pd.read_csv(filename)
    except FileNotFoundError:
        # File path does not exist.
        print("File not found:", filename)
        logging.info("File not found: " + filename)
        return pd.DataFrame()
    except pd.errors.EmptyDataError:
        # File exists but has no data.
        print("File is empty:", filename)
        logging.info("Empty file: " + filename)
        return pd.DataFrame()
    except Exception as error:
        # Any other problem (corrupted file etc).
        print("Could not read:", filename, "-", error)
        logging.info("Could not read: " + filename)
        return pd.DataFrame()


# =====================================================
# SECTION 4 : READ CSV FILES
# =====================================================
investors_data = read_csv_file(os.path.join("data", "investors.csv"))
funds_data = read_csv_file(os.path.join("data", "funds.csv"))
transactions_data = read_csv_file(os.path.join("data", "transactions.csv"))
nav_history_data = read_csv_file(os.path.join("data", "nav_history.csv"))

logging.info("Files Loaded")


# =====================================================
# SECTION 5 : OOP - FundPortfolio CLASS
# =====================================================
class FundPortfolio:
    # Holds the data and does the full analysis.

    def __init__(self, investors, funds, transactions, nav_history):
        # Store the four tables.
        self.investors = investors
        self.funds = funds
        self.transactions = transactions
        self.nav_history = nav_history

        # These get filled by the methods below.
        self.portfolio_df = None
        self.investor_results = None
        self.fund_results = None
        self.metrics = {}

    # -------------------------------------------------
    # SECTION 6 : DATA CLEANING
    # -------------------------------------------------
    def clean_data(self):
        # Annual Income -> fill missing with median.
        median_income = self.investors["AnnualIncome"].median()
        self.investors["AnnualIncome"] = self.investors["AnnualIncome"].fillna(median_income)

        # Risk Profile -> fill missing with "Moderate".
        self.investors["RiskProfile"] = self.investors["RiskProfile"].fillna("Moderate")

        # Expense Ratio -> fill missing with mean.
        mean_expense = self.funds["ExpenseRatio"].mean()
        self.funds["ExpenseRatio"] = self.funds["ExpenseRatio"].fillna(mean_expense)

        # NAV -> fill missing with previous day value (forward fill).
        self.nav_history = self.nav_history.sort_values(["FundID", "Date"])
        self.nav_history["NAV"] = self.nav_history["NAV"].ffill()

        # Remove duplicate transactions.
        self.transactions = self.transactions.drop_duplicates()

        logging.info("Missing Values Cleaned")
        print("Data cleaning done.")

    # -------------------------------------------------
    # SECTION 7 : MERGE DATA
    # -------------------------------------------------
    def merge_data(self):
        # transactions + investors
        merged = pd.merge(self.transactions, self.investors, on="InvestorID")
        # + funds
        merged = pd.merge(merged, self.funds, on="FundID")

        # Latest NAV per fund from nav_history.
        nav_sorted = self.nav_history.sort_values(["FundID", "Date"])
        latest_nav = nav_sorted.groupby("FundID").last().reset_index()
        latest_nav = latest_nav[["FundID", "NAV"]]
        latest_nav = latest_nav.rename(columns={"NAV": "CurrentNAV"})

        # + current NAV (keep all rows).
        merged = pd.merge(merged, latest_nav, on="FundID", how="left")
        # Funds without NAV history keep their transaction NAV.
        merged["CurrentNAV"] = merged["CurrentNAV"].fillna(merged["NAV"])

        # Current value and profit/loss.
        merged["CurrentValue"] = merged["Units"] * merged["CurrentNAV"]
        merged["ProfitLoss"] = merged["CurrentValue"] - merged["Amount"]

        self.portfolio_df = merged
        print("Data merged. Rows:", len(self.portfolio_df))

    # -------------------------------------------------
    # SECTION 8 : NUMPY CALCULATIONS
    # -------------------------------------------------
    def numpy_analysis(self):
        print("\n----- NUMPY ANALYSIS -----")

        # Mean investment amount.
        mean_investment = np.mean(self.portfolio_df["Amount"])
        print("Mean Investment Amount:", round(mean_investment, 2))

        # Median investor income.
        median_income = np.median(self.investors["AnnualIncome"])
        print("Median Investor Income:", round(median_income, 2))

        # Standard deviation of NAV.
        std_nav = np.std(self.nav_history["NAV"])
        print("Standard Deviation of NAV:", round(std_nav, 2))

        # Return for each fund from nav_history (last vs first NAV).
        fund_returns_list = []
        fund_id_list = self.nav_history["FundID"].unique()
        for fund_id in fund_id_list:
            one_fund = self.nav_history[self.nav_history["FundID"] == fund_id]
            one_fund = one_fund.sort_values("Date")
            first_nav = one_fund["NAV"].iloc[0]
            last_nav = one_fund["NAV"].iloc[-1]
            fund_return = (last_nav - first_nav) / first_nav * 100
            fund_returns_list.append(fund_return)

        fund_returns_array = np.array(fund_returns_list)
        # 90th and 95th percentile fund returns.
        print("90th Percentile Fund Return:", round(np.percentile(fund_returns_array, 90), 2))
        print("95th Percentile Fund Return:", round(np.percentile(fund_returns_array, 95), 2))

        # Correlation between income and investment amount.
        correlation_matrix = np.corrcoef(self.portfolio_df["AnnualIncome"], self.portfolio_df["Amount"])
        print("Correlation (Income vs Investment):", round(correlation_matrix[0][1], 4))

        # Average daily NAV.
        print("Average Daily NAV:", round(np.mean(self.nav_history["NAV"]), 2))

        # Save fund returns for later.
        self.fund_returns_list = fund_returns_list
        self.fund_id_list = fund_id_list

        logging.info("Analysis Completed")

    # -------------------------------------------------
    # SECTION 9 : INVESTOR ANALYSIS
    # -------------------------------------------------
    def investor_analysis(self):
        print("\n----- INVESTOR ANALYSIS -----")

        # Portfolio value per investor, highest first.
        investor_value = self.portfolio_df.groupby("InvestorName")["CurrentValue"].sum()
        investor_value = investor_value.sort_values(ascending=False)

        # Top 20 investors.
        top_20_investors = investor_value.head(20)
        print("\nTop 20 Investors by Portfolio Value:")
        print(top_20_investors)

        # Total invested per investor.
        investor_investment = self.portfolio_df.groupby("InvestorName")["Amount"].sum()

        # Investment above 10 Lakhs (1000000).
        big_investors = investor_investment[investor_investment > 1000000]
        print("\nInvestors with Investment above 10 Lakhs:", len(big_investors))

        # High risk investors.
        high_risk = self.investors[self.investors["RiskProfile"] == "High"]
        print("High Risk Investors:", len(high_risk))

        # More than 10 transactions.
        transaction_count = self.portfolio_df.groupby("InvestorName")["TransactionID"].count()
        active_investors = transaction_count[transaction_count > 10]
        print("Investors with more than 10 transactions:", len(active_investors))

        # Annual income above 15 Lakhs (1500000).
        rich_investors = self.investors[self.investors["AnnualIncome"] > 1500000]
        print("Investors with Income above 15 Lakhs:", len(rich_investors))

        # Investor report table.
        report = self.portfolio_df.groupby("InvestorName").agg(
            TotalInvested=("Amount", "sum"),
            CurrentValue=("CurrentValue", "sum"),
            ProfitLoss=("ProfitLoss", "sum")
        )
        report = report.sort_values("CurrentValue", ascending=False).reset_index()

        self.investor_results = report
        self.top_20_investors = top_20_investors

    # -------------------------------------------------
    # SECTION 10 : FUND ANALYSIS
    # -------------------------------------------------
    def fund_analysis(self):
        print("\n----- FUND ANALYSIS -----")

        # Small table of fund returns with names.
        fund_return_df = pd.DataFrame({
            "FundID": self.fund_id_list,
            "Return": self.fund_returns_list
        })
        fund_return_df = pd.merge(fund_return_df, self.funds, on="FundID")

        # Best and worst performing funds.
        best_row = fund_return_df.sort_values("Return", ascending=False).iloc[0]
        print("Best Performing Fund:", best_row["FundName"], "-", round(best_row["Return"], 2), "%")

        worst_row = fund_return_df.sort_values("Return", ascending=True).iloc[0]
        print("Worst Performing Fund:", worst_row["FundName"], "-", round(worst_row["Return"], 2), "%")

        # Highest expense ratio.
        costliest_fund = self.funds.sort_values("ExpenseRatio", ascending=False).iloc[0]
        print("Highest Expense Ratio:", costliest_fund["FundName"], "-", costliest_fund["ExpenseRatio"])

        # Highest AUM (approx = total amount invested per fund).
        fund_aum = self.portfolio_df.groupby("FundName")["Amount"].sum().sort_values(ascending=False)
        print("Highest AUM Fund (by total invested):", fund_aum.index[0], "-", round(fund_aum.iloc[0], 2))

        # Most popular fund (most transactions).
        fund_popularity = self.portfolio_df.groupby("FundName")["TransactionID"].count().sort_values(ascending=False)
        print("Most Popular Fund:", fund_popularity.index[0])

        # Fund report table.
        fund_report = fund_return_df[["FundID", "FundName", "Category", "ExpenseRatio", "Return"]]
        fund_report = fund_report.sort_values("Return", ascending=False).reset_index(drop=True)
        self.fund_results = fund_report

    # -------------------------------------------------
    # SECTION 11 : OUTLIER DETECTION
    # -------------------------------------------------
    def remove_outliers(self):
        print("\n----- OUTLIER DETECTION -----")

        # Remove investment amounts above the 99th percentile.
        limit_99 = np.percentile(self.portfolio_df["Amount"], 99)
        rows_before = len(self.portfolio_df)
        self.portfolio_df = self.portfolio_df[self.portfolio_df["Amount"] <= limit_99]
        print("Investment outliers removed:", rows_before - len(self.portfolio_df))

        # Remove NAV changes beyond mean +/- 3 standard deviations.
        nav = self.nav_history.sort_values(["FundID", "Date"])
        nav["NAV_Change"] = nav.groupby("FundID")["NAV"].diff()
        change_mean = nav["NAV_Change"].mean()
        change_std = nav["NAV_Change"].std()
        lower_limit = change_mean - (3 * change_std)
        upper_limit = change_mean + (3 * change_std)

        nav_before = len(nav)
        is_first_day = nav["NAV_Change"].isna()
        inside_range = (nav["NAV_Change"] >= lower_limit) & (nav["NAV_Change"] <= upper_limit)
        nav = nav[is_first_day | inside_range]
        print("NAV outliers removed:", nav_before - len(nav))

        self.nav_history = nav

    # -------------------------------------------------
    # SECTION 12 : FINANCE METRICS
    # -------------------------------------------------
    def portfolio_metrics(self):
        print("\n----- FINANCE METRICS -----")

        total_invested = self.portfolio_df["Amount"].sum()
        total_current = self.portfolio_df["CurrentValue"].sum()

        # Total portfolio value.
        print("Total Portfolio Value:", round(total_current, 2))

        # Absolute return = current - invested.
        absolute_return = total_current - total_invested
        print("Absolute Return:", round(absolute_return, 2))

        # Portfolio return % = (current - invested) / invested * 100
        portfolio_return_pct = (total_current - total_invested) / total_invested * 100
        print("Portfolio Return %:", round(portfolio_return_pct, 2))

        # Average holding period in days and years.
        today = pd.to_datetime("today")
        purchase_dates = pd.to_datetime(self.portfolio_df["TransactionDate"])
        holding_days = (today - purchase_dates).dt.days
        average_holding_days = holding_days.mean()
        years = average_holding_days / 365
        print("Average Holding Period (days):", round(average_holding_days, 2))

        # CAGR = (current / invested) ^ (1 / years) - 1
        if years > 0:
            cagr = ((total_current / total_invested) ** (1 / years) - 1) * 100
        else:
            cagr = 0
        print("CAGR %:", round(cagr, 2))

        # Annualized return = portfolio return % / years
        if years > 0:
            annualized_return = portfolio_return_pct / years
        else:
            annualized_return = 0
        print("Annualized Return %:", round(annualized_return, 2))

        # Diversification score = number of fund categories.
        number_of_categories = self.portfolio_df["Category"].nunique()
        print("Portfolio Diversification Score (categories):", number_of_categories)

        # Expense ratio impact = sum(Amount * ExpenseRatio / 100)
        expense_cost = self.portfolio_df["Amount"] * self.portfolio_df["ExpenseRatio"] / 100
        total_expense_impact = expense_cost.sum()
        print("Expense Ratio Impact:", round(total_expense_impact, 2))

        # Sharpe ratio (simplified) = (avg return - risk free) / std of returns
        each_return = self.portfolio_df["ProfitLoss"] / self.portfolio_df["Amount"] * 100
        average_return = each_return.mean()
        std_return = each_return.std()
        risk_free_rate = 6
        if std_return > 0:
            sharpe_ratio = (average_return - risk_free_rate) / std_return
        else:
            sharpe_ratio = 0
        print("Sharpe Ratio (Simplified):", round(sharpe_ratio, 2))

        # Category-wise investment %.
        category_investment = self.portfolio_df.groupby("Category")["Amount"].sum()
        category_percent = category_investment / total_invested * 100
        print("\nCategory-wise Investment %:")
        print(category_percent.round(2))

        # Fund allocation %.
        fund_investment = self.portfolio_df.groupby("FundName")["Amount"].sum()

        # Save data for charts.
        self.category_investment = category_investment
        self.fund_investment = fund_investment

        # Save single numbers for the report.
        self.metrics["TotalPortfolioValue"] = round(total_current, 2)
        self.metrics["TotalInvested"] = round(total_invested, 2)
        self.metrics["AbsoluteReturn"] = round(absolute_return, 2)
        self.metrics["PortfolioReturnPct"] = round(portfolio_return_pct, 2)
        self.metrics["CAGRPct"] = round(cagr, 2)
        self.metrics["AnnualizedReturnPct"] = round(annualized_return, 2)
        self.metrics["DiversificationScore"] = number_of_categories
        self.metrics["AverageHoldingDays"] = round(average_holding_days, 2)
        self.metrics["ExpenseRatioImpact"] = round(total_expense_impact, 2)
        self.metrics["SharpeRatio"] = round(sharpe_ratio, 2)

    # -------------------------------------------------
    # SECTION 13 : DATA VISUALIZATION
    # -------------------------------------------------
    def create_charts(self):
        # Make the charts folder if missing.
        if not os.path.exists("charts"):
            os.makedirs("charts")

        # Chart 1: Portfolio allocation (pie by category).
        plt.figure()
        plt.pie(self.category_investment, labels=self.category_investment.index, autopct="%1.1f%%")
        plt.title("Portfolio Allocation by Category")
        plt.tight_layout()
        plt.savefig("charts/portfolio_allocation_pie.png")
        plt.show()

        # Chart 2: Fund-wise investment (bar, top 10).
        top_funds = self.fund_investment.sort_values(ascending=False).head(10)
        plt.figure()
        plt.bar(top_funds.index, top_funds.values)
        plt.title("Top 10 Funds by Investment")
        plt.xlabel("Fund Name")
        plt.ylabel("Investment Amount")
        plt.xticks(rotation=90)
        plt.tight_layout()
        plt.savefig("charts/fund_wise_investment_bar.png")
        plt.show()

        # Chart 3: Monthly investment trend (line).
        monthly = self.portfolio_df.copy()
        monthly["Month"] = pd.to_datetime(monthly["TransactionDate"]).dt.to_period("M").astype(str)
        monthly_investment = monthly.groupby("Month")["Amount"].sum()
        plt.figure()
        plt.plot(monthly_investment.index, monthly_investment.values, marker="o")
        plt.title("Monthly Investment Trend")
        plt.xlabel("Month")
        plt.ylabel("Investment Amount")
        plt.xticks(rotation=45)
        plt.tight_layout()
        plt.savefig("charts/monthly_investment_trend_line.png")
        plt.show()

        # Chart 4: Category-wise returns (bar).
        category_returns = self.portfolio_df.groupby("Category")["ProfitLoss"].sum()
        plt.figure()
        plt.bar(category_returns.index, category_returns.values)
        plt.title("Category-wise Returns")
        plt.xlabel("Category")
        plt.ylabel("Total Profit / Loss")
        plt.tight_layout()
        plt.savefig("charts/category_wise_returns_bar.png")
        plt.show()

        # Chart 5: NAV movement (line for one fund).
        first_fund_id = self.nav_history["FundID"].iloc[0]
        one_fund_nav = self.nav_history[self.nav_history["FundID"] == first_fund_id]
        one_fund_nav = one_fund_nav.sort_values("Date")
        plt.figure()
        plt.plot(one_fund_nav["Date"], one_fund_nav["NAV"], marker="o")
        plt.title("NAV Movement - " + str(first_fund_id))
        plt.xlabel("Date")
        plt.ylabel("NAV")
        plt.xticks(rotation=45)
        plt.tight_layout()
        plt.savefig("charts/nav_movement_line.png")
        plt.show()

        # Chart 6: Top 10 investors (horizontal bar).
        top_10_investors = self.top_20_investors.head(10)
        plt.figure()
        plt.barh(top_10_investors.index, top_10_investors.values)
        plt.title("Top 10 Investors by Portfolio Value")
        plt.xlabel("Portfolio Value")
        plt.ylabel("Investor Name")
        plt.tight_layout()
        plt.savefig("charts/top_10_investors_barh.png")
        plt.show()

        logging.info("Charts Generated")
        print("\nCharts saved in the charts folder.")

    # -------------------------------------------------
    # SECTION 14 : EXPORT REPORTS
    # -------------------------------------------------
    def export_reports(self):
        # Make the reports folder if missing.
        if not os.path.exists("reports"):
            os.makedirs("reports")

        # Save the three reports.
        self.portfolio_df.to_csv("reports/portfolio_report.csv", index=False)
        self.investor_results.to_csv("reports/investor_report.csv", index=False)
        self.fund_results.to_csv("reports/fund_report.csv", index=False)

        logging.info("Reports Exported")
        print("Reports saved in the reports folder.")


# =====================================================
# SECTION 15 : MAIN FUNCTION
# =====================================================
def main():
    print("Starting Mutual Fund Portfolio Analysis...")

    # Create the object with the four tables.
    portfolio = FundPortfolio(
        investors_data,
        funds_data,
        transactions_data,
        nav_history_data
    )

    # Run each step in order.
    portfolio.clean_data()
    portfolio.merge_data()
    portfolio.numpy_analysis()
    portfolio.investor_analysis()
    portfolio.fund_analysis()
    portfolio.remove_outliers()
    portfolio.portfolio_metrics()
    portfolio.create_charts()
    portfolio.export_reports()

    print("\nAnalysis finished successfully.")
    logging.info("Program Finished")


if __name__ == "__main__":
    main()
