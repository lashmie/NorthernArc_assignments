
package Message;
public class Wa implements Message {
    private String message;
    Wa(String message){
        this.message=message;
    }
    public void message(){
        System.out.println("the message"+message+"is send via normal whatsapp message");
    }
}

