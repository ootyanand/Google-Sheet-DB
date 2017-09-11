/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googledb;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import gdatabase.GRecord;

/**
 *
 * @author ootyanand
 */
@SuppressWarnings("serial")
public class EditRecordDialog extends Dialog{
    
    GRecord record;
    List<JTextField> fields;
    

    public EditRecordDialog(Frame owner, Object... params) {
        super(owner, "Edit Record", true, params);
        setDlgSize(new Dimension(600, 500));
        setResizable(true);
    }

    @Override
    public Component createCenterPan(Object... params){
        
        fields = new ArrayList<>();
        JScrollPane scrollPanel = new javax.swing.JScrollPane();
        
        record = (GRecord) params[0];
        
        int numPairs = record.getColumn().size();
        
        Dimension dim = new Dimension(Integer.MAX_VALUE, 23);
        //Create and populate the panel.
        JPanel p = new JPanel(new SpringLayout());
        for (int i = 0; i < numPairs; i++) {
            JLabel l = new JLabel(record.getColumn(i).getName(), JLabel.TRAILING);
            p.add(l);
            JTextField textField = new JTextField(10);
            textField.setPreferredSize(dim);
            textField.setMaximumSize(dim);
            if(record.getValues().size()>i)
                textField.setText(record.getValue(i).toString());
            l.setLabelFor(textField);
            fields.add(textField);
            p.add(textField);
        }
        //Lay out the panel.
        SpringUtilities.makeCompactGrid(p,
                                numPairs, 2, //rows, cols
                                6, 6,        //initX, initY
                                6, 6);       //xPad, yPad
     
        scrollPanel.setViewportView(p);
        scrollPanel.setBorder(BorderFactory.createEmptyBorder(15, 7, 7, 15));
//        JPanel outer = new JPanel(new BorderLayout());
//        outer.add(scrollPanel);
        return scrollPanel;
    }
    
    @Override
    public void addButtons(){
        addButton(new JButton(CMD_OK));
        addButton(new JButton(CMD_CANCEL));
    }
    
    @Override
    public void onOK(){
        int numPairs = record.getColumn().size();
        for (int i = 0; i < numPairs; i++) {
            record.setValue(i, fields.get(i).getText());
        }
        setVisible(false);
    }
    
    @Override
    public void onCancel(){
        record = null;
        setVisible(false);
    }
}
