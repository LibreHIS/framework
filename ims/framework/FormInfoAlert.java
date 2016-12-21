package ims.framework;

import ims.framework.enumerations.InternalAlertUniqueIndex;
import ims.framework.utils.ImagePath;

public final class FormInfoAlert extends InternalAlert 
{	
	private static final long serialVersionUID = 1L;
	
	public FormInfoAlert(String tooltip)
	{
		super(null, null, tooltip);		
		this.icon = new ImagePath(0, "g/debug.png", new Integer(16), new Integer(16));		
	}
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof FormInfoAlert)			
			return this.id == ((FormInfoAlert)obj).id;
		return false;
	}	
	public final int getIndex()
	{
		return InternalAlertUniqueIndex.FormInfo.getIndex();
	}
	@Override
	public final boolean enabledInEditMode()
	{
		return true;
	}
}
