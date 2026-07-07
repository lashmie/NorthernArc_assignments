package intro;

public class reverse {
    public static void main(String[] args) {
        String s = new String("Lavanya");
//        for(int i=0;i<s.length()-1;i++){
//            System.out.println(s.charAt(i));
//        }
        for(int i=s.length()-1;i>=0;i--){
            System.out.println(s.charAt(i));
        }
    }
}
