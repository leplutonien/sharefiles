package fwkShareFiles;

import java.awt.Color;
import java.awt.Font;

public interface Constantes {
    /**Pour stipuler que la connexion au serveur est réussi*/
    final String CONNEXION_SUCESS="CONNEXION_SUCESS";
    /**Pour stipuler que l'authentification  est réussi*/
    final String AUTHENTIFICATION_SUCESS="AUTHENTIFICATION_SUCESS";
    /**Pour stipuler qu'on a fini d'agit avec le serveur*/
    final String END="END";    
    /**pour demander au serveur d'envoyer les fichiers partagés*/
    final String GET_SHARE_FILE="GET_SHARE_FILE";
    /** */
    final String DOWNLOAD_FILE="DOWNLOAD_FILE";
    /**Pour signaler au client que le fichier qu'il veut télécharger n'existe pas*/
    final String NOT_EXIST="NOT_EXIST";
    /**Pour signaler le debut du telechargement*/
    final String BEGIN_DOWNLOAD="BEGIN_DOWNLOAD";
    /**Pour signaler la fin du telechargement*/
    final String END_DOWNLOAD="END_DOWNLOAD";
    /**Pour signaler le debut de téléchargement d'un fichier*/
    final String BEGIN_DOWNLOAD_FILE="BEGIN_DOWNLOAD_FILE";
    /**Pour signaler le debut de téléchargement d'un fichier*/
    final String END_DOWNLOAD_FILE="END_DOWNLOAD_FILEE";
    /**Pour signaler la fin de tous les telechargements*/
    final String DOWNLOADS_TERMINATE="DOWNLOADS_TERMINATE";
    /**Pour stipuler qu'un MsgDlg est un message d'ereur */    
    final String ERROR_MESSAGE="ERROR_MESSAGE";
    /**Pour stipuler qu'un MsgDlg est un message d'information */
    final String INFORMATION_MESSAGE="INFORMATION_MESSAGE";
     /**Pour stipuler qu'un MsgDlg est un message d'avertissement */
    final String WARNING_MESSAGE="WARNING_MESSAGE";
    /**Pour le titre de l'application **/
    final String TITLE_SERVER="ShareFile-Serveur";
    final String TITLE_CLIENT="ShareFile-Client";
    
    public final Font police1 = new Font("Dialog",Font.PLAIN,14);
    public final Font police2 = new Font("Dialog",Font.BOLD,14);
    public final Font police3 = new Font("Dialog",Font.BOLD,18);
    public final Font police4 = new Font("Dialog",3,14);
    public final Color color1 = new Color(0, 128, 128);
    
}
