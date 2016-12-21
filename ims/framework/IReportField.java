package ims.framework;

/**
 * @author mmihalec
 */
public interface IReportField 
{
	Object getValue();
	String getType();
	String getName();
	String getUniqueIdentifier();
	void setType(String value);
	void setName(String value);
}
