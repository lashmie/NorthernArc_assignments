class Student{
    private String name;
    private String grade;
    private String school;

    Student(String name,String grade,String school){
        this.name=name;
        this.grade=grade;
        this.school=school;
    }
    
    public void setName(String name){
        this.name=name;
    }
    public void setGrade(String grade){
        this.grade=grade;
    }
    public void setSchool(String school){
        this.school=school;
    }

    public String getName(){
        return name;
    }
    public String getGrade(){
        return grade;
    }
    public String getSchool(){
        return school;
    }

    public void showStudentDetails(){
        System.out.println("The name of the student is "+name+" and he got the grade "+ grade +" and his school"+ school + " helped him a lot");
    }
}