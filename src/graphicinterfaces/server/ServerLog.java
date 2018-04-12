package graphicinterfaces.server;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import fwkShareFiles.Constantes;
import graphicinterfaces.UtilitairesGui;

public class ServerLog extends JPanel implements Constantes, ActionListener{
	private GUIServer guiserver;
	JTextArea jta_log;
	JButton bt_saveLog;
	public ServerLog(GUIServer guiserver)
	{
		super();
		this.guiserver = guiserver;
		initComponents();
	}

	private void initComponents() 
	{
		this.setLayout(new BorderLayout());
		
		JPanel pan1 = new JPanel(new FlowLayout(FlowLayout.LEFT,2,2));
		JLabel label1 = new JLabel("::: Activité sur le serveur");
		label1.setFont(police3);
		label1.setForeground(color1);
		pan1.add(label1);
		bt_saveLog = new JButton("Sauvegarder");
		pan1.add(bt_saveLog);
		
		jta_log = new JTextArea();
		jta_log.setEditable(false);
		
		JScrollPane jsp = new JScrollPane(jta_log);
		
		this.add(pan1,BorderLayout.NORTH);
		this.add(jsp,BorderLayout.CENTER);
		
		bt_saveLog.addActionListener(this);
	}
	
	public void addLog(String log)
	{
		jta_log.append(log+"\n");
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == bt_saveLog)
		{
			String path = UtilitairesGui.getFileChooser("D", "Enregistrer le fichier log", guiserver);
			
			if(path != null)
			{
				File dest = new File(path+"/log_"+TITLE_SERVER+"("+new Date().toString()+").txt");
				
				try 
				{
					FileWriter fw = new FileWriter(dest);
					fw.write(jta_log.getText());
					fw.close();
					JOptionPane.showMessageDialog(guiserver,"Sauvegarde effectuée avec succès",TITLE_SERVER,JOptionPane.INFORMATION_MESSAGE);
				} 
				catch (IOException ex) 
				{
					JOptionPane.showMessageDialog(guiserver,ex.getMessage(),TITLE_SERVER,JOptionPane.ERROR_MESSAGE); 
				}
			}
			
		}
		
	}

}
