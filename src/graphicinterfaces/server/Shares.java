package graphicinterfaces.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import fwkShareFiles.Constantes;
import fwkShareFiles.IPv4;
import fwkShareFiles.ShareFile;
import graphicinterfaces.UtilitairesGui;

public final class Shares extends JPanel implements Constantes,ActionListener,ListSelectionListener{
	private JTextField txtpath ;
	private JButton bt_choix_path;
	private JRadioButton jrb_canDownload,jrb_canNotDownload;
	private JTextArea jta_listeIp;
	private JButton bt_save,bt_delete,bt_clean;
	private JTable jtab;
    private DefaultTableModel model ;
    private Vector<ShareFile> myShareFiles = new Vector<>();
    private GUIServer guiserveur;
    private JTree tree;
    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode treeRoot;
    /**
     * Pour la ligne sélectionner dans la JTable
     */
    private int indexSelected = -1;	
    
	public Shares(GUIServer guiserveur)
	{
		super();
		this.guiserveur = guiserveur;
		initComponents();		
	}

	private void initComponents() 
	{
		this.setLayout(new BorderLayout());
		
		JPanel pan1 = new JPanel(new FlowLayout(FlowLayout.LEFT));		
		pan1.setBorder(BorderFactory.createEtchedBorder());		
		
		JPanel pan3 = new JPanel(new FlowLayout(FlowLayout.LEFT,2,2));
		JLabel label1 = new JLabel("::: Gestion des documents partagés");
		label1.setFont(police3);
		label1.setForeground(color1);
		pan3.add(label1);
		
		//#################"la partie du  choix du document à partager
		JPanel pan4 = new JPanel(new FlowLayout(FlowLayout.LEFT,2,2));
		JLabel label2 = new JLabel("Path          :");
		label2.setFont(police1);
		txtpath = new JTextField(20);
		txtpath.setFont(police4);
		txtpath.setEditable(false);
		bt_choix_path = new JButton("...");
		bt_choix_path.setFont(police2);
		pan4.add(label2);
		pan4.add(txtpath);
		pan4.add(bt_choix_path);
		
		JPanel pan5 = new JPanel(new FlowLayout(FlowLayout.LEFT,2,2));
		JLabel label3 = new JLabel("Restriction :");
		label3.setFont(police1);
		jrb_canDownload = new JRadioButton("Autorisé",true);
		jrb_canDownload.setFont(police1);		
		jrb_canNotDownload = new JRadioButton("Non autorisé");
		jrb_canNotDownload.setFont(police1);
		ButtonGroup bg=new ButtonGroup();
		bg.add(jrb_canDownload);
		bg.add(jrb_canNotDownload);
		
		pan5.add(label3);
		pan5.add(jrb_canDownload);
		pan5.add(jrb_canNotDownload);
		
        jta_listeIp = new JTextArea(3,20);
        JScrollPane jsp = new JScrollPane(jta_listeIp);
        jta_listeIp.setToolTipText("<html><h3>Donner l'adresse IP des machines distantes " +
        		"répondant au critère de restriction et les séparant par ';' <br>" +
        		"si aucune information(IP) n'est mise, tous les clients auront accès au fichier.</h3></html>");
        
        JPanel pan6 = new JPanel(new GridLayout(3, 1));
        pan6.setBorder(BorderFactory.createTitledBorder("Document"));
        pan6.add(pan4);
        pan6.add(pan5);
        pan6.add(jsp);        
               
		//############## pour les boutons de controle
		JPanel pan7 = new JPanel(new FlowLayout(FlowLayout.LEFT,2,2));
		bt_save = new JButton("Enregistrer", new ImageIcon("images/save.png"));
		bt_delete = new JButton("Supprimer", new ImageIcon("images/delete.png"));
		bt_clean = new JButton("Effacer");
		bt_save.setFont(police1);
		bt_delete.setFont(police1);		
		bt_clean.setFont(police1);
		pan7.add(bt_save);
		pan7.add(bt_delete);	
		pan7.add(bt_clean);
		
		//########### pour le JTable ##################"
		jtab = new JTable();
	    String titres[]=new String[] {"Path","Type"};
	    //on rend inéditatble les cellules
	    model = new DefaultTableModel(null,titres)
	    {
	    	boolean[] canEdit = new boolean [] {false, false};
	        @Override
	        public boolean isCellEditable(int rowIndex, int columnIndex) 
	        {
	        	return canEdit [columnIndex];
	        }
	    };
	       
	    jtab.setFont(police1);
	    jtab.setModel(model);       
	    JScrollPane   jsp2 = new JScrollPane();
	    jsp2.setViewportView(jtab);      
	    jsp2.setPreferredSize(new Dimension(406, 320));
	    //on empeche la selection multiple des cellules
	    jtab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    //on empeche le grad des header
	    jtab.getTableHeader().setReorderingAllowed(false);   
	    //
	    jtab.getColumnModel().getColumn(0).setPreferredWidth(200);
	    jtab.getColumnModel().getColumn(1).setPreferredWidth(15); 
	    //////////
		
		JPanel pan9 = new JPanel();
		
		JPanel pan8 = new JPanel();
		pan8.setLayout(new BoxLayout(pan8, BoxLayout.PAGE_AXIS));
        pan8.add(pan3);
        pan8.add(pan9);        
		pan8.add(pan6);
		pan8.add(pan7);
		pan8.add(pan9);
		pan8.add(jsp2);		
		
		pan1.add(pan8);	
		
		//###################pour le JTee ##########################
		JPanel pan2 = new JPanel();
		pan2.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JPanel pan10 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel label4 = new JLabel("::: Archithecture des documents partagés");
		label4.setFont(police2);
		label4.setForeground(color1);
		pan10.add(label4);
		
		tree = new JTree();	
		treeRoot = new DefaultMutableTreeNode("Mes documents partagés");
		treeModel = new DefaultTreeModel(treeRoot);
		tree.setModel(treeModel);
		tree.setRootVisible(true);
		tree.setEditable(false);		
		JScrollPane jsp1 = new JScrollPane(tree);
		jsp1.setPreferredSize(new Dimension(125, 550));
		
		JPanel pan11 = new JPanel();
		pan11.setLayout(new BoxLayout(pan11, BoxLayout.PAGE_AXIS));
		pan11.add(pan10);
		JPanel pan12 = new JPanel();
		pan11.add(pan12);
		pan11.add(jsp1);
		
		pan2.add(pan11);		
		//###################fin JTee ##########################
		
		this.add(pan1,BorderLayout.CENTER);
		this.add(pan2,BorderLayout.EAST);
		//les écouteurs
		bt_save.addActionListener(this);
		bt_delete.addActionListener(this);
		bt_choix_path.addActionListener(this);
		bt_clean.addActionListener(this);
		//on ajoute un écouteur sur la jtable
	    ListSelectionModel selectionModel=jtab.getSelectionModel();
	    selectionModel.addListSelectionListener(this);      
		manageButtons();
		
	}
	/**
	 * Permet de construire l'arbre 
	 */
	private void buildTree()
	{
		treeRoot.removeAllChildren();		
		int tot = myShareFiles.size();
		
		for (int i = 0; i < tot; i++)
		{
			treeRoot.add(createAllNode(myShareFiles.get(i)));
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
	 * Pour la gestion des boutons
	 */
	private void manageButtons()
	{
		if(txtpath.getText().isEmpty())
			bt_save.setEnabled(false);
		else
			bt_save.setEnabled(true);
		
		if(indexSelected == -1)
			bt_delete.setEnabled(false);
		else
			bt_delete.setEnabled(true);
		
	}
	
	/**
	 * Pour remplir la JTable
	 */
	private void refreshJTable()
	{
		//effacement des ligne du modele
        int i,tot=model.getRowCount();        
        for(i=0;i<tot;i++)
            model.removeRow(0); 
        
        tot = myShareFiles.size();
        
        for(i=0;i<tot;i++)
        {
        	model.addRow(new String[]{myShareFiles.get(i).getAbsolutePath(),
        			myShareFiles.get(i).getTypeFile()});
        }
	}
	
	/**
	 * Permet de choisir un document à partager
	 */
	private String choosePath()
	{
		String ret = null;
		ButtonGroup bg=new ButtonGroup();
		JRadioButton jrb1 = new JRadioButton("Fichier",true);
		JRadioButton jrb2 = new JRadioButton("Dossier");
		jrb1.setFont(police1);
		jrb2.setFont(police1);
		bg.add(jrb1);
		bg.add(jrb2);
		String option[]={"Valider","Annuler"};
	    Object message[]={jrb1,jrb2};
	    
	    int resultat=JOptionPane.showOptionDialog(guiserveur,message,"Type de document",JOptionPane.DEFAULT_OPTION,
	            JOptionPane.INFORMATION_MESSAGE,
	        new ImageIcon(""),option,option[0]);
	    switch (resultat) {
		case 0:
			if(jrb1.isSelected() == true)
				ret = UtilitairesGui.getFileChooser("F","Document à partager", guiserveur);
			else
			{
				if(jrb2.isSelected() == true)
					ret = UtilitairesGui.getFileChooser("D","Document à partager", guiserveur);
			}
			break;

		default:
			break;
		}
	        
		return ret;
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object src = e.getSource();
		
		if(src == bt_choix_path)
		{
			String path = choosePath();
			if(path != null)
				txtpath.setText(path);
			manageButtons();
		}
		
		if(src == bt_clean )
		{
			txtpath.setText(null);
			jta_listeIp.setText(null);
			indexSelected = -1;
			jrb_canDownload.setSelected(true);
			manageButtons();
		}
		
		if(src == bt_delete)
		{
			if(guiserveur.getServerState() && myShareFiles.size() == 1)
			{
				JOptionPane.showMessageDialog(this,"La liste des documents partagés ne peut être vide, quand le serveur est lancé.",
						TITLE_SERVER,JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				int rep=JOptionPane.showConfirmDialog(this,"Voullez-vous vraiment supprimer ce document partagé ? ",TITLE_SERVER,
	                    JOptionPane.YES_NO_OPTION);            
	            if(rep == JOptionPane.YES_OPTION)
	            {
	                myShareFiles.remove(indexSelected);
	                refreshJTable();
	                bt_clean.doClick();
	            }
	            buildTree();            
	            guiserveur.setShareFiles(myShareFiles);				
			}
			
		}
		
		if(src == bt_save)
		{
			boolean ok = true;
			String [] IPs = null;
			if(!jta_listeIp.getText().trim().isEmpty())
			{
				IPs = jta_listeIp.getText().split(";");
				
				for(String ip : IPs)
				{
					if(!IPv4.isIPv4(ip))
					{
						JOptionPane.showMessageDialog(guiserveur,"Erreur au niveau des " +
								"adresses IP",guiserveur.getTitle(),JOptionPane.OK_OPTION);
						ok = false;
						break;
					}		
				}
			}
			
			if(ok)
			{
				if(indexSelected != -1)
				{
					myShareFiles.remove(indexSelected);					
				}
				ShareFile sf = new ShareFile(txtpath.getText());
				
				if(jrb_canDownload.isSelected() == true && IPs != null)
					sf.addwhoCanDownload(IPs);
				
				if(jrb_canNotDownload.isSelected() == true && IPs != null)
					sf.addwhoCanNotDownload(IPs);
				
				if(sf.exists())
				{
					myShareFiles.add(sf);
					refreshJTable();
					bt_clean.doClick();
				}
			}
			buildTree();
			guiserveur.setShareFiles(myShareFiles);
		}
		
	}

	@Override
	public void valueChanged(ListSelectionEvent event) 
	{
		if( event.getSource() == jtab.getSelectionModel() & event.getFirstIndex() >= 0 )
        {
            if(jtab.getSelectedRow() != -1)
            {
            	bt_clean.doClick();
               indexSelected = jtab.getSelectedRow();
               ShareFile sf = myShareFiles.get(indexSelected);
               txtpath.setText(sf.getAbsolutePath());
               
               if(sf.getWhoCanDownload().length > 0)
               {
            	   jrb_canDownload.setSelected(true);
            	   String[] ips = sf.getWhoCanDownload();
            	   
            	   for (int i = 0; i < ips.length; i++) 
            	   {
            		   jta_listeIp.setText(ips[i]+";");				
            	   }            	   
               }
               if(sf.getWhoCanNotDownload().length > 0)
               {
            	   jrb_canNotDownload.setSelected(true);
            	   String[] ips = sf.getWhoCanNotDownload();
            	   
            	   for (int i = 0; i < ips.length; i++) 
            	   {
            		   jta_listeIp.setText(ips[i]+";");				
            	   }            	   
               }
               manageButtons();
            }
        }
	}

}
