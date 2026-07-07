package homeworkouts;



public class Methodthrows {


    public static void check(int age) throws Exception{
        if(age <18){
            throw new Exception("you are not eligible to vote");
        }
        System.out.println("your are eligble to vote");
    }

    public static void main(String[] args) {
        try {
            check(5);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("you are not able to vote");
        }
    }

}

