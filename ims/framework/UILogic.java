package ims.framework;

public interface UILogic
{
	public void free();
	/**
	  * Load the interface class to be used by the Logic.
	  * The usual implementation is to construct the class name 
	  * for the interface based on the module name of the form,
	  * and load that Class.
	  * But implementations may change this behaviour.
	  * @return
	  * @throws ClassNotFoundException
	*/
	public Class getDomainInterface() throws ClassNotFoundException;
}