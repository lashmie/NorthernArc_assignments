package homeworkouts;

public class basic2 {
    public static void main(String[] args) {
        try {
            String a = null;
            System.out.println(a.length());
        }
        catch(Exception e){
//            System.out.println(e.getMessage());
            System.out.println("Null cannot be printed");
        }

    }
}
