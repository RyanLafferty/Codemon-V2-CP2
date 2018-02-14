/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Codemon;

import java.io.*;


/**
 *
 * @author Lafferty
 */
public class Codemon
{

    //get c lib
    static
    {
        System.loadLibrary("codemon");
    }

    //define c methods
    native int compileToFile(String f1, String f2);
    native int testJni(String file, String name, int limit);
    native int selfTestJni(String file1, String file2, String name1, String name2, int limit);
    native int pvpJni(int players, String file, String name);
    native int reportJni(String reportID, String fileName);

/****************LEGACY CMD LINE FUNCTIONS*******************************/
    native int compile(String file);
    native int test(String file, int limit);
    native int selfTest(String file1, String file2, int limit);
    native int pvp(int players, String file);
    native int getReport(String reportID);
/*****************USED FOR DEBUGGING***********************************/
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        //int retVal = 0;
        //String reportID = "";
        //Codemon myCodemon = new Codemon();
        //use for debug
        //myCodemon.compileToFile("Source/kraken.cm", "Codemon/kraken.codemon");
        //retVal = myCodemon.testJni("Codemon/kraken.codemon", "krakenAssemble", 0);
        //retVal = myCodemon.selfTestJni("Codemon/kraken.codemon", "Codemon/kraken.codemon", "krakenAssemble1", "krakenAssemble2", 0);
        //retVal = myCodemon.pvpJni(2, "Codemon/kraken.codemon", "krakenAssemble");
        //retVal = myCodemon.pvpJni(2, "Codemon/kraken.codemon", "krakenAssemble");


        //System.out.println(retVal);
        //reportID = Integer.toString(retVal);
        //myCodemon.reportJni(reportID, "Reports/tempReport.txt");

        //myCodemon.getReport("1000");
        MainGui codemon = new MainGui();
    }

}
