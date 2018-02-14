package Codemon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/*Name: Ryan Lafferty
ID: 0853370
Date: 10/18/2015
Assignment 2*/

public class FightButton extends JButton implements ActionListener
{
    private MainGui reference = null;
    /*
    *Hello Button Constructor
    */
    public FightButton()
    {
        super("Fight");
        this.addActionListener(this);
    }

    public FightButton(MainGui ref)
    {
        super("Fight");
        this.addActionListener(this);
        this.reference = ref;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(this.reference.fightFrame == null)
        {
            this.reference.fightFrame = new Fight(this.reference);
        }
    }

}
