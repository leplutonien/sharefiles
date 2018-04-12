package graphicinterfaces.client;

import javax.swing.JOptionPane;

import fwkShareFiles.Constantes;

public class RunClient implements Constantes{
	
	public static void main(String[] args) 
	{
		//on s'assure que java7 est installé vus les inst
	    String [] params = System.getProperty("java.version").split("\\.");
	    if(Integer.parseInt(params[0]) == 1 && Integer.parseInt(params[1])  < 7)
	    {                     
	           JOptionPane.showMessageDialog(null,"Vous devez installer JAVA7 ou une version supérieur avant"
	                   + " de lancer ce programme.",TITLE_CLIENT,JOptionPane.INFORMATION_MESSAGE);
	    }
	    else
	    {
	           GUIClient gc = new GUIClient();
	           gc.setVisible(true);
	    }		
	}
	

}
