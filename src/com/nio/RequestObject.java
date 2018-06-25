package com.nio;

import java.io.Serializable;
import java.util.Arrays;

public class RequestObject implements Serializable{

	
	private static final long serialVersionUID = 7567397174801208054L;
	/** �ļ������� */  
	private int nameLength;
	/** �ļ��� */ 
	private byte[] fileName;
	/** �ļ����� */
	private int contentLength;
	/** �ļ����� */ 
	private byte[] contents;
    
	public RequestObject () {}
	
	public RequestObject (int nameLength, byte[] fileName, int contentLength, byte[] contents) {
		this.nameLength = nameLength;
		this.fileName = fileName;
		this.contentLength = contentLength;
		this.contents = contents;
	}
	
	public int getNameLength() {
		return nameLength;
	}

	public void setNameLength(int nameLength) {
		this.nameLength = nameLength;
	}

	public byte[] getFileName() {
		return fileName;
	}

	public void setFileName(byte[] fileName) {
		this.fileName = fileName;
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public byte[] getContents() {
		return contents;
	}

	public void setContents(byte[] contents) {
		this.contents = contents;
	}
	
	@Override
	public String toString() {
		return "[ nameLength : " + nameLength
				+" , fileName : " + Arrays.toString(fileName)
				+" , contentLength : " + contentLength
				+" , contents : " + contents +"]";
	}
	
}