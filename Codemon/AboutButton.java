package Codemon;

import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/*Name: Ryan Lafferty
ID: 0853370
Date: 10/18/2015
Assignment 2*/

public class AboutButton extends JButton implements ActionListener
{
    private MainGui mainGUIRef;
    /*
    *Hello Button Constructor
    */
    public AboutButton()
    {
        super("About");
        this.addActionListener(this);
    }

    public AboutButton(MainGui ref)
    {
        super("About");
        this.addActionListener(this);
        this.mainGUIRef = ref;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        //System.out.println("Open About Frame!!");
        this.readFile();
    }

    private void readFile()
    {
        char [] c = {0};
        String bufferToWindow = "";
        FileReader reader = null;

        if(this.mainGUIRef == null)
        {
            return;
        }

        try
        {
            reader = new FileReader("README.txt");
            while(reader.read(c, 0, 1) != -1)
            {
                bufferToWindow = bufferToWindow + c[0];
            }
            JTextArea readMe = new JTextArea(bufferToWindow, 35, 50);
            readMe.setEditable(false);
            JScrollPane readMePane = new JScrollPane(readMe);
            JOptionPane.showMessageDialog(null, readMePane, "Ryan Lafferty 0853370", JOptionPane.DEFAULT_OPTION);
        }
        catch(Exception e)
        {
            System.out.println("Error: Could not read from file.");
        }
        finally
        {
            if(reader != null)
            {
                try
                {
                    reader.close();
                }
                catch(Exception e)
                {
                    System.out.println("Error: Failed to close the file.");
                }
            }
        }
    }

}
