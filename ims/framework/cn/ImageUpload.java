package ims.framework.cn;

import ims.configuration.ConfigFlag;
import ims.configuration.EnvironmentConfig;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.Part;

public class ImageUpload extends HttpServlet 
{
	private static final long serialVersionUID = -1L;
	private static final org.apache.log4j.Logger LocalLogger = ims.utils.Logging.getLogger(Upload.class);
		
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{	
		if (LocalLogger.isInfoEnabled())
			LocalLogger.info("Image Upload Request: ");
		
		Map<String, UploadFileContent> contents = new HashMap<String, UploadFileContent>();		
		StringBuffer sb = new StringBuffer();
		String rootPath = EnvironmentConfig.getUserImagesStorePath();	
		if (rootPath == null)
			rootPath = getServletContext().getRealPath("/") + ConfigFlag.GEN.FILE_UPLOAD_DIR;
		
		if (rootPath.startsWith("/"))
		{			
			rootPath.replace('\\', '/');			
		}
		else if (rootPath.startsWith("\\\\"))
		{		
			rootPath.replace('/', '\\');			
		}
				
		sb.append("<response>");	

		MultipartParser parser = new MultipartParser(request, 500000000);
		Part part = null;
		
		while ((part = parser.readNextPart()) != null)
		{			
			if (part.isFile())
			{
				FilePart filePart = (FilePart) part;
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				filePart.writeTo(byteArrayOutputStream);
				
				UploadFileContent upf = new UploadFileContent();				
				upf.content = byteArrayOutputStream.toByteArray();
				upf.fileName = filePart.getFileName();
				
				contents.put(filePart.getFilePath(), upf);				
			}
		}
				
		if (contents.size() > 0)
		{
			request.getRequestURL();			
			Iterator<String> iter = contents.keySet().iterator();
			String filePath = "";
			
			while (iter.hasNext())
			{
				String fullFileName = iter.next();
				UploadFileContent upcontent = contents.get(fullFileName);
				String filenameType = (upcontent.fileName).substring(upcontent.fileName.length()-3).toLowerCase();
				String storeName = upcontent.fileName;				
				
				System.out.println("Remote host URL  : " + request.getRemoteHost());
				System.out.println("Requested URL  : " + request.getRequestURL());
				System.out.println("Image filename  : " + fullFileName);
				System.out.println("Image type  : " + filenameType);
				System.out.println("Store name  : " + storeName);
				
				try
				{
					System.out.println("Directory path: " + rootPath);
					
					File newDir = new File(rootPath);						
					if (!newDir.exists()) {
						System.out.println("Make new directory: " + newDir.getPath());
						newDir.mkdirs();					   
					}
					
					filePath = rootPath + storeName;					
					System.out.println("Write image file: " + filePath);
					
					 
					File file = new File(filePath);
					boolean success = file.createNewFile();
					if (!success) {
						System.out.println("Failed to create image file: " + filePath);	
					}
					else {
						FileOutputStream out = new FileOutputStream(file, false);
						out.write(upcontent.content);
						out.flush();
						out.close();
						System.out.println("File image " + file + " write ok");	
					}
						
				}
				
				catch(IOException e)
				{
					LocalLogger.error("Failed to write file image with name = " + storeName, e);
					throw e;
				}
				
				catch (Exception e) 
				{					
					e.printStackTrace();
				}
					
				sb.append("<item name=\"");
				sb.append("File image " + filePath + " uploaded succeful to " + filePath);
				sb.append("\"");
				sb.append("/>");
				sb.append("</response>");
				
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(sb.toString());
			}					
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
	
	private class UploadFileContent
	{
		public String fileName;
		public byte[] content;
	}
}
