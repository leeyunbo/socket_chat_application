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
				while(true) { //언제 들어올지 모르는 새로운 접속자를 위하여 종료되지 않도록 한다.
					System.out.println("사용자 접속 대기");
					Socket socket = server.accept(); // 사용자가 들어올때 까지 대기, 들어오면 소켓 생성
					System.out.println("사용자가 접속하였습니다.");
					NickNameThread thread = new NickNameThread(socket); //해당 쓰레드는 계속 사용자를 기다려야 하므로 새로운 스레드로 넘김
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
				//닉네임 수신 
				dos.writeUTF(nickName+" 님 환영합니다.");
				// 환영 메시지를 전달한다.
				sendToClient("서버 : " + nickName +"님이 접속하였습니다.");
				// 접속되어있는 사용자들에게 메시지를 뿌린다. 
				UserClass user = new UserClass(nickName, socket); 
				// 사용자 정보를 관리하는 객체를 생성한다.
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
		
		public UserClass(String nickName, Socket socket) { //유저 리스트
			try {// 유저의 메시지를 계속 수신하기 위한 대기상태에 있어야 하므로 Thread 클래스를 상속받게 구현 
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
						//클라이언트에게 메시지를 수신받는다.
						String msg = dis.readUTF();
						// 사용자들에게 메시지를 전달한다. 
						sendToClient(nickName+ " : " + msg);
					}
					
				} catch (Exception e) { e.printStackTrace();}
			}
		
		
	}
	
	public synchronized void sendToClient(String msg) { //동기화 처리 함수 
		try {
			for (UserClass user : user_list) { // 채팅 유저 목록의 유저들에게 전달 
				user.dos.writeUTF(msg); // 각 유저의 고유의 스트림을 통해 전달한다.
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
