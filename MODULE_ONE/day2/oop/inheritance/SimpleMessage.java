public class SimpleMessage {
    protected String message;

    public void setMessage(String m){
    this.message=m;
        }
    public String getMessage(){
        return message;
    }
    public void send(String message){
        System.out.println("Sending message"+message);
    }

}
