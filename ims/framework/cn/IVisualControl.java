package ims.framework.cn;

import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;

public interface IVisualControl
{
	public void restore(IControlData data, boolean isNew);
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException;
	public void renderControl(StringBuffer sb) throws ConfigurationException;
	public void renderData(StringBuffer sb) throws ConfigurationException;
}
