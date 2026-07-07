package newfeatures.functional;

public class anonymous {
    public static void main(String[] args) {

        greeting gm = new greeting(){
            public void greet(){
                System.out.println("Good morning");
            }
        };
        gm.greet();

        greeting gn = new greeting(){
            public void greet(){
                System.out.println("Good afternoon");
            }
        } ;
        gn.greet();

        greeting evening = new greeting(){
            public void greet(){
                System.out.println("Good evening");
            }
        };

        greeting day = new greeting(){
            public void greet(){
                System.out.println("good morning");
            }
        };


    }
}
