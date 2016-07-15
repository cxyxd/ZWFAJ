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
	private static int count = 0;// ȫ�ֱ���Ƿ�Ϊ��һ�ε���


	 public void doGet(HttpServletRequest request, HttpServletResponse response) {
		 doPost(request, response);
	 }
	 
	// ��ȡ���ݵ���ʵʱ״̬
	public void doPost(HttpServletRequest request,HttpServletResponse response) {
		// ���ø���Ӧ���ڻ����ж�ȡ
	//	System.out.println("i have been called by session "+request.getSession().getId());
		response.addHeader("Expires", "0");
		response.addHeader("Cache-Control",
				"no-store, no-cache, must-revalidate");
		response.addHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		
		int allLoop = 0;// һ��ѭ����Ȧ
		int bytesRead = 0;// �Ѿ����������
		int currentLoop=0; //�Ѿ������Ȧ��
		long getElapsedTimeInSeconds = 0;// ����Ѿ��ϴ���ʱ��
		ParseStatusWithFAJ ps=(ParseStatusWithFAJ) request.getSession().getAttribute("parseStatus");
		if (ps==null) {
			return;
		}
		allLoop = ps.getAllLoop();
		bytesRead = ps.getSolvedPassenger().intValue();
		currentLoop=ps.getCurrentloop().intValue();
		
	//	System.out.println(allLoop+"   "+bytesRead);
		getElapsedTimeInSeconds = (System.currentTimeMillis() - ps.getStartTime()) / 1000;

		// �����ϴ���ɵİٷֱ�
		String percentComplete = "0";
		if (allLoop > 0) {
			double k = (double) currentLoop / allLoop * 100;
			int temp=(int) (k*10);
			k=temp/10;
			percentComplete =""+k;
		}
		// ����ϴ����õ�ʱ��
		long timeInSeconds = getElapsedTimeInSeconds;

		try {

			
			if (currentLoop != allLoop) {
				response.getWriter().println("<br/>");
				response.getWriter().println(
						"<div class=\"prog-border\"><div class=\"prog-bar\" style=\"width: "
								+ percentComplete + "%;\"></div></div>");
				response.getWriter().println("�Ѿ�����"+bytesRead+"��<br/>");
				response.getWriter().println(
						"Runtime: " + formatTime(timeInSeconds) + " <br/>");
			} 
			
			// ���ϴ�״̬���ظ��ͻ���
			if (allLoop==currentLoop&&allLoop>0&&currentLoop>0) {
				response.getWriter().print("finished,");
				response.getWriter().print(ps.getStartTime()+","); 
				response.getWriter().print("�ɹ�"+bytesRead+"��"); 
				response.getWriter().print("��ʱ "+formatTime(timeInSeconds));
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

	// ҳ����ô˷�������鵼�빤���Ƿ����ڽ���ʱ��0���� 1�����ڽ���
	public int checkImportStatus() {
		return count;
	}
}