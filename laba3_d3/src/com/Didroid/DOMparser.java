package com.Didroid;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

//класс для парсинга файла с студентами
//и его перезаписью при изменениях
public class DOMparser {

    private String filename;
    private List<Student> students = new ArrayList<>();

    public DOMparser(String filename) {
        Document doc = null;
        this.filename = filename;
        try{
            doc = BuildDocument(filename);
        }   catch (Exception e){
            System.out.println("Open error: "+ e);
        }
        Node rootNode = doc.getFirstChild();
        NodeList dataListStud = rootNode.getChildNodes();

        int ID = 0;
        String name = "";
        String surname = "";
        int age = 0;
        int studentID = 0;
        for (int i=0;i<dataListStud.getLength();i++){
            if (dataListStud.item(i).getNodeType()!=Node.ELEMENT_NODE){
                continue;
            }

            NodeList element = dataListStud.item(i).getChildNodes();
            for (int j = 0; j < element.getLength();j++){
                if (element.item(j).getNodeType()!=Node.ELEMENT_NODE){
                    continue;
                }
                Node currentItem = element.item(j);
                switch (currentItem.getNodeName()) {
                    case "ID": {
                        ID = Integer.parseInt(currentItem.getTextContent());
                        break;
                    }
                    case "name": {
                        name = currentItem.getTextContent();
                        break;
                    }
                    case "surname": {
                        surname = currentItem.getTextContent();
                        break;
                    }
                    case "age": {
                        age = Integer.parseInt(currentItem.getTextContent());
                        break;
                    }
                    case "studentID": {
                        studentID = Integer.parseInt(currentItem.getTextContent());
                        break;
                    }
                }
            }

            students.add(new Student(ID,name,surname,age,studentID));
        }

    }

    public void saveAllStud(List<Student> studentList){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try{
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element rootElement = doc.createElement( "Data");
            doc.appendChild(rootElement);
            for (Student stud: studentList){
                rootElement.appendChild(createNode(doc,stud));
            }
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult fileNew = new StreamResult(new File(filename));
            transformer.transform(source, fileNew);
        }   catch (Exception e){
            System.out.println("Open error: "+ e);
        }
    }

    private Node createNode(Document file, Student stud){
        Element studNode = file.createElement("student");
        studNode.appendChild(createNodeElem(file,"ID",String.valueOf(stud.getID())));
        studNode.appendChild(createNodeElem(file,"name",stud.getName()));
        studNode.appendChild(createNodeElem(file,"surname",stud.getSurname()));
        studNode.appendChild(createNodeElem(file,"age",String.valueOf(stud.getAge())));
        studNode.appendChild(createNodeElem(file,"studentID",String.valueOf(stud.getStudentID())));
        return studNode;
    }

    private Node createNodeElem(Document file,String nameTag, String valTag){
        Element node = file.createElement(nameTag);
        node.appendChild(file.createTextNode(valTag));
        return node;
    }

    public List<Student> getStudents() {
        return students;
    }

    private static Document BuildDocument(String filename) throws Exception{
        File file = new File(filename);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        return dbf.newDocumentBuilder().parse(file);
    }

}
