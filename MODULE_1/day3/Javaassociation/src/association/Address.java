package association;

public class Address{

    private String houseNo;
    private String street;
    private String city;
    private String state;
    private String zip;

    Address(String houseNo,String street,String city,String state,String zip){
        this.houseNo=houseNo;
        this.street=street;
        this.city=city;
        this.state=state;
        this.zip=zip;
    }

    // Getter and Setter for houseNo
    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    // Getter and Setter for street
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    // Getter and Setter for city
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // Getter and Setter for state
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    // Getter and Setter for zip
    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
