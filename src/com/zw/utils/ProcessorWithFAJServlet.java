package com.zw.utils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zw.model.ParseStatusWithFAJ;

/**
 * http://my.oschina.net/u/1866821/blog/313802?fromerr=EaBjgPtN
 * @author Administrator
 *
 */
public class ProcessorWithFAJServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2967815236819826776L;
	private static int count = 0;// 全局标记是否为第一次调用


	 public void doGet(HttpServletRequest request, HttpServletResponse response) {
		 doPost(request, response);
	 }
	 
	// 读取数据导入实时状态
	public void doPost(HttpServletRequest request,HttpServletResponse response) {
		// 设置该响应不在缓存中读取
	//	System.out.println("i have been called by session "+request.getSession().getId());
		response.addHeader("Expires", "0");
		response.addHeader("Cache-Control",
				"no-store, no-cache, must-revalidate");
		response.addHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		
		int allLoop = 0;// 一共循环几圈
		int bytesRead = 0;// 已经处理的人数
		int currentLoop=0; //已经处理的圈数
		long getElapsedTimeInSeconds = 0;// 获得已经上传得时间
		ParseStatusWithFAJ ps=(ParseStatusWithFAJ) request.getSession().getAttribute("parseStatus");
		if (ps==null) {
			return;
		}
		allLoop = ps.getAllLoop();
		bytesRead = ps.getSolvedPassenger().intValue();
		currentLoop=ps.getCurrentloop().intValue();
		
	//	System.out.println(allLoop+"   "+bytesRead);
		getElapsedTimeInSeconds = (System.currentTimeMillis() - ps.getStartTime()) / 1000;

		// 计算上传完成的百分比
		String percentComplete = "0";
		if (allLoop > 0) {
			double k = (double) currentLoop / allLoop * 100;
			int temp=(int) (k*10);
			k=temp/10;
			percentComplete =""+k;
		}
		// 获得上传已用的时间
		long timeInSeconds = getElapsedTimeInSeconds;

		try {

			
			if (currentLoop != allLoop) {
				response.getWriter().println("<br/>");
				response.getWriter().println(
						"<div class=\"prog-border\"><div class=\"prog-bar\" style=\"width: "
								+ percentComplete + "%;\"></div></div>");
				response.getWriter().println("已经处理"+bytesRead+"人<br/>");
				response.getWriter().println(
						"Runtime: " + formatTime(timeInSeconds) + " <br/>");
			} 
			
			// 将上传状态返回给客户端
			if (allLoop==currentLoop&&allLoop>0&&currentLoop>0) {
				response.getWriter().print("finished,");
				response.getWriter().print(ps.getStartTime()+","); 
				response.getWriter().print("成功"+bytesRead+"人"); 
				response.getWriter().print("耗时 "+formatTime(timeInSeconds));
				return;
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	private String formatTime(double timeInSeconds) {
		long seconds = (long) Math.floor(timeInSeconds);
		long minutes = (long) Math.floor(timeInSeconds / 60.0);
		long hours = (long) Math.floor(minutes / 60.0);

		if (hours != 0) {
			return hours + "h " + (minutes % 60) + "m "
					+ (seconds % 60) + "s";
		} else if (minutes % 60 != 0) {
			return (minutes % 60) + "m " + (seconds % 60) + "s";
		} else {
			return (seconds % 60) + " s";
		}
	}

	// 页面调用此方法，检查导入工作是否正在进行时，0：无 1：正在进行
	public int checkImportStatus() {
		return count;
	}
}