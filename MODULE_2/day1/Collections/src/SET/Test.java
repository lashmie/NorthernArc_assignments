package SET;
public class Test {

    public static void main(String[] args) {

        try {

            int x = 10 / 0;
        }
        catch (ArithmeticException e) {

            System.out.println("Arithmetic");

        }

        catch (Exception e) {

            System.out.println("Exception");

        }



    }

}