import java.util.*;
public class MainAnimal {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Enter 1 for lion");
        System.out.println("Enter 2 for dog");
        System.out.println("Enter 3 for deer");
        System.out.println("Enter 4 for deer");

        int val =sc.nextInt();
        if(val==1){
            Lion l1= new Lion();
            l1.eat();
            l1.talk();
            l1.walk();
            l1.rule();
        }
        else if (val==2){
            Dog d1= new Dog();
            d1.eat();
            d1.talk();
            d1.walk();
            d1.gaurd();
        }
        else if(val==3){
            Deer d2 = new Deer();
            d2.eat();
            d2.talk();
            
        }
        else if(val==4){
Animal a1 = new Animal();
a1.eat();
a1.talk();
a1.walk();
        }
    }
}
