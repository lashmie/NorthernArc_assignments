package exercise;
//count spaces
public class count_Words {
    public static void main(String[] args) {
        String str = new String(" hello world");
        int count=0;
        for(int i=0;i<str.length();i++){
            char ch = str.charAt(i);
            if (ch ==' ') {
                count++;
            }

        }
        System.out.println(count);
    }
}
