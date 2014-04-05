package cn.sjtu.edu.se.kvstore;

/**
 * @author Francis
 * @version 0.0.1
 *
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class TestClient {
	public static final String IP_ADDR = "127.0.0.1";// 服务器地址
	public static final int PORT = 8888;// 服务器端口号

	public static void testReqs() {
		Random rand = new Random();
		for (int i = 0; i < 1200; i++) {
			Socket socket = null;
			try {
				// 创建一个流套接字并将其连接到指定主机上的指定端口号
				socket = new Socket(IP_ADDR, PORT);

				// 读取服务器端数据
				DataInputStream input = new DataInputStream(
						socket.getInputStream());
				// 向服务器端发送数据
				DataOutputStream out = new DataOutputStream(
						socket.getOutputStream());

				int j = rand.nextInt(100);
				out.writeUTF("get " + j);
				System.out.println("get " + j);

				String ret = input.readUTF();
				System.out.println("receive message: " + ret);

				if ("OK".equals(ret)) {
					System.out.println("客户端将关闭连接");
					Thread.sleep(500);
					break;
				}

				Thread.sleep(1000);

				out.close();
				input.close();
			} catch (Exception e) {
				System.out.println("客户端异常:" + e.getMessage());
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
						socket = null;
						System.out.println("客户端 finally 异常:" + e.getMessage());
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		System.out.println("客户端启动...");
		System.out.println("当接收到服务器端字符为 \"OK\" 的时候, 客户端将终止\n");

		testReqs();

	}
}