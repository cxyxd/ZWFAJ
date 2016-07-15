package com.zw;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.RecursiveAction;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.zw.model.ParseStatusWithFAJ;
import com.zw.model.Passenger;

/**
 * @author: shuang.gao  Date: 2015/7/14 Time: 8:16
 */
public class PassengerTask extends RecursiveAction {
	private  String urlGetCookieAndValidate="http://www.travelsky.com/tsky/servlet/CallYanServlet?title=home";
	private  String urlValidate="http://www.travelsky.com/tsky/validate";
	private  String cookie = null;
	private  String fielLocation="";
	private int circleCount=1;
	private ParseStatusWithFAJ parseStatus;
	
	private int minCountPreCircle=10;  //每轮至少得验证出10张票
	

    private static final long serialVersionUID = -6196480027075657316L;


    private List<Passenger> array;

    private int low;

    private int high;


    public PassengerTask(List<Passenger> list, int low, int high,int circleCount,ParseStatusWithFAJ parseStatus) {
        this.array = list;
        this.low = low;
        this.high = high;
        this.circleCount=circleCount;
        this.parseStatus=parseStatus;
    }

    @Override
    protected void compute() {
 

        if (high - low <= (array.size()/4)) {
        	List<Passenger> array2=new ArrayList<Passenger>();
        
        	for (int i = low; i <= high; i++) {
				array2.add(array.get(i));
			}
        	solve(array2,circleCount);
        } else {
			// 1. 一个大任务分割成两个子任务
			int mid = (low + high) >>> 1;
			PassengerTask left = new PassengerTask(array, low, mid,
					circleCount, parseStatus);
			PassengerTask right = new PassengerTask(array, mid + 1, high,
					circleCount, parseStatus);
			invokeAll(left,right);
//			left.fork();
//			right.fork();
//			left.join();
//			right.join();
        }

    }

    private void solve(List<Passenger> array, int circleCount) {

    	int previousOKCount=0; //上一轮成功的次数
    	for (int j = 0; j < circleCount; j++) {
    		int okCount=0;  //这一轮成功的人数
			for (int i = 0; i < array.size();i++) {
				Passenger p = (Passenger) array.get(i);
		//		if (i == 0)
				    getPicAndCookie(urlGetCookieAndValidate);
				List<NameValuePair> params = getParameter(p);
				getDetail(urlValidate, params, p);
				if ("ok".equals(p.getMsg())||
						"电子票号错误".equals(p.getMsg())) {
					okCount++;
					array.remove(i);
					parseStatus.getSolvedPassenger().incrementAndGet();
				//	System.out.println("成功了 "+p.getId()+"  "+p.getName()+" "+Thread.currentThread().getName()+" "+parseStatus.getStartTime());
				}
			}
			
			System.out.println("i changed "+okCount+" "+new Date()+" count: "+j+" "+Thread.currentThread().getName()+" "+parseStatus.getStartTime());
			
			parseStatus.getCurrentloop().incrementAndGet();
			 
			//是否提前结束循环
			if (array.size()>400&&circleCount>4) {
				if(okCount-previousOKCount>minCountPreCircle){
					previousOKCount=okCount;
				}else {
					System.out.println("提前结束 "+new Date()+" count: "+j+" "+Thread.currentThread().getName()+" "+parseStatus.getStartTime());
					return; 
				}
			}

		}
		
	}



    
    public List<NameValuePair> getParameter(Passenger p){
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		 
		params.add(new BasicNameValuePair("eticketNoORIn",p.getTicketNo()));
		params.add(new BasicNameValuePair("passengerName_src",p.getName()));
		try {
			params.add(new BasicNameValuePair("passengerName",URLEncoder.encode(p.getName(), "utf-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		params.add(new BasicNameValuePair("validateFlag","0"));
		params.add(new BasicNameValuePair("invoiceNo",""));
		params.add(new BasicNameValuePair("imgSrc","/tsky/images/loading.gif"));
		 
		params.add(new BasicNameValuePair("eticketNo",p.getTicketNo()));
		params.add(new BasicNameValuePair("invoiceNo",""));
		
		//这个目前比较操蛋
		//在e盘下 我下载了验证码 
		//然后人工读出 后把字符写进src/yzm.txt
		//String randCode=getYZM("src/yzm.txt");
		String randCode="1234";
		//System.out.println(randCode);
		params.add(new BasicNameValuePair("code", randCode));
		return params;
	}
	


	/*
	 * 获取第一次下载图片的时候 远程服务器给我发的cookie
	 */
	public void getCookie(HttpResponse httpResponse) throws ClientProtocolException, IOException{

		
		Header[] headers=httpResponse.getHeaders("Set-Cookie");
		//BIGipServerpool_ip_port 这个参数是和负载均衡相关的
		//JSESSIONID 这就是sessionid
		cookie="";
		for(Header h:headers){
			if (h.getValue().contains("BIGipServerpool_122")) {
				String BIGipServerpool=h.getValue().substring("BIGipServerpool_122.119.122.179_80=".length(), 
						h.getValue().indexOf(";"));
				
				cookie+="BIGipServerpool_122.119.122.179_80="+BIGipServerpool;
			}
			if (h.getValue().contains("JSESSIONID")) {
				String JSESSIONID=h.getValue().substring("JSESSIONID=".length(), 
						h.getValue().indexOf(";"));
				
				cookie+="JSESSIONID="+JSESSIONID;
			}
			//这些参数的作用 我不清楚 加上就是了
			cookie+="Hm_lvt_486e71cc1c3c5d7a07853a6e72364f55=1456716098;__utma=88932958.523101033.1456716098.1456716098.1456716098.1;__utmz=88932958.1456716098.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none);CNZZDATA1256052643=1173832624-1456716029-%7C1456884431";
			
		}
	//	System.out.println("this is cookie  "+cookie);
		
	}
	
		
	/*
	 * 
	 *根据验证码的url 第一次获得cookid并下载图片  
	 */
	public CloseableHttpResponse getPicAndCookie(String url) {
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(url);
		CloseableHttpResponse response =null;
		try {
			 response = httpclient.execute(httppost);
			 getCookie(response);
		} catch (ClientProtocolException e) {
			System.out.println("ClientProtocolException sp"+e.getMessage());
		//	e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException  sp"+e.getMessage());
		//	e.printStackTrace();
		}
		
		httppost.releaseConnection();
		return response;
	}
	
	
	public  void getDetail(String url,List<NameValuePair> params,Passenger p)  {
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(url);

		httppost.setHeader("Host", "www.travelsky.com");
		httppost.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
		httppost.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httppost.setHeader("Accept-Language",
				"zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		httppost.setHeader("Referer", "http://www.travelsky.com/tsky/validate");
		httppost.setHeader("Cookie", cookie);
		httppost.setHeader("Connection", "keep-alive");

		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(6000).setConnectTimeout(6000).build();// 设置请求和传输超时时间
			httppost.setConfig(requestConfig);
			CloseableHttpResponse response = httpclient.execute(httppost);

			parseResult(response, p);
			//httppost.releaseConnection(); 
			//System.out.println("处理完" + p.getName() + " " + p.getTicketNo());
		} catch (SocketTimeoutException e) { 
		//	System.out.println(new Date()+"  "+"Read timed out pps");
		} catch (ClientProtocolException e) {
			System.out.println(new Date()+"ip ClientProtocolException");
		} catch (IOException e) {
			System.out.println(new Date()+"ip IOException ");
		}catch (Exception e) { 
			System.out.println(new Date()+"ip  Exception ");
			System.out.println("psp ");
		}finally{ 
			httppost.releaseConnection();
		}

	}
	
	
	public void parseResult(CloseableHttpResponse response, Passenger p){
		HttpEntity entity = response.getEntity();
		String jsonStr=null;
		try {
			jsonStr = EntityUtils.toString(entity, "utf-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			p.setMsg("返回空白");
			return;
		}

		if (jsonStr.contains("航段2")) {
			p.setMsg("ok");
			p.setDetail("多个航段");
			return;
		}
		
		if(jsonStr.contains("您好")){
			p.setMsg("ok");
			int index=jsonStr.indexOf("<span class=\"mr35\">");
			if (index>0) {
				String time= jsonStr.substring(index+19, index+12+16+3);
				p.setTime(time);
			}
			index=jsonStr.indexOf("<div class=\"coln coln-third\">");
			if (index>0) {
				String flightNo=jsonStr.substring(index+29, index+6+30);
				flightNo=flightNo.substring(0, flightNo.indexOf("<"));
				p.setFlightNo(flightNo);
			}	            
			String detail="<div class=\"coln coln-fourth coln-green status";
			index=jsonStr.indexOf(detail);
			if (index>0) {
				detail=jsonStr.substring(index+detail.length(),index+detail.length()+30);
				int from=detail.indexOf(">");
				int to=detail.indexOf("<");
				detail=detail.substring(from+1, to);
				p.setDetail(detail);
			}			
		}
		
		if (jsonStr.length()==0) {
			p.setMsg("返回空白");
		}
		
		if (jsonStr.contains("系统错误")) {
			p.setMsg("系统错误,请稍候再试");
		}
		if(jsonStr.contains("var validatorResult = false")){
			p.setMsg("电子票号错误");
		}
		
		if (p.getMsg()==null) {
			p.setMsg("返回空白");
		}
		 
//		if(jsonStr.length()>0&&!jsonStr.contains("系统错误")){
//			System.out.println("ppp "+jsonStr);
//		}
		
		
	}
	


	public String getFielLocation() {
		return fielLocation;
	}

	public void setFielLocation(String fielLocation) {
		this.fielLocation = fielLocation;
	}

	public ParseStatusWithFAJ getParseStatus() {
		return parseStatus;
	}

	public void setParseStatus(ParseStatusWithFAJ parseStatus) {
		this.parseStatus = parseStatus;
	}

	public int getCircleCount() {
		return circleCount;
	}

	public void setCircleCount(int circleCount) {
		this.circleCount = circleCount;
	}
	
	
}