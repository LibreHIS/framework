package ims.framework.delegates;

public interface FileSelected 
{
	public void handle(String fileName) throws ims.framework.exceptions.PresentationLogicException;
}
