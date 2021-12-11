package com.Didroid;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

//приложение сервера
public class Main {

    public static void main(String[] args) {

        //открытие порта сервера и парсинг файла со студентам
        try (ServerSocket server= new ServerSocket(3345)){
            DOMparser dp = new DOMparser("DataStore.xml");
            AllStudents allStudents = new AllStudents(dp.getStudents());
            int quantityStud = allStudents.getQuantity();

            //запуск прослушки сокета
            while (true) {
                try {

                    //при подключении клиента создание нового потока для данного клиента
                    Socket client = server.accept();
                    new Thread(() -> {
                        
                        // иннициализация объектов для чтения общения
                        try (BufferedReader reader =new BufferedReader(new InputStreamReader(client.getInputStream()));
                            ObjectOutputStream outObj = new ObjectOutputStream (client.getOutputStream());
                             ObjectInputStream inObj = new ObjectInputStream(client.getInputStream());
                            PrintWriter outMess = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true)){
                            System.out.println("Client accept");
                            while (!client.isClosed()){

                                //вывод информации о студентах для пользователя
                                outMess.println(quantityStud);
                                for (Student stud: allStudents.getStudentList()){
                                    outObj.writeObject(stud);
                                }
                                outObj.flush();
                                int reqVal;
                                String respCondition;
                                String requestType = reader.readLine();

                                //обработка запросов клиента
                                switch (requestType) {
                                    case "redact":
                                        System.out.println("Client want to redact ...");
                                        reqVal = Integer.parseInt(reader.readLine());
                                        System.out.println("Send node to redact ...");
                                        Student redStud = allStudents.getStudent(reqVal);
                                        outObj.writeObject(redStud);
                                        System.out.println("Waiting feedback ...");
                                        redStud = (Student)inObj.readObject();
                                        allStudents.redactStud(redStud,reqVal);
                                        dp.saveAllStud(allStudents.getStudentList());
                                        respCondition = "----Change successfully----";
                                        System.out.println(respCondition);
                                        outMess.println(respCondition);
                                        break;
                                    case "new":
                                        System.out.println("Client want to create new node ...");
                                        Student newStud = (Student)inObj.readObject();
                                        allStudents.addNewStud(newStud);
                                        dp.saveAllStud(allStudents.getStudentList());
                                        respCondition = "----Change successfully----";
                                        System.out.println(respCondition);
                                        outMess.println(respCondition);
                                        break;
                                    case "quit":
                                        System.out.println("Client disconnect");
                                        reader.close();
                                        outObj.close();
                                        inObj.close();
                                        outMess.close();
                                        client.close();
                                        break;
                                }
                            }
                        } catch (IOException | ClassNotFoundException e){
                            e.printStackTrace();
                        } finally {
                            try {
                                client.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
