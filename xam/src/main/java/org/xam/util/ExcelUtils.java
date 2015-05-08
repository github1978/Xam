package org.xam.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	
	private XSSFWorkbook workbook;
	
	public ExcelUtils(){
		
	}
	
	public ExcelUtils(XSSFWorkbook workbook){
		this.workbook = workbook;
	}

	/**
	 * 根据传入的列头名称，返回指定行的单元格内容
	 * 
	 * @param name
	 *            列头名称
	 * @param sheet
	 *            工作表对象
	 * @param rowindex
	 *            行索引
	 */
	public static String getCellContentByName(String name, XSSFSheet sheet,
			int rowindex) {
		int headerindex = -1;
		XSSFRow headerow = sheet.getRow(0);
		XSSFRow dataRow = sheet.getRow(rowindex);
		Iterator<Cell> cells = headerow.cellIterator();
		while (cells.hasNext()) {
			Cell cell = cells.next();
			if (cell.getStringCellValue().equals(name)) {
				headerindex = cell.getColumnIndex();
				break;
			}
		}
		if (headerindex == -1) {
			try {
				throw new Exception("在指定的Excel中未找到指定的列");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return getCellContent(dataRow.getCell(headerindex));
	}

	/**
	 * 返回指定Cell（单元格）的内容
	 * 
	 * @param cell
	 * @return
	 */
	public static String getCellContent(Cell cell) {
		if (cell == null) {
			return null;
		}
		if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			// 判断excel中的数据如果是23.0型的double类型，则返回23
			// 如果是23.02则返回23.02
			String d = String.valueOf(cell.getNumericCellValue());
			if (d.indexOf(".0") > 0 && d.split(".0").length == 1) {
				return String.valueOf((int) cell.getNumericCellValue());
			}
			return d;
		} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			return "";
		} else {
			return String.valueOf(cell.getStringCellValue());
		}
	}

	/**
	 * 在excel中创建一行数据
	 * 
	 * @param rowNO
	 *            在哪一行创建信息
	 * @param rowHeight
	 *            行高
	 * @param data 数据数组
	 */
	public XSSFRow createExcelRow(XSSFSheet sheet, int rowNO, int rowHeight,
			String[] data) {
		XSSFRow row = sheet.createRow(rowNO);
		row.setHeight((short) rowHeight);
		Cell cell = null;
		for (int i = 0; i < data.length; i++) {
			sheet.setColumnWidth(i, 20 * 256); // 设置列宽，20个字符宽度。宽度参数为1/256，故乘以256
			cell = row.createCell(i);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(data[i]);
		}
		return row;
	}

	/**
	 * @param fileName
	 */
	public void exportExcel(String fileName) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(new File(fileName));
			workbook.write(os);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public static void main(String[] args){
//		XSSFWorkbook book = new XSSFWorkbook();
//		ExcelTools etool = new ExcelTools(book);
//		String[] columnHeader={"账号","码"};
//		etool.createExcelRow(book.createSheet("测试报告"), 0, 300, columnHeader);
//		etool.exportExcel("D:\\1.xlsx");
//	}

}
