/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googledb;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author ootyanand
 */
public class Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	
    	/* Set the Nimbus look and feel */
    	setupUI();
        
    	/* Create and show the main frame */
        new MainFrame().setVisible(true);
    }
    
    public static void setupUI(){
    	/* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Application.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Application.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Application.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Application.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    public static ImageIcon getImageIcon(String imgFileName, int width, int height){
        ImageIcon imageIcon = getImageIcon(imgFileName); 
        Image image = imageIcon.getImage(); // transform it 
        Image newimg = image.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        return new ImageIcon(newimg);  // transform it back
    }
    
    public static ImageIcon getImageIcon(String imgFileName, int size){
    	return getImageIcon(imgFileName, size, size);
    }
    
    public static ImageIcon getImageIcon(String imgFileName){
        return new ImageIcon(MainFrame.class.getResource("/googledb/resources/" + imgFileName));
    }
}
