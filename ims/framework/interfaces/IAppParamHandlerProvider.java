package ims.framework.interfaces;

import ims.framework.UIEngine;

public interface IAppParamHandlerProvider
{
	void handle(UIEngine engine, IAppParam[] params);
}
