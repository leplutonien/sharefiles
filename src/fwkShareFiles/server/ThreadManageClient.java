package fwkShareFiles.server;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Vector;

import fwkShareFiles.Constantes;
import fwkShareFiles.InterWin;
import fwkShareFiles.ShareFile;

/**
 * <p>
 * <span style="font-weight:bold;text-align: center;color:green">
 * Titre: ThreadManageClient</span><br>
 * <span style="font-weight:bold">
 * Description:</span><br>
 * classe permettant de traiter les requetes au niveau du serveur</p>
 * @author OGOULOLA Marlin Yannick
 * @society PlutoSoft-Benin
 */
public class ThreadManageClient extends Thread implements Constantes  {    
    private Socket client=null;
    private ObjectOutputStream out=null;
    private ObjectInputStream in=null;   
    private InterWin mygui;
    private Vector<String> who_is_connected=null;
    private String info_client;
    private String ipClient=null;
    private Server serveur=null;
    private  ShareFile[] elementsShare=null;

    public ThreadManageClient(Socket client,Server serveur,InterWin gui,Vector<String> who_is_connected) 
    {
        try
        {
            this.client=client;
            this.serveur=serveur;
            this.mygui=gui;           
            this.who_is_connected=who_is_connected;
            this.elementsShare=serveur.getElementsShare();
            out=new ObjectOutputStream(client.getOutputStream());
            out.flush();
            in=new ObjectInputStream(client.getInputStream());
            ipClient=client.getInetAddress().getHostAddress();
        } 
        catch (IOException ex) 
        {
            mygui.showMsgDlg("Une erreur est survenue :"+ex.getMessage(),ERROR_MESSAGE);
        }        
    }
    
    @Override
    public void run()
    {
        try 
        {
           //on stipule au client que la connection est reussi
           out.writeObject(CONNEXION_SUCESS);  
           out.flush();
           this.mygui.showMsgDlg("Connexion du : "+ipClient,INFORMATION_MESSAGE);                       
           //on attend les parametres de connexion
           String[] read=(String[])in.readObject(); 
           if(isValidCredentials(read[0],read[1]))
           {
               info_client=client.getInetAddress().getHostName()+" ("+ipClient+")";
               who_is_connected.add(info_client);               
               //on rafraichi la liste de ceux qui sont connecté au serveur
                mygui.RefrechElement(who_is_connected);
               //on stipule la reussite de l'AUTHENTIFICATION
               mygui.showMsgDlg(AUTHENTIFICATION_SUCESS+" pour le "+ipClient,INFORMATION_MESSAGE);
               out.writeObject(AUTHENTIFICATION_SUCESS);  
               out.flush();               
               String  receivedAction;
               do
               {
                 receivedAction=null;
                 receivedAction =(String)in.readObject();
                 
                 switch(receivedAction)
                 {
                     case GET_SHARE_FILE:
                     {
                         mygui.showMsgDlg("Le "+ipClient+" demande la liste des documents partagés"
                                ,INFORMATION_MESSAGE);
                         //on lui envoi les elements auquel il a droit
                         out.writeObject(sendShareFiles());  
                         out.flush();
                         mygui.showMsgDlg("Liste des documments partagés envoyée au "
                                 +ipClient,INFORMATION_MESSAGE);
                     }; break;
                         
                     case DOWNLOAD_FILE:
                     {
                         ShareFile[] files_to_download=(ShareFile[])in.readObject();
                         int fileCount=files_to_download.length;
                         for(int i=0;i< fileCount;i++)
                         {
                             sendProcessing(files_to_download[i]);                           
                         }                         
                     };break;
                 }                 
               } while (!receivedAction.equalsIgnoreCase(END));
               //pour la deconnection de ce client
               if(receivedAction.equalsIgnoreCase(END))
               {
                   who_is_connected.remove(info_client);
                   mygui.RefrechElement(who_is_connected);
                   mygui.showMsgDlg("Déconnection de "+ipClient,INFORMATION_MESSAGE);
                   in.close();
                   out.close();                   
                   this.interrupt();
               }             
           } 
           else
           {
               mygui.showMsgDlg("AUTHENTIFICATION_ECHOUE pour le "+ipClient,INFORMATION_MESSAGE);
               out.writeObject(null);  
               out.flush(); 
               //fermeture de la connexion
               mygui.showMsgDlg("Déconnection de "+ipClient,INFORMATION_MESSAGE);
               in.close();
               out.close();                   
               this.interrupt();
           }           
        } 
        catch (IOException | ClassNotFoundException e) 
        {
            if(e.getMessage()!= null && e.getMessage().equalsIgnoreCase("Connection reset"))
            {
            	mygui.showMsgDlg("Le "+ipClient+" s'est deconnecté de façon inattendue <"+e.getMessage()+">",ERROR_MESSAGE);                                
            }
            else
             mygui.showMsgDlg("Une erreur est survenue "+e.getMessage(),INFORMATION_MESSAGE);
            who_is_connected.remove(info_client);
            this.interrupt();
        }        
    }
    
    private boolean isValidCredentials(String login,String pwd)
    {
        return true;
    }
    
    /**Configure les documents partagés à présenter au client **/
    private ShareFile[] sendShareFiles()
    {
        //on rafraichi la liste des elements partagés
        this.elementsShare=serveur.getElementsShare();
       
        //on prepare la liste des fichiers auquels ce client 
        //tcp a droit
        Vector<ShareFile> sendShareFiles=new Vector<>();
        int tot=this.elementsShare.length;
        for(int i=0;i<tot;i++)
        {
            if(haveAccessRight(elementsShare[i]))
                sendShareFiles.add(elementsShare[i]); 
                
        }
        //on transforme l'objet vector en tableau de ShareFile
        tot=sendShareFiles.size();
        ShareFile[] ret=new ShareFile[tot];
        int i=0;
        Enumeration<ShareFile> E=sendShareFiles.elements();
        while (E.hasMoreElements()) 
        {
             ret[i]=E.nextElement();
             i++;
        }        
        return ret;       
    }
    
    /**
     * permet de savoir si le client en cours a des droits sur un document
     * @param sf
     * @return 
     */
    private boolean haveAccessRight(ShareFile sf)
    {
        boolean ret=false;
        //on rafraichi les informations que ces types d'objets contient
        sf.refrechInformations();
        if(sf.getWhoCanDownload().length>0)
        {
            if(isContain(sf.getWhoCanDownload(),ipClient))
                   ret=true;               
        }
        else
        {
            if(sf.getWhoCanNotDownload().length>0)
            {
                if(!isContain(sf.getWhoCanNotDownload(),ipClient))
                    ret=true;                   
            }
            else
            {       // aucun droit defini                    
                    ret=true;                   
            }                
        }       
        return ret;
    }
    /**
     * Assure le processus de téléchargement d'un document
     * @param file_to_download 
     */
    private void sendProcessing(ShareFile file_to_download)
    {
        try 
        {
            if(candownload(file_to_download))
            {
                //on prévient le client du debut d'envoi du bloc
                out.writeObject(BEGIN_DOWNLOAD);  
                out.flush();                
                sendFile(file_to_download);               
               //on envoi au client la fin du telechargement
                out.writeObject(END_DOWNLOAD);  
                out.flush();
                mygui.showMsgDlg(ipClient+" vient de download le "+file_to_download.getTypeFile()+" "
                    +file_to_download.getAbsolutePath()
                    ,INFORMATION_MESSAGE);             
            }
            else
            {
                 //on le spécifie au client
                 out.writeObject(NOT_EXIST);  
                 out.flush();              
                 mygui.showMsgDlg(ipClient+" a tenté de télécharger le "+file_to_download.getTypeFile()
                        +file_to_download.getAbsolutePath()+" qui n'existe plus sur le serveur "
                        + "ou dans son repectoire de téléchargement ou auquel il m'a plus droit"
                        ,ERROR_MESSAGE);                
            }
            
        } 
        catch (IOException e) 
        {
            mygui.showMsgDlg("Une erreur est survenue lors de l'envoi du fichier :"
                    +file_to_download.getAbsolutePath()+" au "
                    +ipClient+"==>"+e.getMessage(),ERROR_MESSAGE);            
        }        
    }
    
    private void sendFile(ShareFile file_to_download)
    {
        if(!file_to_download.isDirectory())
        {
            send(file_to_download);
        }
        else
        {
            ShareFile [] subfiles =file_to_download.listFiles();      
            int count=subfiles.length;
            for (int i = 0; i < count; i++) 
            {
                sendFile(subfiles[i]);
            }
        }
    }
       
    private void send(ShareFile file_to_download) 
    {
        try 
        {
           byte donnee[]= new byte[64000];
           int lu=0;           
           //InputStream inp=file_to_download.toURL().openStream();       
           FileInputStream inp = new FileInputStream(file_to_download);
          
           while((lu = inp.read(donnee)) != -1) //tant que c'est pas la fin du fichier
           {
        	   out.write(donnee, 0, lu);        	   
           }           
           //on ferme le fichier
           inp.close();
           //on libere le fichier
           out.writeObject("");  
           out.flush(); 
        } 
        catch (IOException e) 
        {
            mygui.showMsgDlg("Une erreur :"+e.getMessage(),ERROR_MESSAGE);            
        }
    }
    /**
     * Cette methode permert de verifier:
     * <ul>
     * <li>Si le documment est toujours partagé</li>
     * <li>Si le client a toujours le droit de le download</li>
     * <li>Si le fichier existe sur le disque </li>
     * </ul>
     * @param sf
     * @return true si tout est bon false sinon
     */
    private boolean candownload(ShareFile sf)
    {
        boolean ret=false;
       //on rafraichi la liste des elements partagés
       this.elementsShare=serveur.getElementsShare();
       //on verifie sf est toujours partagé
       boolean ok=false;
       for (int i = 0; i < elementsShare.length; i++) 
       { 
            if(sf.getName().equalsIgnoreCase(elementsShare[i].getName()))
            {
               ok=true;
               break;
            }            
       }
        if(ok)//le document est toujours partagé
        {
            //on verifie s'il a un droit sur fichier et 
            //que le fichier existe toujours
            if(haveAccessRight(sf) && sf.exists())
            {
              ret=true;  
            }            
        }
        return  ret;
    }
    
    /**
     * permet de savoir si un element appartient à un tableau
     * @param data
     * @param val
     * @return
     */
    public boolean isContain(String [] data,String val)
    {
        boolean ret=false;
        int tot=data.length;
        for (int i = 0; i <tot; i++) 
        {
            if(data[i].equalsIgnoreCase(val))
            {
                ret=true;
                break;
            }
        }        
        return ret;
    }
}
