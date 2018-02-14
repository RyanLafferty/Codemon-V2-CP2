package Codemon;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowListener;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

/*Name: Ryan Lafferty
ID: 0853370
Date: 10/18/2015
Assignment 2*/

public class Fight extends JFrame implements ActionListener, MouseListener, WindowListener
{
    MainGui codemonReference = null;

    //file chooser
    private JFileChooser fc = null;

    //directory choosers
    private DirectoryDialog changeSource = null;
    private DirectoryDialog changeCodemon = null;
    private DirectoryDialog changeReports = null;

    //File filters
    private FileNameExtensionFilter allFileFilter = null;
    private FileNameExtensionFilter codeFileFilter = null;
    private FileNameExtensionFilter binaryFileFilter = null;

    //object variables
    public File sourceDirectory = new File("./Source");
    public File codemonDirectory = new File("./Codemon");
    public File reportsDirectory = new File("./Reports");
    public String sourceDirectoryString = "./Source";
    public String codemonDirectoryString = "./Codemon";
    public String reportsDirectoryString = "./Reports";
    public int fcReturn = 0;
    public int iterationLimit = 0;
    public int pvpMode = 2;

    //resolution values
    private int width = 640;
    private int height = 480;

    //Panels
    private JPanel mainPanel = new JPanel();

    //Menu Bars
    private JMenuBar mainMenu = new JMenuBar();

    //Menus
    private JMenu configMenu = null;
    private JMenu reportsMenu = null;
    private JMenu helpMenu = null;

    //menu items
    private JMenuItem assembleBuildMenuItem = null;
    private JMenuItem assembleLaunchMenuItem = null;

    private JMenuItem sourceDirConfigMenuItem = null;
    private JMenuItem codemonDirConfigMenuItem = null;
    private JMenuItem reportDirConfigMenuItem = null;
    private JMenuItem iterationLimitConfigMenuItem = null;
    private JRadioButtonMenuItem vs1ConfigMenuItem = null;
    private JRadioButtonMenuItem vs2ConfigMenuItem = null;
    private JRadioButtonMenuItem vs3ConfigMenuItem = null;

    private JMenuItem viewreportsMenuItem = null;
    private JMenuItem viewDeleteMenuItem = null;
    private JMenuItem viewFetchAllMenuItem = null;

    private JMenuItem helpMenuItem = null;

    //Button icons
    private ImageIcon runTestIcon = null;
    private ImageIcon runSelfTestIcon = null;
    private ImageIcon vsIcon = null;
    private ImageIcon deleteIcon = null;

    //Buttons
    private JButton runTestButton = null;
    private JButton runSelfTestButton = null;
    private JButton vsButton = null;
    private JButton deleteButton = null;

    //Toolbars
    private JToolBar toolBar = new JToolBar();

    //Labels
    private JLabel codemon1Label = null;
    private JLabel codemon2Label = null;
    private JLabel quickViewLabel = null;
    private JLabel reportsViewLabel = null;

    //Combo Boxes
    private JComboBox <String> codemon1ComboBox;
    private JComboBox <String> codemon2ComboBox;

    //String arrays
    private String codemonCompiledList [];
    private String codemonSourceList [];
    private String reportsList [];

    //FilenameFilters
    FilenameFilter sourceFilter = null;
    FilenameFilter codemonFilter = null;
    FilenameFilter reportFilter = null;

    //ArrayList
    private ArrayList <String> reportList;

    //JLists
    private JList <String> quickViewList;
    private JList <String> reportsViewList;


    //Scrollpanes
    private JScrollPane quickViewScrollPane = null;
    private JScrollPane reportsViewScrollPane = null;

    //TextFields
    private JTextField statusField = new JTextField("Status: Idle", 50);

    /*
    *Constructor for gui interface
    */
    public Fight()
    {
        //build frame
        super("Codemon Training Center");
        this.setSize(this.width, this.height);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
    public Fight(MainGui reference)
    {
        //build frame
        super("Codemon Training Center");
        this.setSize(this.width, this.height);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //update global variables
        this.codemonReference = reference;
        this.updateGlobalVariables();

        //generate lists
        this.createFileNameFilters();
        this.generateLists();
        this.generateQuickViewList();
        this.generateReportsViewList();
        this.buildComboBoxes();

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

        this.validate();
        this.repaint();
        this.pack();
    }

    /*
    Def: Update current object variables
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
        this.sourceDirConfigMenuItem = new JMenuItem("Source Directory");
        this.codemonDirConfigMenuItem = new JMenuItem("Codemon Directory");
        this.reportDirConfigMenuItem = new JMenuItem("Reports Directory");
        this.iterationLimitConfigMenuItem = new JMenuItem("Iteration Limit");
        this.vs1ConfigMenuItem = new JRadioButtonMenuItem("vs. 1", true);
        this.vs2ConfigMenuItem = new JRadioButtonMenuItem("vs. 2");
        this.vs3ConfigMenuItem = new JRadioButtonMenuItem("vs. 3");

        this.viewreportsMenuItem = new JMenuItem("View");
        this.viewDeleteMenuItem = new JMenuItem("Delete");
        this.viewFetchAllMenuItem = new JMenuItem("Fetch All");

        this.helpMenuItem = new JMenuItem("Help");

        //add action listeners
        this.sourceDirConfigMenuItem.addActionListener(this);
        this.codemonDirConfigMenuItem.addActionListener(this);
        this.reportDirConfigMenuItem.addActionListener(this);
        this.iterationLimitConfigMenuItem.addActionListener(this);
        this.vs1ConfigMenuItem.addActionListener(this);
        this.vs2ConfigMenuItem.addActionListener(this);
        this.vs3ConfigMenuItem.addActionListener(this);

        this.viewreportsMenuItem.addActionListener(this);
        this.viewDeleteMenuItem.addActionListener(this);
        this.viewFetchAllMenuItem.addActionListener(this);

        this.helpMenuItem.addActionListener(this);

        //build menus
        this.configMenu = new JMenu("Config");
        this.reportsMenu = new JMenu("Reports");
        this.helpMenu = new JMenu("Help");

        //add menu items
        this.configMenu.add(this.sourceDirConfigMenuItem);
        this.configMenu.add(this.codemonDirConfigMenuItem);
        this.configMenu.add(this.reportDirConfigMenuItem);
        this.configMenu.add(this.iterationLimitConfigMenuItem);
        this.configMenu.add(this.vs1ConfigMenuItem);
        this.configMenu.add(this.vs2ConfigMenuItem);
        this.configMenu.add(this.vs3ConfigMenuItem);

        this.reportsMenu.add(this.viewreportsMenuItem);
        this.reportsMenu.add(this.viewDeleteMenuItem);
        this.reportsMenu.add(this.viewFetchAllMenuItem);

        this.helpMenu.add(this.helpMenuItem);

        //add menus
        this.mainMenu.add(this.configMenu);
        this.mainMenu.add(this.reportsMenu);
        this.mainMenu.add(this.helpMenu);

        //set Labels
        this.sourceDirConfigMenuItem.setText(("Source Directory (" + this.sourceDirectory.getPath() + ")"));
        this.codemonDirConfigMenuItem.setText(("Codemon Directory (" + this.codemonDirectory.getPath() + ")"));
        this.reportDirConfigMenuItem.setText(("Reports Directory (" + this.reportsDirectory.getPath() + ")"));
    }

    /*
    Def: Builds the menu toolbar
    Args:
    Ret:
    */
    private void buildToolBar()
    {
        this.runTestIcon = null;
        this.runSelfTestIcon = null;
        this.vsIcon = null;
        this.deleteIcon = null;

        //build icons
        this.runTestIcon = new ImageIcon("./Icons/runTestIcon.png");
        this.runSelfTestIcon = new ImageIcon("./Icons/runSelfTestIcon.png");
        this.vsIcon = new ImageIcon("./Icons/vsIcon.png");
        this.deleteIcon = new ImageIcon("./Icons/deleteIcon.png");

        //build buttons
        if(this.runTestIcon == null || this.runSelfTestIcon == null || this.vsIcon == null ||
           this.deleteIcon == null)
        {
            this.runTestButton = new JButton("Run Test");
            this.runSelfTestButton = new JButton("Run Self Test");
            this.vsButton = new JButton("VS");
            this.deleteButton = new JButton("Delete");
        }
        else
        {
            this.runTestButton = new JButton("Run Test", this.runTestIcon);
            this.runSelfTestButton = new JButton("Run Self Test", this.runSelfTestIcon);
            this.vsButton = new JButton("VS", this.vsIcon);
            this.deleteButton = new JButton("Delete", this.deleteIcon);
        }

        //add action listeners
        this.runTestButton.addActionListener(this);
        this.runSelfTestButton.addActionListener(this);
        this.vsButton.addActionListener(this);
        this.deleteButton.addActionListener(this);

        //add buttons to toolbar
        this.toolBar.add(this.runTestButton);
        this.toolBar.add(this.runSelfTestButton);
        this.toolBar.add(this.vsButton);
        this.toolBar.add(this.deleteButton);

        //set up toolbar
        this.toolBar.setFloatable(false);
    }

    /*
    Def: Create's the file name filters that are used with the JFileChooser
    Args:
    Ret:
    */
    private void createFileNameFilters()
    {
        this.sourceFilter = null;
        this.codemonFilter = null;
        this.reportFilter = null;

        /*I know this coding style looks questionable below but this is how
        oracle's docs tell me to implement file name filters.
        I do not approve of inner classes.*/
        this.sourceFilter = new FilenameFilter()
        {
            public boolean accept(File Directory, String fileName)
            {
                String name = fileName.toLowerCase();
                if(name.endsWith(".cm"))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        };
        this.codemonFilter = new FilenameFilter()
        {
            public boolean accept(File Directory, String fileName)
            {
                String name = fileName.toLowerCase();
                if(name.endsWith(".codemon"))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        };
        this.reportFilter = new FilenameFilter()
        {
            public boolean accept(File Directory, String fileName)
            {
                String name = fileName.toLowerCase();
                if(name.endsWith(".txt"))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        };
    }

    /*
    Def: Creates a list of strings to be used to generate a list of reports located
         in the current Reports directory.
    Args:
    Ret:
    */
    private void getReportsList()
    {
        char [] c = {0};
        String buffer = "";
        String file = "";
        File doesExist = null;
        FileReader reader = null;
        BufferedReader bufferReader = null;

        try
        {
            this.reportList = null;
            this.reportList = new ArrayList <String>();

            reader = new FileReader("ReportList.txt");
            bufferReader = new BufferedReader(reader);

            while(buffer != null)
            {
                buffer = bufferReader.readLine();
                if(buffer != null)
                {
                    file = this.reportsDirectory.getPath() + "/" + buffer + ".txt";

                    doesExist = null;
                    doesExist = new File(file);
                    if(doesExist.exists() == true || buffer.contains("DNE"))
                    {
                        this.reportList.add(buffer);
                    }
                    else
                    {
                        this.reportList.add((buffer + " DNE"));
                    }
                }
            }
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
    Def: Generates the lists to be displayed.
    Args:
    Ret:
    */
    private void generateLists()
    {
        int i;

        i = 0;

        this.codemonCompiledList = null;
        this.codemonSourceList = null;
        this.reportsList = null;

        this.codemonCompiledList = this.codemonDirectory.list(this.codemonFilter);
        this.codemonSourceList = this.sourceDirectory.list(this.sourceFilter);
        //this.reportsList = this.reportsDirectory.list(this.reportFilter);
        this.getReportsList();
        this.reportsList = this.reportList.toArray(new String[this.reportList.size()]);


        /*System.out.println("\n\n\ngenerate lists\n\n\n");
        for(i = 0; i < codemonSourceList.length; i++)
        {
            System.out.println(codemonSourceList[i]);
        }*/
    }

    /*
    Def: Generates the source code list using the list of strings that was previously generated.
    Args:
    Ret:
    */
    private void generateQuickViewList()
    {
        this.quickViewList = null;
        this.quickViewScrollPane = null;

        if(this.codemonSourceList != null)
        {
            this.quickViewList = new JList<String>(this.codemonSourceList);
            this.quickViewList.setVisibleRowCount(10);
            this.quickViewList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            if(this.quickViewList != null)
            {
                this.quickViewList.addMouseListener(this);
                this.quickViewScrollPane = new JScrollPane(this.quickViewList);
            }
        }
    }

    /*
    Def: Generates the viewable reports list.
    Args:
    Ret:
    */
    private void generateReportsViewList()
    {
        this.reportsViewList = null;
        this.reportsViewScrollPane = null;

        if(this.reportsList != null)
        {
            this.reportsViewList = new JList<String>(this.reportsList);
            this.reportsViewList.setVisibleRowCount(10);
            this.reportsViewList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            if(this.reportsViewList != null)
            {
                this.reportsViewList.addMouseListener(this);
                this.reportsViewScrollPane = new JScrollPane(this.reportsViewList);
            }
        }
    }

    /*
    Def: Builds codemon combo boxes using the list of codemon in the Codemon directory.
    Args:
    Ret:
    */
    private void buildComboBoxes()
    {
        this.codemon1ComboBox = null;
        this.codemon2ComboBox = null;

        if(this.codemonCompiledList != null)
        {
            this.codemon1ComboBox = new JComboBox<String>(this.codemonCompiledList);

            this.codemon2ComboBox = new JComboBox<String>(this.codemonCompiledList);
            this.codemon2ComboBox.addItem(new String("-none-"));
            this.codemon2ComboBox.setSelectedItem(new String("-none-"));
        }
    }

    /*
    Def: Updates the lists that will be displayed to the user.
    Args:
    Ret:
    */
    private void updateLists()
    {
        if(this.codemonSourceList != null && this.codemonSourceList.length > 0)
        {
            this.quickViewList.setListData(this.codemonSourceList);
        }
        else
        {
            this.quickViewList.setListData(this.codemonSourceList);
        }
        if(this.reportsViewList != null && this.reportsList.length > 0)
        {
            this.reportsViewList.setListData(this.reportsList);
        }
        else
        {
            this.reportsViewList.setListData(this.reportsList);
        }

    }

    /*
    Def: Updates the codemon combo boxes.
    Args:
    Ret:
    */
    private void updateComboBoxes()
    {
        int i = 0;

        if(this.codemon1ComboBox != null)
        {
            this.codemon1ComboBox.removeAllItems();
        }
        if(this.codemon2ComboBox != null)
        {
            this.codemon2ComboBox.removeAllItems();
        }

        for(i = 0; i < this.codemonCompiledList.length; i++)
        {
            this.codemon1ComboBox.addItem(new String(this.codemonCompiledList[i]));
            this.codemon2ComboBox.addItem(new String(this.codemonCompiledList[i]));
        }
        this.codemon2ComboBox.addItem(new String("-none-"));
        this.codemon2ComboBox.setSelectedItem(new String("-none-"));
    }

    /*
    *Builds the main panel.
    */
    private void buildMainPanel()
    {
        //build labels
        this.codemon1Label = new JLabel("Codemon1:");
        this.codemon2Label = new JLabel("Codemon2:");
        this.quickViewLabel = new JLabel("Quick View");
        this.reportsViewLabel = new JLabel("Reports View");

        //build combo boxes

        //set up layout manager
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        //gbc.weightx = 0.1;
        //gbc.weighty = 0.1;
        this.mainPanel.setLayout(new GridBagLayout());

         //build toolbar
        this.buildToolBar();

        //add components
        this.mainPanel.add(this.toolBar, gbc);
        gbc.gridy++;

        this.mainPanel.add(this.codemon1Label, gbc);
        gbc.gridx++;
        if(this.codemon1ComboBox != null)
        {
            this.mainPanel.add(this.codemon1ComboBox, gbc);
        }
        gbc.gridx = 0;
        gbc.gridy++;

        this.mainPanel.add(this.codemon2Label, gbc);
        gbc.gridx++;
        if(this.codemon2ComboBox != null)
        {
            this.mainPanel.add(this.codemon2ComboBox, gbc);
        }
        gbc.gridx = 0;
        gbc.gridy++;

        this.mainPanel.add(this.quickViewLabel, gbc);
        gbc.gridx++;
        this.mainPanel.add(this.reportsViewLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        if(this.quickViewList != null)
        {
            this.mainPanel.add(this.quickViewScrollPane, gbc);
            gbc.gridx++;

            if(this.reportsViewList != null)
            {
                this.mainPanel.add(this.reportsViewScrollPane, gbc);
            }
            gbc.gridx = 0;
            gbc.gridy++;
        }

        this.statusField.setEditable(false);
        this.mainPanel.add(this.statusField, gbc);

        //display panel
        this.mainPanel.setVisible(true);
    }

    /*
    Def: Builds the file chooser that will allow the user to select directories and files from.
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
    }

    /*
    Def: Displays a file chooser to the user.
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
    Def: Displays a file chooser to the user.
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
    Def: Displays a file chooser to the user.
    Args:
    Ret:
    */
    private void changeReportsDirectory()
    {
        this.changeReports = new DirectoryDialog("Change Reports Directory");
        if(this.changeReports != null)
        {
            this.changeReports.okButton.addActionListener(this);
            this.changeReports.cancelButton.addActionListener(this);
            this.changeReports.chooseDirectoryButton.addActionListener(this);
        }
    }

    /*
    Def: Opens a directory that was selected by the user.
    Args: The mode value (int) used to determine which directory is to be changed.
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
            else if(mode == 3)
            {
                this.reportsDirectory = this.fc.getSelectedFile();
                this.reportsDirectoryString = this.reportsDirectory.getPath();
                //System.out.println(this.reportsDirectoryString);
            }
            else
            {

            }
        }
    }

    /*
    Def: Displays a dialog that allows the user select the iteration limit.
    Args:
    Ret:
    */
    private void setIterationLimit()
    {
        String temp = null;
        temp = JOptionPane.showInputDialog(this, "Set Iteration Limit:",
                                          "Iteration Limit",  JOptionPane.PLAIN_MESSAGE);

        try
        {
            this.iterationLimit = Integer.parseInt(temp);
        }
        catch(Exception e)
        {
            this.iterationLimit = 0;
        }
        if(this.iterationLimit < 0 || this.iterationLimit > 10000)
        {
            this.iterationLimit = 0;
        }
    }

    /*
    Def: Submits a codemon to the server and runs a solo self test.
    Args:
    Ret:
    */
    private void runTest()
    {
        int reportID = 0;
        int index =0;
        String codemon = null;
        String name = null;
        Codemon lib = new Codemon();

        if(this.codemonCompiledList == null || this.codemon1ComboBox == null)
        {
            return;
        }
        if(this.codemon1ComboBox.getItemCount() <= 0)
        {
            return;
        }

        codemon = (String) this.codemon1ComboBox.getSelectedItem();

        if(codemon == null || codemon.length() <= 0)
        {
            System.out.println("Error: Cannot run bad codemon.");
            return;
        }

        name = codemon;
        index = name.indexOf(".");
        if(index > 0)
        {
            name = name.substring(0, index);
        }

        codemon = this.codemonDirectory.getPath() + "/" + codemon;

        reportID = lib.testJni(codemon, name, this.iterationLimit);

        if(reportID > 0)
        {
            this.storeReportLink(Integer.toString(reportID));
        }


        //System.out.println(codemon + " " + name);
        //System.out.println(reportID);

        lib = null;
    }

    /*
    Def: Submits two codemon to the server and runs a 2 player self test.
    Args:
    Ret:
    */
    private void runSelfTest()
    {
        int reportID = 0;
        int index =0;
        String codemon1 = null;
        String codemon2 = null;
        String name1 = null;
        String name2 = null;
        Codemon lib = new Codemon();

        if(this.codemonCompiledList == null || this.codemon1ComboBox == null || this.codemon2ComboBox == null)
        {
            return;
        }

        codemon1 = (String) this.codemon1ComboBox.getSelectedItem();
        codemon2 = (String) this.codemon2ComboBox.getSelectedItem();

        if(codemon1 == null || codemon1.length() <= 0 || codemon2 == null || codemon2.length() <= 0)
        {
            System.out.println("Error: Cannot run bad codemon.");
            return;
        }

        if(codemon2.equals("-none-"))
        {
            System.out.println("Error: Cannot run none as codemon2.");
            return;
        }

        name1 = codemon1;
        index = name1.indexOf(".");
        if(index > 0)
        {
            name1 = name1.substring(0, index);
        }
        name2 = codemon2;
        index = name2.indexOf(".");
        if(index > 0)
        {
            name2= name2.substring(0, index);
        }

        codemon1 = this.codemonDirectory.getPath() + "/" + codemon1;
        codemon2 = this.codemonDirectory.getPath() + "/" + codemon2;

        reportID = lib.selfTestJni(codemon1, codemon2,  name1, name2, this.iterationLimit);

        if(reportID > 0)
        {
            this.storeReportLink(Integer.toString(reportID));
        }

        //System.out.println(codemon + " " + name);
        //System.out.println(reportID);

        lib = null;
    }

    /*
    Def: Submits a codemon to the server for a specified pvp battled determined by the user.
    Args:
    Ret:
    */
    private void runPVP()
    {
        int reportID = 0;
        int index =0;
        String codemon = null;
        String name = null;
        Codemon lib = new Codemon();

        if(this.codemonCompiledList == null || this.codemon1ComboBox == null)
        {
            return;
        }

        codemon = (String) this.codemon1ComboBox.getSelectedItem();

        if(codemon == null || codemon.length() <= 0)
        {
            System.out.println("Error: Cannot run bad codemon.");
            return;
        }

        name = codemon;
        index = name.indexOf(".");
        if(index > 0)
        {
            name = name.substring(0, index);
        }

        codemon = this.codemonDirectory.getPath() + "/" + codemon;

        reportID = lib.pvpJni(this.pvpMode, codemon, name);

        if(reportID > 0)
        {
            this.storeReportLink(Integer.toString(reportID));
        }

        //System.out.println(codemon + " " + name);
        //System.out.println(reportID);

        lib = null;
    }

    /*
    Def: Action event handler
    Args: An action event to handle (ActionEvent).
    Ret:
    */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();
        //System.out.println(action);

        this.updateGlobalVariables();

        if(action.contains("Source Directory"))
        {
            if(this.changeSource == null)
            {
                this.statusField.setText("Status: Changing source directory.");
                this.fc.setCurrentDirectory(this.sourceDirectory);
                this.changeSourceDirectory();
                this.generateLists();
                this.updateLists();
                //this.statusField.setText("Status: Idle");
            }
        }
        else if(action.contains("Codemon Directory"))
        {
            if(this.changeCodemon == null)
            {
                this.statusField.setText("Status: Changing codemon directory.");
                this.fc.setCurrentDirectory(this.codemonDirectory);
                this.changeCodemonDirectory();
                this.generateLists();
                this.updateComboBoxes();
            }
        }
        else if(action.contains("Reports Directory"))
        {
            if(this.changeReports == null)
            {
                this.statusField.setText("Status: Changing reports directory.");
                this.fc.setCurrentDirectory(this.reportsDirectory);
                this.changeReportsDirectory();
                this.generateLists();
                this.updateLists();
                //this.statusField.setText("Status: Idle");
            }
        }
        else if(action.equals("Iteration Limit"))
        {
            this.statusField.setText("Status: Setting iteration limit.");
            this.setIterationLimit();
            //this.statusField.setText("Status: Idle");
        }
        else if(action.equals("View"))
        {
            this.statusField.setText("Status: Loading report.");
            this.viewReport(this.reportsViewList.getSelectedValue());
            //this.statusField.setText("Status: Idle");
        }
        else if(action.equals("Delete"))
        {
            if(this.reportsViewList != null)
            {
                this.statusField.setText("Status: Deleting report.");
                this.deleteReport(this.reportsViewList.getSelectedValue());
                //this.statusField.setText("Status: Idle");
            }
        }
        else if(action.equals("Fetch All"))
        {
            this.statusField.setText("Status: Fetching all reports.");
            this.fetchAllReports();
            //this.statusField.setText("Status: Idle");
        }
        else if(action.equals("Help"))
        {
            this.statusField.setText("Status: Loading help file.");
            this.readFromFile("FIGHTREADME.txt");
            //this.statusField.setText("Status: Idle");
        }
        else if(action.equals("OK"))
        {
            this.statusField.setText("Status: Changing directories.");
            if(this.changeSource != null)
            {
                this.sourceDirectoryString = this.changeSource.userInput.getText();
                if(this.sourceDirectoryString != null && this.sourceDirectoryString.length() > 0)
                {
                    this.sourceDirectory = null;
                    this.sourceDirectory = new File(this.sourceDirectoryString);
                    if(this.sourceDirectory.exists() == false || this.sourceDirectory.isDirectory() == false)
                    {
                        this.sourceDirectory = new File("./Source");
                        this.sourceDirectoryString = this.sourceDirectory.getPath();
                    }
                }
                else
                {
                    this.sourceDirectory = null;
                    this.sourceDirectory = new File("./Source");
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
                this.generateLists();
                this.updateLists();
                this.updateComboBoxes();
            }
            else if(this.changeReports != null)
            {
                this.reportsDirectoryString = this.changeReports.userInput.getText();
                if(this.reportsDirectoryString != null && this.reportsDirectoryString.length() > 0)
                {
                    this.reportsDirectory = null;
                    this.reportsDirectory = new File(this.reportsDirectoryString);
                    if(this.reportsDirectory.exists() == false || this.reportsDirectory.isDirectory() == false)
                    {
                        this.reportsDirectory = new File("./Reports");
                        this.reportsDirectoryString = this.reportsDirectory.getPath();
                    }
                }
                else
                {
                    this.reportsDirectory = null;
                    this.reportsDirectory = new File("./Reports");
                }
                this.changeReports.dispose();
                this.changeReports = null;
            }
            else
            {

            }
            this.statusField.setText("Status: updating lists.");
            this.generateLists();
            this.updateLists();
            //this.statusField.setText("Status: Idle");
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
            else if(this.changeReports != null)
            {
                this.changeReports.dispose();
                this.changeReports = null;
            }
        }
        else if(action.equals("Choose Directory"))
        {
            this.statusField.setText("Status: Changing directories and updating lists.");
            if(this.changeSource != null)
            {
                this.changeSource.dispose();
                this.changeSource = null;
                this.openDirectory(1);
                this.generateLists();
                this.updateLists();
            }
            else if(this.changeCodemon != null)
            {
                this.changeCodemon.dispose();
                this.changeCodemon = null;
                this.openDirectory(2);
                this.generateLists();
                this.updateComboBoxes();
            }
            else if(this.changeReports != null)
            {
                this.changeReports.dispose();
                this.changeReports = null;
                this.openDirectory(3);
                this.generateLists();
                this.updateLists();
            }
            //this.statusField.setText("Status: Idle");
        }
        else if(action.equals("vs. 1"))
        {
            this.pvpMode = 2;
            this.vs1ConfigMenuItem.setSelected(true);
            this.vs2ConfigMenuItem.setSelected(false);
            this.vs3ConfigMenuItem.setSelected(false);
            //System.out.println(this.pvpMode);
        }
        else if(action.equals("vs. 2"))
        {
            this.pvpMode = 3;
            this.vs1ConfigMenuItem.setSelected(false);
            this.vs2ConfigMenuItem.setSelected(true);
            this.vs3ConfigMenuItem.setSelected(false);
            //System.out.println(this.pvpMode);
        }
        else if(action.equals("vs. 3"))
        {
            this.pvpMode = 4;
            this.vs1ConfigMenuItem.setSelected(false);
            this.vs2ConfigMenuItem.setSelected(false);
            this.vs3ConfigMenuItem.setSelected(true);
            //System.out.println(this.pvpMode);
        }
        else if(action.equals("Run Test"))
        {
            this.statusField.setText("Status: Running test.");
            this.runTest();
            //this.statusField.setText("Status: Idle");
        }
        else if(action.equals("Run Self Test"))
        {
            this.statusField.setText("Status: Running self test.");
            this.runSelfTest();
            //this.statusField.setText("Status: Idle");
        }
        else if(action.equals("VS"))
        {
            this.statusField.setText(("Status: Uploading " + this.pvpMode + " player request."));
            this.runPVP();
            //this.statusField.setText("Status: Idle");
        }
        else
        {

        }

        if(this.codemonReference != null)
        {
            this.updateParentGlobalVariables();
        }
        this.sourceDirConfigMenuItem.setText(("Source Directory (" + this.sourceDirectory.getPath() + ")"));
        this.codemonDirConfigMenuItem.setText(("Codemon Directory (" + this.codemonDirectory.getPath() + ")"));
        this.reportDirConfigMenuItem.setText(("Reports Directory (" + this.reportsDirectory.getPath() + ")"));

        this.validate();
        this.repaint();
        this.pack();
    }

    public void mousePressed(MouseEvent e)
    {
       //dont care
    }

    public void mouseReleased(MouseEvent e)
    {
       //dont care
    }

    public void mouseEntered(MouseEvent e)
    {
        //dont care
    }

    public void mouseExited(MouseEvent e)
    {
        //dont care
    }

    public void mouseClicked(MouseEvent e)
    {
        viewListItem(e);
    }

    /*
    Def: If the user double clicks then the selected item is displayed.
    Args: A mouse event to handle (MouseEvent).
    Ret:
    */
    private void viewListItem(MouseEvent e)
    {
        if(e.getComponent() != null && e.getClickCount() == 2)
        {
            JList list = ((JList)e.getSource());
            if(list.equals(this.reportsViewList) == true)
            {
                this.statusField.setText("Status: Loading Report");
                this.viewReport((String)list.getSelectedValue());
            }
            else if(list.equals(this.quickViewList) == true)
            {
                this.statusField.setText("Status: Loading source file.");
                this.viewSource((String)list.getSelectedValue());
            }
            //this.statusField.setText("Status: Idle");
        }
    }

    /*
    Def: Deletes a report from the report master list and local directory.
    Args: The report id to delete (String).
    Ret:
    */
    private void deleteReport(String reportID)
    {
        File fileToRemove = null;

        this.generateLists();
        this.updateLists();
        if(this.reportList == null)
        {
            System.out.println("Error: bad list");
            return;
        }

        if(this.reportList.contains(reportID) == true)
        {
            this.reportList.remove(reportID);
            fileToRemove = new File(this.reportsDirectory.getPath() + "/" + reportID + ".txt");
            //System.out.println("trying Removing " + fileToRemove.getPath());


            if(fileToRemove.exists())
            {
                //System.out.println("Removing " + fileToRemove.getPath());
                fileToRemove.delete();
                fileToRemove = null;
            }

            //write new list to file
            FileWriter writer = null;
            BufferedWriter bufWrite = null;
            File masterList = new File("./ReportList.txt");
            int i = 0;
            String listToPrint [] = this.reportList.toArray(new String[this.reportList.size()]);
            String splitStrings [] = null;

            try
            {
                writer = new FileWriter(masterList);
                bufWrite = new BufferedWriter(writer);
                for(i = 0; i < listToPrint.length; i++)
                {
                    //System.out.println(listToPrint[i]);
                    if(listToPrint[i].contains("DNE") == true)
                    {
                        splitStrings = listToPrint[i].split(" ");
                        if(splitStrings != null && splitStrings.length > 0 && splitStrings[0] != null)
                        {
                            //System.out.println("split strings: " + splitStrings[0]);
                            bufWrite.write(splitStrings[0]);
                            bufWrite.newLine();
                        }
                    }
                    else
                    {
                        bufWrite.write(listToPrint[i]);
                        bufWrite.newLine();
                    }
                }
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
                        bufWrite.close();
                        writer.close();
                        this.generateLists();
                        this.updateLists();
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error: Failed to close the file.");
                    }
                }
            }
        }
    }

    /*
    Def: Stores a report link that will allow for the report to be fetched later.
    Args: The report id to store (String).
    Ret:
    */
    private void storeReportLink(String reportID)
    {
        File fileToRemove = null;

        this.generateLists();
        this.updateLists();
        if(this.reportList == null)
        {
            System.out.println("Error: bad list");
            return;
        }

        if(this.reportList.contains(reportID) == true || this.reportList.contains((reportID + " DNE")) == true)
        {
            System.out.println("Error: already in list");
            return;
        }

        this.reportList.add(reportID);

        //write new list to file
        FileWriter writer = null;
        BufferedWriter bufWrite = null;
        File masterList = new File("./ReportList.txt");
        int i = 0;
        String listToPrint [] = this.reportList.toArray(new String[this.reportList.size()]);
        String splitStrings [] = null;

        try
        {
            writer = new FileWriter(masterList);
            bufWrite = new BufferedWriter(writer);
            for(i = 0; i < listToPrint.length; i++)
            {
                //System.out.println(listToPrint[i]);
                if(listToPrint[i].contains("DNE") == true)
                {
                    splitStrings = listToPrint[i].split(" ");
                    if(splitStrings != null && splitStrings.length > 0 && splitStrings[0] != null)
                    {
                        //System.out.println("split strings: " + splitStrings[0]);
                        bufWrite.write(splitStrings[0]);
                        bufWrite.newLine();
                    }
                }
                else
                {
                    bufWrite.write(listToPrint[i]);
                    bufWrite.newLine();
                }
            }
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
                    bufWrite.close();
                    writer.close();
                    this.generateLists();
                    this.updateLists();
                }
                catch(Exception e)
                {
                    System.out.println("Error: Failed to close the file.");
                }
            }
        }
    }

    /*
    Def: Fetches the specified report.
    Args: The report id to fetch (String).
    Ret:
    */
    private void fetchReport(String reportID)
    {
        String outputFile = null;
        String splitStrings [] = null;
        File testFile = null;
        int returnVal = 0;
        Codemon lib = new Codemon();

        if(this.reportsDirectory == null)
        {
            lib = null;
            return;
        }

        if(reportID.contains("DNE"))
        {
            splitStrings = reportID.split(" ");
            if(splitStrings != null && splitStrings.length > 0 && splitStrings[0] != null)
            {
                reportID = splitStrings[0];
            }
        }

        outputFile = this.reportsDirectory + "/" + reportID + ".txt";

        if(new File(outputFile).exists() == true)
        {
            lib = null;
            return;
        }

        returnVal = lib.reportJni(reportID, outputFile);

        testFile = new File(outputFile);
        if(testFile.length() <= 0)
        {
            testFile.delete();
        }

        lib = null;
        testFile = null;
    }

    /*
    Def: Fetches all pending reports.
    Args:
    Ret:
    */
    private void fetchAllReports()
    {
        File filePtr = null;
        String fileStr = "";
        int i = 0;

        this.generateLists();
        this.updateLists();
        if(this.reportList == null)
        {
            System.out.println("Error: bad list");
            return;
        }

        for(i = 0; i < this.reportList.size(); i++)
        {
            //System.out.println((String)this.reportList.get(i));
            this.fetchReport((String)this.reportList.get(i));
        }

        this.generateLists();
        this.updateLists();
    }

    /*
    Def: Displays a codemon source file to the uer.
    Args: The name of the source file to display (String).
    Ret:
    */
    private void viewSource(String name)
    {
        String filePath = null;
        File f = null;

        if(name == null)
        {
            return;
        }

        filePath = this.sourceDirectory.getPath() + "/" + name;
        f = new File(filePath);
        if(f.exists() == true)
        {
            this.readFromFileFast(filePath);
        }

        f = null;
    }

    /*
    Def: Displays a report to the user.
    Args: The report to display (String).
    Ret:
    */
    private void viewReport(String name)
    {
        String filePath = null;
        File f = null;

        if(name == null)
        {
            return;
        }

        filePath = this.reportsDirectory.getPath() + "/" + name + ".txt";
        f = new File(filePath);
        if(f.exists() == true)
        {
            this.readFromFileFast(filePath);
        }

        f = null;
    }

    /*
    Def: Reads data from a file stream character by character and displays it to the user.
    Args: The file path (String).
    Ret:
    */
    private void readFromFile(String filePath)
    {
        char [] c = {0};
        String bufferToWindow = "";
        FileReader reader = null;

        try
        {
            reader = new FileReader(filePath);
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
                    this.revalidate();
                    this.repaint();
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
    Def: Handles the window closing event.
    Args: The window event to handle (WindowEvent).
    Ret:
    */
    public void windowClosing(WindowEvent e)
    {
        this.updateParentGlobalVariables();
        this.codemonReference.fightFrame = null;
    }

    public void windowClosed(WindowEvent e)
    {

    }

    public void windowOpened(WindowEvent e)
    {

    }

    public void windowIconified(WindowEvent e)
    {

    }

    public void windowDeiconified(WindowEvent e)
    {

    }

    public void windowActivated(WindowEvent e)
    {

    }

    public void windowDeactivated(WindowEvent e)
    {

    }

    /*
    Def: Updates the parent objects variables.
    Args:
    Ret:
    */
    private void updateParentGlobalVariables()
    {
        if(this.codemonReference != null)
        {
            this.codemonReference.sourceDirectory = null;
            this.codemonReference.codemonDirectory = null;
            this.codemonReference.reportsDirectory = null;

            this.codemonReference.sourceDirectory = this.sourceDirectory;
            this.codemonReference.codemonDirectory = this.codemonDirectory;
            this.codemonReference.reportsDirectory = this.reportsDirectory;
            this.codemonReference.sourceDirectoryString = this.sourceDirectoryString;
            this.codemonReference.codemonDirectoryString = this.codemonDirectoryString;
            this.codemonReference.reportsDirectoryString = this.reportsDirectoryString;

            this.codemonReference.fightOpen = true;
        }
    }
}
