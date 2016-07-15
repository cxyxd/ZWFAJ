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
//  	list.add(new Passenger(1,"杨杰", "7818530899658"));
//		list.add(new Passenger(2,"何肖瑾", "7818513570248"));
//		list.add(new Passenger(3,"金琪", "7848502136024"));
//		list.add(new Passenger(4,"戴迎春", "7848502136023"));
		list.add(new Passenger(5,"GUO/PENG ", "7816715248402"));
		
		return list; 
		
	}

	public static List<Passenger> getPassage(String location) {

		Workbook readwb = null;
		InputStream instream=null;
		List<Passenger> list = new LinkedList<Passenger>();
		try {
			// 构建Workbook对象, 只读Workbook对象
			// 直接从本地文件创建Workbook
			instream = new FileInputStream(location);
			readwb = Workbook.getWorkbook(instream);
			// Sheet的下标是从0开始
			// 获取第一张Sheet表
			Sheet readsheet = readwb.getSheet(0);

			// 获取Sheet表中所包含的总行数
			int rsRows = readsheet.getRows();
			// 获取指定单元格的对象引用

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
	 * 输出Excel
	 * 
	 * @param os
	 */
	public static void writeExcel(OutputStream os,List<Passenger> list) {
		try {

			WritableWorkbook wwb = Workbook.createWorkbook(os);
			// 创建Excel工作表 指定名称和位置
			WritableSheet ws = wwb.createSheet("Test Sheet 1", 0);

			// **************往工作表中添加数据*****************

			// 1.添加Label对象
			Label label=null;
			label= new Label(0, 0, "序号");
			ws.addCell(label);
			label= new Label(1, 0, "电子客票号");
			ws.addCell(label);
			label= new Label(2, 0, "旅客姓名");
			ws.addCell(label);
			label= new Label(3, 0, "验票信息");
			ws.addCell(label);
			label= new Label(4, 0, "时间");
			ws.addCell(label);
			label= new Label(5, 0, "航班");
			ws.addCell(label);
			label= new Label(6, 0, "状态");
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


			// 写入工作表
			wwb.write();
			wwb.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		//输出Excel   
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