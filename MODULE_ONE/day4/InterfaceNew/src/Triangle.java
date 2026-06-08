public class Triangle implements Shape{
        private double length;
        private double height;

        Triangle(double length,double lengh){
            this.length=length;
            this.height=lengh;

        }

        public double getLength(){
            return length;
        }
        public double getHeight(){
            return height;
        }
        public void setLength(double length){
            this.length=length;
        }
        public void setHeight(double height){
            this.height=height;
        }

        public void calculate(){
            System.out.println("The area is :"+0.5*getLength()*getHeight());
        }
    }



