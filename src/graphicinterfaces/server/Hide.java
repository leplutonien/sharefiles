package graphicinterfaces.server;

import java.awt.AWTException;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * @author plutonien
 */

public class Hide {
    private final Image icon=new ImageIcon("images/server.gif").getImage();
    private final Font police = new Font("Dialog",Font.PLAIN,13);
    
    public Hide(final JFrame fm)
    {
        if(SystemTray.isSupported())
        {
            final TrayIcon trayIcon = new TrayIcon(icon,fm.getTitle());
            final SystemTray tray = SystemTray.getSystemTray();
            
            PopupMenu popup = new PopupMenu();
            MenuItem openItem = new MenuItem("Afficher");
            MenuItem endItem = new MenuItem("Fermer");
            popup.add(openItem);
            popup.addSeparator();
            popup.add(endItem);
            
            trayIcon.setPopupMenu(popup);
            try 
            { 
                tray.add(trayIcon);
            }
            catch (AWTException ex) 
            {
                System.out.println(ex.getMessage());
            }
            
            openItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    fm.setVisible(true);
                    fm.setExtendedState(fm.getExtendedState() | fm.MAXIMIZED_BOTH);
                    tray.remove(trayIcon);
                }               
            });
            
            endItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    JLabel label=new JLabel("Voullez-vous vraiment fermer ? ");
                    label.setFont(police);
                    int rep=JOptionPane.showConfirmDialog(fm,label,fm.getTitle(),JOptionPane.YES_NO_OPTION);
                    if(rep==JOptionPane.YES_OPTION)
                    {
                       tray.remove(trayIcon);
                       System.exit(0); 
                    }
                }                
            });       
        }        
    }    
}