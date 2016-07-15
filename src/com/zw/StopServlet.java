package com.zw;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.zw.model.ParseStatusWithFAJ;

public class StopServlet extends HttpServlet {


	public StopServlet() {
		super();
	}


	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ParseStatusWithFAJ ps=(ParseStatusWithFAJ) request.getSession().getAttribute("parseStatus");
		if (ps==null) {
			return;
		}
		HttpProxyWithforkAndJoin hp=new HttpProxyWithforkAndJoin();
		hp.setFielLocation(getServletContext().getRealPath("attachment"));
		hp.setParseStatus(ps);
		hp.writeXLS(ps.getList());
		request.getRequestDispatcher("processorWithFAJNoStop.jsp").forward(request, response);
	}


	public void init() throws ServletException {
		// Put your code here
	}

}
