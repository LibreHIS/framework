package ims.framework.controls;

import ims.framework.Control;
import ims.framework.delegates.FileSelected;
import ims.framework.delegates.FileUploaded;

public abstract class FileUploader extends Control
{
	private static final long	serialVersionUID	= 1L;

	public void setFileUploadedEvent(FileUploaded delegate)
	{
		this.fileUploadedDelegate = delegate;
	}	
	public void setFileSelectedEvent(FileSelected delegate)
	{
		this.fileSelectedDelegate = delegate;
	}	
	protected void free()
	{
		super.free();
		
		this.fileUploadedDelegate = null;
		this.fileSelectedDelegate = null;
	}  
	
	protected FileUploaded fileUploadedDelegate;
	protected FileSelected fileSelectedDelegate;

	public abstract void setOverwriteOnUpload(boolean value); 
	public abstract void setEnabledOverwriteOnUpload(boolean value);
}
