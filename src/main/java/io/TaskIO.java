package io;

import model.Date;
import model.Task;
import model.TaskBase;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.Instant;

public class TaskIO {
    public static void main(String[] args) throws Exception{
       /* TaskIO taskIO = new TaskIO();
        TaskBase taskBase = new TaskBase();
        TaskBase loadedBase = new TaskBase();
        Task task1 = new Task("task1", "description1", "user1", "user1", new Date(java.util.Date.from(Instant.now()).toString()), new Date(java.util.Date.from(Instant.from(Instant.now()).plusSeconds(3)).toString()), "family", false);
        Task task2 = new Task("task2", "description2", "user2", "user2", new Date(java.util.Date.from(Instant.now()).toString()), new Date(java.util.Date.from(Instant.from(Instant.now()).plusSeconds(3)).toString()), "work", false);
        Task task3 = new Task("task3", "description3", "user3", "user1", new Date(java.util.Date.from(Instant.now()).toString()), new Date(java.util.Date.from(Instant.from(Instant.now()).plusSeconds(3)).toString()), "others", false);
        taskBase.addTask(task1);
        taskBase.addTask(task2);
        taskBase.addTask(task3);
        taskIO.saveTasksToFile("C:/Users/Anna/Desktop/tasks.xml", taskBase);
        taskIO.loadTasksFromFile("C:/Users/Anna/Desktop/tasks.xml", loadedBase);
        System.out.println(loadedBase);*/
    }

    public synchronized void loadTasksFromFile(String filename, TaskBase taskBase)throws Exception {

        File fXmlFile = new File(filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("task");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String[] in_date = eElement.getElementsByTagName("income_date").item(0).getTextContent().split(" ");
                String[] out_date = eElement.getElementsByTagName("outcome_date").item(0).getTextContent().split(" ");
                Task task = new Task(eElement.getElementsByTagName("name").item(0).getTextContent(),
                        eElement.getElementsByTagName("description").item(0).getTextContent(),
                        eElement.getElementsByTagName("user").item(0).getTextContent(),
                        eElement.getElementsByTagName("task_giver").item(0).getTextContent(),
                        new Date(in_date[0], in_date[1]),
                        new Date(out_date[0], out_date[1]),
                        eElement.getElementsByTagName("group").item(0).getTextContent(),
                        Boolean.valueOf(eElement.getElementsByTagName("condition").item(0).getTextContent()));
                taskBase.addTask(task);
            }
        }
    }

    public synchronized void saveTasksToFile(String filename, TaskBase taskBase) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("taskbase");
        doc.appendChild(rootElement);

        for (int i = 0; i < taskBase.getTaskBase().size(); i++) {

            Element staff = doc.createElement("task");
            rootElement.appendChild(staff);

            Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode(taskBase.getTaskBase().get(i).getName()));
            staff.appendChild(name);

            Element description = doc.createElement("description");
            description.appendChild(doc.createTextNode(taskBase.getTaskBase().get(i).getDescription()));
            staff.appendChild(description);

            Element user = doc.createElement("user");
            user.appendChild(doc.createTextNode(taskBase.getTaskBase().get(i).getUser()));
            staff.appendChild(user);

            Element task_giver = doc.createElement("task_giver");
            task_giver.appendChild(doc.createTextNode(taskBase.getTaskBase().get(i).getTaskGiver()));
            staff.appendChild(task_giver);

            Element income_date = doc.createElement("income_date");
            income_date.appendChild(doc.createTextNode(taskBase.getTaskBase().get(i).getIncomeDate().toString()));
            staff.appendChild(income_date);

            Element outcome_date = doc.createElement("outcome_date");
            outcome_date.appendChild(doc.createTextNode(taskBase.getTaskBase().get(i).getOutcomeDate().toString()));
            staff.appendChild(outcome_date);

            Element group = doc.createElement("group");
            group.appendChild(doc.createTextNode(taskBase.getTaskBase().get(i).getGroup()));
            staff.appendChild(group);

            Element condition = doc.createElement("condition");
            condition.appendChild(doc.createTextNode(String.valueOf(taskBase.getTaskBase().get(i).getCondition())));
            staff.appendChild(condition);
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filename));
        transformer.transform(source,result);
    }
}

