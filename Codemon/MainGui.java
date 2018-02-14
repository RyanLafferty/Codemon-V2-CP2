package Codemon;

import java.awt.GridBagConstraints;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*Name: Ryan Lafferty
ID: 0853370
Date: 10/18/2015
Assignment 2*/

public class MainGui extends JFrame implements WindowListener, ActionListener
{
    public Codemon libraryReference = null;

    //object variables
    public File sourceDirectory = new File("./Source");
    public File codemonDirectory = new File("./Codemon");
    public File reportsDirectory = new File("./Reports");
    public File currentFile = null;
    public String currentFileString = null;
    public String sourceDirectoryString = "./Source";
    public String codemonDirectoryString = "./Codemon";
    public String reportsDirectoryString = "./Reports";
    public String fileName = null;

    public boolean trainOpen = false;
    public boolean fightOpen = false;

    //resolution values
    private int width = 640;
    private int height = 480;

    //Panels
    private JPanel mainPanel = new JPanel();

    //Menus

    //Sub Menus

    //Menu Items

    //Text Areas

    //Text Fields

    //Buttons
    private JButton trainButton = null;
    private JButton fightButton = null;
    private JButton aboutButton = null;
    private JButton exitButton = null;

    //Paths
    private String imageFilePath = "./mainSplash.png";

    //Files
    private File imageFile = null;

    //Icons
    private ImageIcon menuScreen = null;

    //Labels
    private JLabel welcomeLabel = null;
    private JLabel nameLabel = null;

    //Images
    private BufferedImage screen = null;

    //frames
    public Train trainFrame = null;
    public Fight fightFrame = null;

    /*
    *Constructor for gui interface
    */
    MainGui()
    {
        //build frame
        super("Codemon");
        this.setSize(this.width, this.height);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //build menu screen
        this.buildMainMenuScreen();

        //build panels
        this.buildMainPanel();

        //add panels
        this.add(this.mainPanel);

        this.addWindowListener(this);

        this.validate();
        this.repaint();
        this.pack();

        //display frame
        this.setVisible(true);
    }

    /*
    * Another Gui constructor
    */
    MainGui(Codemon libRef)
    {
        //build frame
        super("Codemon");
        this.setSize(this.width, this.height);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.libraryReference = libRef;

        //build menu screen
        this.buildMainMenuScreen();

        //build panels
        this.buildMainPanel();

        //add panels
        this.add(this.mainPanel);

        this.addWindowListener(this);

        this.validate();
        this.repaint();
        this.pack();

        //display frame
        this.setVisible(true);
    }

    /*
    *Builds the main menu screen.
    */
    private void buildMainMenuScreen()
    {
        try
        {
            this.imageFile = new File(this.imageFilePath);
            this.screen = ImageIO.read(this.imageFile);
            this.menuScreen = new ImageIcon(this.screen);
            this.welcomeLabel = new JLabel(this.menuScreen);
        }
        catch(Exception e)
        {
            System.out.println("Error: menu screen could not be loaded!");
        }
    }

    /*
    *Builds the main panel.
    */
    private void buildMainPanel()
    {
        //set up layout manager
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.mainPanel.setLayout(new GridBagLayout());

        //build buttons
        this.trainButton = new TrainButton(this);
        this.fightButton = new FightButton(this);
        this.aboutButton = new AboutButton(this);
        this.exitButton = new JButton("Exit");

        //build label
        this.nameLabel = new JLabel("CIS*2750F15 - Author: Ryan Lafferty");

        //add listeners
        this.exitButton.addActionListener(this);


        //add menu screen
        if(this.welcomeLabel != null)
        {
            this.mainPanel.add(this.welcomeLabel, gbc);
        }
        gbc.gridy++;

        //add buttons
        this.mainPanel.add(this.trainButton, gbc);
        gbc.gridy++;
        this.mainPanel.add(this.fightButton, gbc);
        gbc.gridy++;
        this.mainPanel.add(this.aboutButton, gbc);
        gbc.gridy++;
        this.mainPanel.add(this.exitButton, gbc);
        gbc.gridy++;
        this.mainPanel.add(this.nameLabel, gbc);
        gbc.gridy++;

        //display panel
        this.mainPanel.setVisible(true);
    }

    //action event handler
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();
        System.out.println(action);

        if(action.equals("Exit"))
        {
            this.windowClosing(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

    //window closing handler
    public void windowClosing(WindowEvent e)
    {
        if(this.trainFrame == null || this.trainFrame.modified == false)
        {
            System.exit(0);
        }
    }

    public void windowClosed(WindowEvent e)
    {
        //dont care
    }

    public void windowOpened(WindowEvent e)
    {
        //dont care
    }

    public void windowIconified(WindowEvent e)
    {
        //dont care
    }

    public void windowDeiconified(WindowEvent e)
    {
        //dont care
    }

    public void windowActivated(WindowEvent e)
    {
        //dont care
    }

    public void windowDeactivated(WindowEvent e)
    {
        //dont care
    }
}
