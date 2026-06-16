package first;

public class MyThread extends Thread{
    public MyThread(String name){//first rule of inheritance
        super(name);//to uniquely identify
    }

public void run(){
    //System.out.println("My thread");
    for(int i=1;i<=100;i++){
        System.out.println(i+" "+this.getName());
    }
}

}
