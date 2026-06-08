//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
    StringBuilder sb = new StringBuilder("hello");
        System.out.println(sb);
        sb.append(" world");
        System.out.println(sb);
        sb.insert(0,"Hi ");
        System.out.println(sb);
        sb.delete(0,3);
        System.out.println(sb);
        sb.replace(6,11,"java");
        System.out.println(sb);
        sb.reverse();
        System.out.println(sb);
        System.out.println(  sb.capacity());

    }
}