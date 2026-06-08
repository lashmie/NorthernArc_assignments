package association;

public class Passport {

    private String passportNo;
    private String country;
    private String issueDate;
    private String expiryDate;
    private Person person;

    // Default Constructor
    public Passport() {
    }

    // Parameterized Constructor
    public Passport(String passportNo, String country, String issueDate,
                    String expiryDate) {
        this.passportNo = passportNo;
        this.country = country;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;

    }
    public void person(Person p){
        this.person=p;
    }

    // Getter and Setter for passportNo
    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    // Getter and Setter for country
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    // Getter and Setter for issueDate
    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    // Getter and Setter for expiryDate
    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

//    // Getter and Setter for person
//    public String getPerson() {
//        return person;
//    }
//
//    public void setPerson(String person) {
//        this.person = person;
//    }

    // toString Method
    @Override
    public String toString() {
        return "Passport{" +
                "passportNo='" + passportNo + '\'' +
                ", country='" + country + '\'' +
                ", issueDate='" + issueDate + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", person='" + person.toString()+ '\'' +
                '}';
    }
}
