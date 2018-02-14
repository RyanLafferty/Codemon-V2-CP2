package Codemon;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*Name: Ryan Lafferty
ID: 0853370
Date: 10/18/2015
Assignment 2*/

public class DirectoryDialog extends JFrame
{
    public int status = 0;

    public JButton okButton;
    public JButton cancelButton;
    public JButton chooseDirectoryButton;
    public JTextField userInput;
    public JLabel userPrompt;
    public JPanel mainPanel = new JPanel();

    DirectoryDialog(String dialogTitle)
    {
        super(dialogTitle);

        this.buildButtons();
        this.buildDialog();
        this.add(this.mainPanel);

        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setSize(640, 480);
        this.setVisible(true);
        this.repaint();
    }

    private void buildButtons()
    {
        this.okButton = new JButton("OK");
        //this.okButton.addActionListener(this);

        this.cancelButton = new JButton("Cancel");
        //this.cancelButton.addActionListener(this);

        this.chooseDirectoryButton = new JButton("Choose Directory");
        //this.chooseDirectoryButton.addActionListener(this);
    }

    private void buildDialog()
    {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        this.userPrompt = new JLabel("Direct Path:");
        this.userInput = new JTextField(25);

        this.mainPanel.add(this.userPrompt, gbc);
        gbc.gridy++;
        this.mainPanel.add(this.userInput, gbc);
        gbc.gridy++;


        this.mainPanel.add(this.okButton, gbc);
        gbc.gridx++;
        this.mainPanel.add(this.cancelButton, gbc);
        gbc.gridx++;
        this.mainPanel.add(this.chooseDirectoryButton, gbc);
        gbc.gridx++;
    }


   // @Override
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();
        if(action.equals("OK"))
        {
            this.status = 1;
        }
        else if(action.equals("Cancel"))
        {
            this.status = 2;
        }
        else if(action.equals("Choose Directory"))
        {
            this.status = 3;
        }
    }

    void addActionListener(Train aThis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
