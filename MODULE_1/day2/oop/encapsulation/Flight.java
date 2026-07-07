public class Flight {
  private String airline;
  private int flightnumber;
  private String source;
  private String destination;
  private String arrivalDateTime;
  private String depatureDateTime;

  public void setAirline(String com){
    this.airline=com;
  }
   public void setflightnumber(int no){
    this.flightnumber=no;
  }
    public void setDestination(String des ){
   this.destination=des ;
  }
  public void setSource(String s ){
   this.source=s;
  }
    public void setarrivalDateTime(String at  ){
   this.arrivalDateTime = at;
  }
    public void setdepatureDateTime(String dt ){
   this.depatureDateTime= dt;
  }

  //getters
     public String getAirline(){
        return airline;

  }
   public int getflightnumber(){
    return flightnumber;
  }
    public String getDestination( ){
   return destination ;
  }
    public String getarrivalDateTime(){
   return arrivalDateTime;
  }
    public String getdepatureDateTime(){
   return depatureDateTime;
  } 
  public void getstatus(){
System.out.println();
  }
    public void showDetails(){
}

public void showdetails(){
  System.out.println("airline"+airline);

    
System.out.println("flightnumber"+flightnumber);
  // private String source;
  // private String destination;
  // private String arrivalDateTime;
  // private String depatureDateTime;
}


}
