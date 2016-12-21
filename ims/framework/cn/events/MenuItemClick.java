package ims.framework.cn.events;

import java.io.Serializable;

public class MenuItemClick implements IMenuEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public MenuItemClick(int controlID, int menuID, int menuItemID)
	{
		this.controlID = controlID;
		this.menuID = menuID;
		this.menuItemID = menuItemID;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public int getMenuID()
	{
		return this.menuID;
	}
	public int getMenuItemID()
	{
		return this.menuItemID;
	}
	private int controlID;	
	private int menuID;
	private int menuItemID;
}
