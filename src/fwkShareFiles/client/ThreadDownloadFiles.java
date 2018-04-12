package fwkShareFiles.client;

import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.jws.Oneway;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import fwkShareFiles.Constantes;
import fwkShareFiles.InterWin;
import fwkShareFiles.ShareFile;


/**
 * <p>
 * <span style="font-weight:bold;text-align: center;color:green">
 * Titre: ThreadDownloadFiles</span><br>
 * <span style="font-weight:bold">
 * Description:</span><br>
 * classe permettant de traiter la requete de téléchargement de fichier
 * et d'éviter le mode bloquant</p>
 * @author OGOULOLA Marlin Yannick
 * @version 1.07.14
 */
public class ThreadDownloadFiles extends Thread implements Constantes{
    private ObjectOutputStream out=null;
    private ObjectInputStream in=null;
    private InterWin mygui=null;
    /**Compte le nombre de fichier qui a été download */
    private int succes=0;
    /*le dossier de destination des fichiers telechargés */
    private String dest=null;
    /*Jprogressbar pour l'évolution generale du transfert */
    private JProgressBar jpGene=null;
    /*label pour affcher des infos pour jpGene*/
    private JLabel info_jpGene=null;
    /*Jprogressbar pour l'évolution du transfert d'un fichier*/
    private JProgressBar jpFile=null;
    /*label pour show des informations pour*/
    private JLabel info_jpFile=null;
    /**le nombre de documments A transferer **/
    private int tot=0;
    /**les documments à télécharger **/
    private ShareFile[] files_to_download=null;
    private String read=null;
    /** la taille total des fichiers à télécharger**/
    private long length_files_to_download=0;
    /**  Pour le compteur du jpGene**/
    private long compteurGene = 0;
    
    public ThreadDownloadFiles(ObjectOutputStream out, ObjectInputStream in, String destination,InterWin gui,
            ShareFile[] files_to_download,Object... obj) 
    {
        this.out = out;
        this.in = in;
        this.dest=destination;     
        this.files_to_download=files_to_download;
        this.mygui=gui;
        if(obj!=null)
        {
            for(int i=0;i<obj.length;i++)
            {
                if(i==0) jpGene=(JProgressBar)obj[0];
                if(i==1) info_jpGene=(JLabel)obj[1];
                if(i==2) jpFile=(JProgressBar)obj[2];
                if(i==3) info_jpFile=(JLabel)obj[3];
            }
        }
        
        //calcul de la taille total des fichiers téléchargés
        for (ShareFile sf : this.files_to_download) 
        {
            length_files_to_download+=sf.length();
        }
        
        //calcul du nombre de fichiers à télécharger
        for (ShareFile sf : this.files_to_download) 
        {
            tot+=sf.getNb_elements();
        }
        
        this.start();
        
    }
    
    @Override
    public void run()
    {
        for(int i=0;i<files_to_download.length;i++)
        {
            try 
            {
                read =(String)in.readObject();                               
                if(read.equalsIgnoreCase(BEGIN_DOWNLOAD))
                {                	
                    downloadFile(files_to_download[i],dest);                    
                    read =(String)in.readObject();                    
                    if(read.equalsIgnoreCase(END_DOWNLOAD))
                    {
                         //                     
                    }
                }
                else
                {
                    if(read.equalsIgnoreCase(NOT_EXIST))
                    {
                        mygui.showMsgDlg("Le "+files_to_download[i].getTypeFile()+" "
                                +files_to_download[i].getName()
                                +" n'existe plus sur le serveur",ERROR_MESSAGE); 
                        if(info_jpGene!=null)
                            info_jpGene.setText(i+1+"/"+tot);
                    }                    
                }
            } 
            catch (IOException | ClassNotFoundException e) 
            {
                mygui.showMsgDlg("Impossible de joindre le serveur",ERROR_MESSAGE);
                //this.interrupt();
                break;                
            }         
        }
        //on tente d'ouvrir le dossier d'enregistrement des fichier downloadé
        if(succes>0)
        {
            if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(java.awt.Desktop.Action.OPEN))
            {
                Component comp=null;
                if(mygui instanceof Component)
                    comp=(Component)mygui;
                int rep = JOptionPane.showConfirmDialog(comp,"Téléchargement terminé.\nVoullez-vous l'ouvrir?",TITLE_CLIENT,JOptionPane.YES_NO_OPTION);
                if(rep==0)
                {
                    try 
                    { 
                        Desktop.getDesktop().open(new File(dest));// pour l'ouvrir
                    } 
                    catch (IOException ex) 
                    {
                        mygui.showMsgDlg("Commande d'ouverture non exécuté",INFORMATION_MESSAGE);                       
                    }
                }
            }
            else
            {
                mygui.showMsgDlg("Téléchargement terminés",INFORMATION_MESSAGE);
            }
            
        }
        mygui.RefrechElement((Object)DOWNLOADS_TERMINATE);
        this.interrupt();
    }  

    private void saveShareFile(ShareFile par,String dest_file) 
    {        
        int recu=0;//taille du byte recu
        long taillefichier=0;
        long compteurcourant=0;
        float evolution=0;           
        
        //initialisation des barres de progression  pour le fichier
        if(jpFile!=null)
        {
        	jpFile.setValue((int)evolution);
            jpFile.setString(jpFile.getValue()+"%");
        }
        taillefichier=par.length();        
        try 
        {        	
            FileOutputStream outBuffer=new FileOutputStream(new File(dest_file));
            
            byte data[] = new byte[64000];
            //on notifie le fichier en cours de telechargement
            if(info_jpFile!=null)
            {
                info_jpFile.setText(par.getName());                        
            }
            
            while(((recu=in.read(data))!=-1))//tant que pas fin fichier
            {            	
                compteurcourant +=recu;
                outBuffer.write(data, 0,recu);
                evolution=((100*compteurcourant)/taillefichier);
                if(jpFile!=null)
                {
                    jpFile.setValue((int)evolution);
                    jpFile.setString(jpFile.getValue()+"%");
                }
                compteurGene += recu;                
                evolution=((100*compteurGene)/length_files_to_download);                 
                if(jpGene!=null)
                {
                    jpGene.setValue((int)evolution);
                    jpGene.setString(jpGene.getValue()+"%");                            
                }
            }
           
            outBuffer.close();
            read =(String)in.readObject();
            succes++;
            
            if(info_jpGene!=null)
                info_jpGene.setText(succes+"/"+tot);             
          
        } 
        catch (IOException | ClassNotFoundException e) 
        {
            mygui.showMsgDlg("Impossible de joindre le serveur",ERROR_MESSAGE); 
           // this.interrupt();
        } 
    }
    
    private void downloadFile(ShareFile sf,String destination)
    {
        if(!sf.isDirectory())
        {
            // si c'est un fichier,on compose le chemin de destination du fichier
            String dest_file=destination+sf.getName();
            saveShareFile(sf,dest_file);  
            
        }
        else
        {
            //on crée le dossier de destination
            destination=destination+sf.getName()+"/";
            File fl=new File(destination);
            if(!fl.exists())
                fl.mkdir();
            
            ShareFile [] subfiles =sf.listFiles();      
            int count=subfiles.length;
            
            for (int i = 0; i < count; i++) 
            {
                downloadFile(subfiles[i], destination);
            }
        }
    }
    
}
