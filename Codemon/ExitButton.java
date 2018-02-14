package Codemon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.event.WindowEvent;

/*Name: Ryan Lafferty
ID: 0853370
Date: 10/18/2015
Assignment 2*/

public class ExitButton extends JButton implements ActionListener
{
    private MainGui reference = null;
    /*
    *Hello Button Constructor
    */
    public ExitButton()
    {
        super("Exit");
        this.addActionListener(this);
    }

    public ExitButton(MainGui ref)
    {
        super("Exit");
        this.addActionListener(this);
        this.reference = ref;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        System.out.println("Exit!!");
        WindowEvent w = new WindowEvent(this.reference, WindowEvent.WINDOW_CLOSING);
        /*if(this.reference != null && this.reference.fightOpen == false && this.reference.trainOpen == false)
        {
            //exit
        }
        else if(this.reference == null)
        {
            //report error
        }
        else
        {
            //prompt user to close windows
        }*/
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
