public class Artist {
private String name;
private String artForm;
private int experience;

public Artist(String name, String artForm, int experience) {
    this.name = name;
    this.artForm = artForm;
    this.experience = experience;
}

public void perform() {
    System.out.println(name + " is performing " + artForm);
}

public void getDetails() {
    System.out.println(
        "Name: " + name + "\n" +
        "Art Form: " + artForm + "\n" +
        "Experience: " + experience + " years\n"
    );
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public String getArtForm() {
    return artForm;
}

public void setArtForm(String artForm) {
    this.artForm = artForm;
}

public int getExperience() {
    return experience;
}

public void setExperience(int experience) {
    this.experience = experience;
}

}