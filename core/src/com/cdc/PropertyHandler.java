package com.cdc;

import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class PropertyHandler {
    YamlReader reader;

    public static Object[] getProperties(File file){
        try {
            YamlReader reader = new YamlReader(new FileReader(file));
            List<Object> propertyList = new ArrayList();
            Object next = null;
            while((next = reader.read()) != null)
                propertyList.add(next);
            return propertyList.toArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
