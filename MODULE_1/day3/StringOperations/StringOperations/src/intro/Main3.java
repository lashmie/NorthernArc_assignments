package intro;

public class Main3 {
    public static void main(String[] args) {
        String data="hello world";
        System.out.println(data.charAt(0));
        System.out.println(data.length());
        System.out.println(data.charAt(data.length()-1));
        System.out.println(data.substring(0,5));
        System.out.println(data.substring(9));
        System.out.println(data.replace("world","java"));
        System.out.println(data.toLowerCase());
        System.out.println(data.toUpperCase());
        System.out.println(data.trim());
        System.out.println(data.indexOf("o"));
        System.out.println(data.lastIndexOf("o"));
        System.out.println(data.contains("z"));
        System.out.println(data.split(" "));
        System.out.println(data.startsWith("hello"));
        System.out.println(data.endsWith("world"));
        System.out.println(data.concat("galaxy"));
        String s1="sachin";
        String s2="saurav";
        System.out.println(s1.compareTo(s2));
        String s3="sachin";
        String s4="sachin";
        System.out.println(s3.equals(s4));
        System.out.println(s3.equalsIgnoreCase(s4));


    }
}
