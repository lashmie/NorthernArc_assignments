package aggregation.classassigment;

public class MainPlayerTeam {
    public static void main(String[] args) {
        Team twister = new Team("twister","india");
        twister.plays(new Player("lavanya",9.0));
        twister.plays(new Player("vishnu priya",5.9));
        twister.detail();
        System.out.println(twister.getPlayersList());
    }
}
