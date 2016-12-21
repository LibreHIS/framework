package ims.framework.controls;

import ims.framework.Control;
import ims.framework.delegates.HTMLViewerClick;
import ims.framework.utils.SizeInfo;
import ims.framework.utils.TextileString;

abstract public class HTMLViewer extends Control
{
	private static final long serialVersionUID = 1L;
	
	protected void free()
	{
		super.free();
		
		this.delegate = null;
	}	

	abstract public void setIFrameValue(String url);
    abstract public void setHTML(String value);
    abstract public void setHTML(String value, boolean isEmbededObject);    
    abstract public void setHTML(TextileString value);
    abstract public SizeInfo getCurrentSize();
	
    public void setHTMLViewerClickEvent(HTMLViewerClick delegate)
    {
        this.delegate = delegate; 
    }
 
    protected HTMLViewerClick delegate = null;
}
