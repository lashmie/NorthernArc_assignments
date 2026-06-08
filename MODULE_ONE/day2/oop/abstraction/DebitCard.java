class DebitCard extends Payment{
    @Override
    public void things_needed(){
        System.out.println("It needs the debitcard ");

    }
    @Override
    public void limit(){
        System.out.println("It has 50000 rs limit ");
    }
     @Override
    public void paid(double amount){
        System.out.println("The amount"+amount+"paid via debit card");
    }
   
}