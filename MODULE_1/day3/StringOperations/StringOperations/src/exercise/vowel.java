package exercise;

public class vowel {
    public static void main(String[] args) {
        String val = new String("lavanya");
        int count=0;
        for(int i=0;i<=val.length()-1;i++){
            char check=val.charAt(i);
            if(check =='a' || check =='e'|| check=='i'||check=='o'||check=='u'){
                count++;
            }
        }
        System.out.println("count"+count);
    }
}
