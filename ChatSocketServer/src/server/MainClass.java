package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class MainClass {
	
	private ServerSocket server;
	
	ArrayList<UserClass> user_list;
	
	public static void main(String[] args) {
		new MainClass();
	}
	
	public MainClass() {
		try {
			user_list = new ArrayList<UserClass>();
			server = new ServerSocket(30000);
			ConnectionThread thread = new ConnectionThread();
			thread.start();
			
		} catch(Exception e) { e.printStackTrace();};
	}
	
	class ConnectionThread extends Thread {
		@Override
		public void run() {
			try {
				while(true) { //���� ������ �𸣴� ���ο� �����ڸ� ���Ͽ� ������� �ʵ��� �Ѵ�.
					System.out.println("����� ���� ���");
					Socket socket = server.accept(); // ����ڰ� ���ö� ���� ���, ������ ���� ����
					System.out.println("����ڰ� �����Ͽ����ϴ�.");
					NickNameThread thread = new NickNameThread(socket); //�ش� ������� ��� ����ڸ� ��ٷ��� �ϹǷ� ���ο� ������� �ѱ�
					thread.start();
				}
			} catch(Exception e) { e.printStackTrace();}
		}
	}
	
	class NickNameThread extends Thread {
		private Socket socket;
		
		public NickNameThread(Socket socket) {
			this.socket = socket; 
		}
		
	public void run() {
		try {
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				DataInputStream dis = new DataInputStream(is);
				DataOutputStream dos = new DataOutputStream(os);
			
				String nickName = dis.readUTF();
				//�г��� ���� 
				dos.writeUTF(nickName+" �� ȯ���մϴ�.");
				// ȯ�� �޽����� �����Ѵ�.
				sendToClient("���� : " + nickName +"���� �����Ͽ����ϴ�.");
				// ���ӵǾ��ִ� ����ڵ鿡�� �޽����� �Ѹ���. 
				UserClass user = new UserClass(nickName, socket); 
				// ����� ������ �����ϴ� ��ü�� �����Ѵ�.
				user.start();
				user_list.add(user);
			 } catch(Exception e) {e.printStackTrace();}
		}
	}
	
	class UserClass extends Thread {
		String nickName;
		Socket socket; 
		DataInputStream dis;
		DataOutputStream dos; 
		
		public UserClass(String nickName, Socket socket) { //���� ����Ʈ
			try {// ������ �޽����� ��� �����ϱ� ���� �����¿� �־�� �ϹǷ� Thread Ŭ������ ��ӹް� ���� 
				this.nickName = nickName;
				this.socket = socket; 
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				dis = new DataInputStream(is);
				dos = new DataOutputStream(os);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			
			public void run() {
				try {
					while(true) {
						//Ŭ���̾�Ʈ���� �޽����� ���Ź޴´�.
						String msg = dis.readUTF();
						// ����ڵ鿡�� �޽����� �����Ѵ�. 
						sendToClient(nickName+ " : " + msg);
					}
					
				} catch (Exception e) { e.printStackTrace();}
			}
		
		
	}
	
	public synchronized void sendToClient(String msg) { //����ȭ ó�� �Լ� 
		try {
			for (UserClass user : user_list) { // ä�� ���� ����� �����鿡�� ���� 
				user.dos.writeUTF(msg); // �� ������ ������ ��Ʈ���� ���� �����Ѵ�.
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
