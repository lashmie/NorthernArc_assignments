package fintech;

public class BankMain {
    public static void main(String[] args) {
        BankClass b = new BankClass(1000);
        try{
            b.withdraw(8000);
        }
        catch (BankException e){
            System.out.println(e.getMessage());
        }

    }
}
