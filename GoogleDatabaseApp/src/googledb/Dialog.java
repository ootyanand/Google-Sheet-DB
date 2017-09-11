/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googledb;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author ootyanand
 */
@SuppressWarnings("serial")
public class Dialog extends javax.swing.JDialog{
    
    Dimension dlgSize = new Dimension(500, 300);
    
    Component titlePanel;
    Component centerPanel;
    Component buttonPanel;
    
    static final String CMD_OK = "OK";
    static final String CMD_CANCEL = "Cancel";

    public Dialog(Frame owner, String title, boolean modal, Object... params) {
        super(owner, title, modal);
        init(params);
    }
    
    private void init(Object... params) {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        add(titlePanel = createTitlePan(params), BorderLayout.NORTH);
        add(centerPanel = createCenterPan(params), BorderLayout.CENTER);
        add(buttonPanel = createButtonPan(params), BorderLayout.SOUTH);
        //centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 7, 7, 15));
        addButtons();
        //setDlgSize(dlgSize);
        pack();
        setLocationRelativeTo(getParent());
    }
    
    public Component createTitlePan(Object... params){
        return new JPanel();
    }
    public Component createCenterPan(Object... params){
        return new JPanel();
    }
    public Component createButtonPan(Object... params){
        return new JPanel(new FlowLayout(FlowLayout.CENTER));
    }
    
    public void addButtons(){
        addButton(new JButton(CMD_OK));
    }
    
    public Component getCenterPan(){
        return centerPanel;
    }
    
    public Component getTitlePan(){
        return titlePanel;
    }
    
    public Component getButtonPan(){
        return buttonPanel;
    }
    
    public void onCmd(String cmd){
        switch (cmd) {
            case CMD_OK:		onOK(); break;
            case CMD_CANCEL: 	onCancel(); break;
            default:        	break;
        }
    }
    
    public void onOK()      { setVisible(false); }
    public void onCancel()  { setVisible(false); }
    
    public void addButton(JButton btn){
        btn.addActionListener((ActionEvent e) -> { onCmd(btn.getText());  });
        ((JPanel)buttonPanel).add(btn);
    }
    
    public void setDlgSize(Dimension dim){
        setSize(dim);
        setLocationRelativeTo(super.getParent());
    }
}
