package Message;

public class TextMessage implements Message {
    private String message;
    TextMessage(String message){
        this.message=message;
    }
    public void message(){
        System.out.println("the message"+message+"is send via normal ext message");
    }
}
