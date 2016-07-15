package com.zw.utils;

//in ExcelOperater   

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import com.zw.model.Passenger;


import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelOperater {
	
	public static List<Passenger> getSomePassage(){
		List<Passenger> list = new LinkedList<Passenger>();
//  	list.add(new Passenger(1,"���", "7818530899658"));
//		list.add(new Passenger(2,"��Ф�", "7818513570248"));
//		list.add(new Passenger(3,"����", "7848502136024"));
//		list.add(new Passenger(4,"��ӭ��", "7848502136023"));
		list.add(new Passenger(5,"GUO/PENG ", "7816715248402"));
		
		return list; 
		
	}

	public static List<Passenger> getPassage(String location) {

		Workbook readwb = null;
		InputStream instream=null;
		List<Passenger> list = new LinkedList<Passenger>();
		try {
			// ����Workbook����, ֻ��Workbook����
			// ֱ�Ӵӱ����ļ�����Workbook
			instream = new FileInputStream(location);
			readwb = Workbook.getWorkbook(instream);
			// Sheet���±��Ǵ�0��ʼ
			// ��ȡ��һ��Sheet��
			Sheet readsheet = readwb.getSheet(0);

			// ��ȡSheet������������������
			int rsRows = readsheet.getRows();
			// ��ȡָ����Ԫ��Ķ�������

			for (int i = 2; i < rsRows; i++) {
				String name = readsheet.getCell(5, i).getContents();
				String ticketNo = readsheet.getCell(1, i).getContents();
				int id = Integer.valueOf(readsheet.getCell(0, i).getContents());
				if (StringUtils.isNotEmpty(name)
						&& StringUtils.isNotEmpty(ticketNo)) {
					list.add(new Passenger(id,name, ticketNo));
				}

			}

		} catch(NumberFormatException e){
			
		}
		
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				instream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			readwb.close();
		}
		return list;

	}

	/**
	 * ���Excel
	 * 
	 * @param os
	 */
	public static void writeExcel(OutputStream os,List<Passenger> list) {
		try {

			WritableWorkbook wwb = Workbook.createWorkbook(os);
			// ����Excel������ ָ�����ƺ�λ��
			WritableSheet ws = wwb.createSheet("Test Sheet 1", 0);

			// **************�����������������*****************

			// 1.���Label����
			Label label=null;
			label= new Label(0, 0, "���");
			ws.addCell(label);
			label= new Label(1, 0, "���ӿ�Ʊ��");
			ws.addCell(label);
			label= new Label(2, 0, "�ÿ�����");
			ws.addCell(label);
			label= new Label(3, 0, "��Ʊ��Ϣ");
			ws.addCell(label);
			label= new Label(4, 0, "ʱ��");
			ws.addCell(label);
			label= new Label(5, 0, "����");
			ws.addCell(label);
			label= new Label(6, 0, "״̬");
			ws.addCell(label);
			for (int i = 0; i < list.size(); i++) {
				label= new Label(0, i+1, ""+list.get(i).getId());
				ws.addCell(label);
				label= new Label(1, i+1, list.get(i).getTicketNo());
				ws.addCell(label);
				label= new Label(2, i+1, list.get(i).getName());
				ws.addCell(label);
				label= new Label(3, i+1, list.get(i).getMsg());
				ws.addCell(label);
				
				label= new Label(4, i+1, list.get(i).getTime());
				ws.addCell(label);
				label= new Label(5, i+1, list.get(i).getFlightNo());
				ws.addCell(label);
				label= new Label(6, i+1, list.get(i).getDetail());
				ws.addCell(label);
			}


			// д�빤����
			wwb.write();
			wwb.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		//���Excel   
				File fileWrite = new File("src/final.xls");   
				try {
					fileWrite.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
				try {
					OutputStream os = new FileOutputStream(fileWrite);
					writeExcel(os,null);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
				
	}

}