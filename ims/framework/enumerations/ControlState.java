package ims.framework.enumerations;

import java.io.Serializable;

/**
 * @author mchashchin
 */
public class ControlState implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static final ControlState UNKNOWN = new ControlState(1);
	public static final ControlState ENABLED = new ControlState(2);
	public static final ControlState DISABLED = new ControlState(3);
	public static final ControlState READONLY = new ControlState(4);
	public static final ControlState EDITABLE = new ControlState(5);
	public static final ControlState HIDDEN = new ControlState(6);

	private ControlState(int id)
	{
		this.id = id;
	}
	
	public String toString()
	{
		if (this.id == 1)
			return "Unknown";
		else if (this.id == 2)
			return "Enabled";
		else if (this.id == 3)
			return "Disabled";
		else if (this.id == 4)
			return "Readonly";
		else if (this.id == 5)
			return "Editable";
		return "Hidden";
	}

	private int id;
}
