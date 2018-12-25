package io;

import model.Date;
import model.Task;
import model.TaskBase;

import model.TaskFields;
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


public class TaskIO {
    public synchronized void loadTasksFromFile(String filename, TaskBase taskBase)throws Exception {
        File fXmlFile = new File(filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName(TaskFields.TASK);
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String[] in_date = eElement.getElementsByTagName(TaskFields.INCOME_DATE).item(0).getTextContent().split(" ");
                String[] out_date = eElement.getElementsByTagName(TaskFields.OUTCOME_DATE).item(0).getTextContent().split(" ");
                Task task = new Task(eElement.getElementsByTagName(TaskFields.NAME).item(0).getTextContent(),
                        eElement.getElementsByTagName(TaskFields.DESCRIPTION).item(0).getTextContent(),
                        eElement.getElementsByTagName(TaskFields.USER).item(0).getTextContent(),
                        eElement.getElementsByTagName(TaskFields.TASK_GIVER).item(0).getTextContent(),
                        new Date(in_date[0], in_date[1]),
                        new Date(out_date[0], out_date[1]),
                        eElement.getElementsByTagName(TaskFields.GROUP).item(0).getTextContent(),
                        Boolean.valueOf(eElement.getElementsByTagName(TaskFields.CONDITION).item(0).getTextContent()));
                taskBase.addTask(task);
            }
        }
    }

    public synchronized void saveTasksToFile(String filename, TaskBase taskBase) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement(TaskFields.TASKBASE);
        doc.appendChild(rootElement);

        for (int i = 0; i < taskBase.getTaskBase().size(); i++) {

            Element staff = doc.createElement(TaskFields.TASK);
            rootElement.appendChild(staff);

            Element name = doc.createElement(TaskFields.NAME);
            name.appendChild(doc.createTextNode(taskBase.getTaskBase().get(i).getName()));
            staff.appendChild(name);

            Element description = doc.createElement(TaskFields.DESCRIPTION);
            description.appendChild(doc.createTextNode(taskBase.getTaskBase().get(i).getDescription()));
            staff.appendChild(description);

            Element user = doc.createElement(TaskFields.USER);
            user.appendChild(doc.createTextNode(taskBase.getTaskBase().get(i).getUser()));
            staff.appendChild(user);

            Element task_giver = doc.createElement(TaskFields.TASK_GIVER);
            task_giver.appendChild(doc.createTextNode(taskBase.getTaskBase().get(i).getTaskGiver()));
            staff.appendChild(task_giver);

            Element income_date = doc.createElement(TaskFields.INCOME_DATE);
            income_date.appendChild(doc.createTextNode(taskBase.getTaskBase().get(i).getIncomeDate().toString()));
            staff.appendChild(income_date);

            Element outcome_date = doc.createElement(TaskFields.OUTCOME_DATE);
            outcome_date.appendChild(doc.createTextNode(taskBase.getTaskBase().get(i).getOutcomeDate().toString()));
            staff.appendChild(outcome_date);

            Element group = doc.createElement(TaskFields.GROUP);
            group.appendChild(doc.createTextNode(taskBase.getTaskBase().get(i).getGroup()));
            staff.appendChild(group);

            Element condition = doc.createElement(TaskFields.CONDITION);
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

