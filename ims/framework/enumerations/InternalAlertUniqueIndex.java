package ims.framework.enumerations;

public final class InternalAlertUniqueIndex
{
	int index;
	
	public static InternalAlertUniqueIndex FormInfo = new InternalAlertUniqueIndex(-100);
	public static InternalAlertUniqueIndex RIE = new InternalAlertUniqueIndex(-1);
	
	private InternalAlertUniqueIndex(int index)
	{
		this.index = index;
	}
	
	public int getIndex()
	{
		return index;
	}
}
