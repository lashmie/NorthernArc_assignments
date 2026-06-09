package Code;
//loan portfolio has a Loan
import java.util.List;
public class LoanPortfolio {

    private String portfolioName;
    private List<Loan> loans;

    public String getPortfolioName() {
        return portfolioName;
    }

    public LoanPortfolio(
            String portfolioName,
            List<Loan> loans) {

        this.portfolioName = portfolioName;
        this.loans = loans;
    }

    public void show() {

        System.out.println("Portfolio : " + portfolioName);

        for (Loan loan : loans) {
            System.out.println(loan.getCustomerName());
        }
    }
}