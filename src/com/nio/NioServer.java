package com.nio;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;



public class NioServer {
	Selector selector = null;
	ServerSocketChannel serverSocketChannel = null;
	private NioServerHandler handler;
	
	public NioServer () throws Exception {
		selector = Selector.open();
		// �򿪷������׽���ͨ��  
		serverSocketChannel = ServerSocketChannel.open();
		
		// ����ͨ��������ģʽ������ 
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.socket().setReuseAddress(true);
		serverSocketChannel.socket().bind(new InetSocketAddress(9999));
		
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		
	}
	
	public NioServer (NioServerHandler handler) throws Exception {
		this();
		this.handler = handler;
		
		while (selector.select() > 0) {
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while (it.hasNext()) {
				SelectionKey selectionKey = it.next();
				it.remove();
			    this.handler.excute((ServerSocketChannel)selectionKey.channel());
			}
		}
	}
	
	
	public static void main(String[] args) throws Exception {
	     new NioServer(new NioServerHandler());
	}

}
