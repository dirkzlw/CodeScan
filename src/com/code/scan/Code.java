package com.code.scan;

/**
 * ÿ��ͳ���ļ��Ķ���,���ڼ�¼ÿһ���ļ��е�ͳ����
 *
 * @author Ranger
 */
public class Code {

	private int lineNum = 0;	//����������
	private int spaceNum = 0;	//������Ŀ
	private int noteNum = 0;	//ע����Ŀ
	private String fileDir = null;	//�ļ�Ŀ¼
	
	
	public int getLineNum() {
		return lineNum;
	}
	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}
	public int getSpaceNum() {
		return spaceNum;
	}
	public void setSpaceNum(int spaceNum) {
		this.spaceNum = spaceNum;
	}
	public int getNoteNum() {
		return noteNum;
	}
	public void setNoteNum(int noteNum) {
		this.noteNum = noteNum;
	}
	public String getFileDir() {
		return fileDir;
	}
	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}
	
}
