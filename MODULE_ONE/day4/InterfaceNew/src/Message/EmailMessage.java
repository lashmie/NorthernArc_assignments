package Message;



public class EmailMessage implements Message {
    private String message;
    EmailMessage(String message){
        this.message=message;
    }
    public void message(){
        System.out.println("the message"+message+"is send via email message");
    }
}

