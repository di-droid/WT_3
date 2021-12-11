package com.Didroid;

import java.util.List;

//класс для хранения списка всех студентов
public class AllStudents {

    private List<Student> studentList;

    public AllStudents(List<Student> studentList) {
        this.studentList = studentList;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public int getQuantity(){
        return studentList.size();
    }

    public void addNewStud(Student stud){
        studentList.add(stud);
    }

    public Student getStudent(int ID){
        return studentList.get(ID);
    }

    public void redactStud(Student stud,int ID){
        studentList.set(ID,stud);
    }
}
