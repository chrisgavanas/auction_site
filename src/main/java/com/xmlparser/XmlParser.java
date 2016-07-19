package com.xmlparser;

import com.xmlparser.entitylist.AuctionItemList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Arrays;

public class XmlParser {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Folder with xml files is needed an argument");
            System.exit(-1);
        }

        File file = new File(args[0]);
        File[] datasetFolder = file.listFiles();
        if (datasetFolder == null)
            System.err.println("Path not found");


        Arrays.stream(datasetFolder).forEach(xmlDataset -> {
            try {
                if (!xmlDataset.isDirectory() && xmlDataset.getName().endsWith(".xml")) {
                    JAXBContext jaxbContext = JAXBContext.newInstance(AuctionItemList.class);
                    Unmarshaller jaxbMarshaller = jaxbContext.createUnmarshaller();
                    jaxbMarshaller.unmarshal(xmlDataset);   //TODO save or return to db
                }
            } catch (JAXBException e) {
                System.err.println("Xml parsing failed. Please check the input format.");
            }
        });


    }
}
