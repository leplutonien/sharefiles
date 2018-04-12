package graphicinterfaces.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import fwkShareFiles.Constantes;
import fwkShareFiles.InterWin;
import fwkShareFiles.ShareFile;
import fwkShareFiles.client.Client;

public class DownloadProcessing extends JDialog implements Constantes,InterWin{
	private Client myclient;
	private GUIClient guiclient;
	private JProgressBar progressBar1,progressBar2;
	private String dest;
	private JLabel label_jpb1,label_jpb2 ;
	
	private DownloadProcessing(Client myclient,String dest,ShareFile[] files_to_download,GUIClient guiclient)
	{
		super(guiclient,true);
		this.myclient = myclient;
		this.guiclient = guiclient;
		this.dest = dest;
		initComponents();
		this.setVisible(true);
		
		myclient.DownloadFile(files_to_download,dest,progressBar1,label_jpb1,progressBar2,label_jpb2);
	}


	private void initComponents() 
	{
		this.setTitle(guiclient.getTitle()+"-Téléchargement");
		this.setSize(389,125);
		this.setLocationRelativeTo(guiclient);        
        this.setResizable(false);  
        //
        Container container = this.getContentPane();          
        
        JPanel pan1 = new JPanel(new FlowLayout(FlowLayout.LEFT,2,2));
        JLabel label1 = new JLabel("Destination :");
        label1.setFont(police1);
        JLabel label2 = new JLabel(dest);
        pan1.add(label1);
        pan1.add(label2);
        
        JPanel pan2 = new JPanel(new GridLayout(2,1));
        //pan2.setLayout(new BoxLayout(pan2, BoxLayout.PAGE_AXIS));
        progressBar1 = new JProgressBar(0, 100);
        progressBar1.setValue(0);
        progressBar1.setStringPainted(true);        
        progressBar1.setForeground(new Color(0,205,5));
        label_jpb1 = new JLabel("");        
        pan2.add(progressBar1);
        pan2.add(label_jpb1);        
        //pan2.setBackground(Color.cyan);
        
        JPanel pan3 = new JPanel(new GridLayout(2,1));
        progressBar2 = new JProgressBar(0, 100);
        progressBar2.setValue(0);
        progressBar2.setStringPainted(true);
        label_jpb2 = new JLabel("");
        pan3.add(progressBar2);
        pan3.add(label_jpb2);  
        //pan3.setBackground(color1);
        
        container.setLayout(new BorderLayout());         
        
        container.add(pan1,BorderLayout.NORTH);        
        container.add(pan2,BorderLayout.CENTER);
        container.add(pan3,BorderLayout.SOUTH);       
        
	}
	
	/**
	 * Permert de lancer la fenêtre de téléchargement
	 * @param myclient
	 * @param dest
	 * @param guiserver
	 */
	public static void begin(Client myclient,String dest,ShareFile[] file_to_download,GUIClient guiclient)
	{
		if(myclient != null && myclient.getClientState())
		{
			
			new DownloadProcessing(myclient, dest, file_to_download,guiclient);
		}
		else
		{
			JOptionPane.showMessageDialog(guiclient,"La connection au serveur n'est pas effectuée.\n" +
					"Impossible de lancer le téléchargement",TITLE_CLIENT,JOptionPane.ERROR_MESSAGE);
		}
	}


	@Override
	public void showMsgDlg(String Msg, String type) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void RefrechElement(Object obj) {
		// TODO Auto-generated method stub
		
	}

}
