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
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import gdatabase.GColumn;
import gdatabase.GRecord;
import gdatabase.GTable;

/**
 *
 * @author ootyanand
 */
@SuppressWarnings("serial")
public class RecordView extends JPanel{
    
    private JTable table;
    MainFrame mainFrm;
    
    public RecordView(MainFrame mf) {
        super(new BorderLayout());
        this.mainFrm = mf;
        add(new JScrollPane(table = new JTable()));
        
        table.setModel(new DefaultTableModel(
            new Object [][] {
                {"", "", "", ""},
                {"", "", "", ""},
                {"", "", "", ""},
                {"", "", "", null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        table.setRowSelectionAllowed(true);
        
        clearTable();
        table.setDefaultEditor(Object.class, null);
        
        table.addMouseListener(new MouseListener() {
			
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
    
    public void setMainFrame(MainFrame mf){
        mainFrm = mf;
    }
    
    public void clearTable(){
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        if (model.getRowCount() > 0) {
            for (int i = model.getRowCount() - 1; i > -1; i--) {
                model.removeRow(i);
            }
        }
        model.setColumnCount(0);
    }
    
    public void loadColumns(GTable gtable) throws IOException{
        if(gtable==null) return;
        
        List<GColumn> columns = gtable.getColumns();
        if(columns==null) return;
        
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        columns.forEach((column)->{
            model.addColumn(column);
        });
    }
    
    private void loadRecords(GTable gtable) throws IOException {
        if(gtable==null) return;
        List<GRecord> records = gtable.getRecords();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        records.forEach((record)->{
            model.addRow(record.getValues().toArray());
        });
    }
    
    public void load(GTable gtable) throws IOException {
        clearTable();
        loadColumns(gtable);
        loadRecords(gtable);
    }
    
    public int getSelectedRow(){
        return table.getSelectedRow();
    }
    
    public void showPopup(MouseEvent e){
    	
    	int rowCount = table.getRowCount();
    	
    	if(rowCount>0){
    		int row = table.rowAtPoint( e.getPoint() );
    		int column = table.columnAtPoint( e.getPoint() );

    		if (! table.isRowSelected(row))
    			table.changeSelection(row, column, false, false);
    	}
        
    	JPopupMenu popup = new JPopupMenu();
    	popup.add(mainFrm.createMenuItem(MainFrame.CI_ADD_RECORD));
    	if(rowCount>0){
    		popup.add(mainFrm.createMenuItem(MainFrame.CI_EDIT_RECORD));
    		popup.add(mainFrm.createMenuItem(MainFrame.CI_DELETE_RECORD));
    	}
    	popup.show(e.getComponent(), e.getX(), e.getY());
    	
    }
}
