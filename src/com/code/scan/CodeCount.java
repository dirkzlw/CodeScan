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
 * ͳ��������
 *
 */
@SuppressWarnings("all")
public class CodeCount {
	private int lineNum = 0; // ͳ�ƴ���������
	private int spaceNum = 0; // ͳ�ƿ���
	private int noteNum = 0; // ͳ��ע��
	private int fileNum = 0; // ͳ��.suf�ļ�

	private String[] SUFTYPES = { "asp", "aspx", "c", "cc", "cls", "cpp", "cs", "ctl", "cxx", "dfm", "frm", "h", "hh",
			"hpp", "htm", "html", "inc", "java", "jsp", "pas", "php", "php3", "properties", "rc", "sh", "sql", "tlh",
			"tli", "txt", "vb", "xml" };

	List<String> sufList = new ArrayList<>();

	List<Code> fileList = new ArrayList<>(); // ��¼�ļ�Ŀ¼

	/**
	 * ����ָ���ļ�/Ŀ¼ͳ�Ʒ��������ĺ�׺��
	 * 
	 * @param file
	 */
	public void countSufs(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles(); // ��ȡĿ¼�µ������ļ�
			if (files != null && files.length > 0) { // ��Ŀ¼�´����ļ�
				for (File f : files) { // �����ļ�
					if (f.isDirectory()) { // ���Ŀ¼���Դ���Ŀ¼����ݹ����ͳ��
						countSufs(f);
					} else {
						// ��ȡ�ļ���׺��,������׺������sufList
						String fileName = f.getName();
						String suf = fileName.substring(fileName.lastIndexOf(".") + 1);
						// �ж��������˺�׺��û�б���ӹ����ҷ�������
						if (!sufList.contains(suf) && Arrays.binarySearch(SUFTYPES, suf) >= 0) {
							sufList.add(suf);
						}
					}
				}
			}
		} else {
			// ��ȡ�ļ���׺��,������׺������sufList
			String fileName = file.getName();
			String suf = fileName.substring(fileName.lastIndexOf(".") + 1);
			// �ж��������˺�׺��û�б���ӹ����ҷ�������
			if (!sufList.contains(suf) && Arrays.binarySearch(SUFTYPES, suf) >= 0) {
				sufList.add(suf);
			}
		}
	}

	/**
	 * �����ļ���ͳ�ƴ�����
	 * 
	 * @param file
	 *            Ҫͳ�Ƶ��ļ�
	 * @param suf
	 *            �ļ���׺��
	 */
	public void countCodes(File file, List<String> sufs) {
		if (file.isDirectory()) { // �ж�Ҫͳ�Ƶ��ļ��ǲ���Ŀ¼
			File[] files = file.listFiles(); // ��ȡĿ¼�µ������ļ�
			if (files != null && files.length > 0) { // ��Ŀ¼�´����ļ�
				for (File f : files) { // �����ļ�
					if (f.isDirectory()) { // ���Ŀ¼���Դ���Ŀ¼����ݹ����ͳ��
						countCodes(f, sufs);
					} else {
						// У�Ժ�׺��
						String fileName = f.getName();
						String suf = fileName.substring(fileName.lastIndexOf(".") + 1);
						if (sufs.contains(suf)) {// �ж��ļ��Ƿ����ƶ������ļ�
							Code code = new Code();	//����ÿ��ͳ��ÿ���ļ��������Ķ���
							countLine(f,code); // ͳ�ƴ��ƶ������ļ��Ĵ�������
							fileNum++; // �ļ�������+1
							code.setFileDir(f.getPath());
							fileList.add(code); // ���ļ�������ӵ�����
						}
					}
				}
			}
		} else {
			// ��ȡ�ļ���׺��,������׺������sufList
			String fileName = file.getName();
			String suf = fileName.substring(fileName.lastIndexOf(".") + 1);
			if (sufs.contains(suf)) {// �ж��ļ��Ƿ���ָ�������ļ�
				Code code = new Code();
				countLine(file,code); // ͳ�ƴ��ƶ������ļ��Ĵ�������
				fileNum++; // �ļ�������+1
				code.setFileDir(file.getPath());
				fileList.add(code); // ���ļ�������ӵ�����
			}
		}
	}

	/**
	 * ͳ��ָ���ļ��Ĵ���/����/ע������
	 * 
	 * @param f
	 *            ָ�����ļ�
	 */
	public void countLine(File f,Code code) {
		BufferedReader br = null; // �������ļ�����
		try {
			br = new BufferedReader(new FileReader(f)); // ʵ���ļ���
			String line = null;
			while ((line = br.readLine()) != null) { // ѭ���ж��Ƿ��ȡ���ļ�ĩβ
				String trim = line.trim();
				if (trim.matches("")) { // ƥ�����
					spaceNum++; // �ܿ���+1
					code.setSpaceNum(code.getSpaceNum()+1); //��Ӧ�ļ�����+1
				} else if (trim.startsWith("#")) {
					spaceNum++;
					code.setSpaceNum(code.getSpaceNum()+1); //��Ӧ�ļ�����+1
				} else if (trim.startsWith("//")) { // ƥ�䵥��ע��
					noteNum++;
					code.setNoteNum(code.getNoteNum()+1);
				} else if (trim.startsWith("/**")) { // ƥ���ı�ע�� ͬ����ע��
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
				} else if (trim.startsWith("/*")) { // ƥ�����ע��
					if (trim.endsWith("*/")) { // �Ƿ�ֻ��һ��
						noteNum++;
						code.setNoteNum(code.getNoteNum()+1);
					} else {
						while ((line = br.readLine()) != null) { // ����ֻ��һ�У�����ű���
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
					if (trim.endsWith("-->")) { // �Ƿ�ֻ��һ��
						noteNum++;
						code.setNoteNum(code.getNoteNum()+1);
					} else {
						while ((line = br.readLine()) != null) { // ����ֻ��һ�У�����ű���
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
				lineNum++; // ������+1
				code.setLineNum(code.getLineNum()+1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close(); // �ر���
			} catch (Exception e) {
			}
		}
	}

	/**
	 * �����¼��������
	 * @param table
	 * @param prefix
	 */
	public List<Code> outMsg(JTable table,String prefix) {
		Vector vName = new Vector();
		vName.add("�ļ�Ŀ¼");
		vName.add("������");
		vName.add("ע����");
		vName.add("������");
		vName.add("������");
		Vector vData = new Vector();
		Vector vRow = new Vector();
		vRow.add(prefix);
		vRow.add(lineNum-spaceNum-noteNum);	//ʵ�ʴ�����=������-ע��-����
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
	 * ������¼���ļ�
	 * @return
	 */
	public void exportToFile(CodeCount codeCount,String prefix,JFrame frame) {
		try {
			// �������
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		JFileChooser fileChooser = new JFileChooser("D:\\CodeCountTestFile"); // �ļ�ѡ����+Ĭ��·��
		fileChooser.setFileFilter(new FileNameExtensionFilter("Excel ������(*.xlsx)", "xlsx")); // ѡ������
		int resultVal = fileChooser.showSaveDialog(frame); // ��ʾѡ����
		if (resultVal == fileChooser.APPROVE_OPTION) { // �ж��Ƿ�ȷ��ѡ���ļ�
			String path = fileChooser.getSelectedFile().toString().split("\\.")[0];
			
			//����Excel���
			createExcel(path,codeCount,prefix);
			
		} else {
			JOptionPane.showMessageDialog(null, "Excel�ļ�δ����!!!", "����", JOptionPane.WARNING_MESSAGE, null);
		}
	}
	
	/**
	 * ����Excel
	 */
	public void createExcel(String path,CodeCount codeCount,String prefix) {
		
		// ͨ�ñ���
		int rowNo = 0, cellNo = 1;
		Row nRow = null; // ͨ���ж���
		Cell nCell = null; // ͨ�õ�Ԫ�����
		
		Workbook wb = new SXSSFWorkbook(500);   //֧��Excel2007+�汾     ��չ��:xlsx  

		//2.����������
		Sheet sheet = wb.createSheet();
		
		sheet.setColumnWidth(cellNo++, 52*256);
		
		cellNo = 1; //����
		
		//3.�����ж���Row
		/*******�����*******************/
		nRow = sheet.createRow(rowNo++);  //��ȡ�ж���
		//4.������Ԫ�����    ��0����
		nCell = nRow.createCell(cellNo);
		//�ϲ���Ԫ��
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 5));
		
		//5.���õ�Ԫ������
		nCell.setCellValue("������ͳ����ϸ��Ϣ");
		nCell.setCellStyle(bigTitle(wb));
		
		/************С����*****************/
		String[] titles = {"�ļ�Ŀ¼","������","ע����","������","������"};
		nRow = sheet.createRow(rowNo++);
		nRow.setHeightInPoints(26.25f);   //�����и�
		for (String s : titles) {
			nCell = nRow.createCell(cellNo++);	//���õ�Ԫ�����
			nCell.setCellValue(s);
			nCell.setCellStyle(title(wb));
		}
		
		//��������
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
		
		//�������
		List<Code> list = codeCount.getFileList();
		for (Code code : list) {
			cellNo = 1;	//����
			
			nRow = sheet.createRow(rowNo++);
			
			//�����ļ�Ŀ¼
			nCell = nRow.createCell(cellNo++);
			nCell.setCellValue(code.getFileDir().replace(prefix, ""));
			nCell.setCellStyle(text(wb));
			
			//���ô�����
			nCell = nRow.createCell(cellNo++);
			nCell.setCellValue(code.getLineNum() - code.getSpaceNum() - code.getNoteNum());
			nCell.setCellStyle(text(wb));
			
			//����ע����
			nCell = nRow.createCell(cellNo++);
			nCell.setCellValue(code.getNoteNum());
			nCell.setCellStyle(text(wb));
			
			//���ÿ�����Ŀ
			nCell = nRow.createCell(cellNo++);
			nCell.setCellValue(code.getSpaceNum());
			nCell.setCellStyle(text(wb));
			
			//����������
			nCell = nRow.createCell(cellNo++);
			nCell.setCellValue(code.getLineNum());
			nCell.setCellStyle(text(wb));
			
		}
		
		//7.����,�ر���
		OutputStream os = null;
		try {
		os = new FileOutputStream(path+".xlsx");
		wb.write(os);  //�������
		} catch (Exception e) {
		
		e.printStackTrace();
		} finally {
		try {
		os.close();		//�ر���
		} catch (Exception e) {}
		}
		
		JOptionPane.showMessageDialog(null, "Excel����ɹ�", "��ʾ", JOptionPane.PLAIN_MESSAGE, null);
		
	}
	
	//��������ʽ
		public CellStyle bigTitle(Workbook wb){
			CellStyle style = wb.createCellStyle();
			Font font = wb.createFont();
			font.setFontName("����");
			font.setFontHeightInPoints((short)20);
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);					//����Ӵ�
			
			style.setFont(font);
			
			style.setAlignment(CellStyle.ALIGN_CENTER);					//�������
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);		//�������
			
			return style;
		}
		//С�������ʽ
		public CellStyle title(Workbook wb){
			CellStyle style = wb.createCellStyle();
			Font font = wb.createFont();
			font.setFontName("����");
			font.setFontHeightInPoints((short)12);
			
			style.setFont(font);
			
			style.setAlignment(CellStyle.ALIGN_CENTER);					//�������
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);		//�������
			
			style.setBorderTop(CellStyle.BORDER_THIN);					//��ϸ��
			style.setBorderBottom(CellStyle.BORDER_THIN);				//��ϸ��
			style.setBorderLeft(CellStyle.BORDER_THIN);					//��ϸ��
			style.setBorderRight(CellStyle.BORDER_THIN);				//��ϸ��
			
			return style;
		}
			
		//������ʽ
		public CellStyle text(Workbook wb){
			CellStyle style = wb.createCellStyle();
			Font font = wb.createFont();
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short)10);
			
			style.setFont(font);
			
			style.setAlignment(CellStyle.ALIGN_LEFT);					//�������
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);		//�������
			
			style.setBorderTop(CellStyle.BORDER_THIN);					//��ϸ��
			style.setBorderBottom(CellStyle.BORDER_THIN);				//��ϸ��
			style.setBorderLeft(CellStyle.BORDER_THIN);					//��ϸ��
			style.setBorderRight(CellStyle.BORDER_THIN);				//��ϸ��
			
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