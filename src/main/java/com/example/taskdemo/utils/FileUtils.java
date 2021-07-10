package com.example.taskdemo.utils;

import java.io.*;
import java.nio.file.Files;

public class FileUtils
{
    private String getHtmlByPath(String filePath) throws IOException {
       
        File file = new File(filePath);
        if(file.exists())
        {
            try(FileInputStream fileInputStream = new FileInputStream(file);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));)
            {
                StringBuilder result = new StringBuilder();

                String line="";
                while ((line = bufferedReader.readLine()) !=null)
                {
                    result.append(line).append("\n");
                }
                return result.toString();
            }
        }
        return "";
    }
}
