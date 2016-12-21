package ims.framework.cn;


import java.util.Map;

import javax.servlet.http.HttpSession;

import ims.domain.SessionData;
import ims.framework.Control;
import ims.framework.SessionConstants;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.data.MenuItemData;
import ims.framework.delegates.MenuItemClick;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.Image;

/**
 * @author mmihalec
 */
public final class MenuItem extends ims.framework.MenuItem
{
	private static final long serialVersionUID = 6463804813630234238L;
	public MenuItem(int id)
	{
		this.id = id;
	}
	public void setContext(HttpSession session, String text, boolean enabled, boolean visible, Image icon, boolean canEditData, boolean beginAGroup)
	{
		this.text = text;
		this.data.setText(text);
		this.enabled = enabled;
		this.data.setEnabled(enabled);
		this.visible = visible;
		this.data.setVisible(visible);
		this.icon = icon;
		this.data.setIcon(icon);
		this.session = session;		
		this.canEditData = canEditData;
		this.beginAGroup = beginAGroup;
	}
	public int getID()
	{
		return this.id;
	}
	public boolean isEnabled()
	{
		return this.enabled;
	}
	public void setEnabled(boolean value)
	{		
		this.enabled = value;
		this.data.setEnabled(value);
	}
	public boolean isVisible()
	{
		return this.visible;		
	}
	public void setVisible(boolean value)
	{
		this.visible = value;
		this.data.setVisible(value);
	}
	public Image getIcon()
	{
		return this.icon;
	}
	public void setIcon(Image icon)
	{
		this.icon = icon;
		this.data.setIcon(icon);
	}
	public String getText() 
	{
		return this.text;
	}
	public void setText(String value) 
	{				
		this.text = value;
		this.data.setText(value);
	}		
	public void setIdentifier(Object value) 
	{
		this.identifier = value;
	}
	public Object getIdentifier() 
	{
		return this.identifier;
	}
	public void click(Control sender) throws PresentationLogicException
	{
		if(this.clickDelegate != null)
			this.clickDelegate.handle(sender);
	}
	public void setClickEvent(MenuItemClick delegate)
    {
        this.clickDelegate = delegate;        
    }
	
	public void free()
	{
		this.clickDelegate = null;		
	}	
	public void restore(IControlData data)
	{
		this.data = (MenuItemData)data;
		this.text = this.data.getText();
		this.enabled = this.data.getEnabled();
		this.visible = this.data.getVisible();
		this.icon = this.data.getIcon();
	}
	public boolean getBeginAGroup()
	{
	    return this.beginAGroup;
	}
	public void render(StringBuffer sb, boolean formIsReadOnly) throws ConfigurationException
	{
		sb.append("<menuitem id=\"");
		sb.append(this.id);
		sb.append("\" visible=\"");
		sb.append(this.isVisible() ? "true" : "false");
		
		if(this.isVisible())
		{
			sb.append("\" text=\"");
			sb.append(ims.framework.utils.StringUtils.encodeXML(this.text));
			sb.append("\" enabled=\"");
			if(formIsReadOnly && this.canEditData)
				sb.append("false");
			else
				sb.append(this.isEnabled() ? "true" : "false");			
	
			if(this.icon != null)
			{
				sb.append("\" img=\"");
			    SessionData sessData = (SessionData) this.session.getAttribute(SessionConstants.SESSION_DATA);
				Map map = sessData.configurationModule.get().getRegisteredImages();
				Image img = (Image)map.get(new Integer(this.icon.getImageId()));
				if(img == null)
					throw new ConfigurationException("Image is not registered {ID = " + String.valueOf(this.icon.getImageId()) + "}");
				sb.append(img.getImagePath());
			}
		}

		sb.append("\" />");		
	}
	public IControlData getData()
	{
		return this.data;
	}
	public boolean wasChanged() 
	{
		return this.data.wasChanged();
	}
	public void markUnchanged() 
	{
		this.data.markUnchanged();
	}
	
	protected int id;
	protected String text;
	protected boolean enabled = true;
	protected boolean visible = true;
	protected Image icon;
	protected MenuItemClick clickDelegate = null;
	protected boolean canEditData = true;
	protected boolean beginAGroup = false;
	private MenuItemData data = new MenuItemData();
	private transient HttpSession session;
	private Object identifier;

}
