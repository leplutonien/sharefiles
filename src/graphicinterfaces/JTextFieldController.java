package graphicinterfaces;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * <p>
 * <span style="font-weight:bold;text-align: center;color:green">
 * Titre: JTextFieldController</span><br>
 * <span style="font-weight:bold">
 * Description:</span><br>
 * Classe pour controller la saisie dans les champs JTextField</p>
 * @author OGOULOLA Marlin Yannick 
  */
public class JTextFieldController extends PlainDocument{
    /**Pour activer aucun contrôle sur l'objet JTextField*/
    public static int DEFAULT = 0;
    /**Pour activer l'objet JTextField à accepter rien que des chiffres */
    public static int ALL_TO_NUMBER = 1;
    /**Pour activer la transformation de tous les caractères saisies en majuscule*/
    public static int ALL_TO_UPPERCASE = 2;
    private int control;
    
    public JTextFieldController(int control)
    {
        super();
        
        if(control != ALL_TO_NUMBER && control != ALL_TO_UPPERCASE)
        {
            this.control = DEFAULT;
        }
        else
        {
            this.control = control; 
        }
    }
    
    @Override
    public void  insertString (int offset, String str, AttributeSet attr) throws BadLocationException
    {
        String new_str = null;
        if(control == ALL_TO_NUMBER)
        {
            try 
            {
                int a = Integer.parseInt(str);
                new_str = a+"";
            } 
            catch (NumberFormatException e) {}
        }
        
        if(control == ALL_TO_UPPERCASE)
        {
            new_str = str.toUpperCase();
        }
        
        if(control == DEFAULT)
        {
            new_str = str;
        }
        
        super.insertString(offset,new_str, attr);
    }
    
}
