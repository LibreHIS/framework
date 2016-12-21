package ims.framework.factory;

import ims.framework.interfaces.IPatMergeServletController;

public class PatientMergeServletControllerFactory
{
	private static IPatMergeServletController controller;
	
	public static IPatMergeServletController getController()
	{
		if (controller == null)
		{
			try
			{
				Class c = Class.forName("ims.core.helper.PatientMergerServlet");
				controller = (IPatMergeServletController) c.newInstance();
			}
			catch (Exception e)
			{
				return null;
			}
		}
		return controller;
	}

}
