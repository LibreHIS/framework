package ims.framework.delegates;

public class CancelArgs
{
    public boolean isCancel() 
    { 
    	return this.cancel; 
    }
    public void setCancel(boolean value) 
    { 
    	this.cancel = value; 
    }
    
    private boolean cancel = false;
}
