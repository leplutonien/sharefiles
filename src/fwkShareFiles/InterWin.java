package fwkShareFiles;

/**
 * <p>
 * <span style="font-weight:bold;text-align: center;color:green">
 * Titre: InterWin</span><br>
 * <span style="font-weight:bold">
 * Description:</span><br>
 * Interface contenant un ensemble de méthodes que doit implémenter
 * les fenetres graphiques utilisant ce framework.
 * Elle permet d'interagit avec des fenetres</p>
 * @author OGOULOLA Marlin Yannick
 * @society PlutoSoft-Benin
 */
public interface InterWin {
     /**
     * Methode permettant d'afficher les messages dialogues
     * @param Msg libelle du msg
     * @param type le type de message qui peut être
     * un message d'erreur, une notification ...
     */
    public void showMsgDlg(String Msg,String type);
    
    /** Permet de rafraichir des parties du gui 
     * @param obj l'objet à rafraichir
     */
    public void RefrechElement(Object obj);
    
    
}
