package com.liontest;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;;


public class App 
{
    private static ArrayList<String> fileNames = null;
    private static Locale locale = Locale.Tradition_Zh;

    public static void main( String[] args )
    {
        try
        {
            analyzeArgs(args);
        }
        catch(IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
            return;
        }

        ExecutorService es = Executors.newFixedThreadPool(3);
        for(String fileName: fileNames)
        {
            Transition trans=new Transition(locale);
            trans.setFileName(fileName);
            es.submit(trans);
        }

        try{
            es.shutdown();
            es.awaitTermination(60, TimeUnit.SECONDS);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Read Command line and analysis the command line parameters
     * @param args the argument with "-" prefix stands for the transfer approach.
     *  "S" stands for transfer from Simple to Traditional Chinese
     *  "T" stands for transfer from Tradtional to Simple Chinese
     * @throws IllegalArgumentException
     */
    private static void analyzeArgs(String args[]) throws IllegalArgumentException
    {
        fileNames=new ArrayList<String>();
        if(args.length==0 || args==null)
        {
            throw new IllegalArgumentException("No argument is input!");
        }
        for(String arg : args)
        {
            if(arg.charAt(0)=='-')
            {
                String transferType = arg.substring(1);
                switch (transferType.toUpperCase())
                {
                    case "S": 
                        locale=Locale.Simple_Zh;
                        break;
                    case "T":
                        locale=Locale.Tradition_Zh;
                        break;
                    default:
                        locale=Locale.Tradition_Zh;
                        break;
                }
            }
            else
            {
                fileNames.add(arg);
            }
        }
        return;
    }
    
}
