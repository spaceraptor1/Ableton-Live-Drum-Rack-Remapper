package com.example.rackcopier_be.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Service
public class RackService {
    private final DocumentBuilderFactory factory;
    private final DocumentBuilder builder;
    private Logger log = LoggerFactory.getLogger(RackService.class);

    @Autowired
    public RackService() throws ParserConfigurationException {
        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();

    }

    public void copyRacks(String file_paths, MultipartFile mpf) throws IOException, SAXException {
        InputStream gis = new GZIPInputStream(mpf.getInputStream());
        Document doc = builder.parse(gis);
        gis.close();

        ArrayList<String> paths = getPathList(file_paths);
        ArrayList<String> relPaths = getRelPathList(paths);
        ArrayList<Document> docs = new ArrayList<>(1);

        for (int i = 0; i < paths.size(); i++) {
            docs.add(copyRack(paths.get(i), relPaths.get(i), doc));
        }

    }

    public Document copyRack(String path, String relPath, Document document) {
        log.atInfo().log("Creating rack for: " + path);
        Document doc = (Document) document.cloneNode(true);
        NodeList sampleParts = doc.getElementsByTagName("SampleParts");

        log.atDebug().log("Changing " + sampleParts.getLength() + " file references.");

        for (int i = 0; i < sampleParts.getLength(); i++) {
            Node sampleRef = sampleParts.item(i).getChildNodes().item(1).getChildNodes().item(37).getChildNodes()
                    .item(1);

            log.atDebug().log(sampleRef.getChildNodes().item(3).getAttributes().item(0).getNodeValue());

            Node relPathNode = sampleRef.getChildNodes().item(3);
            Node pathNode = sampleRef.getChildNodes().item(5);

            relPathNode.getAttributes().item(0).setNodeValue(relPath);
            pathNode.getAttributes().item(0).setNodeValue(path);

        }

        return doc;
    }

    public ArrayList<String> getPathList(String file_paths) {
        log.atInfo().log("Parsing target file paths.");
        ArrayList<String> paths = new ArrayList<>(1);
        ArrayList<Integer> seperator = new ArrayList<>(1);
        String seperatorDebug = "";

        for (int i = 0; i < file_paths.length(); i++) {
            if (file_paths.charAt(i) == '"') {
                seperator.add(i);
                seperatorDebug += i + " ";
            }
        }
        log.atDebug().log("seperator indexes for: " + file_paths + ":\n" + seperatorDebug);

        for (int i = 1; i < seperator.size(); i += 2) {
            log.atDebug().log("adding: " + file_paths.substring(seperator.get(i - 1) + 1, seperator.get(i)));
            paths.add(file_paths.substring(seperator.get(i - 1) + 1, seperator.get(i)));
        }

        return paths;
    }

    private String getRelPath(String path) {
        Path base = Path.of("C:/Users/party/OneDrive/Documents/Ableton/User Library");
        Path target = Path.of(path);
        String relPath = base.relativize(target).toString();

        log.atDebug().log(path + " relatavised to: \n" + relPath);
        return relPath;

    }

    public ArrayList<String> getRelPathList(ArrayList<String> paths) {
        log.atInfo().log("Getting relative paths.");
        ArrayList<String> relPaths = new ArrayList<>(1);
        for (String path : paths) {
            relPaths.add(getRelPath(path));
        }

        return relPaths;
    }
}