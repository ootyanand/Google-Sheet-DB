/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googledb;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 *
 * @author ootyanand
 */
@SuppressWarnings("serial")
public class DbPropertiesDialog extends Dialog{
    
    private JTextField textFieldDBName;
    private JTextField textFieldDbId;
    private JTextArea textFieldClientSecret;
    private JTextField textFieldRefreshToken;

    public DbPropertiesDialog(Frame owner, Object... params) {
        super(owner, "Database Properties", true, params);
    }

    public void initData(Object... params){
        DatabaseInfo dbInfo = (DatabaseInfo) params[0]; 
        DBCredential cr = (DBCredential) params[1];
                
        textFieldDBName.setText(dbInfo.getName());
        textFieldDbId.setText(dbInfo.getId());
        
        try {
            textFieldClientSecret.setText(cr.getClientSecretJSON());
            textFieldClientSecret.setSelectionStart(0);
            textFieldClientSecret.setSelectionEnd(0);
        } catch (IOException ex) {
            Logger.getLogger(DbPropertiesDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        textFieldRefreshToken.setText(cr.getRefreshToken());
    }
    
    @Override
    public Component createCenterPan(Object... params){
        JLabel labelDbName = new javax.swing.JLabel("Database Name:");
        JLabel labelDbId = new javax.swing.JLabel("Database ID:");
        JLabel labelRefreshToken = new javax.swing.JLabel("Refresh Token:");
        JLabel labelClientSecret = new javax.swing.JLabel("Client Secret:");
        
        textFieldDBName = new javax.swing.JTextField();
        textFieldDbId = new javax.swing.JTextField();
        textFieldRefreshToken = new javax.swing.JTextField();
        textFieldClientSecret = new javax.swing.JTextArea();
        
        textFieldDBName.setEditable(false);
        textFieldDbId.setEditable(false);
        textFieldRefreshToken.setEditable(false);
        textFieldClientSecret.setEditable(false);
        
        textFieldDBName.setMaximumSize( new Dimension(  Integer.MAX_VALUE, 30) );
        textFieldDbId.setMaximumSize( new Dimension(  Integer.MAX_VALUE, 30) );
        textFieldRefreshToken.setMaximumSize( new Dimension(  Integer.MAX_VALUE, 30) );
        
        initData(params);
        
        JPanel p = new JPanel(new SpringLayout());
        p.add(labelDbName);
        p.add(textFieldDBName);
        p.add(labelDbId);
        p.add(textFieldDbId);
        p.add(labelRefreshToken);
        p.add(textFieldRefreshToken);
        p.add(labelClientSecret);
        p.add(textFieldClientSecret);
        SpringUtilities.makeCompactGrid(p,
                                4, 2, //rows, cols
                                6, 6,        //initX, initY
                                6, 6);     

        p.setBorder(BorderFactory.createEmptyBorder(15, 7, 7, 15));
        return p;
    }
}

