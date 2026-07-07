class Employee{
    constructor(private name:string, private age:number){

    }
show():String{
return this.name+" "+this.age;
}
}
let e1:Employee = new Employee("lavanya",89);
