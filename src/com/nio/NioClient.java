package com.nio;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

public class NioClient {

	public static void main(String[] args) {
		new Thread(new MyRunnable(new NioClientHandler())).start();
	}
    
	private static final class MyRunnable implements Runnable {
        private static final String FILEPATH = "f:/oracle.zip";
        
        private NioClientHandler handler;
        private MyRunnable (NioClientHandler handler) {
			this.handler = handler;
		}
		@Override
		public void run() {
			SocketChannel socketChannel = null;
			
			try {
				// 和ServerSocket连接与传统的socket类似  
                // 无非就是换成了套接字通道 
				socketChannel = SocketChannel.open();
				SocketAddress socketAddress = new InetSocketAddress(InetAddress.getLocalHost(),9999);
				socketChannel.connect(socketAddress);
				long start = System.currentTimeMillis();
				// 发送数据  
				handler.sendData(socketChannel,FILEPATH , "oracle.zip");
				// 接收服务器反馈的信息
				String response = handler.receiveDate(socketChannel);
				
				long end = System.currentTimeMillis();
				System.out.println("用时" + (end - start) + "ms");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					socketChannel.close();
				} catch (Exception ex) {
				     ex.printStackTrace();
				}
			}
			
		}
		
	}
	
}
