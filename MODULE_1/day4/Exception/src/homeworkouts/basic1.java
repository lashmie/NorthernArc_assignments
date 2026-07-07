package homeworkouts;

public class basic1 {
    public static void main(String[] args) {
        try{
            int a=10/0;
            System.out.println(a);
        }
        catch(ArithmeticException e){
            System.out.println(e.getMessage());
        }
        catch(Exception e){
            System.out.println("Some random exception");
        }


    }
}
