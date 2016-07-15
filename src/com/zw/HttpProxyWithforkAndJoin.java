package com.zw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

import com.zw.model.ParseStatusWithFAJ;
import com.zw.model.Passenger;
import com.zw.utils.ExcelOperater;




public class HttpProxyWithforkAndJoin {
	
	private  String fielLocation="";
	private int circleCount=8;
	private ParseStatusWithFAJ parseStatus;
	

	
	public void writeXLS(List<Passenger> all) {
		//���Excel
		long startTime=parseStatus.getStartTime();
		File fileWrite = new File(fielLocation+"//result_"+startTime+".xls");   
		try {
			fileWrite.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		OutputStream os=null;
		try {
			os = new FileOutputStream(fileWrite);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}   
		ExcelOperater.writeExcel(os, all);
		parseStatus.setCurrentloop(new AtomicInteger( parseStatus.getAllLoop()) );
	}
	
	public static void main(String[] args) {
		new HttpProxyWithforkAndJoin().getResult();
	}
	

	public List<Passenger> getResult() {

		List<Passenger> list = ExcelOperater.getPassage(fielLocation+"//myfile.xls");
		List<Passenger> all = new ArrayList<Passenger>();
		
    	int taskCount=list.size()/((list.size()/4)+1);
    	int judge=list.size()&((list.size()/4)+1);
    	if(judge!=0)
    		taskCount++;
    	parseStatus.setList(list);
    	parseStatus.setAllLoop( circleCount ); 
    	//ΪɶҪ��1?
    	//����1 ��writeXLS����
    	parseStatus.setAllLoop( (parseStatus.getAllLoop()*taskCount)+1);
    	
		 // 1. ��������
    	PassengerTask passengerTask = new PassengerTask(list, 0, list.size()-1,circleCount,parseStatus);
 
      

        // 2. �����̳߳�
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        // 3. �ύ�����̳߳�
        forkJoinPool.submit(passengerTask);

        
        passengerTask.join();
		System.out.println("kps "+parseStatus.getAllLoop()+"  "+parseStatus.getCurrentloop());
          
        forkJoinPool.shutdown();
          
        if(passengerTask.isCompletedNormally()) {  
            System.out.printf("Main: The process has completed normally.\n");  
        }
       
          
        System.out.println("Main: End of the program.\n");  

		all.addAll(list);
		return all;
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
