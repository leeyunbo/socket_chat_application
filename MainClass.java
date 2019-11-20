package server;

import java.net.ServerSocket;
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

}
