package fwkShareFiles.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Vector;

import fwkShareFiles.Constantes;
import fwkShareFiles.InterWin;
import fwkShareFiles.ShareFile;

/**
 * <p>
 * <span style="font-weight:bold;text-align: center;color:green">
 * Titre: Server</span><br>
 * <span style="font-weight:bold">
 * Description:</span><br>
 * Classe permettant de gerer du serveur de fichier</p>
 * @author OGOULOLA Marlin Yannick
 * @society PlutoSoft-Benin
 */
public class Server implements Runnable, Constantes{    
     private ServerSocket myServeur;    
     private int port;
     /** interface graphique sous laquelle le serveur est lancé**/
     private InterWin mygui;
     private Thread th=null;    
     /**liste des elements partagés**/
     private Vector<ShareFile> elementsShare=null;
     /** pour avoir l'etat du serveur **/
     private boolean state=false;
     /*pour la liste de ceux qui sont connecté  au serveur*/
     private Vector<String> who_is_connected=null; 
     private Vector<Thread> threadRuning = null;     
     /**
      * Constructeur de cette classe 
      * @param port le port sur lequel le serveur sera lancé
      * @param gui interface graphique sous laquelle ce serveur est lancé
      */     
    public Server(int port,InterWin gui)  
    {
        this.elementsShare = new Vector<>();
        this.who_is_connected=new Vector<>();
        this.port=port;        
        this.mygui=gui;
    }
    
    /*
     * Cette methode demarre le serveur de fichier
     * @return true si le serveur est bien lancé false sinon
     */
    public boolean launchServer()
    {
        if(elementsShare==null || elementsShare.isEmpty())
        {
              mygui.showMsgDlg("Impossible de demarrer ce serveur: <Aucun elements partagé>",ERROR_MESSAGE);            
        }
        else
        {
             try 
             {        
                 myServeur=new ServerSocket(port); 
                 state=true;                 
                 th=new Thread(this);
                 th.start();
                 mygui.showMsgDlg("Le serveur de partage est lancée au port "+port,INFORMATION_MESSAGE);
                 threadRuning = new Vector<>();
                 threadRuning.add(th);
                 
             }
             catch (IOException ex) 
             {
                 mygui.showMsgDlg("Une erreur : "+ex.getMessage(),ERROR_MESSAGE);
             }            
         }        
         return state;        
    }
    /**
     * Methode permettant d'ajouter ou de modifier les fichiers partagés
     * @param eltsShare les elements à partager
     */
    public void addElementsShare(ShareFile[] eltsShare)
    {
        int tot=eltsShare.length;
        
        if(tot>0)
        {
           //on vide la liste existante
           this.elementsShare.clear();
           //on ajoute la nouvelle liste tout en s'assurant de 
           //l'existence des fichiers.juste par précaussion
           for(int i=0; i<tot;i++)
               if(eltsShare[i].exists())
               {
                   this.elementsShare.add(eltsShare[i]);                    
               }
        }
        else
            mygui.showMsgDlg("La liste des documents partagés ne peut être vide"
                    ,ERROR_MESSAGE);
    }
    /**
     * Methode retournant la liste de fichiers partagés
     * @return 
     */
    public ShareFile[] getElementsShare() 
    {
        int tot=this.elementsShare.size();
        ShareFile[] ret=new ShareFile[tot];
        int i=0;
        Enumeration<ShareFile> E=this.elementsShare.elements();
        while (E.hasMoreElements()) 
        {
             ret[i]=E.nextElement();            
             i++;
        }        
        return ret;
    }
    
    /**
     * permet de savoir l'etat du serveur.
     * @return Etat du serveur
     */
    public boolean  getServerState()
    {
        return state;
    }
    /**
     * methode permettant d'arreter le serveur
     * @return true si le serveur est bien arrete false sinon
     */
    public boolean stopServer()
    {
        try
        {
            if(myServeur!=null)
            {
                myServeur.close();
                state=false; 
            }
            mygui.showMsgDlg("Arrêt du serveur de fichier",INFORMATION_MESSAGE);
        }
        catch(IOException ex)
        {
            mygui.showMsgDlg(ex.getMessage(),ERROR_MESSAGE);
        }
       
        //on arrete tous les thread crées
        /*Enumeration<Thread> E=this.threadRuning.elements();
        while (E.hasMoreElements()) 
        {
             E.nextElement().interrupt();
        } 
        */
        th.interrupt();
        threadRuning.clear();
        return !state;        
    }
    /**
     * Permet de renvoyer la liste de clients connectés au serveur
     * @return
     */
    public Vector<String> get_who_is_connected()
    {
        return who_is_connected;
    }    

    @Override
    public void run() 
    {
        while (state) 
        {
            try 
            {
                Socket client=myServeur.accept();             
                ThreadManageClient thMC = new ThreadManageClient(client,this,mygui,who_is_connected);
                threadRuning.add(thMC);
                thMC.start();                
            }
            catch (IOException ex) 
            {
               mygui.showMsgDlg(ex.getMessage(),ERROR_MESSAGE);
            }            
        }        
    }  
    
    
}
