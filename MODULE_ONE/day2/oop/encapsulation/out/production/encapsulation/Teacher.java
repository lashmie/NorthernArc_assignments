public class Teacher {
private String name;
private String subject;
private int experience;

public Teacher(String name, String subject, int experience) {
    this.name = name;
    this.subject = subject;
    this.experience = experience;
}

public void teach() {
    System.out.println(name + " is teaching " + subject);
}

public void getDetails() {
    System.out.println(
        "Name: " + name + "\n" +
        "Subject: " + subject + "\n" +
        "Experience: " + experience + " years\n"
    );
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public String getSubject() {
    return subject;
}

public void setSubject(String subject) {
    this.subject = subject;
}

public int getExperience() {
    return experience;
}

public void setExperience(int experience) {
    this.experience = experience;
}

}