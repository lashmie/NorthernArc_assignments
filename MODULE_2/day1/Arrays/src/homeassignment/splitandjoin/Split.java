package homeassignment.split;

public class Split {
    public static void main(String[] args) {
        String data="hwllo world";
        System.out.println(data);
        String[] words=data.split(" ");
        for (String val : words) {
            System.out.println(val);
        }


    }


}
