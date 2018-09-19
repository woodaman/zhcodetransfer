package com.liontest;

import java.util.ResourceBundle;
import java.io.*;

public class Transition implements Runnable
{
    private ResourceBundle mappingTable = null;
    private String bundle_name = null;
    private String fileName = null;

    public void setFileName(String name){
        fileName=name;
    }

    /**
     * Default Tansition from Traditional Chinese to Simple Chinese
     */
    public Transition()
    {
        if(bundle_name==null)
        {
            bundle_name="TraditionToSimple_zh_CN";
        }
    }

    public Transition(Locale from)
    {
        if(from==Locale.Tradition_Zh)
        {
            bundle_name="TraditionToSimple_zh_CN"; 
        }
        else
        {
            bundle_name="SimpleToTradition_zh_CN";
        }
        
    }

    public String transfer(String inputString)
    {
        if (bundle_name!=null)
            mappingTable=ResourceBundle.getBundle(bundle_name);  
        if(inputString==null){
            throw new IllegalArgumentException("Input is null");
        }

        StringBuilder sb = new StringBuilder();

        if(inputString.length()>1 && mappingTable.containsKey(inputString)){
            return mappingTable.getString(inputString);
        }

        for(char ch : inputString.toCharArray())
        {
            String singleCharacter = String.valueOf(ch);
            sb.append(mappingTable.containsKey(singleCharacter) ? mappingTable.getString(singleCharacter):singleCharacter);
        }

        return sb.toString();
    }

    /**
     * Read file content into a String
     * @param fileName
     * File Name
     * @return
     * The file content with a String.
     */
    public String readFiles(String fileName)
    {
         StringBuilder sb = new StringBuilder();
         // Read files
         System.out.println("Read "+ fileName);
         try(BufferedReader reader=new BufferedReader(new FileReader(fileName))){
            String line=null;
            while((line=reader.readLine())!=null)
            {
                String transLine = this.transfer(line);
                //System.out.println(transLine);
                sb.append(transLine);
                sb.append(System.getProperty("line.separator")); // Add line break
            }
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
            return "File cannot be read";
        }
        return sb.toString();   
    }

    /**
     * Write the content into the files
     * @param fileName
     * file name with full path
     * @param fileContent
     * String content to be write into the file
     */
    public void writeFiles(String fileName,String fileContent)
    {
        System.out.println("Writing "+ fileName);
        try(BufferedWriter writer=new BufferedWriter(new FileWriter(fileName))){
            writer.write(fileContent);
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
        return;
    }

    public void run()
    {
        if(fileName!=null)
        {
            writeFiles(fileName, readFiles(this.fileName));
        }
    }
}