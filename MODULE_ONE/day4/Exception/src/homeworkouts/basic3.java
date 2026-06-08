package homeworkouts;

public class basic3 {
    public static void main(String[] args) {
        int a=8;
        try{
            if(a<18){
                throw new Exception("not 18");
            }
            System.out.println("you are eligible to vote");
        }
        catch(Exception e){
//            System.out.println("you are not allowded to vote");
            System.out.println(e.getMessage());
        }
    }
}
