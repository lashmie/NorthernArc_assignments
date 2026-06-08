package Message;

public class MainMessage {
    public static void main(String[] args) {
        Message m1 = new EmailMessage("hello office");
        m1.message();
        Message m2=new Wa("hawaaiii");
        m2.message();
        Message m3= new TextMessage("jkkjnakan");
        m3.message();
    }
}
