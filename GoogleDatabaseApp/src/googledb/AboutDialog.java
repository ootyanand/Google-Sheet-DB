/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googledb;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author ootyanand
 */
@SuppressWarnings("serial")
public class AboutDialog extends Dialog{
    
    public AboutDialog(Frame owner) {
        super(owner, "About", true);
    }
    
    @Override
    public Component createCenterPan(Object... params){
        JPanel panInfo = new JPanel();
        panInfo.setLayout(new BoxLayout(panInfo, BoxLayout.Y_AXIS));
        
        panInfo.add(Box.createHorizontalStrut(7));
        panInfo.add(new JLabel("Google Database"));
        panInfo.add(new JLabel("Version: 0.0.0.1"));
        panInfo.add(Box.createHorizontalStrut(7));
        
        
        JPanel pan = new JPanel(new BorderLayout());
        pan.add(new JLabel(Application.getImageIcon("logo.png", 48, 48)), BorderLayout.WEST);
        pan.add(Box.createHorizontalStrut(20), BorderLayout.CENTER);
        pan.add(panInfo, BorderLayout.EAST);
        pan.setBorder(BorderFactory.createEmptyBorder(15, 7, 7, 15));
        return pan;
    }
}
