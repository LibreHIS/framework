package ims.framework.delegates;

public interface FileUploaded 
{
	public void handle(String fileName) throws ims.framework.exceptions.PresentationLogicException;
}
