package ims.framework.interfaces;


public interface IPatMergeServletController
{
	public void startup() throws Exception;
	public void shutdown();
	public boolean isRunning();
	public IPatMergeInterfaceComponent getPatMerge();
}
