package graphicinterfaces;

import java.io.Serializable;

import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author OGOULOLA Marlin
 *
 */
public final class MyTableModel  extends DefaultTableModel 
{
	private Object [][] data;
	private String [] title;
	private boolean[] canEdit = null;
	
	public MyTableModel (Object[][] data, String[] title)
	{
		super(data, title);
		this.data = data;
		this.title = title;		
	}

	
	
	@Override
    public boolean isCellEditable(int rowIndex, int columnIndex) 
    {
		if(canEdit == null)
			return true;
		else
			return canEdit [columnIndex];
    }
		
	public void setCanEdit(boolean [] canEdit)
	{
		this.canEdit = canEdit;
	}
}
