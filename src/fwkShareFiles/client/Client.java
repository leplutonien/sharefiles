package fwkShareFiles.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import fwkShareFiles.Constantes;
import fwkShareFiles.InterWin;
import fwkShareFiles.ShareFile;


/**
 * <p>
 * <span style="font-weight:bold;text-align: center;color:green">
 * Titre: Client</span><br>
 * <span style="font-weight:bold">
 * Description:</span><br>
 * Classe permettant d'interagir avec le serveur de fichier</p>
 * @author OGOULOLA Marlin Yannick
 * @society PlutoSoft-Benin
 */

public class Client implements Constantes {
    
    private InetAddress IpServeur;
    private int port;
    private Socket socket;
    private boolean conneted=false; 
    private boolean authenticated=false;  
    private InterWin mygui;
    private ObjectOutputStream out;
    private ObjectInputStream in;  
    private File dstFile=null;
    private FileOutputStream fileOutputStream=null;
   
    /**
     * Constructeur de la classe
     * @param AdrServeur adrresse du serveur
     * @param port le port com sur lequel le serveur est lancé
     * @param gui interface graphique sous laquelle le client est lancé
     */
    public Client(String AdrServeur, int port,InterWin gui) 
    {
        try
        {
            this.port = port;
            this.IpServeur=InetAddress.getByName(AdrServeur); 
            this.mygui=gui;
        }
        catch(UnknownHostException e)
        {
            mygui.showMsgDlg("Adresse IP du serveur est incorrect",ERROR_MESSAGE);
            mygui.showMsgDlg("Détails: "+e.getMessage(),ERROR_MESSAGE);
        }        
    }
    
    /**
     * Permet de se connecter au serveur
     * @return true(succes) ou false(echec)
     */
    public boolean connecter()
    {
        if(!conneted)
        {
            try
            {
                socket=new Socket(IpServeur,port);                    
                out=new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                in=new ObjectInputStream(socket.getInputStream());            
                String  recu=(String)in.readObject(); 
                if(recu.equalsIgnoreCase(CONNEXION_SUCESS))
                {
                    conneted=true;                   
                }                
                
            }
            catch(Exception e)
            {
               mygui.showMsgDlg("Impossible de se connecter au serveur",ERROR_MESSAGE);
               mygui.showMsgDlg("Détails: "+e.getMessage(),ERROR_MESSAGE);
            }            
        }
        return conneted;
    }
    
    /**
     * Permet de couper la connexion au serveur
     * @return vrai si tout s'est bien passé false sinon
     */
    public boolean disConnect()
    {
        if(conneted)
        {
            try 
            {
                //on notifie la deconnection au serveur
                if(!socket.isClosed())//on s'assure que le serveur n'est pas offline
                {
                    out.writeObject(END);            
                    out.flush();
                }
                out.close();
                in.close();
                socket.close();
                conneted = false;
            } 
            catch (Exception e) 
            {
                System.err.println(e.getMessage()); 
            }
        }        
        return conneted;
    }
    public boolean getClientState()
    {
        return conneted;                
    }
    
    /**
     * Permet de s'authentifier sur le serveur
     * @param login
     * @param pwd
     * @return
     */
    public boolean authenticate(String login,String pwd)
    {
        try 
        {           
            out.writeObject(new String[]{login,pwd});  
            out.flush();
            String  recu=(String)in.readObject();
            if(recu.equalsIgnoreCase(AUTHENTIFICATION_SUCESS))
            {
                authenticated=true;                
            }
            else
            {
                //on ferme la connexion au serveur
                this.disConnect();
            }
        } 
        catch (Exception e) 
        {
            mygui.showMsgDlg("Impossible de Joindre le serveur",ERROR_MESSAGE);           
        }
        return authenticated;
    }
    
    public boolean isAuthenticated()
    {
    	return authenticated;
    }
    
    /**
     * Permet d'avoir la liste des documents partagés
     * @return
     */
    public ShareFile[]  getShareFile()
    {
        ShareFile[] sharedFiles = null;
        try 
        {           
            out.writeObject(GET_SHARE_FILE);  
            out.flush();
            sharedFiles=(ShareFile[])in.readObject(); 
        } 
        catch (Exception ex) 
        {
             mygui.showMsgDlg("Impossible de Joindre le serveur",ERROR_MESSAGE);
        }
        return sharedFiles;
    }
    
    /**
     * @param files_to_download
     * @param destination
     * @param obj elle regroupe un ensemble d'elts optionnel. Elle contient dans l'ordre
     * <ul>
     * <li>Jprogressbar pour l'évolution generale du transfert</li>
     * <li>Un label pour affcher des infos pour le precedent JProgressbar</li>
     * <li>Jprogressbar pour l'évolution du transfert d'un fichier</li>
     * <li>un label pour show des informations pour le precedent JProgressbar </li>
     * </ul>
     * @return
     */
    public int DownloadFile(ShareFile[] files_to_download,String destination,Object... obj)
    {
        int tot=0;        
        try 
        {           
            out.writeObject(DOWNLOAD_FILE);  
            out.flush();
            out.writeObject(files_to_download);  
            out.flush();
            new ThreadDownloadFiles(out, in,destination,mygui,files_to_download,obj);            
        } 
        catch (IOException ex) 
        {
            mygui.showMsgDlg("Des problèmes sont survenus lors de l'envoi de "
                    + "la requete de téléchargement",ERROR_MESSAGE);
        }     
        return tot;
    }  
    
}
