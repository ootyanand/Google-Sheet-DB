/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googledb;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import gdatabase.GTable;
import gsheet.GWorkSheetInfo;

/**
 *
 * @author ootyanand
 */
@SuppressWarnings("serial")
public class DatabaseView extends javax.swing.JPanel {
    RecordView recordView;
    MainFrame mainFrm;
    GTable selectedTable;
    static GTable noDatabase = null;
    
    static {
    	try {
			noDatabase = new GTable(null, new GWorkSheetInfo("<No Database Open>", -1));
		} catch (InvalidParameterException | IOException e) {
			e.printStackTrace();
		}
    }
    
    private javax.swing.JTree tree;
    
    public DatabaseView(MainFrame mf, RecordView rv) {
        super(new BorderLayout());
        
        this.mainFrm = mf;
        this.recordView = rv;
        
        add(new JScrollPane(tree = new javax.swing.JTree()));
        
        tree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                treeValueChanged(evt);
            }
        });

        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        Icon closedIcon = Application.getImageIcon("database.png", 16);
        Icon openIcon = Application.getImageIcon("database.png", 16);
        Icon leafIcon = Application.getImageIcon("table-open.png", 16);
        renderer.setClosedIcon(closedIcon);
        renderer.setOpenIcon(openIcon);
        renderer.setLeafIcon(leafIcon);
        
        clearNodes();
        
        tree.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger())                 
                    showPopup(e);
			}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
    }

    public GTable getSelectedTable(){
        return selectedTable;
    }
    
    public void clearNodes(){
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.removeAllChildren();
        root.setUserObject(noDatabase);
        model.nodeChanged(root);
        model.reload();
    }
    
    public void setDatabaseName(String name) throws InvalidParameterException, IOException{
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.setUserObject(new GTable(mainFrm.db, new GWorkSheetInfo(name, -1)));
        model.nodeChanged(root);
    }
    
    public void addTables(List<GTable> tables){
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        tables.forEach((table) -> {
            root.add(new DefaultMutableTreeNode(table));
        });
        tree.expandRow(0);
    }
    
    public void load(){
        try {
            clearNodes();
            List<GTable> tables = mainFrm.db.getTables();
            setDatabaseName(mainFrm.db.getName());
            addTables(tables);
            selectTable((selectedTable!=null)?selectedTable.getName():null);
        } catch (IOException ex) {
            Logger.getLogger(DatabaseView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void selectTable(String tableName){
        try {
            
            DefaultMutableTreeNode node = findNode(tableName);
            if(node==null){
                tree.setSelectionPath(null);
                return;
            }
            
            GTable ws = (GTable) node.getUserObject();
            
            if(tree.getLastSelectedPathComponent() != node){
                TreePath path = new TreePath(node.getPath());
                tree.setSelectionPath(path);
            } else {
                selectedTable = new GTable(mainFrm.db, ws.getName());
                recordView.load(selectedTable);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(DatabaseView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private List<DefaultMutableTreeNode> getSearchNodes(DefaultMutableTreeNode root) {
        List<DefaultMutableTreeNode> searchNodes = new ArrayList<>();

        Enumeration<?> e = root.preorderEnumeration();
        while(e.hasMoreElements()) {
            searchNodes.add((DefaultMutableTreeNode)e.nextElement());
        }
        return searchNodes;
    }
    
    public final DefaultMutableTreeNode findNode(String searchString) {

        List<DefaultMutableTreeNode> searchNodes = getSearchNodes((DefaultMutableTreeNode)tree.getModel().getRoot());
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();

        DefaultMutableTreeNode foundNode = null;
        int bookmark = -1;
        
        if(searchString==null){
            return foundNode;
        }

        if( currentNode != null ) {
            for(int index = 0; index < searchNodes.size(); index++) {
                if( searchNodes.get(index) == currentNode ) {
                    bookmark = index;
                    break;
                }
            }
        }

        for(int index = bookmark + 1; index < searchNodes.size(); index++) {    
            if(searchNodes.get(index).toString().toLowerCase().contains(searchString.toLowerCase())) {
                foundNode = searchNodes.get(index);
                break;
            }
        }

        if( foundNode == null ) {
            for(int index = 0; index <= bookmark; index++) {    
                if(searchNodes.get(index).toString().toLowerCase().contains(searchString.toLowerCase())) {
                    foundNode = searchNodes.get(index);
                    break;
                }
            }
        }
        return foundNode;
    }   
    
    private void treeValueChanged(javax.swing.event.TreeSelectionEvent evt) {                                  
        if(recordView!=null){
            try {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                        tree.getLastSelectedPathComponent();
                
                /* if nothing is selected */
                if (node == null){
                    recordView.load(null);
                    return;
                }
                GTable ws = (GTable) node.getUserObject();
                
                if(ws.getId()==-1) //root node
                    return;
                
                selectedTable = new GTable(mainFrm.db, ws.getName());
                recordView.load(selectedTable);
            } catch (IOException ex) {
                Logger.getLogger(DatabaseView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
	protected void showPopup(MouseEvent e) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		if (node == null) {
			return;
		}

		GTable ws = (GTable) node.getUserObject();
		JPopupMenu popup = new JPopupMenu();

		if (ws.getId() == -1) { // If Root Node
			popup.add(mainFrm.createMenuItem(MainFrame.CI_DB_PROPERTY));
			popup.add(mainFrm.createMenuItem(MainFrame.CI_CREATE_TABLE));
			popup.add(mainFrm.createMenuItem(MainFrame.CI_OPEN_IN_BROWSER));
		} else {
			popup.add(mainFrm.createMenuItem(MainFrame.CI_RENAME_TABLE));
			popup.add(mainFrm.createMenuItem(MainFrame.CI_DELETE_TABLE));
			popup.addSeparator();
			popup.add(mainFrm.createMenuItem(MainFrame.CI_ADD_COLUMN));
			popup.add(mainFrm.createMenuItem(MainFrame.CI_DELETE_COLUMN));
			popup.addSeparator();
			popup.add(mainFrm.createMenuItem(MainFrame.CI_ADD_RECORD));
		}
		popup.show(e.getComponent(), e.getX(), e.getY());
	}
}
