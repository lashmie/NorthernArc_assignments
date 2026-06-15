package newfeatures.functional;

public class lambda {
    public static void main(String[] args) {
        greeting gn = ()->{
            System.out.println("Good night");
        };
        gn.greet();

        greeting gm =()->{
            System.out.println("Good morning");
        };
        gm.greet();
    }
}
