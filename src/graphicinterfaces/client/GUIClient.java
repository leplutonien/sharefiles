package graphicinterfaces.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import fwkShareFiles.Constantes;
import fwkShareFiles.IPv4;
import fwkShareFiles.InterWin;
import fwkShareFiles.ShareFile;
import fwkShareFiles.client.Client;
import graphicinterfaces.MyTableModel;
import graphicinterfaces.MyWindowListener;
import graphicinterfaces.UtilitairesGui;

/**
 * Classe principale pour l'interface graphique du client
 * @author OGOULOLA Marlin Yannick
 */

public final class GUIClient extends JFrame implements Constantes,ActionListener,KeyListener,InterWin{
	private JTextField txt_adrServer;
	private JButton bt_connect,bt_disconnect,bt_reload,bt_download;
	private JTable jtab;
    private MyTableModel model ;
    private JTree tree;
    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode treeRoot;
	private String [] comboData = new String[]{"OUI","NON"}; 
	private Client myclient = null;
	private ShareFile [] mysharedFiles;	
	private JProgressBar progressBar1,progressBar2;
	
	private JLabel label_jpb1,label_jpb2 ;
	public GUIClient()
	{
		super();
		initComponents();
		txt_adrServer.setText("127.0.0.1:4445");
		manageButtons();
	}

	private void initComponents() 
	{
		this.setTitle(TITLE_CLIENT);
		//on applique le look and Feel de l'OS
		UtilitairesGui.lookandfeel(this);
		//on met un logo pour la fenetre
		UtilitairesGui.SetLogo(this, null);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setSize(765, 510);
		//on centre le gui
		this.setLocationRelativeTo(null);
		//on bloque le redimensionnement de la fenetre
	    this.setResizable(false);   
		//le container pricipale
        Container container = this.getContentPane();   
		container.setLayout(new BorderLayout());
				
		//###############"gestion du toolbar###################       
	    JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL); 	    
	    toolbar.setBorder(BorderFactory.createEtchedBorder());
	    toolbar.setFloatable(false);
	    toolbar.setRollover(true); 
	    JPanel pan3 = new JPanel();
        pan3.setLayout(new FlowLayout(FlowLayout.LEFT,2,2));
        
        JLabel label1 = new JLabel("Serveur :");
        label1.setFont(police1);
        pan3.add(label1);
        
        txt_adrServer = new JTextField(10);
        txt_adrServer.setFont(police1);
        txt_adrServer.setToolTipText("<html><h3>Donner l'adresse IP du serveur suivi du port d'écoute IP:port</h3></html>");
        pan3.add(txt_adrServer);
        
        bt_connect = new JButton("Se connecter");
        bt_connect.setFont(police1);
        pan3.add(bt_connect);
        
        bt_disconnect = new JButton("Se déconnecter", new ImageIcon("images/delete.png"));
        bt_disconnect.setFont(police1);
        pan3.add(bt_disconnect);
        
        bt_reload = new JButton("Rafraichir", new ImageIcon("images/redo.png"));
        bt_reload.setFont(police1);
        bt_reload.setToolTipText("Rafraichir la liste des documents partagés");
        pan3.add(bt_reload);
        
        bt_download = new JButton("Télécharger", new ImageIcon("images/download.png"));
        bt_download.setFont(police1);
        bt_download.setToolTipText("Rafraichir la liste des documents partagés");
        pan3.add(bt_download);
        
        toolbar.add(pan3);
        //###########"
        JPanel pan4 = new JPanel(new BorderLayout());
        //########### pour le JTable ##################"
      	jtab = new JTable();
      	String titres[]=new String[] {"Fichier","Type","Poid","Choisir?"};     	
        
	    model = new MyTableModel(null,titres); 
	    //on rend inéditatble quelques cellules
	    model.setCanEdit(new boolean [] {false, false,false,true});   
	    jtab.setFont(police1);
	    jtab.setModel(model);       
	    JScrollPane   jsp1 = new JScrollPane();
	    jsp1.setViewportView(jtab);      
	    jsp1.setPreferredSize(new Dimension(406, 410));
	    //on empeche la selection multiple des cellules
	    jtab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    //on empeche le grad des header
	    jtab.getTableHeader().setReorderingAllowed(false);
	    
	    jtab.getColumnModel().getColumn(0).setPreferredWidth(100);
	    jtab.getColumnModel().getColumn(1).setPreferredWidth(15);
	    jtab.getColumnModel().getColumn(3).setPreferredWidth(15); 
	    //le rendu du jtable
	    jtab.getColumn("Choisir?").setCellEditor(new DefaultCellEditor(new JComboBox(comboData)));
        //######## pour le JTree
	    tree = new JTree();	
		treeRoot = new DefaultMutableTreeNode("Documents partagés");
		treeModel = new DefaultTreeModel(treeRoot);
		tree.setModel(treeModel);
		tree.setRootVisible(true);
		tree.setEditable(false);		
		JScrollPane jsp2 = new JScrollPane(tree);
		jsp2.setPreferredSize(new Dimension(200, 550));
		
		//###########gestion de la barre d'etat############
	    JLabel  baretat = new JLabel("By OGOULOLA Marlin Yannick");
	    baretat.setFont(new Font("Dialog",Font.BOLD,12));
	    baretat.setPreferredSize(new Dimension(696, 25));	    
	    JPanel stateBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    stateBar.setBorder(BorderFactory.createEtchedBorder());
	    stateBar.add(baretat);
	    
	    //##########" les JProgressBar
	    JPanel pan5 = new JPanel(new GridLayout(2,1));        
        progressBar1 = new JProgressBar(0, 100);
        progressBar1.setValue(0);
        progressBar1.setStringPainted(true);        
        progressBar1.setForeground(new Color(0,205,5));
        label_jpb1 = new JLabel("");        
        pan5.add(progressBar1);
        pan5.add(label_jpb1);        
        //pan5.setBackground(Color.cyan);
        
        JPanel pan6 = new JPanel(new GridLayout(2,1));
        progressBar2 = new JProgressBar(0, 100);
        progressBar2.setValue(0);
        progressBar2.setStringPainted(true);
        progressBar2.setForeground(new Color(0,205,5));
        label_jpb2 = new JLabel("");
        pan6.add(progressBar2);
        pan6.add(label_jpb2); 
        //pan6.setBackground(Color.cyan);
        
        JPanel pan7 = new JPanel();
        pan7.setLayout(new BoxLayout(pan7, BoxLayout.PAGE_AXIS));
        pan7.add(pan5);
        pan7.add(pan6);
        pan7.add(stateBar);
        
		//######### disposition sur la gui
	    pan4.add(jsp1,BorderLayout.CENTER);
	    pan4.add(jsp2,BorderLayout.EAST);
	    
        JPanel jp = new JPanel();
		jp.setLayout(new BoxLayout(jp, BoxLayout.PAGE_AXIS));
		jp.add(toolbar);		
		jp.add(pan4);
		this.add(jp,BorderLayout.CENTER);
		
		
		this.add(pan7,BorderLayout.SOUTH);
		
		 //les écouteurs
        this.addWindowListener(new MyWindowListener(this));
        bt_connect.addActionListener(this);
        bt_disconnect.addActionListener(this);
        bt_download.addActionListener(this);
        bt_reload.addActionListener(this);
        txt_adrServer.addKeyListener(this);  
        
	}
	
	private void manageButtons() 
	{		
		if(txt_adrServer.getText().trim().isEmpty() || (myclient != null && myclient.getClientState()))
		{
			 //gestion des bouttons
	        bt_connect.setEnabled(false);
	        bt_disconnect.setEnabled(false);
	        bt_download.setEnabled(false);
	        bt_reload.setEnabled(false);
		}
		else
		{
			bt_connect.setEnabled(true);
	        bt_disconnect.setEnabled(false);
	        bt_download.setEnabled(false);
	        bt_reload.setEnabled(false);			
		}
		
		if(myclient != null && myclient.getClientState())
		{
			bt_connect.setEnabled(false);
	        bt_disconnect.setEnabled(true);
	        bt_download.setEnabled(true);
	        bt_reload.setEnabled(true);
		}
	}
	
	private void refreshTable()
	{
		//effacement des ligne du modele
        int i,tot=model.getRowCount();        
        for(i=0;i<tot;i++)
        	model.removeRow(0); 
        if(mysharedFiles != null)
        {
        	for (ShareFile sf : mysharedFiles) 
            {
            	model.addRow(new Object[]{sf.getName(),sf.getTypeFile(),sf.lengthToString(),comboData[1]});			
    		}
        }        
	}
	
	/**
	 * Permet de construire l'arbre 
	 */
	private void buildTree()
	{
		treeRoot.removeAllChildren();
		
		if(mysharedFiles != null)
		{
			for (ShareFile sf : mysharedFiles)
			{
				treeRoot.add(createAllNode(sf));
			}			
		}
		
		treeModel.reload();
	}
	
	/**
	 * Permet de créer tous les noeuds d'un document
	 * @param sf
	 * @return
	 */
	private DefaultMutableTreeNode createAllNode(ShareFile sf)
	{
		DefaultMutableTreeNode nodeParent = new DefaultMutableTreeNode(sf.getName()) ;
		
		if(sf.isDirectory())
		{
			 ShareFile [] sub =sf.listFiles(); 
			 int count= sub.length;
			 if(count > 0)
			 {
				 for (int i = 0; i < count; i++)
				 {
					 nodeParent.add(createAllNode(sub[i]));
				 }
			 }
			 
		}		
		return nodeParent;
	}
	
	/**
	 * Recupère les documents à télécharger
	 * @return un tableau de {@link ShareFile} ou null
	 */
	private  ShareFile[] getDocumentsToDownload()
	{
		ShareFile [] ret = null;
		Vector<ShareFile> vect = new Vector<>();
		
		if(mysharedFiles != null)
		{
			for (int i = 0; i < model.getRowCount(); i++) 
			{
					if(model.getValueAt(i, 3).toString().equals(comboData[0]))
					{
						vect.add(mysharedFiles[i]);
					}
			}
		}
		//transformation du vecteur en tableau
		if(vect.size() > 0)
		{
			int tot=vect.size();
	        ret = new ShareFile[tot];
	        int i=0;
	        Enumeration<ShareFile> E=vect.elements();
	        while (E.hasMoreElements()) 
	        {
	             ret[i]=E.nextElement();            
	             i++;
	        } 
		}
		return ret;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object src = e.getSource();
		
		if(src == bt_reload)
		{
			if(myclient!=null && myclient.getClientState())
			{
				this.mysharedFiles = myclient.getShareFile();
				this.refreshTable();
				buildTree();
			}			
		}
		
		if(src == bt_disconnect)
		{
			if(myclient != null && myclient.getClientState())
			{
				myclient.disConnect();
				if(!myclient.getClientState())
				{
					//effacement des ligne du modele
			        int i,tot=model.getRowCount();        
			        for(i=0;i<tot;i++)
			        	model.removeRow(0); 
			        mysharedFiles = null;
			        treeRoot.removeAllChildren();
			        treeModel.reload();
			        manageButtons();
			        txt_adrServer.setEditable(true);
			        progressBar1.setValue(0);
			        progressBar2.setValue(0);
			        label_jpb1.setText("");
			        label_jpb2.setText("");
				}
			}
		}
		
		if(src == bt_download)
		{
			ShareFile[] docToDownload = getDocumentsToDownload();
			if(docToDownload != null  && docToDownload.length > 0)
			{
				String savePath = UtilitairesGui.getFileChooser("D", "Choisir une destination", this);
				
				if(savePath != null && !savePath.isEmpty())
				{
					if(myclient != null && myclient.getClientState())
					{
						bt_download.setEnabled(false);
						bt_disconnect.setEnabled(false);
						bt_reload.setEnabled(false);
						myclient.DownloadFile(docToDownload,savePath,progressBar1,label_jpb1,progressBar2,label_jpb2);						
					}
					
				}
				else
				{
					JOptionPane.showMessageDialog(this,"Aucun dossier de destination n'est choisi",
							TITLE_CLIENT,JOptionPane.ERROR_MESSAGE);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(this,"Veuillez choisir des documents à télécharger",
						TITLE_CLIENT,JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if(src == bt_connect)
		{
			String [] elts = txt_adrServer.getText().split(":");
			
			if(elts != null && elts.length == 2)
			{
				if(IPv4.isIPv4(elts[0]))
				{
					int port;
					try
					{
						port =  Integer.parseInt(elts[1]);
						myclient = new Client(elts[0], port, this);
						
						if(myclient.connecter())
						{
							//on peut prévoir un systeme d'authentification
							//mais ici on laisse passer
							myclient.authenticate("login", "pwd");
							
							if(myclient.isAuthenticated() && myclient.getClientState())
							{
								manageButtons();
								txt_adrServer.setEditable(false);
								bt_reload.doClick();
							}
						}						
					}
					catch(NumberFormatException e1)
					{
						JOptionPane.showMessageDialog(this,"Erreur au niveau du port d'écoute du serveur",
								TITLE_CLIENT,JOptionPane.ERROR_MESSAGE);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this,"L'adresse IP du serveur est incorrecte",
							TITLE_CLIENT,JOptionPane.ERROR_MESSAGE);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(this,"Erreur au niveau de l'adresse serveur",
						TITLE_CLIENT,JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		Object src = e.getSource();
		
		if(src == txt_adrServer)
		{
			manageButtons();
		}
		
	}	

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showMsgDlg(String Msg, String type) 
	{
		if(type == INFORMATION_MESSAGE)
			JOptionPane.showMessageDialog(this,Msg,	TITLE_CLIENT,JOptionPane.INFORMATION_MESSAGE);
		
		if(type == ERROR_MESSAGE)
			JOptionPane.showMessageDialog(this,Msg,	TITLE_CLIENT,JOptionPane.ERROR_MESSAGE);
		manageButtons();
	}

	@Override
	public void RefrechElement(Object obj) 
	{
		if(obj.equals(DOWNLOADS_TERMINATE))
		{
			manageButtons();
		}
		
	}
}