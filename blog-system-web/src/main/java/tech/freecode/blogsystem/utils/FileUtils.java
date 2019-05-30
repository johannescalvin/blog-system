package tech.freecode.blogsystem.utils;

import java.io.*;
import java.util.*;

public class FileUtils {
    public static String  getFileContentFromDisk(InputStream inputStream){
        if (inputStream == null){
            return "";
        }

        StringBuilder builder = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = reader.readLine()) != null){
                builder.append(line+"\r\n");
            }
            reader.close();

        }catch (IOException ex){
            ex.printStackTrace();
        }
        String str = builder.toString();
        builder = null;
        return str;
    }
    public static String  getFileContentFromDisk(File file){
        if(!file.exists()){
            return "";
        }
        StringBuilder builder = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = null;
            while ((line = reader.readLine()) != null){
                builder.append(line+"\r\n");
            }
            reader.close();

        }catch (IOException ex){
            ex.printStackTrace();
        }
        String str = builder.toString();
        builder = null;
        return str;
    }

    public static String  getFileContentFromDisk(String fileName){
        return getFileContentFromDisk(new File(fileName));
    }

    public static void saveFileContent2Disk(String fileName,String content){
        try {
            File file = new File(fileName);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            FileWriter fileWriter = new FileWriter(file,false);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

    public static void writeArrayItemPerLine(String fileName, int[] array) {
        try{
            FileWriter fileWriter = new FileWriter(fileName);
            for(int i = 0; i < array.length; i++){
                fileWriter.append(i+"\t"+array[i]+"\r\n");
                if ( (i+1) % 1000 == 0){
                    fileWriter.flush();
                }
            }
            fileWriter.flush();
            fileWriter.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

    public static List<File> listFiles( String filename, String suffix){
        ArrayList<File> fileArrayList = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()){
            return fileArrayList;
        }

        listFiles(file,suffix,fileArrayList);
        return fileArrayList;
    }
    // sort sub files with modified time ascending
    private static List<File> listFiles(File file, String suffix,ArrayList<File> fileArrayList){



        if (file.isDirectory()) {
            File[] files = file.listFiles();

            for (File f : files) {
                listFiles(f,suffix,fileArrayList);
            }
        }else {
            if (file.getName().endsWith(suffix)){
                fileArrayList.add(file);
            }
        }

        return fileArrayList;
    }

    public static void writeItemPerLine(String path,String filename,Iterable<String> contents){
        try{
            FileWriter fileWriter = new FileWriter(path+filename);
            for (String content : contents){
                fileWriter.append(content+"\r\n");
            }
            fileWriter.flush();
            fileWriter.close();
        }catch (IOException e){

        }
    }

    public static Set<String> readUniqueItemPerLine(String path,String filename){
        Set<String> set = new TreeSet<>();
        File file = new File(path+filename);
        if (!file.exists()){
            return set;
        }

        BufferedReader reader = null ;
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = reader.readLine()) != null){
                set.add(line);
            }
            reader.close();
        }catch (IOException ex){

        }finally {
            return set;
        }
    }
}