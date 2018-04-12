package graphicinterfaces;

import java.awt.Component;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 * Classe permettant de créer une JList avec des images à coté 
 * @author OGOULOLA Marlin Yannick 
 * @society PlutoSoft-Benin
 */
public final class MyJList extends JList
{
	private  Vector<ElementListe> elements;	
	
	public MyJList()
	{
		super();
		elements = new Vector<>();
	}
	/**
	 * Permet d'ajouter un élement à la MyJList
	 */
	public void addElement(String logo,String label)
	{
		if(logo != null && label != null)
		{
			elements.add(new ElementListe(logo, label));
		}
	}
    
	/**
	 * Pour obtenir le rendu de MyJList
	 */
	public void getResult()
	{
		//on transforme collection en tableau
		ElementListe[] ret = new ElementListe[elements.size()];
		int i = 0;
		Enumeration<ElementListe> E = elements.elements();
		while(E.hasMoreElements())
		{
			ret[i] = E.nextElement();            
            i++;
		}
		this.setModel(new Model(ret));
		this.setCellRenderer(new Renderer());
	}
}

class  ElementListe{
    private String Logo,Lib;
    
    public ElementListe(String Logo,String Lib)
    {
        this.Logo=Logo;
        this.Lib=Lib;        
    }
    public String getLogo() 
    {
        return Logo;
    }
    public String getLib() 
    {
        return Lib;
    }
    @Override
    public String toString()
    {
       return getLib();        
    }
}

class Model implements ListModel{
   private ElementListe collection[];
   
   public Model(ElementListe collection[])
   {
       this.collection=collection;       
   }    

    @Override
    public int getSize() 
    {
        return collection.length;
    }

    @Override
    public Object getElementAt(int index) 
    {
        return collection[index];
    }

    @Override
    public void addListDataListener(ListDataListener l) {}
    
    @Override
    public void removeListDataListener(ListDataListener l){ }    
}

class Renderer extends DefaultListCellRenderer {
  private Hashtable iconTable = new Hashtable();
  
  public Component getListCellRendererComponent(JList list,
                                                Object value,
                                                int index,
                                                boolean isSelected,
                                                boolean hasFocus) {
    JLabel label =
      (JLabel)super.getListCellRendererComponent(list,
                                                 value,
                                                 index,
                                                 isSelected,
                                                 hasFocus);
    if (value instanceof ElementListe) {
      ElementListe elt = (ElementListe)value;
      ImageIcon icon = (ImageIcon)iconTable.get(value);
      if (icon == null) {
        icon = new ImageIcon(elt.getLogo());
        iconTable.put(value, icon);
      }
      label.setIcon(icon);
    } else {
      // Clear old icon; needed in 1st release of JDK 1.2
      label.setIcon(null); 
    }
    return(label);
  }
}
