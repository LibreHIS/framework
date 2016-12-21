package ims.framework;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class StaticGzipFilter implements Filter
{
	public void init(FilterConfig arg0) throws ServletException
	{
	}
	
	public void destroy() 
	{
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
	{
		final HttpServletRequest httpRequest = (HttpServletRequest)request;

		HttpServletResponseWrapper respWrapper = new HttpServletResponseWrapper((HttpServletResponse)response) 
		{
			{
				HttpServletResponse response = (HttpServletResponse)this.getResponse();
				String pathInfo = httpRequest.getRequestURI(); 
				if(pathInfo.endsWith(".js.gz")) 
				{
					response.setContentType("text/javascript");	
				} 
				else if(pathInfo.endsWith(".css.gz")) 
				{
					response.setContentType("text/css");
				} 
				else 
				{
					response.setContentType("text/plain"); // Fallback	
				}
				response.addHeader("Content-encoding", "gzip");
			}

			public void setContentType(String type) 
			{
				// Ignore it.
			}
		};
		chain.doFilter(request, respWrapper);
	}	
}
