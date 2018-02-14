package Codemon;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/*Name: Ryan Lafferty
ID: 0853370
Date: 10/18/2015
Assignment 2*/


public class Train extends JFrame implements KeyListener, ActionListener, WindowListener
{
    MainGui codemonReference = null;

    //file chooser
    private JFileChooser fc;

    //directory choosers
    DirectoryDialog changeSource = null;
    DirectoryDialog changeCodemon = null;

    //File filters
    private FileNameExtensionFilter allFileFilter = null;
    private FileNameExtensionFilter codeFileFilter = null;
    private FileNameExtensionFilter binaryFileFilter = null;



    //file variables
    public File sourceDirectory = new File("./Source");
    public File codemonDirectory = new File("./Codemon");
    public File currentFile = null;
    public String currentFileString = null;
    public String sourceDirectoryString = "./Source";
    public String codemonDirectoryString = "./Codemon";
    public String fileName = null;
    public boolean modified = false;
    public boolean opened = false;
    public int fcReturn = 0;
    private boolean ctrlPressed = false;

    //resolution values
    private int width = 640;
    private int height = 480;

    //Panels
    private JPanel mainPanel = new JPanel();

    //Menu Bars
    private JMenuBar mainMenu = new JMenuBar();

    //Menus
    private JMenu fileMenu = null;
    private JMenu buildMenu = null;
    private JMenu configMenu = null;
    private JMenu helpMenu = null;

    //menu items
    private JMenuItem newFileMenuItem = null;
    private JMenuItem openFileMenuItem = null;
    private JMenuItem saveFileMenuItem = null;
    private JMenuItem saveAsFileMenuItem = null;
    private JMenuItem quitFileMenuItem = null;

    private JMenuItem assembleBuildMenuItem = null;
    private JMenuItem assembleLaunchMenuItem = null;

    private JMenuItem sourceDirConfigMenuItem = null;
    private JMenuItem codemonDirConfigMenuItem = null;

    private JMenuItem helpMenuItem = null;

    //Text Fields
    private JTextField fileNameIndicator = new JTextField("NEW FILE", 25);
    private JTextField fileModifiedIndicator = new JTextField("", 10);

    //Text Areas
    private JTextArea textWindow = new JTextArea(20,40);

    //Scroll Pane
    private JScrollPane textPane = new JScrollPane(this.textWindow);

    //Button icons
    private ImageIcon newIcon = null;
    private ImageIcon openIcon = null;
    private ImageIcon saveIcon = null;
    private ImageIcon saveAsIcon = null;
    private ImageIcon assembleIcon = null;

    //Buttons
    private JButton newButton = null;
    private JButton openButton = null;
    private JButton saveButton = null;
    private JButton saveAsButton = null;
    private JButton assembleButton = null;

    //Toolbars
    private JToolBar toolBar = new JToolBar();

    /*
    *Constructor for gui interface
    */
    public Train()
    {
        //build frame
        super("Codemon Training Center");
        this.setSize(this.width, this.height);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //build menu
        this.buildMenu();

        //set menu bar
        this.setJMenuBar(this.mainMenu);

        //build panels
        this.buildMainPanel();

        //configure file chooser
        this.buildFileChooser();

        //add panels
        this.add(this.mainPanel);

        //display frame
        this.setVisible(true);
    }

    /*
    *Constructor for gui interface
    */
    public Train(MainGui reference)
    {
        //build frame
        super("Codemon Training Center");
        this.setSize(this.width, this.height);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //update global variables
        this.codemonReference = reference;
        this.updateGlobalVariables();

        //build menu
        this.buildMenu();

        //set menu bar
        this.setJMenuBar(this.mainMenu);

        //build panels
        this.buildMainPanel();

        //configure file chooser
        this.buildFileChooser();

        //add panels
        this.add(this.mainPanel);

        this.addWindowListener(this);

        //display frame
        this.setVisible(true);

        this.pack();
        this.validate();
        this.repaint();
    }

    /*
    Def: Updates the object varaiables.
    Args:
    Ret:
    */
    private void updateGlobalVariables()
    {
        if(this.codemonReference != null)
        {
            this.sourceDirectory = null;
            this.codemonDirectory = null;

            this.sourceDirectory = this.codemonReference.sourceDirectory;
            this.codemonDirectory = this.codemonReference.codemonDirectory;
            this.sourceDirectoryString = this.codemonReference.sourceDirectoryString;
            this.codemonDirectoryString = this.codemonReference.codemonDirectoryString;
        }
    }

    /*
    *Builds the menus.
    */
    private void buildMenu()
    {
        //build menu items
        this.newFileMenuItem = new JMenuItem("New");
        this.openFileMenuItem = new JMenuItem("Open");
        this.saveFileMenuItem = new JMenuItem("Save");
        this.saveAsFileMenuItem = new JMenuItem("Save As");
        this.quitFileMenuItem = new JMenuItem("Quit");

        this.assembleBuildMenuItem = new JMenuItem("Assemble");
        this.assembleLaunchMenuItem = new JMenuItem("Assemble and Launch");

        this.sourceDirConfigMenuItem = new JMenuItem("Source Directory");
        this.codemonDirConfigMenuItem = new JMenuItem("Codemon Directory");

        this.helpMenuItem = new JMenuItem("Help");

        //add action listeners
        this.newFileMenuItem.addActionListener(this);
        this.openFileMenuItem.addActionListener(this);
        this.saveFileMenuItem.addActionListener(this);
        this.saveAsFileMenuItem.addActionListener(this);
        this.quitFileMenuItem.addActionListener(this);
        this.assembleBuildMenuItem.addActionListener(this);
        this.assembleLaunchMenuItem.addActionListener(this);
        this.sourceDirConfigMenuItem.addActionListener(this);
        this.codemonDirConfigMenuItem.addActionListener(this);
        this.helpMenuItem.addActionListener(this);

        //build menus
        this.fileMenu = new JMenu("File");
        this.buildMenu = new JMenu("Build");
        this.configMenu = new JMenu("Config");
        this.helpMenu = new JMenu("Help");

        //add menu items
        this.fileMenu.add(this.newFileMenuItem);
        this.fileMenu.add(this.openFileMenuItem);
        this.fileMenu.add(this.saveFileMenuItem);
        this.fileMenu.add(this.saveAsFileMenuItem);
        this.fileMenu.add(this.quitFileMenuItem);

        this.buildMenu.add(this.assembleBuildMenuItem);
        this.buildMenu.add(this.assembleLaunchMenuItem);

        this.configMenu.add(this.sourceDirConfigMenuItem);
        this.configMenu.add(this.codemonDirConfigMenuItem);

        this.helpMenu.add(this.helpMenuItem);

        //add menus
        this.mainMenu.add(this.fileMenu);
        this.mainMenu.add(this.buildMenu);
        this.mainMenu.add(this.configMenu);
        this.mainMenu.add(this.helpMenu);

        //add Labels
        this.sourceDirConfigMenuItem.setText(("Source Directory (" + this.sourceDirectory.getPath() + ")"));
        this.codemonDirConfigMenuItem.setText(("Codemon Directory (" + this.codemonDirectory.getPath() + ")"));
    }

    /*
    Def: Builds the menu toolbar.
    Args:
    Ret:
    */
    private void buildToolBar()
    {
        this.newIcon = null;
        this.openIcon = null;
        this.saveIcon = null;
        this.saveAsIcon = null;
        this.assembleIcon = null;

        //build icons
        this.newIcon = new ImageIcon("./Icons/newIcon.png");
        this.openIcon = new ImageIcon("./Icons/openIcon.png");
        this.saveIcon = new ImageIcon("./Icons/saveIcon.png");
        this.saveAsIcon = new ImageIcon("./Icons/saveAsIcon.png");
        this.assembleIcon = new ImageIcon("./Icons/assembleIcon.png");

        //build buttons
        if(this.newIcon == null || this.openIcon == null || this.saveIcon == null ||
           this.saveAsIcon == null || this.assembleIcon == null)
        {
            this.newButton = new JButton("New");
            this.openButton = new JButton("Open");
            this.saveButton = new JButton("Save");
            this.saveAsButton = new JButton("Save As");
            this.assembleButton = new JButton("Assemble");
        }
        else
        {
            this.newButton = new JButton("New", this.newIcon);
            this.openButton = new JButton("Open", this.openIcon);
            this.saveButton = new JButton("Save", this.saveIcon);
            this.saveAsButton = new JButton("Save As", this.saveAsIcon);
            this.assembleButton = new JButton("Assemble", this.assembleIcon);
        }

        //add action listeners
        this.newButton.addActionListener(this);
        this.openButton.addActionListener(this);
        this.saveButton.addActionListener(this);
        this.saveAsButton.addActionListener(this);
        this.assembleButton.addActionListener(this);

        //add buttons to toolbar
        this.toolBar.add(this.newButton);
        this.toolBar.add(this.openButton);
        this.toolBar.add(this.saveButton);
        this.toolBar.add(this.saveAsButton);
        this.toolBar.add(this.assembleButton);

        //set up toolbar
        this.toolBar.setFloatable(false);
    }


    /*
    *Builds the main panel.
    */
    private void buildMainPanel()
    {
        //set up layout manager
        GridBagConstraints gbc = new GridBagConstraints();
        this.mainPanel.setLayout(new GridBagLayout());
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;

         //build toolbar
        this.buildToolBar();

        //add key listener
        this.textWindow.addKeyListener(this);

        //add toolbar
        this.mainPanel.add(this.toolBar, gbc);
        gbc.gridy++;

        this.fileNameIndicator.setEditable(false);
        this.mainPanel.add(this.fileNameIndicator, gbc);
        gbc.gridy++;

        this.mainPanel.add(this.textPane, gbc);
        gbc.gridy++;

        this.fileModifiedIndicator.setEditable(false);
        this.mainPanel.add(this.fileModifiedIndicator, gbc);

        //display panel
        this.mainPanel.setVisible(true);
    }

    /*
    Def: Buidlds the file chooser that allows the user to select directories and files.
    Args:
    Ret:
    */
    private void buildFileChooser()
    {
         //Create File Chooser
        this.fc = new JFileChooser();

        //Create File filters
        this.allFileFilter = new FileNameExtensionFilter("All Codemon File Types", "cm", "codemon");
        this.codeFileFilter = new FileNameExtensionFilter("All Codemon Source Files", "cm");
        this.binaryFileFilter = new FileNameExtensionFilter("All Codemon Binary Files", "codemon");

        //configure filters
        this.fc.addChoosableFileFilter(this.codeFileFilter);
        this.fc.addChoosableFileFilter(this.binaryFileFilter);
        this.fc.addChoosableFileFilter(this.allFileFilter);
        //this.fc.setFileFilter(this.codeFileFilter);
    }

    /*
    Def: Save a file as a specified name.
    Args:
    Ret:
    */
    private void saveFileAs()
    {
        JOptionPane saveAsPane = new JOptionPane("Save As", JOptionPane.PLAIN_MESSAGE,
                                                 JOptionPane.DEFAULT_OPTION);
        String currentFileName = "";

        if(this.currentFile != null)
        {
            currentFileName = this.currentFile.getName();
        }

        this.currentFileString = saveAsPane.showInputDialog(this, "Please enter a file name: ", currentFileName);
        if(this.currentFileString == null || this.currentFileString.length() <= 0)
        {
            this.currentFileString = saveAsPane.showInputDialog(this, "Warning: Please enter a file name: ", currentFileName, JOptionPane.WARNING_MESSAGE);
        }
        while(this.currentFileString == null || this.currentFileString.length() <= 0)
        {
            this.currentFileString = saveAsPane.showInputDialog(this, "Error: Please enter a file name: ", currentFileName, JOptionPane.ERROR_MESSAGE);
        }

        saveAsPane = null;
        currentFileName = null;

        if(this.currentFileString.contains(".") == false)
        {
            this.currentFileString = this.currentFileString + ".cm";
        }

        this.currentFileString = this.sourceDirectory.getPath() + "/" + this.currentFileString;
        this.currentFile = new File(this.currentFileString);
        try
        {
            this.currentFile.createNewFile();
            this.writeFile();
            this.fileName = this.currentFile.getName();
        }
        catch(Exception e)
        {
            System.out.println("Error: Failed to create new file.");
        }
    }

    /*
    Def: Save the current file.
    Args:
    Ret:
    */
    private void saveFile()
    {
        this.fcReturn = this.fc.showOpenDialog(null);
        if(this.fcReturn == JFileChooser.APPROVE_OPTION)
        {
            this.currentFile = fc.getSelectedFile();
            this.currentFileString = this.currentFile.getPath();
            System.out.println("Save: " + this.currentFileString);
            this.writeFile();
            this.fileName = this.currentFile.getName();
        }
    }

    /*
    Def: Open's a specified file given by the user.
    Args:
    Ret:
    */
    private void openFile()
    {
        this.fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.fcReturn = this.fc.showOpenDialog(null);
        if(this.fcReturn == JFileChooser.APPROVE_OPTION)
        {
            this.currentFile = fc.getSelectedFile();
            this.currentFileString = this.currentFile.getPath();
            System.out.println(this.currentFileString);
            this.opened = true;
            this.fileName = this.currentFile.getName();
        }
    }

    /*
    Def: Reads a file into the text area.
    Args:
    Ret:
    */
    private void readFile()
    {
        char [] c = {0};
        String bufferToWindow = "";
        FileReader reader = null;

        try
        {
            reader = new FileReader(this.currentFile);
            while(reader.read(c, 0, 1) != -1)
            {
                bufferToWindow = bufferToWindow + c[0];
            }
            this.textWindow.setText(bufferToWindow);
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

    /*
    Def: Writes data from the text area into the current file.
    Args:
    Ret:
    */
    private void writeFile()
    {
        String bufferToFile = this.textWindow.getText();
        FileWriter writer = null;

        try
        {
            writer = new FileWriter(this.currentFile);
            //System.out.println("Buffer to file = " + bufferToFile);
            writer.write(bufferToFile);
        }
        catch(Exception e)
        {
            System.out.println("Error: Could not write to file.");
        }
        finally
        {
            if(writer != null)
            {
                try
                {
                    writer.close();
                }
                catch(Exception e)
                {
                    System.out.println("Error: Failed to close the file.");
                }
            }
        }
    }

    /*
    Def: Opens a current directory specified by the user.
    Args: The mode value (int), used to determine which directory to change.
    Ret:
    */
    private void openDirectory(int mode)
    {
        this.fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.fcReturn = this.fc.showOpenDialog(null);
        if(this.fcReturn == JFileChooser.APPROVE_OPTION)
        {
            if(mode == 1)
            {
                this.sourceDirectory = this.fc.getSelectedFile();
                this.sourceDirectoryString = this.sourceDirectory.getPath();
                //System.out.println(this.sourceDirectoryString);
            }
            else if(mode == 2)
            {
                this.codemonDirectory = this.fc.getSelectedFile();
                this.codemonDirectoryString = this.codemonDirectory.getPath();
                //System.out.println(this.codemonDirectoryString);
            }
            else
            {

            }
        }
    }

    /*
    Def: Allows the user to change the source directory.
    Args:
    Ret:
    */
    private void changeSourceDirectory()
    {
        this.changeSource = new DirectoryDialog("Change Source Directory");
        if(this.changeSource != null)
        {
            this.changeSource.okButton.addActionListener(this);
            this.changeSource.cancelButton.addActionListener(this);
            this.changeSource.chooseDirectoryButton.addActionListener(this);
        }
    }

    /*
    Def: Allows the user to change the codemon directory.
    Args:
    Ret:
    */
    private void changeCodemonDirectory()
    {
        this.changeCodemon = new DirectoryDialog("Change Codemon Directory");
        if(this.changeCodemon != null)
        {
            this.changeCodemon.okButton.addActionListener(this);
            this.changeCodemon.cancelButton.addActionListener(this);
            this.changeCodemon.chooseDirectoryButton.addActionListener(this);
        }
    }

    /*
    Def: Assembles a codemon into a binary blob.
    Args:
    Ret:
    */
    private void assembleCodemon()
    {
        if(this.currentFileString != null && this.currentFileString.length() > 0)
        {
            int returnVal = 0;
            int index = -1;
            String outputFile = null;
            String outFile = null;
            Codemon lib = new Codemon();
            File outFileCheck;

            outputFile = this.currentFile.getName();
            index = outputFile.indexOf(".");
            if(index > 0)
            {
                outFile = outputFile.substring(0, index);
            }
            outFile = this.codemonDirectoryString + "/" + outFile + ".codemon";

            outFileCheck = new File(outFile);

            if(outFileCheck.exists() == true)
            {
                int choice = 0;
                Object choices [] = {"Yes", "No"};

                choice = JOptionPane.showOptionDialog
                (this, "Overwrite the existing file?", "Overwrite File",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, choices, choices[0]);

                //System.out.println(choice);

                if(choice == 1)
                {
                    return;
                }
            }

            if(outFile != null && this.currentFileString != null)
            {
                returnVal = lib.compileToFile(this.currentFileString, outFile);
                if(returnVal < 0)
                {
                    this.readFromFileFast("ERRORFILE.txt");
                }
            }

            lib = null;
        }
    }

    /*
    Def: Handles keys typed in the text area to determine if the file has been modified
    Args: The key event to be handled (KeyEvent).
    Ret:
    */
    @Override
    public void keyTyped(KeyEvent e)
    {
        this.modified = true;
        this.fileModifiedIndicator.setText("Modified");
        this.repaint();
    }

    /*
    Def: Handles the keyboard acceleration.
    Args: The key event (KeyEvent) used for the keyboard acceleration.
    Ret:
    */
    @Override
    public void keyPressed(KeyEvent e)
    {
        //System.out.println(e.getKeyText(e.getKeyCode()));
        if(e.getKeyCode() == e.VK_F1)
        {
            this.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Save"));
        }
        else if(e.getKeyCode() == e.VK_F2)
        {
            this.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Save As"));
        }
        else if(e.getKeyCode() == e.VK_F3)
        {
            this.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Open"));
        }
        else if(e.getKeyCode() == e.VK_F4)
        {
            this.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "New"));
        }
        else if(e.getKeyCode() == e.VK_F5)
        {
            this.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Assemble"));
        }
        else if(e.getKeyCode() == e.VK_F6)
        {
            this.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Assemble and Launch"));
        }
        else if(e.getKeyCode() == e.VK_F7)
        {
            this.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Source Directory"));
        }
        else if(e.getKeyCode() == e.VK_F8)
        {
            this.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Codemon Directory"));
        }
        else if(e.getKeyCode() == e.VK_F9)
        {
            this.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Help"));
        }
        else if(e.getKeyCode() == e.VK_F10)
        {
            this.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Quit"));
        }
        else
        {

        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        //not used
    }

    /*
    Def: Handles user actions.
    Args: The action event to handle (ActionEvent).
    Ret:
    */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();
        //System.out.println(action);

        this.updateGlobalVariables();

        if(action.equals("New"))
        {
            //this.fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if(this.modified == true && this.opened == true)
            {
                this.saveFile();
            }
            else if(this.modified == true && this.opened == false)
            {
                this.saveFileAs();
                //System.out.println("New file created: " + this.currentFileString);
            }

            this.textWindow.setText("");
            this.fileModifiedIndicator.setText("");
            this.opened = false;
            this.modified = false;
            this.currentFile = null;
            this.currentFileString = "";
            this.fileName = "";
        }
        else if(action.equals("Open"))
        {
            this.fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            this.fc.setCurrentDirectory(this.sourceDirectory);
            if(this.modified == true && this.opened == false)
            {
                this.saveFileAs();
                this.openFile();
                this.readFile();
            }
            else if(this.modified == true && this.opened == true)
            {
                this.saveFile();
                this.openFile();
                this.readFile();
            }
            else
            {
                this.openFile();
                this.readFile();
            }

            this.fileModifiedIndicator.setText("");
            this.modified = false;
            this.opened = true;
        }
        else if(action.equals("Save"))
        {
            this.fc.setCurrentDirectory(this.sourceDirectory);
            if(this.opened == true)
            {
                this.saveFile();
            }
            else
            {
                this.saveFileAs();
            }

            this.fileModifiedIndicator.setText("");
            this.modified = false;
            this.opened = true;
        }
        else if(action.equals("Save As"))
        {
            this.saveFileAs();

            this.fileModifiedIndicator.setText("");
            this.modified = false;
            this.opened = true;
        }
        else if(action.equals("Quit"))
        {
            if(this.modified == true && this.opened == true)
            {
                this.saveFile();
            }
            else if(this.modified == true && this.opened == false)
            {
                this.saveFileAs();
            }

            if(this.codemonReference != null)
            {
                this.updateParentGlobalVariables();
                this.codemonReference.trainFrame = null;
            }
            this.dispose();
        }
        else if(action.equals("Assemble"))
        {
            this.assembleCodemon();
        }
        else if(action.equals("Assemble and Launch"))
        {
            this.assembleCodemon();
            if(this.codemonReference.fightFrame == null)
            {
                this.updateParentGlobalVariables();
                this.codemonReference.fightFrame = new Fight(this.codemonReference);
            }
        }
        else if(action.contains("Source Directory"))
        {
            if(this.changeSource == null)
            {
                this.fc.setCurrentDirectory(this.sourceDirectory);
                this.changeSourceDirectory();
            }
        }
        else if(action.contains("Codemon Directory"))
        {
            if(this.changeCodemon == null)
            {
                this.fc.setCurrentDirectory(this.codemonDirectory);
                this.changeCodemonDirectory();
            }
        }
        else if(action.equals("Help"))
        {
            this.readHelpFile();
        }
        else if(action.equals("OK"))
        {
            if(this.changeSource != null)
            {
                this.sourceDirectoryString = this.changeSource.userInput.getText();
                if(this.sourceDirectoryString != null && this.sourceDirectoryString.length() > 0)
                {
                    this.sourceDirectory = null;
                    this.sourceDirectory = new File(this.sourceDirectoryString);
                }
                else
                {
                    this.sourceDirectory = null;
                    this.sourceDirectory = new File("./Source");
                    if(this.sourceDirectory.exists() == false || this.sourceDirectory.isDirectory() == false)
                    {
                        this.sourceDirectory = new File("./Source");
                        this.sourceDirectoryString = this.sourceDirectory.getPath();
                    }
                }
                this.changeSource.dispose();
                this.changeSource = null;
            }
            else if(this.changeCodemon != null)
            {
                this.codemonDirectoryString = this.changeCodemon.userInput.getText();
                if(this.codemonDirectoryString != null && this.codemonDirectoryString.length() > 0)
                {
                    this.codemonDirectory = null;
                    this.codemonDirectory = new File(this.codemonDirectoryString);
                    if(this.codemonDirectory.exists() == false || this.codemonDirectory.isDirectory() == false)
                    {
                        this.codemonDirectory = new File("./Codemon");
                        this.codemonDirectoryString = this.codemonDirectory.getPath();
                    }
                }
                else
                {
                    this.codemonDirectory = null;
                    this.codemonDirectory = new File("./Codemon");
                }
                this.changeCodemon.dispose();
                this.changeCodemon = null;
            }
            else
            {

            }
        }
        else if(action.equals("Cancel"))
        {
            if(this.changeSource != null)
            {
                this.changeSource.dispose();
                this.changeSource = null;
            }
            else if(this.changeCodemon != null)
            {
                this.changeCodemon.dispose();
                this.changeCodemon = null;
            }
        }
        else if(action.equals("Choose Directory"))
        {
            if(this.changeSource != null)
            {
                this.changeSource.dispose();
                this.changeSource = null;
                this.openDirectory(1);
            }
            else if(this.changeCodemon != null)
            {
                this.changeCodemon.dispose();
                this.changeCodemon = null;
                this.openDirectory(2);
            }
        }
        else
        {

        }

        if(this.fileName != null && this.fileName.equals("") == false)
        {
            this.fileNameIndicator.setText(this.fileName);
        }
        else
        {
            this.fileNameIndicator.setText("NEW FILE");
        }
        if(this.codemonReference != null)
        {
            this.updateParentGlobalVariables();
        }
        this.sourceDirConfigMenuItem.setText(("Source Directory (" + this.sourceDirectory.getPath() + ")"));
        this.codemonDirConfigMenuItem.setText(("Codemon Directory (" + this.codemonDirectory.getPath() + ")"));

        this.pack();
        this.revalidate();
        this.repaint();
    }

    /*
    Def: Displays the help file to the user.
    Args:
    Ret:
    */
    private void readHelpFile()
    {
        char [] c = {0};
        String bufferToWindow = "";
        FileReader reader = null;

        try
        {
            reader = new FileReader("TRAINREADME.txt");
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

    /*
    Def: Reads data from a file stream line by line and displays it to the user.
    Args: The file path (String).
    Ret:
    */
    private void readFromFileFast(String filePath)
    {
        String bufferToWindow = "";
        String line = "";
        FileReader reader = null;
        BufferedReader buffReader = null;

        try
        {
            reader = new FileReader(filePath);
            buffReader = new BufferedReader(reader);
            while(line != null)
            {
                line = buffReader.readLine();
                if(line != null)
                {
                    bufferToWindow = bufferToWindow + line + "\n";
                }
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
                    buffReader.close();
                }
                catch(Exception e)
                {
                    System.out.println("Error: Failed to close the file.");
                }
            }
        }
    }

    /*
    Def: Handles window closing events.
    Args:
    Ret:
    */
    public void windowClosing(WindowEvent e)
    {
        if(this.modified == false)
        {
            this.updateParentGlobalVariables();
            this.codemonReference.trainFrame = null;
            this.dispose();
        }
    }

    public void windowClosed(WindowEvent e)
    {
        //not used
    }

    public void windowOpened(WindowEvent e)
    {
        //not used
    }

    public void windowIconified(WindowEvent e)
    {
        //not used
    }

    public void windowDeiconified(WindowEvent e)
    {
        //not used
    }

    public void windowActivated(WindowEvent e)
    {
        //not used
    }

    public void windowDeactivated(WindowEvent e)
    {
        //not used
    }

    /*
    Def: Updates the parent objects varaibles.
    Args:
    Ret:
    */
    private void updateParentGlobalVariables()
    {
        if(this.codemonReference != null)
        {
            this.codemonReference.sourceDirectory = null;
            this.codemonReference.codemonDirectory = null;

            this.codemonReference.sourceDirectory = this.sourceDirectory;
            this.codemonReference.codemonDirectory = this.codemonDirectory;
            this.codemonReference.sourceDirectoryString = this.sourceDirectoryString;
            this.codemonReference.codemonDirectoryString = this.codemonDirectoryString;

            this.codemonReference.trainOpen = true;
        }
    }
}
