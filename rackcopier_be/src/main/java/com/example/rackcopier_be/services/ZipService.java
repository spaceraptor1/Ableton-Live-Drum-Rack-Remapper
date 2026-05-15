package com.example.rackcopier_be.services;

import java.io.OutputStream;
import java.util.ArrayList;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

@Service
public class ZipService {
    private Logger log = LoggerFactory.getLogger(ZipService.class);
    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer transformer;
    
    public ZipService(){
        try {
            transformer = tf.newTransformer();

        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
    }

    public byte[] zipFiles(ArrayList<Document> docs){

        DOMSource scource = new DOMSource();
        
        return null;
    }

    private OutputStream docToStream(Document doc){

        

        return null;
    }


}
