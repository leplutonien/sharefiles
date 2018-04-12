package graphicinterfaces;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Classe pour la gestion des événéments sur les icônes 
 * du coin d'une JFrame
 * @author OGOULOLA Marlin 
 */
public class MyWindowListener implements WindowListener {
	
	private JFrame gui;	
	
	public MyWindowListener (JFrame a)
	{
		gui = a;
	}	

	@Override
	public void windowClosing(WindowEvent e) 
	{
		int rep=JOptionPane.showConfirmDialog(gui,"Voullez-vous vraiment fermer?",gui.getTitle(),JOptionPane.YES_NO_OPTION);            
        if(rep == JOptionPane.YES_OPTION)
            System.exit(0);
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) 
	{
		// TODO Auto-generated method stub
				
	}

	@Override
	public void windowDeiconified(WindowEvent e) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) 
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
