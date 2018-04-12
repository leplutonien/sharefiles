package graphicinterfaces.server;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JLabel;
import javax.swing.JPanel;

import fwkShareFiles.Constantes;

/**
 * Classe pour l'affichage des informations sur 
 * la machine demarrant le serveur
 * author OGOULOLA Marlin Yannick
 * @society PlutoSoft-Benin
 */
public class ServeurInfo  extends JPanel implements Constantes{
	private JLabel [] label = new JLabel[11];
	public ServeurInfo()
	{
		super();		
		initComponents();
		refreshServeurInfo();
		setVisible(true);
	}

	private void initComponents() 
	{
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		label[0] = new JLabel("::: Information sur la machine hôte");
		label[0].setFont(police3);
		label[0].setForeground(color1);
		JPanel pan1 = new JPanel(new FlowLayout(FlowLayout.LEFT,2,2));
		pan1.add(label[0]);		
		
		JPanel pan2 = new JPanel(new FlowLayout(FlowLayout.LEFT,2,2));
		label[1] = new JLabel("Adresse IP         :");
		label[1].setFont(police2);
		label[2] = new JLabel();
		label[2].setFont(police1);		
		pan2.add(label[1]);
		pan2.add(label[2]);
		
		JPanel pan3 = new JPanel(new FlowLayout(FlowLayout.LEFT,2,2));
		label[3] = new JLabel("Nom de l'hôte    :");
		label[3].setFont(police2);
		label[4] = new JLabel();
		label[4].setFont(police1);
		pan3.add(label[3]);
		pan3.add(label[4]);
		
		JPanel pan4 = new JPanel(new FlowLayout(FlowLayout.LEFT,2,2));
		label[5] = new JLabel("OS installé         :");
		label[5].setFont(police2);
		label[6] = new JLabel();
		label[6].setFont(police1);	
		pan4.add(label[5]);
		pan4.add(label[6]);		
		
		JPanel pan7 = new JPanel(new FlowLayout(FlowLayout.LEFT,2,2));
		label[7] = new JLabel("Ram                   :");
		label[7].setFont(police2);
		label[8] = new JLabel("unknown");
		label[8].setFont(police1);	
		pan7.add(label[7]);
		pan7.add(label[8]);
		
		JPanel pan8 = new JPanel(new FlowLayout(FlowLayout.LEFT,2,2));
		label[9] = new JLabel("Nb. Processeur :");
		label[9].setFont(police2);
		label[10] = new JLabel();
		label[10].setFont(police1);	
		pan8.add(label[9]);
		pan8.add(label[10]);
		
		
		
		JPanel space = new JPanel();
		JPanel pan6 = new JPanel(new GridLayout(7,1,0,1));
		
		pan6.add(pan1);
		pan6.add(space);
		pan6.add(pan2);
		pan6.add(pan3);
		pan6.add(pan4);	
		pan6.add(pan7);
		pan6.add(pan8);
			
		
		this.add(pan6);
		
		
	}
	
	public void refreshServeurInfo()
	{
		try
		{
			InetAddress a = InetAddress.getLocalHost();
			String res[]=new String[1];
            res=a.toString().split("/");
			label[2].setText(res[1]);
			label[4].setText(res[0]);
		}
		catch(UnknownHostException ex)
		{			
		}
		label[6].setText(System.getProperty("os.name")+" Version "+System.getProperty("os.version")
                +" ("+System.getProperty("sun.os.patch.level")+") "+System.getProperty("os.arch"));	
		label[10].setText(Runtime.getRuntime().availableProcessors()+"");
		
	}

}
