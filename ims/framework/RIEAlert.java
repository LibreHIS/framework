package ims.framework;

import ims.framework.enumerations.InternalAlertUniqueIndex;
import ims.framework.utils.ImagePath;

public final class RIEAlert extends InternalAlert 
{	
	private static final long serialVersionUID = 1L;
	
	public RIEAlert(String tooltip, boolean viewRIEOnly)
	{
		super(null, null, tooltip);
		if(viewRIEOnly)
			this.icon = new ImagePath(0, "g/NonRIE.gif", new Integer(16), new Integer(16));
		else
			this.icon = new ImagePath(0, "g/RIE.gif", new Integer(16), new Integer(16));
	}
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof RIEAlert)			
			return this.id == ((RIEAlert)obj).id;
		return false;
	}	
	public final int getIndex()
	{
		return InternalAlertUniqueIndex.RIE.getIndex();
	}
}
