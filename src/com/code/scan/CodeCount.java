package com.code.scan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;



/**
 * 统计量的类
 *
 */
@SuppressWarnings("all")
public class CodeCount {
	private int lineNum = 0; // 统计代码总行数
	private int spaceNum = 0; // 统计空行
	private int noteNum = 0; // 统计注释
	private int fileNum = 0; // 统计.suf文件

	private String[] SUFTYPES = { "asp", "aspx", "c", "cc", "cls", "cpp", "cs", "ctl", "cxx", "dfm", "frm", "h", "hh",
			"hpp", "htm", "html", "inc", "java", "jsp", "pas", "php", "php3", "properties", "rc", "sh", "sql", "tlh",
			"tli", "txt", "vb", "xml" };

	List<String> sufList = new ArrayList<>();

	List<Code> fileList = new ArrayList<>(); // 记录文件目录

	/**
	 * 根据指定文件/目录统计符合条件的后缀名
	 * 
	 * @param file
	 */
	public void countSufs(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles(); // 获取目录下的所有文件
			if (files != null && files.length > 0) { // 此目录下存在文件
				for (File f : files) { // 遍历文件
					if (f.isDirectory()) { // 如果目录下仍存在目录，便递归进行统计
						countSufs(f);
					} else {
						// 截取文件后缀名,并将后缀名加入sufList
						String fileName = f.getName();
						String suf = fileName.substring(fileName.lastIndexOf(".") + 1);
						// 判断条件：此后缀名没有被添加过，且符合条件
						if (!sufList.contains(suf) && Arrays.binarySearch(SUFTYPES, suf) >= 0) {
							sufList.add(suf);
						}
					}
				}
			}
		} else {
			// 截取文件后缀名,并将后缀名加入sufList
			String fileName = file.getName();
			String suf = fileName.substring(fileName.lastIndexOf(".") + 1);
			// 判断条件：此后缀名没有被添加过，且符合条件
			if (!sufList.contains(suf) && Arrays.binarySearch(SUFTYPES, suf) >= 0) {
				sufList.add(suf);
			}
		}
	}

	/**
	 * 遍历文件，统计代码量
	 * 
	 * @param file
	 *            要统计的文件
	 * @param suf
	 *            文件后缀名
	 */
	public void countCodes(File file, List<String> sufs) {
		if (file.isDirectory()) { // 判断要统计的文件是不是目录
			File[] files = file.listFiles(); // 获取目录下的所有文件
			if (files != null && files.length > 0) { // 此目录下存在文件
				for (File f : files) { // 遍历文件
					if (f.isDirectory()) { // 如果目录下仍存在目录，便递归进行统计
						countCodes(f, sufs);
					} else {
						// 校对后缀名
						String fileName = f.getName();
						String suf = fileName.substring(fileName.lastIndexOf(".") + 1);
						if (sufs.contains(suf)) {// 判断文件是否是制定类型文件
							Code code = new Code();	//创建每个统计每个文件数据量的对象
							countLine(f,code); // 统计此制定类型文件的代码行数
							fileNum++; // 文件计数器+1
							code.setFileDir(f.getPath());
							fileList.add(code); // 将文件名字添加到集合
						}
					}
				}
			}
		} else {
			// 截取文件后缀名,并将后缀名加入sufList
			String fileName = file.getName();
			String suf = fileName.substring(fileName.lastIndexOf(".") + 1);
			if (sufs.contains(suf)) {// 判断文件是否是指定类型文件
				Code code = new Code();
				countLine(file,code); // 统计此制定类型文件的代码行数
				fileNum++; // 文件计数器+1
				code.setFileDir(file.getPath());
				fileList.add(code); // 将文件名字添加到集合
			}
		}
	}

	/**
	 * 统计指定文件的代码/空行/注释行数
	 * 
	 * @param f
	 *            指定的文件
	 */
	public void countLine(File f,Code code) {
		BufferedReader br = null; // 创建读文件的流
		try {
			br = new BufferedReader(new FileReader(f)); // 实例文件流
			String line = null;
			while ((line = br.readLine()) != null) { // 循环判断是否读取到文件末尾
				String trim = line.trim();
				if (trim.matches("")) { // 匹配空行
					spaceNum++; // 总空行+1
					code.setSpaceNum(code.getSpaceNum()+1); //对应文件空行+1
				} else if (trim.startsWith("#")) {
					spaceNum++;
					code.setSpaceNum(code.getSpaceNum()+1); //对应文件空行+1
				} else if (trim.startsWith("//")) { // 匹配单行注释
					noteNum++;
					code.setNoteNum(code.getNoteNum()+1);
				} else if (trim.startsWith("/**")) { // 匹配文本注释 同多行注释
					if (trim.endsWith("*/")) {
						noteNum++;
						code.setNoteNum(code.getNoteNum()+1);
					} else {
						while ((line = br.readLine()) != null) {
							trim = line.trim();
							noteNum++;
							code.setNoteNum(code.getNoteNum()+1);
							lineNum++;
							code.setLineNum(code.getLineNum()+1);
							if (trim.endsWith("*/")) {
								break;
							}
						}
						noteNum++;
						code.setNoteNum(code.getNoteNum()+1);
					}
				} else if (trim.startsWith("/*")) { // 匹配多行注释
					if (trim.endsWith("*/")) { // 是否只有一行
						noteNum++;
						code.setNoteNum(code.getNoteNum()+1);
					} else {
						while ((line = br.readLine()) != null) { // 不是只有一行，便接着遍历
							trim = line.trim();
							noteNum++;
							code.setNoteNum(code.getNoteNum()+1);
							lineNum++;
							code.setLineNum(code.getLineNum()+1);
							if (trim.endsWith("*/")) {
								break;
							}
						}
						noteNum++;
						code.setNoteNum(code.getNoteNum()+1);
					}
				} else if (trim.startsWith("<!--")) {
					if (trim.endsWith("-->")) { // 是否只有一行
						noteNum++;
						code.setNoteNum(code.getNoteNum()+1);
					} else {
						while ((line = br.readLine()) != null) { // 不是只有一行，便接着遍历
							trim = line.trim();
							noteNum++;
							code.setNoteNum(code.getNoteNum()+1);
							lineNum++;
							code.setLineNum(code.getLineNum()+1);
							if (trim.endsWith("-->")) {
								break;
							}
						}
						noteNum++;
						code.setNoteNum(code.getNoteNum()+1);
					}
				}
				lineNum++; // 总行数+1
				code.setLineNum(code.getLineNum()+1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close(); // 关闭流
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 输出记录到界面表格
	 * @param table
	 * @param prefix
	 */
	public List<Code> outMsg(JTable table,String prefix) {
		Vector vName = new Vector();
		vName.add("文件目录");
		vName.add("代码量");
		vName.add("注释量");
		vName.add("空行数");
		vName.add("总行数");
		Vector vData = new Vector();
		Vector vRow = new Vector();
		vRow.add(prefix);
		vRow.add(lineNum-spaceNum-noteNum);	//实际代码量=总行数-注释-空行
		vRow.add(noteNum);
		vRow.add(spaceNum);
		vRow.add(lineNum);
		vData.add(vRow.clone());
		DefaultTableModel model = new DefaultTableModel(vData,vName);
		table.setModel(model);
		
		for (Code c : fileList) {
			Vector row = new Vector();
			row.add(c.getFileDir().replace(prefix, ""));
			row.add(c.getLineNum() - c.getNoteNum() - c.getSpaceNum());
			row.add(c.getNoteNum());
			row.add(c.getSpaceNum());
			row.add(c.getLineNum());
			vData.add(row.clone());
			model = new DefaultTableModel(vData,vName);	
		}
		table.setModel(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(400);
		return fileList;
	}

	/**
	 * 导出记录到文件
	 * @return
	 */
	public void exportToFile(CodeCount codeCount,String prefix,JFrame frame) {
		try {
			// 设置外观
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		JFileChooser fileChooser = new JFileChooser("D:\\CodeCountTestFile"); // 文件选择器+默认路径
		fileChooser.setFileFilter(new FileNameExtensionFilter("Excel 工作簿(*.xlsx)", "xlsx")); // 选择类型
		int resultVal = fileChooser.showSaveDialog(frame); // 显示选择器
		if (resultVal == fileChooser.APPROVE_OPTION) { // 判断是否确定选择文件
			String path = fileChooser.getSelectedFile().toString().split("\\.")[0];
			
			//创建Excel表格
			createExcel(path,codeCount,prefix);
			
		} else {
			JOptionPane.showMessageDialog(null, "Excel文件未保存!!!", "警告", JOptionPane.WARNING_MESSAGE, null);
		}
	}
	
	/**
	 * 创建Excel
	 */
	public void createExcel(String path,CodeCount codeCount,String prefix) {
		
		// 通用变量
		int rowNo = 0, cellNo = 1;
		Row nRow = null; // 通用行对象
		Cell nCell = null; // 通用单元格对象
		
		Workbook wb = new SXSSFWorkbook(500);   //支持Excel2007+版本     拓展名:xlsx  

		//2.创建工作表
		Sheet sheet = wb.createSheet();
		
		sheet.setColumnWidth(cellNo++, 52*256);
		
		cellNo = 1; //重置
		
		//3.创建行对象Row
		/*******大标题*******************/
		nRow = sheet.createRow(rowNo++);  //读取行对象
		//4.创建单元格对象    从0计数
		nCell = nRow.createCell(cellNo);
		//合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 5));
		
		//5.设置单元格内容
		nCell.setCellValue("代码量统计详细信息");
		nCell.setCellStyle(bigTitle(wb));
		
		/************小标题*****************/
		String[] titles = {"文件目录","代码量","注释量","空行数","总行数"};
		nRow = sheet.createRow(rowNo++);
		nRow.setHeightInPoints(26.25f);   //设置行高
		for (String s : titles) {
			nCell = nRow.createCell(cellNo++);	//设置单元格对象
			nCell.setCellValue(s);
			nCell.setCellStyle(title(wb));
		}
		
		//设置总数
		cellNo = 1;
		nRow = sheet.createRow(rowNo++);
		nRow.setHeightInPoints(20.0f);
		for (int i = 0; i < titles.length; i++) {
			nCell = nRow.createCell(cellNo++);
			nCell.setCellStyle(text(wb));
			switch (i) {
			case 0:
				nCell.setCellValue(prefix);
				break;
			case 1:
				nCell.setCellValue(codeCount.getLineNum() - codeCount.getSpaceNum() - codeCount.getNoteNum());
				break;
			case 2:
				nCell.setCellValue(codeCount.getNoteNum());
				break;
			case 3:
				nCell.setCellValue(codeCount.getSpaceNum());
				break;
			case 4:
				nCell.setCellValue(codeCount.getLineNum());
				break;

			default:
				break;
			}
		}
		
		//输出数据
		List<Code> list = codeCount.getFileList();
		for (Code code : list) {
			cellNo = 1;	//重置
			
			nRow = sheet.createRow(rowNo++);
			
			//设置文件目录
			nCell = nRow.createCell(cellNo++);
			nCell.setCellValue(code.getFileDir().replace(prefix, ""));
			nCell.setCellStyle(text(wb));
			
			//设置代码量
			nCell = nRow.createCell(cellNo++);
			nCell.setCellValue(code.getLineNum() - code.getSpaceNum() - code.getNoteNum());
			nCell.setCellStyle(text(wb));
			
			//设置注释量
			nCell = nRow.createCell(cellNo++);
			nCell.setCellValue(code.getNoteNum());
			nCell.setCellStyle(text(wb));
			
			//设置空行数目
			nCell = nRow.createCell(cellNo++);
			nCell.setCellValue(code.getSpaceNum());
			nCell.setCellStyle(text(wb));
			
			//设置总行数
			nCell = nRow.createCell(cellNo++);
			nCell.setCellValue(code.getLineNum());
			nCell.setCellStyle(text(wb));
			
		}
		
		//7.保存,关闭流
		OutputStream os = null;
		try {
		os = new FileOutputStream(path+".xlsx");
		wb.write(os);  //进行输出
		} catch (Exception e) {
		
		e.printStackTrace();
		} finally {
		try {
		os.close();		//关闭流
		} catch (Exception e) {}
		}
		
		JOptionPane.showMessageDialog(null, "Excel保存成功", "提示", JOptionPane.PLAIN_MESSAGE, null);
		
	}
	
	//大标题的样式
		public CellStyle bigTitle(Workbook wb){
			CellStyle style = wb.createCellStyle();
			Font font = wb.createFont();
			font.setFontName("宋体");
			font.setFontHeightInPoints((short)20);
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);					//字体加粗
			
			style.setFont(font);
			
			style.setAlignment(CellStyle.ALIGN_CENTER);					//横向居中
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);		//纵向居中
			
			return style;
		}
		//小标题的样式
		public CellStyle title(Workbook wb){
			CellStyle style = wb.createCellStyle();
			Font font = wb.createFont();
			font.setFontName("黑体");
			font.setFontHeightInPoints((short)12);
			
			style.setFont(font);
			
			style.setAlignment(CellStyle.ALIGN_CENTER);					//横向居中
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);		//纵向居中
			
			style.setBorderTop(CellStyle.BORDER_THIN);					//上细线
			style.setBorderBottom(CellStyle.BORDER_THIN);				//下细线
			style.setBorderLeft(CellStyle.BORDER_THIN);					//左细线
			style.setBorderRight(CellStyle.BORDER_THIN);				//右细线
			
			return style;
		}
			
		//文字样式
		public CellStyle text(Workbook wb){
			CellStyle style = wb.createCellStyle();
			Font font = wb.createFont();
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short)10);
			
			style.setFont(font);
			
			style.setAlignment(CellStyle.ALIGN_LEFT);					//横向居左
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);		//纵向居中
			
			style.setBorderTop(CellStyle.BORDER_THIN);					//上细线
			style.setBorderBottom(CellStyle.BORDER_THIN);				//下细线
			style.setBorderLeft(CellStyle.BORDER_THIN);					//左细线
			style.setBorderRight(CellStyle.BORDER_THIN);				//右细线
			
			return style;
		}
	
	public int getLineNum() {
		return lineNum;
	}

	public int getSpaceNum() {
		return spaceNum;
	}

	public int getNoteNum() {
		return noteNum;
	}

	public int getFileNum() {
		return fileNum;
	}

	public List<String> getSufList() {
		return sufList;
	}

	public void setSufList(List<String> sufList) {
		this.sufList = sufList;
	}

	public List<Code> getFileList() {
		return fileList;
	}

	@Override
	public String toString() {
		return "CodeCount [lineNum=" + lineNum + ", spaceNum=" + spaceNum + ", noteNum=" + noteNum + ", fileNum="
				+ fileNum + ", SUFTYPES=" + Arrays.toString(SUFTYPES) + ", sufList=" + sufList + ", fileList="
				+ fileList + "]";
	}
	
}