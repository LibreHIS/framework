package ims.framework.cn;

import ims.configuration.ConfigFlag;
import ims.configuration.EnvironmentConfig;
import ims.framework.utils.Date;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.Part;

public class PdfUpload extends HttpServlet 
{
	private static final long serialVersionUID = -1L;
	private static final org.apache.log4j.Logger LocalLogger = ims.utils.Logging.getLogger(Upload.class);
		
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{	
		if (LocalLogger.isInfoEnabled())
			LocalLogger.info("File Upload Request: ");

		List<UploadFileParams> params = new ArrayList<UploadFileParams>();
		Map<String, UploadFileContent> contents = new HashMap<String, UploadFileContent>();		
		StringBuffer sb = new StringBuffer();
		sb.append("<response>");	

		String uploadDir = ConfigFlag.GEN.FILE_UPLOAD_DIR.getValue();
		String rootPath;
		String path = null;
		String fullFileName;
		String mountPoint = EnvironmentConfig.getFileUploadMountpoint();
		if (mountPoint.equals(""))
		{
			rootPath = getServletContext().getRealPath(uploadDir);
		}
		else
		{
			if (uploadDir.startsWith("/"))
				uploadDir = uploadDir.substring(1);
				
			if (mountPoint.endsWith("/"))
				rootPath = mountPoint + uploadDir;
			else
				rootPath = mountPoint + "/" + uploadDir;
		}

		ensureRootPathExists(rootPath);
		
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
				upf.contentType = filePart.getContentType();
				upf.content = byteArrayOutputStream.toByteArray();
				upf.fileName = filePart.getFileName();
				
				contents.put(filePart.getFilePath(), upf);
			}
		}
				
		if (params.size() == 0 && contents.size() > 0)
		{
			request.getRequestURL();
			
			Iterator<String> iter = contents.keySet().iterator();
			
			while (iter.hasNext())
			{
				fullFileName = iter.next();
				UploadFileContent upcontent = contents.get(fullFileName);
				String filenameType = (upcontent.fileName).substring(upcontent.fileName.length()-3).toLowerCase();
				String storeName = rootPath + "/" + upcontent.fileName;				
				
				System.out.println("Remote host URL  : " + request.getRemoteHost());
				System.out.println("Requested URL  : " + request.getRequestURL());
				System.out.println("Full filename  : " + fullFileName);
				System.out.println("File type  : " + filenameType);
				System.out.println("Store name  : " + storeName);
				
				try
				{
					if (filenameType.equals("pdf"))
					{
						
						Date date = new Date();
						int year = date.getYear();
						int month = date.getMonth();
						int day = date.getDay();
						
						path = getPdfStorePath() +year+"/"+month+"/"+day+"/";
						System.out.println("Store path  : " + path);
						File newDir=new File(path);
						
						if (!newDir.exists()) {
							System.out.println("Make new directory" + newDir.getPath());
							newDir.mkdirs();					   
						}
						
						System.out.println("Write file  : " + path+fullFileName);
						writeFile(upcontent.content, path+fullFileName);		   
					}
					
				}
				
				catch(IOException e)
				{
					LocalLogger.error("Failed to write file with name = " + storeName, e);
					throw e;
				}
				
				catch (Exception e) 
				{					
					e.printStackTrace();
				}
					
				sb.append("<item name=\"");
				sb.append("File " + fullFileName + " uploaded succeful to " + path);
				sb.append("\"");
				sb.append("/>");
				sb.append("</response>");
				
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(sb.toString());
			}					
		}
	}
	
	private String getPdfStorePath()
	{
		return ConfigFlag.GEN.PDF_STORE_PATH.getValue();
	}
	
	private void ensureRootPathExists(String rootPath)
	{
		File file = new File(rootPath);
		if (!file.exists())
		{
			file.mkdirs();
		}		
	}

	private void writeFile(byte[] content, String fileName) throws IOException
	{		
		System.out.println("Writing file named " + fileName);
		FileOutputStream out = new FileOutputStream(fileName, false);
		out.write(content);
		out.flush();
		out.close();
		System.out.println("Write ok");		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
	
	
	
	private class UploadFileParams
	{
		public String fileName = "";
		public boolean overwrite = true;	
	}
	
	private class UploadFileContent
	{
		public String fileName;
		public String pdfName;
		public String contentType;
		public byte[] content;
	}
}
