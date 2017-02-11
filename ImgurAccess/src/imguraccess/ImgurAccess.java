/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imguraccess;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Paul
 */
public class ImgurAccess {
    
    
   
    public static void main(String[] args) {
        JPanel p = new ActivePanel();
        JFrame f = new UIFrame(p);
    }
    
}
