
package ims.framework;
			
public class Version 
{
	public static final int Major = 2;
	public static final int Minor = 2406;
	public static final String Timestamp = "20150623 14:16:40";
			
	public static final String getVersionInfo() 
	{
		return String.valueOf(Major) + "." + String.valueOf(Minor); 
	}
	public static final String getFullVersionInfo() 
	{
		return String.valueOf(Major) + "." + String.valueOf(Minor) + "." + Timestamp; 
	} 
	public static final String getTimestamp() 
	{
		return Timestamp; 
	} 
}			
		