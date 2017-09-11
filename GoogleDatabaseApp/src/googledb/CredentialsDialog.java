/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googledb;

import gapi.GApi;
import gdatabase.GDatabase;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author ootyanand
 */
@SuppressWarnings("serial")
public class CredentialsDialog extends Dialog{
    
    DBCredential selCredential;
    List<DBCredential> allCredentials;
    
    static final String CMD_ADD = "ADD";
    
    private javax.swing.JComboBox<String> comboCredentials;
    
    public CredentialsDialog(Frame owner, Object... config) {
        super(owner, "Select Credentials", true, config);
        //setDlgSize(dlgSize);
        setSelCredential(((DBConfig)config[0]).getCurentCredential());
        setMinimumSize(new Dimension(360, 100));
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Component createCenterPan(Object... params){
        
        DBConfig config = (DBConfig) params[0];
        
        allCredentials = config.getAllCredentials();
        
        comboCredentials = new JComboBox<>();
        comboCredentials.setModel(new DefaultComboBoxModel(allCredentials.toArray()));
        
        comboCredentials.setMinimumSize(new Dimension(Integer.MAX_VALUE, 30));

        JButton btnDetails = new JButton("Details");
        btnDetails.addActionListener((ActionEvent e) -> { onDetails(); });
        
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel("Credentials:"), BorderLayout.WEST);
        p.add(comboCredentials, BorderLayout.CENTER);
        p.add(btnDetails, BorderLayout.EAST);
        p.setBorder(BorderFactory.createEmptyBorder(15, 7, 7, 15));
        return p;
    }
    
    @Override
    public void addButtons(){
        addButton(new JButton(CMD_ADD));
        addButton(new JButton(CMD_OK));
        addButton(new JButton(CMD_CANCEL));
    }
    
    @Override
    public void onCmd(String cmd){
        switch (cmd) {
            case CMD_OK:		onOK();	break;
            case CMD_CANCEL:  	onCancel(); break;
            case CMD_ADD:     	onAdd(); break;
            default: 			break;
        }
    }
    
    public void setSelCredential(DBCredential cr){
        if(cr==null){
            comboCredentials.setSelectedIndex(-1);
            return;
        }
        
        for (DBCredential d : allCredentials) {
            if (d.getName().equals(cr.getName())) {
                comboCredentials.setSelectedItem(d);
                break;
            }
        }
    }
    
    public void onAdd(){
        String name = getNewName();

        boolean uniqName =  true;
        while(name!=null ){
            for(DBCredential cr : allCredentials){
                if(cr.getName().equalsIgnoreCase(name)){
                    JOptionPane.showMessageDialog(this, "A credential with this name already exists, \nPlease enter adifferent name.", "Create Credentials", JOptionPane.ERROR_MESSAGE);
                    uniqName = false;
                    break;
                }
            }
            
            if(uniqName) break;
            
            name = getNewName();
            uniqName = true;
        }
        
        if(name==null) return;
        
        File file = getClientSecretFile();
        if(file==null) return;
        
        int n = JOptionPane.showConfirmDialog(
                    this,
                    "Do you hvae refresh token?",
                    "Create Credentials",
                    JOptionPane.YES_NO_OPTION);
        
        String refreshToken = null;
        if(n == JOptionPane.YES_OPTION){
            String s = (String)JOptionPane.showInputDialog(
                            this,
                            "Credential Name",
                            "Add Credential",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            "");

            //If a string was returned, say so.
            if ((s != null) && (s.length() > 0)) 
                refreshToken = s;
        }
        
       selCredential = getCredential(file, refreshToken);
       if(selCredential==null) return;
       
       selCredential.setName(name);
       allCredentials.add(selCredential);
        
        setVisible(false);
    }
    
    public void onDetails(){
        try {
            DBCredential cr = (DBCredential) comboCredentials.getSelectedItem();
            if(cr==null) return;
            JTextArea ta = new JTextArea(cr.getClientSecretJSON());
            ta.setEditable(false);
            JOptionPane.showMessageDialog(this, ta, "Credential Details", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException ex) {
            Logger.getLogger(CredentialsDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void onOK(){
        selCredential = (DBCredential) comboCredentials.getSelectedItem();
        setVisible(false);
    }
    
    @Override
    public void onCancel(){
        selCredential = null;
        setVisible(false);
    }
    
    private DBCredential getCredential(File file, String refreshToken){
         try (Reader reader = new FileReader(file)){
            DBCredential cr =  DBCredential.getCredentials(reader);
            if(cr!=null){
                if(refreshToken==null || refreshToken.isEmpty()){
                    String refresh = GApi.getRefreshToken(file.getPath(), GDatabase.getDefaultScope());
                    if(refresh==null || refresh.isEmpty())
                        return null;
                    
                    refreshToken = refresh;
                }
                cr.setRefreshToken(refreshToken);
            }
            return cr;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CredentialsDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CredentialsDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private String getNewName(){
        String s = (String)JOptionPane.showInputDialog(
                            this,
                            "Credential Name",
                            "Add Credential",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            "");

        //If a string was returned, say so.
        if ((s != null) && (s.length() > 0)) 
            return s;
        return null;
    }
    
     private File getClientSecretFile(){
        final JFileChooser fc = new JFileChooser(".");
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            return file;
        } 
        return null;
    }
}
