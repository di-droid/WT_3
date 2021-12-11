package com.Didroid;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

//приложение клиента
public class MainClient {

    public static void main(String[] args) throws InterruptedException {

        try(Socket socketClient = new Socket("localhost", 3345);
            PrintWriter outMess = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream())), true);
            BufferedReader reader =new BufferedReader(
                    new InputStreamReader(socketClient.getInputStream()));
            ObjectOutputStream outObj = new ObjectOutputStream (socketClient.getOutputStream());
            ObjectInputStream inObj = new ObjectInputStream(socketClient.getInputStream()))
        {

            System.out.print("Quantity of students - ");
            int quantityStud = Integer.parseInt(reader.readLine());
            System.out.println(quantityStud);
            System.out.println("Students info:");
            int i;
            for (i = 0; i<quantityStud; i++){
                Student studInfo = (Student)inObj.readObject();
                System.out.println(studInfo.toString());
            }

            String requestType;
            do {
                System.out.println("If you want to redact then write 'redact'");
                System.out.println("If you want to create new node then write 'new'");
                Scanner inputClient = new Scanner(System.in);
                requestType = inputClient.next();
                outMess.println(requestType);
                if (requestType.equals("redact")) {
                    System.out.println("Select ID of student that you want to change");
                    int idSel = inputClient.nextInt();
                    if (idSel < quantityStud) {

                        System.out.println("Get data for this ID ...");
                        outMess.println(idSel);
                        Student redStud = (Student) inObj.readObject();

                        System.out.println("Write your changes:");
                        System.out.print(redStud.getName() + " --> ");
                        redStud.setName(inputClient.next());
                        System.out.print(redStud.getSurname() + " --> ");
                        redStud.setSurname(inputClient.next());
                        System.out.print(redStud.getAge() + " --> ");
                        redStud.setAge(inputClient.nextInt());
                        System.out.print(redStud.getStudentID() + " --> ");
                        redStud.setStudentID(inputClient.nextInt());

                        System.out.println("Sending your change ...");
                        outObj.writeObject(redStud);

                        System.out.println(reader.readLine());
                    }
                }
                if (requestType.equals("new")){
                    System.out.println("Write your node of student:");
                    System.out.print("Name --> ");
                    String name = inputClient.next();
                    System.out.print("Surname --> ");
                    String surname = inputClient.next();
                    System.out.print("Age --> ");
                    int age = inputClient.nextInt();
                    System.out.print("StudentID --> ");
                    int studentID = inputClient.nextInt();
                    Student newStud = new Student(quantityStud,name,surname,age,studentID);
                    System.out.println("Sending your change ...");
                    outObj.writeObject(newStud);

                    System.out.println(reader.readLine());
                }
            } while (!requestType.equals("quit"));

        } catch (IOException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
