package ims.framework.cn.events;

import java.io.Serializable;

import ims.framework.controls.DrawingControlGroup;

public class DrawingControlConfiguratorChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public DrawingControlConfiguratorChanged(int controlID, DrawingControlGroup root)
	{
		this.controlID = controlID;
		this.root = root;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public DrawingControlGroup getRoot()
	{
		return this.root;
	}
	private int controlID;
	private DrawingControlGroup root;
}
