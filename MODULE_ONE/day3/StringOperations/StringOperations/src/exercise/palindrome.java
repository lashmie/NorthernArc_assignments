package exercise;

public class palindrome {
    public static void main(String[] args) {
        String s1= new String("even");

        int i=0;
        int j=s1.length()-1;
        boolean is=true;
        while(i<j){
            if(s1.charAt(i)!=(s1.charAt(j))) {
                is=false;
                break;
            }
            i++;
            j--;
        }

if(is){
    System.out.println("palindrome");
}
else{
    System.out.println("not a palindrome");
}
    }
}
