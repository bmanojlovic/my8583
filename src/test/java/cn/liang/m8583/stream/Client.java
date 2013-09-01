package cn.liang.m8583.stream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;


/**
 * @author 325336, Liang Yabao
 * 2012-4-5
 */
public class Client {

	
	public static void main(String[] args) throws IOException, InterruptedException{
		byte[] bs = "test".getBytes();
		byte[] bb = new byte[1000000000];
		Socket socket = new Socket("10.79.6.11",9999);
		OutputStream os = socket.getOutputStream();
		for(int i =0;i<20;i++){
			os.write("chenxihan".getBytes());
			os.write(" ".getBytes());
			os.write(bs);
			os.write(" ".getBytes());
			os.flush();
			Thread.sleep(10);
		}
		//os.close();
		//socket.close();
		while(true){
			Thread.sleep(2000);
		}
	}
	
}
