package com.example.rackcopier_be.helpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class RackServiceTestsHelper {

    public static String read(String path){

        try {
            return Files.readString(Path.of(path));

        } catch (FileNotFoundException ex) {
            System.getLogger(RackServiceTestsHelper.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);

        } catch (IOException e) {
            System.getLogger(RackServiceTestsHelper.class.getName()).log(System.Logger.Level.ERROR, (String) null, e);
            
        }

        return null;
    }

    public static Object[] getLines(String string){

        Object[] objArray = string.lines().toArray();
        String[] strArray = Arrays.copyOf(objArray, objArray.length, String[].class);

        ArrayList<String> outputList = new ArrayList<String>(1);
        for (String line : strArray) {
            outputList.add(line.replaceAll("\"", ""));
        }

        Object[] output = outputList.toArray();

        return output;


    }
}
