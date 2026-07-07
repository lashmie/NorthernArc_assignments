package exercise;

public class Main4 {
    public static void main(String[] args) {
        Person p1=new Person("Renjitha","K",21);
        Person p2=new Person("Lavanya","E",20);
        Person p3=p1;
        System.out.println(p1==p2);
        System.out.println(p3==p1);
        System.out.println(p1.equals(p2));
        System.out.println(p3.equals(p1));
        System.out.println(p1.hashCode());
        System.out.println(p1.hashCode()==p2.hashCode());
        System.out.println(p1.getClass().getSimpleName());
        System.out.println(p1.getClass().getName());
        System.out.println(p1.toString());
    }

}
