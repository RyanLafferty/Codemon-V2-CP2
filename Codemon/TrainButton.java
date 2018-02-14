package Codemon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/*Name: Ryan Lafferty
ID: 0853370
Date: 10/18/2015
Assignment 2*/

public class TrainButton extends JButton implements ActionListener
{
    private MainGui reference = null;
    /*
    *Hello Button Constructor
    */
    public TrainButton()
    {
        super("Train");
        this.addActionListener(this);
    }

    public TrainButton(MainGui ref)
    {
        super("Train");
        this.addActionListener(this);
        this.reference = ref;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(this.reference.trainFrame == null)
        {
            this.reference.trainFrame = new Train(this.reference);
        }
    }

}
