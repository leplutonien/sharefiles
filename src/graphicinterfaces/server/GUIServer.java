package graphicinterfaces.server;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fwkShareFiles.Constantes;
import fwkShareFiles.InterWin;
import fwkShareFiles.ShareFile;
import fwkShareFiles.server.Server;
import graphicinterfaces.JTextFieldController;
import graphicinterfaces.MyJList;
import graphicinterfaces.MyWindowListener;
import graphicinterfaces.UtilitairesGui;

/**
 * Classe principale pour l'interface graphique du serveur
 * @author OGOULOLA Marlin
 */

public final class GUIServer extends JFrame implements InterWin,Constantes,ActionListener,KeyListener,ListSelectionListener{
	
	//déclaration des variables    
    private JToolBar toolbar;
    private JLabel labport;
    private JTextField txtport;
    private JButton bt_launch;
    private JButton bt_stop;
    private JButton bt_reduire;
    private JTabbedPane tabpane;
    private MyJList liste_action;
    private JList<String> liste_client_connected; 
    private ServeurInfo serveur_info;
    private Shares shares;
    private ServerLog serveur_log;
    private Server myserv = null;
    private ShareFile[] eltsShare = null;
    
    public GUIServer()
    {
    	super();
    	initComponents();
    	txtport.setText("4445");
    	manageButtons();
    }
    
    	private void initComponents() 
	{
		this.setTitle(TITLE_SERVER);
		//on applique le look and Feel de l'OS
		UtilitairesGui.lookandfeel(this);
		//on met un logo pour la fenetre
		UtilitairesGui.SetLogo(this, null);
		//
	    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);  
	    this.setSize(new Dimension(700, 700));
	    this.setLocationRelativeTo(null);
	    //on rend maximised la fenetre
        this.setExtendedState(GUIServer.MAXIMIZED_BOTH);       
        //le container pricipale
        Container container = this.getContentPane();       
        
        JPanel c = new JPanel();
        c.setLayout(new BorderLayout());
       // JScrollPane jp0 = new JScrollPane(c);
        container.add(c);
        
        //###############"gestion du toolbar###################       
        toolbar = new JToolBar(JToolBar.HORIZONTAL); 
        toolbar.setBorder(BorderFactory.createEtchedBorder());
        toolbar.setFloatable(false);
        toolbar.setRollover(true);
        toolbar.setPreferredSize(new Dimension(30, 35));         
        c.add(toolbar,BorderLayout.NORTH);
        
        JPanel pan1 = new JPanel();
        pan1.setLayout(new FlowLayout(FlowLayout.LEFT,2,2));  
        toolbar.add(pan1);
        labport = new JLabel("Port d'écoute :");
        labport.setFont(police2);
        pan1.add(labport);
        txtport = new JTextField(8);
        txtport.setPreferredSize(new Dimension(30, 25));  
        //on active uniquement la saisie des chiffres sur txtport
        txtport.setDocument(new JTextFieldController(JTextFieldController.ALL_TO_NUMBER));
        txtport.setFont(police2);
        pan1.add(txtport);        
        
        bt_launch = new JButton("Lancer",new ImageIcon("images/der.png"));
        bt_launch.setToolTipText("Lancer le serveur");
        bt_launch.setFont(police1);
        bt_stop = new JButton("Arrêter",new ImageIcon("images/delete.png"));
        bt_stop.setToolTipText("Arrêter le serveur");
        bt_stop.setFont(police1);
        bt_reduire = new JButton("Réduire",new ImageIcon("images/link_server.png"));
        bt_reduire.setToolTipText("Rendre invisible");
        bt_reduire.setFont(police1);
        
        pan1.add(bt_launch);
        pan1.add(bt_stop);
        pan1.add(bt_reduire);
        
        //######### FIN gestion du toolbar############
        
        tabpane = new JTabbedPane();
        JScrollPane jp1 = new JScrollPane(tabpane);
        
        JPanel pan2 = new JPanel();
        pan2.setLayout(new BorderLayout());
        
        liste_action = new MyJList();
        liste_action.addElement("images/serveur.png", "Information");
        liste_action.addElement("images/trans.png", "Partages");        
        liste_action.addElement("images/run.png", "Activités");
        liste_action.getResult();
        
        JScrollPane jp2 = new JScrollPane(liste_action);
        jp2.setPreferredSize(new Dimension(200, 250));
        
        liste_client_connected = new JList<String>();
        JScrollPane jp3 = new JScrollPane(liste_client_connected);
        jp3.setPreferredSize(new Dimension(200, 350));        
        JLabel lab_liste_client = new JLabel("Clients connectés au serveur");
        lab_liste_client.setFont(police2);
        lab_liste_client.setForeground(color1);
        JPanel pan3 = new JPanel(new BorderLayout(2,2));
        pan3.add(lab_liste_client,BorderLayout.NORTH);
        pan3.add(jp3,BorderLayout.SOUTH);
        
        //###########gestion de la barre d'etat############
	    JLabel  baretat = new JLabel("By OGOULOLA Marlin Yannick");
	    baretat.setFont(new Font("Dialog",Font.BOLD,12));
	    baretat.setPreferredSize(new Dimension(696, 25));	    
	    JPanel stateBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    stateBar.setBorder(BorderFactory.createEtchedBorder());
	    stateBar.add(baretat);
	    
        pan2.add(jp2, BorderLayout.NORTH);        
        pan2.add(pan3, BorderLayout.SOUTH);
        
        c.add(tabpane,BorderLayout.CENTER);
        c.add(pan2,BorderLayout.WEST);
        c.add(stateBar,BorderLayout.SOUTH);
        manageButtons();
        
        serveur_info = new ServeurInfo(); 
        shares = new Shares(this);
        serveur_log = new ServerLog(this);        
        
        //les écouteurs
        this.addWindowListener(new MyWindowListener(this));
        txtport.addKeyListener(this);
        liste_action.addListSelectionListener(this);
        bt_reduire.addActionListener(this);
        bt_launch.addActionListener(this);
        bt_stop.addActionListener(this);
        
        liste_action.setSelectedIndex(0);
	}
	
	public void setShareFiles(Vector<ShareFile> elementsShare)
	{
		
		int tot=elementsShare.size();
        ShareFile[] ret=new ShareFile[tot];
        int i=0;
        Enumeration<ShareFile> E=elementsShare.elements();
        while (E.hasMoreElements()) 
        {
             ret[i]=E.nextElement();            
             i++;
        }      
        
		this.eltsShare = ret;		
		
		if(myserv != null)
			myserv.addElementsShare(ret);
	}
    
	private void manageButtons()
    {
    	String port = txtport.getText();
    	
    	if(port.isEmpty() || (myserv != null && myserv.getServerState() == true))
    	{
    		bt_stop.setEnabled(false);
    		bt_launch.setEnabled(false);
    	}
    	else
    	{    		
    		bt_launch.setEnabled(true);
    		bt_stop.setEnabled(false);
    	}
    	
    	if(myserv != null && myserv.getServerState() == true)
    	{
    		bt_stop.setEnabled(true);
    		bt_launch.setEnabled(false);
    	}    		
    	else
    		bt_stop.setEnabled(false);
    	
    }
	
	public boolean getServerState()
	{
		if(myserv != null)
			return myserv.getServerState();
		else
			return false;
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object src = e.getSource();
		
		if(src == bt_reduire)
		{
			this.setVisible(false);
            Hide hide = new Hide(this);
		}
		
		if(src == bt_launch)
		{
			myserv = new Server(Integer.parseInt(txtport.getText()),this);
			
			if(eltsShare != null)
				myserv.addElementsShare(eltsShare);
			myserv.launchServer();
			
			if(myserv.getServerState() == true)
				txtport.setEditable(false);
			manageButtons();
		}
		
		if(src == bt_stop)
		{
			if(myserv.getServerState() == true)
			{
				myserv.stopServer();
				txtport.setEditable(true);
				manageButtons();											
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		Object src = e.getSource();
	
		if(src == txtport)
			manageButtons();
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) 
	{
		tabpane.removeAll();
		
		if(liste_action.getSelectedIndex() == 0)
		{
			serveur_info.refreshServeurInfo();
			tabpane.add("Information",serveur_info);
		}
		
		if(liste_action.getSelectedIndex() == 1)
		{
			tabpane.add("Les Fichiers partagés",shares);
		}
		
		if(liste_action.getSelectedIndex() == 2)
		{
			tabpane.add("Activité",serveur_log);
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
		serveur_log.addLog(type+":> "+Msg);		
	}


	@Override
	public void RefrechElement(Object obj) 
	{
		Vector<String> obj2 = (Vector<String>)obj;
		
		liste_client_connected.setListData(obj2);		
	}

     
}