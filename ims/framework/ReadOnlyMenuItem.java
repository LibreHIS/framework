package ims.framework;

import java.io.Serializable;

import ims.base.interfaces.IModifiable;
import ims.framework.delegates.MenuItemClick;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.Image;

/**
 * @author mmihalec
 */
public abstract class ReadOnlyMenuItem implements IModifiable, Serializable
{		
	private static final long serialVersionUID = 1L;
	
	public abstract int getID();
	public abstract boolean isEnabled();
	public abstract void setEnabled(boolean value);
	public abstract boolean isVisible();
	public abstract void setVisible(boolean value);
	public abstract Image getIcon();
	public abstract void setIcon(Image icon);
	public abstract String getText(); 
	public abstract boolean getBeginAGroup();
	public abstract void setIdentifier(Object value);
	public abstract Object getIdentifier();
	public abstract void free();
	public abstract void click(Control sender) throws PresentationLogicException;
	public abstract void setClickEvent(MenuItemClick delegate);	
    public abstract void render(StringBuffer sb, boolean formIsReadOnly) throws ConfigurationException;
    
    public boolean equals(Object value)
    {
    	if(value == null)
    		return false;
    	if(value instanceof ReadOnlyMenuItem)
    		return ((ReadOnlyMenuItem)value).getID() == this.getID();
    	return false;
    }
    public int hashCode()
    {
    	return this.getID();
    }
}
