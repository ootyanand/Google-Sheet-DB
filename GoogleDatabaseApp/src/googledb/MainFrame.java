/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googledb;


import static java.awt.event.InputEvent.*;
import static java.awt.event.KeyEvent.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import gdatabase.GColumn;
import gdatabase.GDatabase;
import gdatabase.GRecord;
import gdatabase.GTable;

/**
 *
 * @author ootyanand
 */
@SuppressWarnings("serial")
public class MainFrame extends javax.swing.JFrame{
    
    GDatabase db;
    DatabaseInfo dbInfo;
    DBConfig dbConfig;
    
    
    public MainFrame() {
        initComponents();
        loadConfig();
    }
    
    private void initComponents(){
    	
        splitPaneMain = new JSplitPane();
        recordView = new RecordView(this);
        datbaseView = new DatabaseView(this, recordView);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Google Database");
        setIconImage(Application.getImageIcon("logo.png", 16, 16).getImage());
        setName("mainFrame"); // NOI18N
        JToolBar toolBarMain = createToolbar();
        setJMenuBar(createMenubar());
        
        splitPaneMain.setDividerLocation(200);
        splitPaneMain.setOneTouchExpandable(true);
        splitPaneMain.setLeftComponent(datbaseView);
        splitPaneMain.setRightComponent(recordView);
        
        JPanel pan = (JPanel) getContentPane();

        pan.setLayout(new BorderLayout());
        pan.add(toolBarMain, BorderLayout.NORTH);
        pan.add(splitPaneMain, BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private JToolBar createToolbar(){
        JToolBar toolBarMain = new JToolBar();
        toolBarMain.setFloatable(false);
        toolBarMain.setRollover(true);
        toolBarMain.add(createButton(CI_OPEN_DB));
        toolBarMain.add(new javax.swing.Box.Filler(new java.awt.Dimension(5, 5), new java.awt.Dimension(5, 5), new java.awt.Dimension(5, 5)));
        toolBarMain.add(createButton(CI_CREATE_DB));
        toolBarMain.add(createButton(CI_ADD_RECORD));
        toolBarMain.add(createButton(CI_EDIT_RECORD));
        toolBarMain.add(createButton(CI_DELETE_RECORD));
        toolBarMain.add(new javax.swing.Box.Filler(new java.awt.Dimension(5, 5), new java.awt.Dimension(5, 5), new java.awt.Dimension(5, 5)));
        toolBarMain.add(createButton(CI_ABOUT));
        return toolBarMain;
    }
    
    private JMenuBar createMenubar(){
        JMenu menuFile = new JMenu("File");
        JMenu menuTable = new JMenu("Table");
        JMenu menuRecord = new JMenu("Record");
        JMenu menuHelp = new JMenu("Help");
        menuRecentDB = new javax.swing.JMenu("Open Recent Database");
        
        menuFile.add(createMenuItem(CI_OPEN_DB));
        menuFile.add(createMenuItem(CI_CREATE_DB));
        menuFile.addSeparator();
        menuFile.add(createMenuItem(CI_DB_PROPERTY));
        menuFile.addSeparator();
        menuFile.add(createMenuItem(CI_CREDENTIALS));
        menuFile.addSeparator();
        menuFile.add(menuRecentDB);
        menuFile.addSeparator();
        menuFile.add(createMenuItem(CI_EXIT));
        
        menuTable.add(createMenuItem(CI_CREATE_TABLE));
        menuTable.add(createMenuItem(CI_RENAME_TABLE));
        menuTable.add(createMenuItem(CI_DELETE_TABLE));
        menuTable.addSeparator();
        menuTable.add(createMenuItem(CI_ADD_COLUMN));
        menuTable.add(createMenuItem(CI_DELETE_COLUMN));
        
        menuRecord.add(createMenuItem(CI_ADD_RECORD));
        menuRecord.add(createMenuItem(CI_EDIT_RECORD));
        menuRecord.add(createMenuItem(CI_DELETE_RECORD));
        
        menuHelp.add(createMenuItem(CI_ABOUT));

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuFile);
        menuBar.add(menuTable);
        menuBar.add(menuRecord);
        menuBar.add(menuHelp);
        return menuBar;
    }
    
    public JButton createButton(CmdInfo ci){
        JButton btn = new JButton();
        if(ci.imgName!=null)  btn.setIcon(Application.getImageIcon(ci.imgName, 24)); // NOI18N
        //if(caption!=null) btn.setText(caption);
        if(ci.tooltip!=null) btn.setToolTipText(ci.tooltip);
        btn.setActionCommand(ci.cmd);
        btn.setFocusable(false);
        btn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn.addActionListener((java.awt.event.ActionEvent evt) -> {
            onCommand(ci.cmd);
        });
        return btn;
    }
    
    public JMenuItem createMenuItem(CmdInfo ci){
        JMenuItem mitem = new JMenuItem();
        if(ci.keyStroke!=null)  mitem.setAccelerator(ci.keyStroke);
        if(ci.imgName!=null)   	mitem.setIcon(Application.getImageIcon(ci.imgName, 24, 24)); // NOI18N
        if(ci.caption!=null)   	mitem.setText(ci.caption);
        if(ci.tooltip!=null)   	mitem.setToolTipText(ci.tooltip);
        mitem.addActionListener((java.awt.event.ActionEvent evt) -> {
            onCommand(ci.cmd);
        });
        return mitem;
    }
    
    private void onCommand(String cmd) {
        switch(cmd){
            case CMD_OPEN_DB:           onCmdOpenDatabase(); return;
            case CMD_CREATE_DB:         onCmdCreateDatabase(); return;
            case CMD_DB_PROPERTY:       onCmdDBProperty(); return;
            case CMD_CREDENTIALS:       onCmdCredential(); return;
            case CMD_EXIT:              onCmdExit(); return;
            case CMD_CREATE_TABLE:      onCmdCreateTable(); return;
            case CMD_RENAME_TABLE:      onCmdRenameTable(); return;
            case CMD_DELETE_TABLE:      onCmdDeleteTable(); return;
            case CMD_ADD_COLUMN:        onCmdAddColumn(); return;
            case CMD_DELETE_COLUMN:     onCmdDeletColumn(); return;
            case CMD_ADD_RECORD:        onCmdAddRecord(); return;
            case CMD_EDIT_RECORD:       onCmdEditRecord(); return;
            case CMD_DELETE_RECORD:     onCmdDeleteRecord(); return;
            case CMD_ABOUT:             onCmdAbout(); return;
        }
    }
    
    private void onCmdAbout(){
        new AboutDialog(this).setVisible(true);
    }

    @SuppressWarnings("rawtypes")
	private void onCmdOpenDatabase() {
        
        if(dbConfig.getCurentCredential()==null){
            JOptionPane.showMessageDialog(this, "Please provide credentials first using menu File->Credentials", "Google Database", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JPanel pan= new JPanel(new BorderLayout());
        pan.add(new JLabel("Enter a database Id:"), BorderLayout.NORTH);
        
        @SuppressWarnings("unchecked")
		JComboBox jcb = new JComboBox(dbConfig.getRecentDatabaseNames().toArray());
        jcb.setEditable(true);
        jcb.setSelectedIndex(-1);
        
        pan.add(jcb);

        int ch = JOptionPane.showConfirmDialog(
                            this,
                            pan,
                            "Open Database",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE, 
                            new javax.swing.ImageIcon(getClass().getResource("/googledb/resources/open-db.png")) );
        
        if(ch == JOptionPane.CANCEL_OPTION) return;
        
        String s = (String) jcb.getSelectedItem();

        //If a string was returned, say so.
        if ((s != null) && (s.length() > 0)) {
            DatabaseInfo d = dbConfig.getDatabaseByID(s);
            if(d==null) {
                d = dbConfig.getDatabaseByName(s);
                if(d==null)
                    d = new DatabaseInfo(null, s);
            }
            openDatabase(d);
        }
    }                                                    

    private void openDatabase(DatabaseInfo dbi){
        try {
            this.db = new GDatabase();
            if(this.db.Open(dbi.getId(), dbConfig.getGoogleCredential())){
                
                if(dbi.name==null){
                    dbi.setName(db.getName());
                    dbConfig.addRecentDB(dbi);
                    dbConfig.save();
                }
                
                this.dbInfo = dbi;
                datbaseView.load();
                return;
            }
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.db = null;
        this.dbInfo = null;
    }
    
    private void onCmdExit() {
        System.exit(0);
    }                                            

    private void onCmdCreateDatabase() {
        if(dbConfig.getCurentCredential()==null){
            JOptionPane.showMessageDialog(this, "Please provide credentials first using menu File->Credentials", "Google Database", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String s = (String)JOptionPane.showInputDialog(
                            this,
                            "Database Name:",
                            "Create Database",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            null);

        //If a string was returned, say so.
        if ((s != null) && (s.length() > 0)) {
            try {
                GDatabase tmpdb = GDatabase.createDatabase(s, dbConfig.getGoogleCredential());
                if(tmpdb!=null && tmpdb.isOpen()){
                    this.dbInfo = new DatabaseInfo(tmpdb.getName(), tmpdb.getDatabaseId());
                    this.db = tmpdb;
                    datbaseView.load();
                    updateRecentDatabases(dbInfo);
                }
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }                                                      

    private void onCmdCreateTable() {
        if(db==null || !db.isOpen()){
            JOptionPane.showMessageDialog(this, "Please open a database first!", "Google Database", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        CreateTableDlg dlg = new CreateTableDlg(this);
        dlg.setVisible(true);
        if(dlg.tableName!=null && dlg.columns!=null){
            try {
                db.createTable(dlg.tableName, dlg.columns);
                datbaseView.load();
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }                                                   

    private void onCmdRenameTable() {
        
        if(db==null || !db.isOpen()){
            JOptionPane.showMessageDialog(this, "Please open a database first!", "Google Database", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        GTable table = datbaseView.getSelectedTable();
        if(table==null){
           JOptionPane.showMessageDialog(this, "No Table Selected!", "Rename Table", JOptionPane.ERROR_MESSAGE);
           return;
        }
        
        String s = (String)JOptionPane.showInputDialog(
                            this,
                            "Table Name:",
                            "Rename:",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            table.getName());

        //If a string was returned, say so.
        if ((s != null) && (s.length() > 0)) {
            try {
                table.rename(s);
                datbaseView.load();
                datbaseView.selectTable(s);
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }                                                   

    private void onCmdDeleteTable() {
        if(db==null || !db.isOpen()){
            JOptionPane.showMessageDialog(this, "Please open a database first!", "Google Database", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            GTable table = datbaseView.getSelectedTable();
            if(table==null){
               JOptionPane.showMessageDialog(this, "No Table Selected!", "Delete Table", JOptionPane.ERROR_MESSAGE);
               return;
            }
            db.dropTable(table.getName());
            datbaseView.load();
            recordView.load(null);
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }                                                   

    private void onCmdAddRecord() {
    	if(db==null || !db.isOpen()){
            JOptionPane.showMessageDialog(this, "Please open a database first!", "Google Database", JOptionPane.ERROR_MESSAGE);
            return;
        }
         
        try {
            GTable table = datbaseView.getSelectedTable();
            if (table == null) {
                JOptionPane.showMessageDialog(this, "No Table Selected!", "Add Record", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (table.getColumns().size()==0) {
                JOptionPane.showMessageDialog(this, "No Column in the table!", "Google Database", JOptionPane.ERROR_MESSAGE);
                return;
            }
            EditRecordDialog dlg = new EditRecordDialog(this, table.newRecord());
            dlg.setVisible(true);
            if (dlg.record != null) {
                table.insert(dlg.record);
                recordView.load(table);
            }
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }                                                 

    private void onCmdEditRecord() {
        if(db==null || !db.isOpen()){
            JOptionPane.showMessageDialog(this, "Please open a database first!", "Google Database", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            GTable table = datbaseView.getSelectedTable();
            if(table==null){
               JOptionPane.showMessageDialog(this, "No Table Selected!", "Edit Record", JOptionPane.ERROR_MESSAGE);
               return;
            }
            if(recordView.getSelectedRow()==-1){
                JOptionPane.showMessageDialog(this, "No Records Selected!", "Edit Record", JOptionPane.ERROR_MESSAGE);
                return;
            }
            GRecord rec = table.getRecords().get(recordView.getSelectedRow());
            EditRecordDialog dlg = new EditRecordDialog(this, rec);
            dlg.setVisible(true);
            if(dlg.record!=null){
                table.update(dlg.record);
                recordView.load(table);
            }
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }                                                  

    private void onCmdDeleteRecord() {
        
        if(db==null || !db.isOpen()){
            JOptionPane.showMessageDialog(this, "Please open a database first!", "Google Database", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            GTable table = datbaseView.getSelectedTable();
            if(table==null){
               JOptionPane.showMessageDialog(this, "No Table Selected!", "Delete Table", JOptionPane.ERROR_MESSAGE);
               return;
            }
            if(recordView.getSelectedRow()==-1){
                JOptionPane.showMessageDialog(this, "No Records Selected!", "Delete Records", JOptionPane.ERROR_MESSAGE);
                return;
            }
            GRecord rec = table.getRecords().get(recordView.getSelectedRow());
            table.delete(rec);
            recordView.load(table);
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }                                                    

    private void onCmdAddColumn() {
        if(db==null || !db.isOpen()){
            JOptionPane.showMessageDialog(this, "Please open a database first!", "Google Database", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        GTable table = datbaseView.getSelectedTable();
        if(table==null){
           JOptionPane.showMessageDialog(this, "No Table Selected!", "Add Columns", JOptionPane.ERROR_MESSAGE);
           return;
        }
        
        String s = (String)JOptionPane.showInputDialog(
                            this,
                            "Column Name:",
                            "Add Column:",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            "");

        //If a string was returned, say so.
        if ((s != null) && (s.length() > 0)) {
            try {
                table.addColumn(s);
                recordView.load(table);
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }                                                 

    private void onCmdDeletColumn() {
        if(db==null || !db.isOpen()){
            JOptionPane.showMessageDialog(this, "Please open a database first!", "Google Database", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        GTable table = datbaseView.getSelectedTable();
        if(table==null){
           JOptionPane.showMessageDialog(this, "No Table Selected!", "Delete Columns", JOptionPane.ERROR_MESSAGE);
           return;
        }
        
        try {
            List<GColumn> columns = table.getColumns();
            if(columns==null || columns.size()==0){
                JOptionPane.showMessageDialog(this, "No columns found!", "Delete Columns", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            GColumn colToDelete = (GColumn)JOptionPane.showInputDialog(
                            this,
                            "Select a column to be deleted:",
                            "Delte Column",
                            JOptionPane.PLAIN_MESSAGE,
                            Application.getImageIcon("logo.png", 16, 16),
                            columns.toArray(new GColumn[columns.size()]),
                            null);

            if ((colToDelete != null)) {
                if(columns.size()==1){
                    table.addColumn("");
                }
                table.deleteColumn(colToDelete.getName());
                recordView.load(table);
            }
        } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }                                                    

    private void onCmdDBProperty() {
        if(db==null || !db.isOpen()){
            JOptionPane.showMessageDialog(this, "Please open a database first!", "Google Database", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DbPropertiesDialog dlg = new DbPropertiesDialog(this, dbInfo, dbConfig.getCurentCredential());
        dlg.setVisible(true);
    }                                                        

    private void onCmdCredential() {
        CredentialsDialog dlg = new CredentialsDialog(this, dbConfig);
        dlg.setVisible(true);
        if(dlg.selCredential==null) return;
        dbConfig.setCurentCredential(dlg.selCredential);
        dbConfig.save();
    }                                                   
    
    private void updateRecentDatabases(DatabaseInfo di){
    	if(dbConfig!=null){
            dbConfig.addRecentDB(di);
            dbConfig.save();
        }
    }
    
    private void loadConfig(){
        dbConfig = DBConfig.load();
        if(dbConfig==null){
            dbConfig =  new DBConfig();
            dbConfig.save();
            return;
        }

        for(DatabaseInfo d: dbConfig.getRecentDatabases()){
            JMenuItem menuItem = new JMenuItem(d.getName());
            menuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/googledb/resources/database.png")));
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openDatabase(d);
                }
            });
            menuRecentDB.add(menuItem);
        }
    }
    
    public static class CmdInfo{
    	String cmd;
    	String caption;
    	String tooltip;
    	String imgName;
    	KeyStroke keyStroke;
    	
		public CmdInfo(String cmd, String caption, String tooltip, String imgName, KeyStroke keyStroke) {
			super();
			this.cmd = cmd;
			this.caption = caption;
			this.tooltip = tooltip;
			this.imgName = imgName;
			this.keyStroke = keyStroke;
		}
    }
    
    private googledb.DatabaseView datbaseView;
    private javax.swing.JMenu menuRecentDB;
    private googledb.RecordView recordView;
    private javax.swing.JSplitPane splitPaneMain;
    
    static final String CMD_OPEN_DB 		= "CMD_OPEN_DB";
    static final String CMD_CREATE_DB 		= "CMD_CREATE_DB";
    static final String CMD_DB_PROPERTY 	= "CMD_DB_PROPERTY";
    static final String CMD_CREDENTIALS 	= "CMD_CREDENTIALS";
    static final String CMD_EXIT 			= "CMD_EXIT";

    static final String CMD_CREATE_TABLE 	= "CMD_CREATE_TABLE";
    static final String CMD_RENAME_TABLE 	= "CMD_RENAME_TABLE";
    static final String CMD_DELETE_TABLE 	= "CMD_DELETE_TABLE";
    static final String CMD_ADD_COLUMN 		= "CMD_ADD_COLUMN";
    static final String CMD_DELETE_COLUMN 	= "CMD_DELETE_COLUMN";

    static final String CMD_ADD_RECORD 		= "CMD_ADD_RECORD";
    static final String CMD_EDIT_RECORD 	= "CMD_EDIT_RECORD";
    static final String CMD_DELETE_RECORD 	= "CMD_DELETE_RECORD";
    static final String CMD_ABOUT 			= "CMD_ABOUT";
    
    static final CmdInfo CI_OPEN_DB 		=	new CmdInfo(CMD_OPEN_DB, 		"Open Database", 		"Open Database", 			"open-db.png", 			KeyStroke.getKeyStroke(VK_O, CTRL_MASK)); 
    static final CmdInfo CI_CREATE_DB 		=	new CmdInfo(CMD_CREATE_DB, 		"Create Database", 		"Create Database", 			"add-db.png", 			KeyStroke.getKeyStroke(VK_N, CTRL_MASK)); 
    static final CmdInfo CI_DB_PROPERTY 	=	new CmdInfo(CMD_DB_PROPERTY, 	"Database Property", 	"Database Property",		"db-properties.png", 	KeyStroke.getKeyStroke(VK_I, CTRL_MASK)); 
    static final CmdInfo CI_CREDENTIALS 	=	new CmdInfo(CMD_CREDENTIALS, 	"Credentials", 			"Credentials", 				 null, 					KeyStroke.getKeyStroke(VK_B, CTRL_MASK)); 
    static final CmdInfo CI_EXIT 			=	new CmdInfo(CMD_EXIT, 			"Exit", 				"Exit", 					"exit.png", 			KeyStroke.getKeyStroke(VK_O, CTRL_MASK));
    static final CmdInfo CI_CREATE_TABLE 	=	new CmdInfo(CMD_CREATE_TABLE, 	"Create", 				"Create Table", 			"add-table.png", 		KeyStroke.getKeyStroke(VK_T, CTRL_MASK)); 
    static final CmdInfo CI_RENAME_TABLE 	=	new CmdInfo(CMD_RENAME_TABLE, 	"Rename", 				"Rename Database", 			"rename-table.png", 	KeyStroke.getKeyStroke(VK_R, CTRL_MASK)); 
    static final CmdInfo CI_DELETE_TABLE 	=	new CmdInfo(CMD_DELETE_TABLE, 	"Delete", 				"Delete Selected Table", 	"delete-table.png", 	KeyStroke.getKeyStroke(VK_D, CTRL_MASK)); 
    static final CmdInfo CI_ADD_COLUMN 		=	new CmdInfo(CMD_ADD_COLUMN, 	"Add Column", 			"Add Column", 				"add-column.png", 		KeyStroke.getKeyStroke(VK_K, CTRL_MASK));
    static final CmdInfo CI_DELETE_COLUMN 	=	new CmdInfo(CMD_DELETE_COLUMN, 	"Delete Column", 		"Delete Column", 			"delete-column.png", 	KeyStroke.getKeyStroke(VK_D, SHIFT_MASK|CTRL_MASK));
    static final CmdInfo CI_ADD_RECORD 		=	new CmdInfo(CMD_ADD_RECORD, 	"Add Record", 			"Add Record",	 			"add-record.png", 		KeyStroke.getKeyStroke(VK_R, CTRL_MASK));
    static final CmdInfo CI_EDIT_RECORD 	=	new CmdInfo(CMD_EDIT_RECORD, 	"Edit Record", 			"Edit Selected Record", 	"edit-record.png", 		KeyStroke.getKeyStroke(VK_E, CTRL_MASK));
    static final CmdInfo CI_DELETE_RECORD 	=	new CmdInfo(CMD_DELETE_RECORD, 	"Delete Record", 		"Delete Selected Record", 	"delete-record.png", 	KeyStroke.getKeyStroke(VK_DELETE, 0));
    static final CmdInfo CI_ABOUT 			=	new CmdInfo(CMD_ABOUT, 			"About", 				"About Google Database", 	"about.png", 			KeyStroke.getKeyStroke(VK_ENTER, CTRL_MASK));
   
}
