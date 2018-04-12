package fwkShareFiles;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Vector;

/**
 * <p>
 * <span style="font-weight:bold;text-align: center;color:green">
 * Titre: ShareFile</span><br>
 * <span style="font-weight:bold">
 * Description:</span><br>
 * Classe representant un fichier partagé</p>
 * @author OGOULOLA Marlin Yannick
 * @society PlutoSoft-Benin
 * 
 */

public  final class ShareFile extends File{
    
    /**Variable specifiant tous ceux qui peuvent télécharger le fichier.
    * Il s'agit de leur adresse IP
    **/
    private Vector<String> whoCanDownload=new Vector<>();
    
     /**Variable specifiant tous ceux qui ne peuvent pas télécharger le fichier.
     * Il s'agit de leur adresse IP
     **/
    private Vector<String> whoCanNotDownload=new Vector<>();
    
    /** Permet de savoir si l'objet partagé est un dossier ou un fichier**/
    private boolean isDirectory=false;
    
    /**permert de savoir le dossier parrent du fichier **/
    private String parent=new String();
    
    /**Pour stocker la taille du fichier ou du dossier**/
    private long lenght=0;
    
    /**pour la liste du contenu d'un dossier**/
    private ShareFile [] subSharedFiles=null;
    
    /**pour le chemin du fichier  **/
    private String absolutePath=new String();
    /**pour stocker le nom du fichier **/
    private String name=new String();
    /**pour stocker le nombre d'éléments représentés par l'objet en cours **/
    private int nb_elements=0;
    
/**
 * <p>Constructeur permettant de créer un objet de type ShareFile
 * avec attribution de droit de téléchargement.<br>
 * Avant l'utilisation de toutes les methodes de cette classe
 * il faut s'assurer que Cet objet existe 
 * {@link fwkShareFiles.ShareFile#exists() exists}
 * </p><br>
 * <span style="font-weight:bold">Exemple</span><br>
 * <code>
 *  ShareFile sf= new ShareFile("/Home/plutonien/projet");<br>
 * if(sf.exists()){<br>
 *   // suite de votre code<br>
 * }
 * </code>
 * @param pathname le chemin vers le fichier ou le dossier
 * @see java.io.File
 */
    public ShareFile(String pathname) 
    {
        super(pathname);
        this.refrechInformations();  
    }   
    
    /**
     * Methode permettant d'ajouter ceux qui ont le droit 
     * de télécharger le fichier.     
     * @param IPs adresses ip des personnes à ajouter
     */
    public void addwhoCanDownload(String[] IPs)
    {
        int tot=IPs.length;
        if(tot>0)
        {
        	this.whoCanNotDownload.clear();
            this.whoCanDownload.clear();
            for (int i = 0; i < tot; i++) 
            {
            	if(IPv4.isIPv4(IPs[i]))
            		this.whoCanDownload.add(IPs[i]);                
            }
        }             
    }
     /**
     * Methode permettant d'ajouter ceux qui n'ont pas le droit 
     * de télécharger le fichier.     
     * @param IPs adresse ip de la personne à exclure
     */
    public void addwhoCanNotDownload(String[] IPs)
    {
         int tot=IPs.length;
         if(tot>0)
         {
        	this.whoCanDownload.clear();
            this.whoCanNotDownload.clear();           
            for (int i = 0; i <tot; i++) 
            {
            	if(IPv4.isIPv4(IPs[i]))
            		this.whoCanNotDownload.add(IPs[i]);                
            }
         }  
    }
    
    /**
     * Cette methode permet de rafraichir ou d'obtenir 
     * les informations sur l'objet en cours.
     */
    public void refrechInformations()
    {
        if(this.exists())
        {
            isDirectory=super.isDirectory(); 
            parent=super.getParent();
            name=super.getName();
            absolutePath=super.getAbsolutePath();
            subSharedFiles=this.subFiles();
            lenght=calculateLength();
            
            ShareFile[] liste =listAllFiles();
            if(liste != null)
               nb_elements=liste.length;
            else
                nb_elements=1;
                
        }            
    }    
    
     public String[] getWhoCanDownload() {
        int tot=this.whoCanDownload.size();
        String[] ret=new String[tot];
        int i=0;
        Enumeration<String> E=this.whoCanDownload.elements();
        while (E.hasMoreElements()) 
        {
             ret[i]=E.nextElement();
             i++;
        } 
        return ret;
    }
     
    /**pour stocker le nombre d'éléments représentés par 
     * l'objet en cours
     * @return 
     **/
    public int getNb_elements() 
    {
        return nb_elements;
    }

    public String[] getWhoCanNotDownload() {
        int tot=this.whoCanNotDownload.size();
        String[] ret=new String[tot];
        int i=0;
        Enumeration<String> E = this.whoCanNotDownload.elements();
        while (E.hasMoreElements()) 
        {
             ret[i]=E.nextElement();
             i++;
        } 
        return ret;
    }  
   
    @Override
    public boolean isDirectory()
    {
        return isDirectory;
    }
    @Override
    public String getParent()
    {
        return parent;
    }    
    
    @Override
    public ShareFile[] listFiles()
    {
        return subSharedFiles;                
    }
    /**
     * Pour avoir les sous éléménts de l'objet en cours
     * @return les sous éléménts des dossiers 
     * ou null si le dossier n'a en pas 
     */
    private ShareFile[] subFiles()
    {
        String[] ss=null;
        ss = super.list();
        
        if (ss == null) 
        	return null;
        
        int n = ss.length;
        ShareFile[] fs = new ShareFile[n];
        for (int i = 0; i < n; i++)
        {
            fs[i] = new ShareFile(this.getAbsolutePath()+"/"+ss[i]);              
        }
        return fs;
    } 
    /**
     * Cette methode calcule la taille d'un fichier en octects
     * @return la taille du fichier 
     */
    private long calculateLength()
    {
        long l=0;
        if(this.exists())
        {
            if(this.subSharedFiles==null)
                l=l+super.length();
            else
            {
                int tot=this.subSharedFiles.length;
                for(int i=0;i<tot;i++)
                {
                    if(!subSharedFiles[i].isDirectory)
                        l=l+subSharedFiles[i].length();
                    else
                        l=l+subSharedFiles[i].calculateLength();
                }            
            }                  
        }        
        return l;        
    }
    
    @Override
    public long length()
    {
        return this.lenght;        
    }
    /**
     * Permet de retourner la {@link fwkShareFiles.ShareFile#length() taille} 
     * du fichier en {"octets","Ko", "Mo", "Go"}
     * @return la taille du fichier 
     */
    public String lengthToString()
    {
        int k=1024;
        String[] mesure=new String[]{"Octets","Ko", "Mo", "Go"};
        String ret=null;
        Double d=(double)this.lenght;
        DecimalFormat df=new DecimalFormat("0.00");
        int index=0;
        for(int i=0;i<mesure.length;i++)
        {  
            if(this.lenght>Math.pow(k,i+1))
            {                
                d=(double)this.lenght/Math.pow(k,i+1);  
                index=i+1;
            }
        }
        ret=df.format(d);
        ret=ret+" "+mesure[index];
        return ret;
    }
    
    /**
     * Cette methode permet de lister tous les fichiers
     * contenu dans un dossier et sous dossiers.
     * Il faut donc s'assurer que l'objet est bien un dossier avec la methode
     * {@link fwkShareFiles.ShareFile#isDirectory isDirectory}
     * @return liste de tous les sous fichiers
     */
    public ShareFile[] listAllFiles()
    {
        if(this.isDirectory)
        {
            Vector<ShareFile> theFiles=new Vector<>();
            lister(this,theFiles);
            int tot=theFiles.size();
            ShareFile[] files=new ShareFile[tot];
            for (int i = 0; i < tot; i++) 
            {
                files[i]=theFiles.get(i);            
            }
             return files;
        }
        else
           return null;
               
    }    
    
    /**
     * Methode permettant de lister tous les fichier d'un dossier
     * @param f le dossier
     * @param lisfichier tableau contenant la liste des éléments de ce dossier
     */
    private void lister(ShareFile f,Vector<ShareFile> lisfichier)
    {
    	ShareFile[] ll=f.listFiles();
    	
    	if(ll != null)
    	{
    		int tot =ll.length;
    	    for(int j=0;j<tot;j++)
            {
                if(ll[j].isFile()) 
                    lisfichier.add(ll[j]);
                else
                    if(ll[j].canRead()) 
                        lister(ll[j],lisfichier);		    	    	  
            }    		
    	}    		     
   }
    
    /**
     * Permet de retouner le type de file
     * @return dossier ou fichier
     */
    public String getTypeFile()
    {
        String ret;
        if(this.isDirectory)
            ret="dossier";
        else
            ret="fichier";
        return ret;
    }
    
    @Override
    public String getName()
    {
        return name; 
    }
    @Override
    public String getAbsolutePath()
    {
        return absolutePath;        
    }   
  
}
