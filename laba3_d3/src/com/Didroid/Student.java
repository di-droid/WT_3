package com.Didroid;

import java.io.Serializable;

//класс для хранения записи о конкретном студенте
public class Student implements Serializable {
    private int ID;
    private String name;
    private String surname;
    private int age;
    private int studentID;

    public Student(int ID, String name, String surname, int age, int studentID) {
        this.ID = ID;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.studentID = studentID;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    @Override
    public String toString() {
        return "Student{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", studentID=" + studentID +
                '}';
    }
}
