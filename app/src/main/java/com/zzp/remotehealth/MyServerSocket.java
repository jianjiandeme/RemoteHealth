package com.zzp.remotehealth;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.Key;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by zzp on 2017/11/8.
 */

public class MyServerSocket {
    String TAG = "serverSocket";

    private int[] ports = {8000,8001,8002,8003,8004,8005};


    public void openServer() throws Exception{
        //打开服务器
    Selector selector = Selector.open();
    for (int i :ports){
        ServerSocketChannel initSer = ServerSocketChannel.open();
        initSer.configureBlocking(false);
        ServerSocket initSock = initSer.socket();
        //绑定监听端口
        InetSocketAddress address = new InetSocketAddress(i);
        initSock.bind(address);

    }
        while (selector.select() > 0){
            //取出全部Key
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            //迭代全部key
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                if(key.isAcceptable()){
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);

                    ByteBuffer inBuf = ByteBuffer.allocateDirect(1024*4);

                    int read = client.read(inBuf);
                    if(read!= 0){
                        
                    }
                    ByteBuffer outBuf = ByteBuffer.allocateDirect(1024*4);

                    outBuf.put(("当前时间为"+new Date()).getBytes());
                    //重置缓冲区
                    outBuf.flip();
                    //输出信息
                    client.write(outBuf);
                    client.close();
                }

                selectionKeys.clear();
            }
        }
    }
}
