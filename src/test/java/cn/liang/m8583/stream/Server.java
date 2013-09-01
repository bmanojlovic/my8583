package cn.liang.m8583.stream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @author 325336, Liang Yabao
 * 2012-4-5
 */
public class Server {

	public static void main(String[] args) throws IOException{
		ServerSocket ss = new ServerSocket(2500);
		Socket socket = ss.accept();
		InputStream is = socket.getInputStream();
		int i =99;
		while((i= is.read())!=-1){
			System.out.print(i+" ");
		}
		is.close();
		socket.close();
		ss.close();
	}
}
