package ims.framework.enumerations;

public class GridColumnType
{
	public static final GridColumnType STRING = new GridColumnType(0);
	public static final GridColumnType BOOL = new GridColumnType(1);
	public static final GridColumnType COMBOBOX = new GridColumnType(2);
	public static final GridColumnType WRAPTEXT = new GridColumnType(3);
	public static final GridColumnType DATE = new GridColumnType(4);
	public static final GridColumnType DECIMAL = new GridColumnType(5);
	public static final GridColumnType IMAGE = new GridColumnType(6);
	public static final GridColumnType INTEGER = new GridColumnType(7);
	public static final GridColumnType TIME = new GridColumnType(8);
	public static final GridColumnType TREE = new GridColumnType(9);
	public static final GridColumnType MUTABLECOMBOBOX = new GridColumnType(10);
	public static final GridColumnType BUTTON = new GridColumnType(11);
	public static final GridColumnType ANSWERBOX = new GridColumnType(12);
	public static final GridColumnType MUTABLEANSWERBOX = new GridColumnType(13);
	public static final GridColumnType COMMENT = new GridColumnType(14);
	public static final GridColumnType PARTIALDATE = new GridColumnType(15);
	public static final GridColumnType HTML = new GridColumnType(16);
	
	private GridColumnType(int id)
	{
		this.id = id;
	}
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof GridColumnType)
			return this.id == ((GridColumnType)obj).id;
		return false;
	}
	public int getID()
	{
		return this.id;
	}
	
	private int id;
}