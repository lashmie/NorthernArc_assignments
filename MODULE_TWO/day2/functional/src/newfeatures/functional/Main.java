package newfeatures.functional;

public class Main {
    public static void main(String[] args) {
        class GM implements greeting{
            public void greet(){
                System.out.println("Good morning");
            }
        }
        class gn implements greeting{
            public void greet(){
                System.out.println("Good afternoon");
            }
        }

        greeting gm = new GM();
        gm.greet();
        greeting gn = new gn();
        gn.greet();
    }
}
