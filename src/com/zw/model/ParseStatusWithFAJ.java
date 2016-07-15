package com.zw.model;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class ParseStatusWithFAJ {
	// ��ɰٷֱ�
	private double percentComplete;
	// �ܹ���Ŀ
	private int totalPassenger=0;
	// �Ѿ��������Ŀ
	private AtomicInteger solvedPassenger=new AtomicInteger(0);
	
	private AtomicInteger currentloop=new AtomicInteger(0);
	private int allLoop;
	
	private List<Passenger> list;
	
	
	//��ʼʱ��
	private long startTime;
	
	
	
	public ParseStatusWithFAJ(){
		startTime=System.currentTimeMillis();
	}
	public double getPercentComplete() {
		return percentComplete;
	}
	public void setPercentComplete(double percentComplete) {
		this.percentComplete = percentComplete;
	}
	public int getTotalPassenger() {
		return totalPassenger;
	}
	public void setTotalPassenger(int totalPassenger) {
		this.totalPassenger = totalPassenger;
	}

	public AtomicInteger getSolvedPassenger() {
		return solvedPassenger;
	}
	public void setSolvedPassenger(AtomicInteger solvedPassenger) {
		this.solvedPassenger = solvedPassenger;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public AtomicInteger getCurrentloop() {
		return currentloop;
	}
	public void setCurrentloop(AtomicInteger currentloop) {
		this.currentloop = currentloop;
	}
	public int getAllLoop() {
		return allLoop;
	}
	public void setAllLoop(int allLoop) {
		this.allLoop = allLoop;
	}
	public List<Passenger> getList() {
		return list;
	}
	public void setList(List<Passenger> list) {
		this.list = list;
	}
}
