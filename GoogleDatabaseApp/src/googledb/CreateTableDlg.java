/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googledb;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;

/**
 *
 * @author ootyanand
 */
@SuppressWarnings("serial")
public class CreateTableDlg extends Dialog{
    
    String tableName;
    java.util.List<Object> columns;
    
    private JTextField textFieldTableName; 
    private javax.swing.JList<String> listColumns;
    
    public CreateTableDlg(Frame owner) {
        super(owner, "Create Table", true);
        setDlgSize(dlgSize);
    }
    
    @Override
    public Component createCenterPan(Object... params){
        
        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener((ActionEvent e) -> { onAdd(); });
        
        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener((ActionEvent e) -> { onDelete(); });
        
        JPanel rightBtnPan = new JPanel();
        rightBtnPan.setLayout(new BoxLayout(rightBtnPan, BoxLayout.Y_AXIS));
        
        rightBtnPan.add(Box.createHorizontalStrut(7));
        rightBtnPan.add(btnAdd);
        rightBtnPan.add(btnDelete);
        rightBtnPan.add(Box.createHorizontalStrut(7));
        
        JLabel lableTableName = new JLabel("Table Name:");
        JLabel lableColumns = new JLabel("Columns:");
        
        textFieldTableName = new JTextField();
        textFieldTableName.setMaximumSize( new Dimension(  Integer.MAX_VALUE, 30) );
        
        listColumns = new JList<String>(new DefaultListModel<>());
        listColumns.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JPanel p = new JPanel(new SpringLayout());
        p.add(lableTableName);
        p.add(textFieldTableName);
        p.add(Box.createVerticalBox());
        p.add(lableColumns);
        p.add(listColumns);
        p.add(rightBtnPan);
        SpringUtilities.makeCompactGrid(p,
                                2, 3, //rows, cols
                                6, 6,        //initX, initY
                                6, 6);     

        p.setBorder(BorderFactory.createEmptyBorder(15, 7, 7, 15));
        
        return p;
    }
    
    @Override
    public void addButtons(){
        addButton(new JButton(CMD_OK));
        addButton(new JButton(CMD_CANCEL));
    }
    
    public void onAdd(){
        String s = (String)JOptionPane.showInputDialog(
                            this,
                            "Column Name:",
                            "Create Table:",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            "");

        //If a string was returned, say so.
        if ((s != null) && (s.trim().length() > 0)) {
            DefaultListModel<String> model = (DefaultListModel<String>) listColumns.getModel();
            model.addElement(s);
        }
    }
    
    public void onDelete(){
        int selIndex = listColumns.getSelectedIndex();
        if(selIndex!=-1){
            DefaultListModel<String> model = (DefaultListModel<String>) listColumns.getModel();
            model.remove(selIndex);
        }
    }
    
    @Override
    public void onOK(){
        tableName = textFieldTableName.getText().trim();
        if(tableName.isEmpty() || listColumns.getModel().getSize()<=0){
            JOptionPane.showMessageDialog(this,
                                "Table name Or Columns is empty!",
                                "Create Table",
                                JOptionPane.ERROR_MESSAGE);
            return;
        }
        columns = new ArrayList<>();
        for(int i=0; i<listColumns.getModel().getSize(); i++){
            columns.add(listColumns.getModel().getElementAt(i));
        }
        setVisible(false);
    }
    
    @Override
    public void onCancel(){
        tableName = null;
        columns = null;
        setVisible(false);
    }
}
