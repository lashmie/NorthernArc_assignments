public class Athlete {
private String name;
private String sport;
private int medals;

public Athlete(String name, String sport, int medals) {
    this.name = name;
    this.sport = sport;
    this.medals = medals;
}

public void play() {
    System.out.println(name + " is playing " + sport);
}

public void getDetails() {
    System.out.println(
        "Name: " + name + "\n" +
        "Sport: " + sport + "\n" +
        "Medals: " + medals + "\n"
    );
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public String getSport() {
    return sport;
}

public void setSport(String sport) {
    this.sport = sport;
}

public int getMedals() {
    return medals;
}

public void setMedals(int medals) {
    this.medals = medals;
}

}