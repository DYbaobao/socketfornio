package com.nio;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NioServerHandler {

    private final static String DIRECTORY = "f:/sat";
    /**
     * ������պͷ���
     */
    public void excute (ServerSocketChannel serverSocketChannel) {
    	SocketChannel socketChannel = null;
    	try {
			socketChannel = serverSocketChannel.accept();
			RequestObject  requestObject = receiveData(socketChannel);
			writeFile(DIRECTORY, requestObject);
			String response = "File" + new String(requestObject.getFileName()) + " SUCCESS......";
			responseData(socketChannel, response);
			
		} catch (Exception e) {
			
		}
    }
    /** 
     * <p>��ȡͨ���е����ݵ�Object��ȥ</p> 
     *  
     * @param socketChannel 
     * @return 
     * @throws Exception 
     */  
    public RequestObject receiveData (SocketChannel socketChannel) throws Exception {
    	// �ļ�������  
    	int nameLength = 0;
    	// �ļ��� 
    	byte[] fileName = null; // ����ļ����ֽ����鷢�������Ǿ��޴ӵ�֪�ļ����������ļ���һ�𷢹���  
    	// �ļ�����
    	int contentLength = 0;
    	// �ļ�����
    	byte[] contents = null;
    	// ���ڽ���ʱǰ4���ֽ����ļ�������
    	int capacity = 4; 
    	ByteBuffer buf = ByteBuffer.allocate(capacity);
    	
    	int size = 0;
    	byte[] bytes = null;
    	
    	size = socketChannel.read(buf);
    	if (size > 0) {
    		buf.flip();
    		capacity = buf.getInt();
    		buf.clear();
    		nameLength = capacity;
    	}
    	
    	// ���ļ���
    	buf = ByteBuffer.allocate(capacity);
    	size = socketChannel.read(buf);
    	if (size >= 0) {
    		buf.flip();
    		bytes = new byte[size];
    		buf.get(bytes);
    		fileName = bytes;
    		buf.clear();
    	}
    	
    	// �õ��ļ����� 
    	capacity = 4;
    	buf = ByteBuffer.allocate(capacity);
    	size = socketChannel.read(buf);
    	if (size >= 0) {
    		buf.flip();
    		contentLength = buf.getInt();
    		buf.clear();
    	}
    	
    	// ���ڽ���buffer�е��ֽ����� 
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // �ļ����ܻ�ܴ�  
    	capacity = 1024;
    	buf = ByteBuffer.allocate(capacity);
    	while ((size = socketChannel.read(buf)) >= 0) {
    		buf.flip();
    		bytes = new byte[size];
    		buf.get(bytes);
    		baos.write(bytes);
    		buf.clear();
    	}
    	contents = baos.toByteArray();
    	
    	RequestObject requestObject = new RequestObject(nameLength,fileName,contentLength,contents);
		
    	return requestObject;	
    }
    
    /** 
     * �ѽ��յ�����д�������ļ��� 
     *  
     * @param drc �����ʼ�Ŀ¼ 
     * @param requestObject   
     * @throws Exception  
     */ 
    public static void writeFile  (String drc,RequestObject requestObject) throws Exception {
    	File file = new File(drc + File.separator + new String(requestObject.getFileName(),"utf-8"));
    	FileOutputStream os = null;
    	try {
			os = new FileOutputStream(file);
			os.write(requestObject.getContents());
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
    	
    }
   
    /** 
     * ���ǽ������ݳɹ���Ҫ���ͻ���һ������ 
     * �����java�Ŀͻ������ǿ���ֱ�Ӹ�����,���������û��Ǹ��ı����ֽ����� 
     * @param socketChannel 
     * @param response 
     */  
    private void responseData (SocketChannel socketChannel,String response) {
    	ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
    	try {
			socketChannel.write(buffer);
			buffer.clear();
			// ȷ��Ҫ���͵Ķ����������˹ر�output ��Ȼ���˽���ʱsocketChannel.read(Buffer)  
            // �ܿ���������� �����԰������L��ע�͵����ᷢ�ֿͻ���һֱ�ȴ��������� 
			socketChannel.socket().shutdownOutput(); // (L)
		} catch (Exception e) {
		     e.printStackTrace();
		}
    }
    
}
