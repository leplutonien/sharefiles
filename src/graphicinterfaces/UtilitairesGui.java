package graphicinterfaces;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileSystemView;

/**
 * <p>
 * <span style="font-weight:bold;text-align: center;color:green">
 * Titre: UtilitairesGui</span><br>
 * <span style="font-weight:bold">
 * Description:</span><br>
 * Classe contenant des actions sur une interface graphique</p>
 * @author OGOULOLA Marlin Yannick
 * @society PlutoSoft-Benin
 */

public final class UtilitairesGui {
	
	/**
	 * Méthode pour attribuer le look and feel de l'OS
	 * sur lequel une interface graquique est lancée.        
	 * @param gui
	 */
    public static void lookandfeel(JFrame gui)
    {
        try 
        { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(gui);
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) 
        {
            System.out.println("FrmUsers.FrmUsers : impossible d'appliquer le théme du systéme");
	    }
    }
    /**
     * Méthode permettant d'attribuer un logo à une fenêtre
     * @param gui la fenêtre
     * @param pathImage le chenin vers le logo
     */
    public static void SetLogo(JFrame gui,String pathImage)
    {
    	if(gui != null && pathImage != null)
             gui.setIconImage(Toolkit.getDefaultToolkit().getImage(pathImage));
        
    }   
    
    public static void Centrer(JFrame gui)
    {
        Dimension dimensionecran = Toolkit.getDefaultToolkit().getScreenSize();
    	gui.setLocation(
    	        (dimensionecran.width-gui.getWidth())/2,
    	        (dimensionecran.height-gui.getHeight())/2
    	        );
    }
    
    /**
	* Méthode qui retourne le systeme d'exploitation sur lequel le programme
	* est lancé. Ceci permet de gérer l'affichage
	* @return
	*/
	public static String getOS() 
	{
		return System.getProperty("os.name");
	}
	
	/**
	 * 
	 * @param type
	 * @param title
	 * @param gui
	 * @return le chemin du {@link File} choisi ou null
	 */
	public static String getFileChooser(String type,String title,JFrame  gui)
	{
		String ret = null;
		
		FileSystemView fsv = FileSystemView.getFileSystemView();
        JFileChooser fc = new JFileChooser(fsv.getHomeDirectory());
        
        if(type.equalsIgnoreCase("D"))
        	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        else
        	fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setDialogTitle(gui.getTitle()+" - "+title);
        
        int ValRetour=fc.showOpenDialog(gui);
        
        if (ValRetour==0)//l'utilisateur à faire un choix
        {
        	File f = fc.getSelectedFile();
            if(f.exists())
            {
            	ret = f.getAbsolutePath();
            	
            	if(f.isDirectory())
            	{
            		if(!ret.endsWith("/"))
            			ret += File.separator;
            	}
            }
        }        
		return ret;
	}
}
