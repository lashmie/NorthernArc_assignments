package Code;

import java.util.List;

public class PortfolioMain {
    Loan loan1 =
            new Carloan("Ravi",500000);

    Loan loan2 =
            new Bikeloan("Kumar",300000);

    LoanPortfolio p1 =
            new LoanPortfolio(
                    "Retail Loans",
                    List.of(loan1, loan2)
            );
    //p1.show();

//p1.show(p1.getPortfolioname(), List.of(loan1, loan2));
//    p1.show(p1.getPortfolioname(), List.of(loan1, loan2));
}
