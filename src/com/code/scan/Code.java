package com.code.scan;

/**
 * 每个统计文件的对象,用于记录每一个文件中的统计量
 *
 * @author Ranger
 */
public class Code {

	private int lineNum = 0;	//代码总行数
	private int spaceNum = 0;	//空行数目
	private int noteNum = 0;	//注释数目
	private String fileDir = null;	//文件目录
	
	
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
