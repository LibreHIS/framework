package ims.framework.cn;

import ims.domain.admin.AppSession;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Logout extends HttpServlet
{
    private static final long serialVersionUID = 1L;
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);		
		if (session != null)
		{
			AppSession appSession = SessionManager.getSession(session.getId());
			if (appSession != null)
			{
				appSession.leaveCcowContext();
			}
			session.invalidate();
		}
		response.setContentType("text/html");
		javax.servlet.ServletOutputStream out = response.getOutputStream();
		out.print("<script>window.self.close();</script>");
		out.close();
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

}