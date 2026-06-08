public class Manager extends Employee {
    protected String team;
    public Manager(String n,String l,int a, String id,String dept,String team){
        super(n,l,a,id,dept);
        this.team=team;}
    
    public void manages(){
        System.out.println("The manager manages "+ team +" team and her name is"+fname);
    }
}
