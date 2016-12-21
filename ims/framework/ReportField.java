package ims.framework;

import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.interfaces.IReportObject;

public class ReportField implements IReportField
{
	public ReportField(ims.framework.Context context, String key, String uniqueIdentifier, String fieldName)
	{
		if(context == null)
			throw new CodingRuntimeException("Invalid context for report field");
		if(key == null)
			throw new CodingRuntimeException("Invalid key for report field");
		if(uniqueIdentifier == null)
			throw new CodingRuntimeException("Invalid unique identifier for report field");
		if(fieldName == null)
			throw new CodingRuntimeException("Invalid name for report field");
		
		this.context = context;
		this.key = key;
		this.uniqueIdentifier = uniqueIdentifier;
		this.fieldName = fieldName;
	}
	public Object getValue()	
	{
		Object value = this.context.get(this.key);
		if(value == null)
			return null;
		
		return ((IReportObject)value).getFieldValueByFieldName(this.fieldName);
	}
	public String getUniqueIdentifier() 
	{
		return this.uniqueIdentifier;
	}
	public String getName()
	{
		return this.name;
	}
	public String getType()
	{
		return this.type;
	}
	public void setName(String value)
	{
		this.name = value;
	}
	public void setType(String value)
	{
		this.type = value;
	}
	
	private String name = null;
	private String type = null;
	private ims.framework.Context context = null;
	private String key;
	private String uniqueIdentifier;
	private String fieldName;
}