type Employee = {
    empId: number;
    empName: string;
    city: string;
};

function display(emp: Employee): void {
    console.log(emp.empId);
    console.log(emp.empName);
    console.log(emp.city);
}

let e1: Employee = {
    empId: 111,
    empName: "Ganguly",
    city: "Chennai"
};

display(e1);