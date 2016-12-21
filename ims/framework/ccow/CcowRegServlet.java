package ims.framework.ccow;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CcowRegServlet extends HttpServlet 
{
	private static final long serialVersionUID = -1L;
	private static final org.apache.log4j.Logger LocalLogger = ims.utils.Logging.getLogger(CcowRegServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		javax.servlet.ServletOutputStream out = response.getOutputStream();
		out.print("CcowRegServlet called with " + request.getQueryString());
		LocalLogger.info("CcowRegServlet called with " + request.getQueryString());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

}
