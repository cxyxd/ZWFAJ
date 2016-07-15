package com.zw.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

import com.alibaba.fastjson.JSONObject;
import com.zw.HttpProxyWithforkAndJoin;
import com.zw.model.ParseStatusWithFAJ;
import com.zw.model.Passenger;

@SuppressWarnings("deprecation")
public class UploadServletWithForkAndJoin extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8193389509229980358L;


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		File file1 = null;
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		
	
		DiskFileUpload diskFileUpload = new DiskFileUpload();
		try {
			List<FileItem> list = diskFileUpload.parseRequest(request);
			
			for (FileItem fileItem : list) {

				if (fileItem.getFieldName().equals("file_data")) {
					file1 = new File(getServletContext().getRealPath("attachment"), "myfile.xls");
					file1.getParentFile().mkdirs();
					file1.createNewFile();
			//		System.out.println(fileItem.getName()+" psd");
					InputStream ins = fileItem.getInputStream();
					OutputStream ous = new FileOutputStream(file1);
					try {
						byte[] buffer = new byte[1024];
						int len = 0;
						while ((len = ins.read(buffer)) > -1)
							ous.write(buffer, 0, len);
					} finally {
						ous.close();
						ins.close();
					}
				}

			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		
		final ParseStatusWithFAJ parseStatus=new ParseStatusWithFAJ();
		request.getSession().setAttribute("parseStatus", parseStatus);
		System.out.println(request.getSession().getId()+" sessionid  upload file");
		int count=8;
		try {
			count=(Integer) request.getSession().getAttribute("loopCount");
		} catch (Exception e) {
			count=8;
		}
		final int count2=count;
		new Thread(new Runnable() {
			@Override
			public void run() { 
				HttpProxyWithforkAndJoin hp = new HttpProxyWithforkAndJoin();
				hp.setFielLocation(getServletContext().getRealPath("attachment"));
				
				hp.setCircleCount(count2);
				hp.setParseStatus(parseStatus);
				List<Passenger> all=hp.getResult();
				hp.writeXLS(all);
			}
		}).start();
		
		 JSONObject jsonObject = new JSONObject();  
		 jsonObject.put("result", "ok");
		 response.getWriter().write(jsonObject.toString());
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}