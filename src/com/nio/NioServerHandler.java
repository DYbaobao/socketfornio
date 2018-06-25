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
     * 处理接收和发送
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
     * <p>读取通道中的数据到Object里去</p> 
     *  
     * @param socketChannel 
     * @return 
     * @throws Exception 
     */  
    public RequestObject receiveData (SocketChannel socketChannel) throws Exception {
    	// 文件名长度  
    	int nameLength = 0;
    	// 文件名 
    	byte[] fileName = null; // 如果文件以字节数组发来，我们就无从得知文件名，所以文件名一起发过个  
    	// 文件长度
    	int contentLength = 0;
    	// 文件内容
    	byte[] contents = null;
    	// 由于解析时前4个字节是文件名长度
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
    	
    	// 拿文件名
    	buf = ByteBuffer.allocate(capacity);
    	size = socketChannel.read(buf);
    	if (size >= 0) {
    		buf.flip();
    		bytes = new byte[size];
    		buf.get(bytes);
    		fileName = bytes;
    		buf.clear();
    	}
    	
    	// 拿到文件长度 
    	capacity = 4;
    	buf = ByteBuffer.allocate(capacity);
    	size = socketChannel.read(buf);
    	if (size >= 0) {
    		buf.flip();
    		contentLength = buf.getInt();
    		buf.clear();
    	}
    	
    	// 用于接收buffer中的字节数组 
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 文件可能会很大  
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
     * 把接收的数据写到本地文件里 
     *  
     * @param drc 本地问价目录 
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
     * 我们接受数据成功后要给客户端一个反馈 
     * 如果是java的客户端我们可以直接给对象,如果不是最好还是给文本或字节数组 
     * @param socketChannel 
     * @param response 
     */  
    private void responseData (SocketChannel socketChannel,String response) {
    	ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
    	try {
			socketChannel.write(buffer);
			buffer.clear();
			// 确认要发送的东西发送完了关闭output 不然它端接收时socketChannel.read(Buffer)  
            // 很可能造成阻塞 ，可以把这个（L）注释掉，会发现客户端一直等待接收数据 
			socketChannel.socket().shutdownOutput(); // (L)
		} catch (Exception e) {
		     e.printStackTrace();
		}
    }
    
}
