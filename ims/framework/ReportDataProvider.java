package ims.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mmihalec
 *
 */
public class ReportDataProvider 
{
	public ReportDataProvider(IReportSeed[] reportSeeds, IReportField[] reportFields)
	{
		this(reportSeeds, reportFields, false);
	}
	public ReportDataProvider(IReportSeed[] reportSeeds, IReportField[] reportFields, boolean excludeNulls)
	{
		this.reportSeeds = reportSeeds;
		this.reportFields = reportFields;
		this.excludeNulls = excludeNulls;
	}
	
	public boolean canProvideData()
	{
		return hasReportData(false);
	}
	public boolean hasData()
	{
		return hasReportData(true);
	}
	public IReportField[] getData()
	{
		return getReportData();
	}
	
	private IReportField[] getReportFields(IReportSeed reportSeed)
	{
		if(reportSeed == null || reportSeed.getUniqueIdentifier() == null)
			return null;
		
		ArrayList<IReportField> list = new ArrayList<IReportField>();
		for(int x = 0; x < this.reportFields.length; x++)
			if(this.reportFields[x] != null && this.reportFields[x].getUniqueIdentifier() != null && this.reportFields[x].getUniqueIdentifier().equals(reportSeed.getUniqueIdentifier()))
				list.add(this.reportFields[x]);
		
		if(list.size() == 0)
			return null;
		
		IReportField[] result = new IReportField[list.size()];		
		for(int x = 0; x < list.size(); x++)
		{
		    IReportField reportField = list.get(x);
		    
		    reportField.setName(reportSeed.getName());
		    reportField.setType(reportSeed.getType());
		    
			result[x] = reportField;						
		}
		
		return result;
	}
	private boolean hasReportData(boolean runtime)
	{
		if(this.reportSeeds == null)
			return false;
		if(this.reportFields == null || this.reportFields.length < this.reportSeeds.length)
			return false;
		
		for(int x = 0; x < this.reportSeeds.length; x++)
		{
			if(this.reportSeeds[x] != null)
			{
				IReportField[] fields = getReportFields(this.reportSeeds[x]);
				if(fields == null || fields.length == 0)
					return false;
				
				if(runtime)
				{
					boolean isNull = true;
					for(int y = 0; y < fields.length; y++)
					{
						if(fields[y].getValue() != null)
						{
							isNull = false;
							break;
						}
					}
					
					if(isNull && !this.reportSeeds[x].canBeNull())
						return false;
				}
			}
		}
			
		return true;
	}
	private IReportField[] getReportData()
	{
		if(excludeNulls)
			return getNotNullReportData();
			
		if(!hasReportData(true))
			return null;
				
		IReportField[] data = new IReportField[this.reportSeeds.length];
		for(int x = 0; x < this.reportSeeds.length; x++)
		{
			IReportField[] fields = getReportFields(this.reportSeeds[x]);
			for(int y = 0; y < fields.length; y++)
			{
				if(fields[y].getValue() != null)
				{				    
					data[x] = fields[y];
					break;
				}
			}
			
			if(data[x] == null)
			    data[x] = new ReportField(this.reportSeeds[x]);
		}
		
		return data;
	}
	
	private IReportField[] getNotNullReportData()
	{
		List<IReportField> data = new ArrayList<IReportField>();
		for(int x = 0; x < this.reportSeeds.length; x++)
		{
			IReportField[] fields = getReportFields(this.reportSeeds[x]);
			if(fields == null)
				return new IReportField[0];
			
			for(int y = 0; y < fields.length; y++)
			{
				if(fields[y].getValue() != null)
				{				    
					data.add(fields[y]);
					break;
				}
			}
		}
		
		IReportField[] result = new IReportField[data.size()];
		data.toArray(result);
		return result;
	}

	private class ReportField implements IReportField
	{
	    public ReportField(Object value, String uniqueIdentifier, String name, String type)
	    {
	        this.value = value;
	        this.uniqueIdentifier = uniqueIdentifier;
	        this.name = name;
	        this.type = type;
	    }
	    public ReportField(IReportSeed reportSeed)
	    {
	        this.value = null;
	        this.uniqueIdentifier = reportSeed.getUniqueIdentifier();
	        this.name = reportSeed.getName();
	        this.type = reportSeed.getType();
	    }
	    public Object getValue() 
	    {
	        return this.value;
	    }
		public String getType()
		{
		    return this.type;
		}
		public String getName()
		{
		    return this.name;
		}
		public String getUniqueIdentifier()
		{
		    return this.uniqueIdentifier;
		}
		public void setType(String value)
		{
		    this.type = value;
		}
		public void setName(String value)
		{
		    this.name = value;
		}
		
		private Object value;
		private String name;
		private String type;
		private String uniqueIdentifier;
	}
	
	private IReportSeed[] reportSeeds;
	private IReportField[] reportFields;
	private boolean excludeNulls;
}
