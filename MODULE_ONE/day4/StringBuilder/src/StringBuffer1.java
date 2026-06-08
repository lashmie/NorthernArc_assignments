public class StringBuffer1 {
    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer("hello ");
        System.out.println(sb);
        sb.append(" world");
        System.out.println(sb);
        sb.insert(0,"Hi ");
        System.out.println(sb);
        sb.delete(0,3);
        System.out.println(sb);
        sb.append("e");
        sb.replace(6,11,"java");
        System.out.println(sb);
        sb.reverse();
        System.out.println(sb);
        System.out.println(  sb.capacity());
    }
}
