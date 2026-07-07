class CreditCard extends Payment{
    @Override
    public void things_needed(){
        System.out.println("It needs the Credit card ");

    }
    @Override
    public void limit(){
        System.out.println("It has 1000000 rs limit ");
    }
     @Override
    public void paid(double amount){
        System.out.println("The amount"+amount+"paid via credit card");
    }
   
}