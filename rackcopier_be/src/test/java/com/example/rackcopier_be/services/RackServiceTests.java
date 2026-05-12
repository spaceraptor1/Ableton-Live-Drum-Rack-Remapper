package com.example.rackcopier_be.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.example.rackcopier_be.helpers.RackServiceTestsHelper;

//@ConfigurationProperties(prefix = "rackservicetests")
@SpringBootTest
public class RackServiceTests {
    private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private static DocumentBuilder builder;
    private RackService rackService;
    //private RackServiceTestsConfig rstc = new RackServiceTestsConfig();
    private Logger log = LoggerFactory.getLogger(RackServiceTests.class);
    private FileWriter fw;

    private final String ADG_CONTENTS_DIRECTORY = System.getenv("Working_Dir") + "\\rackcopier_be\\src\\test\\resources\\adgcontents.txt";
    private final String TEST_ADG_FILE = System.getenv("Working_Dir") + "\\rackcopier_be\\src\\test\\java\\com\\example\\rackcopier_be\\resources\\phaser bernard c.adg";

    private final String SAMPLE_PATHS = RackServiceTestsHelper.read(System.getenv("Working_Dir") + "\\rackcopier_be\\src\\test\\java\\com\\example\\rackcopier_be\\resources\\sample_paths.txt");
    private final String REL_SAMPLE_PATHS = RackServiceTestsHelper.read(System.getenv("Working_Dir") + "\\rackcopier_be\\src\\test\\java\\com\\example\\rackcopier_be\\resources\\rel_sample_paths.txt");
    


    @BeforeEach
    void init() {
        try {

            log.atInfo().log("test adg file directory: " + TEST_ADG_FILE);
            log.atInfo().log("adgcontents directory: " + ADG_CONTENTS_DIRECTORY);
            log.atInfo().log("sample_paths: \n" + SAMPLE_PATHS);
            log.atInfo().log("rel_sample_paths: \n" + REL_SAMPLE_PATHS);

            rackService = new RackService();
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {

            e.printStackTrace();
        }
    }

    @Test
    void testGetPathList() {
        ArrayList<String> paths = rackService.getPathList(SAMPLE_PATHS);

        assertArrayEquals(RackServiceTestsHelper.getLines(SAMPLE_PATHS), paths.toArray());
    }

    @Test
    void testGetRelPaths() {
        ArrayList<String> paths = rackService.getPathList(SAMPLE_PATHS);
        ArrayList<String> relPaths = rackService.getRelPathList(paths);
        
        assertArrayEquals(relPaths.toArray(), RackServiceTestsHelper.getLines(REL_SAMPLE_PATHS));
    }

    @Test
    void copyRackTest() throws Exception {
        File file = new File("C:\\Users\\party\\OneDrive\\Desktop\\cmsc notes\\Reach out.adg");
        String path = "C:\\ableton\\saved samples\\m_drums\\LOOPS\\l8.wav";
        String relPath = "..\\..\\..\\..\\..\\..\\ableton\\saved samples\\m_drums\\LOOPS\\l8.wav";

        FileInputStream fileInputStream = new FileInputStream(file);
        InputStream gis = new GZIPInputStream(fileInputStream);
        //InputStreamReader isr = new InputStreamReader(gis, "UTF-8");
        Document doc = builder.parse(gis);

        Document output = rackService.copyRack(path, relPath, doc);
        File f = new File(System.getenv("Working_Dir") + "\\rackcopy\\src\\test\\resources\\adgcontents.txt");
        fw = new FileWriter(f);
        NodeList nodeList = output.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {

            Node node = nodeList.item(i);
            // node.normalize();
            //System.out.println(node.getNodeName());
            displayChildren(node, 0);

        }
        fw.close();
        gis.close();
        
    }

    void displayChildren(Node node, int layer) {
        NodeList nl = node.getChildNodes();
        int nll = nl.getLength();
        for (int i = 0; i < nl.getLength(); i++) {

            Node child = nl.item(i);
            String out = "";
            String s = "";
            for (int j = 0; j < layer; j++) {
                s = s + "    ";
            }

            if (child.getNodeType() == Node.ELEMENT_NODE) {
                // Text text = (Text) child;
                if (child.hasAttributes()) {
                    Node textAttributes = child.getAttributes().item(0);
                    out = ": " + textAttributes.getNodeName() + " = " + textAttributes.getNodeValue();
                }
            }

            try {
                if (child.getNodeName() != "#text")
                    fw.write(s + child.getNodeName() + out + "\n");
            } catch (Exception e) {
                // TODO: handle exception
            }

            if (child.hasChildNodes()) {
                displayChildren(child, layer + 1);
            }

        }

    }

    
}
