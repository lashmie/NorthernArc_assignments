class Upi extends Payment{
    @Override
    public void things_needed(){
        System.out.println("It needs the phone ");

    }
    @Override
    public void limit(){
        System.out.println("It has 10000 rs limit ");
    }
     @Override
    public void paid(double amount){
        System.out.println("The amount"+amount+"paid via Upi");
    }
   
}