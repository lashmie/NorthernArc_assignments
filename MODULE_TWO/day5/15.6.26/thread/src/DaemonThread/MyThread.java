package DaemonThread;

public class MyThread extends Thread{
    private int delay;
    public MyThread(String name,int delay){
        super(name);
        this.delay=delay;
    }
    public void run(){
        for(int i=0;i<=10;i++){
            try{
                Thread.sleep(delay);
                System.out.println(i+" "+this.getName());
            }catch(InterruptedException e){
                throw new RuntimeException(e);
            }
        }
    }

}
