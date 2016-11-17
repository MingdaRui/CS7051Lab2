import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	BufferedReader reader;
	PrintWriter writer;
	
	public static void main(String[] args) {		
		Client client = new Client();
		client.go();	
	}
	
	public void go() {
		try {
			Socket sock = new Socket("localhost", 9312);
			
			writer = new PrintWriter(sock.getOutputStream());
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);
			String test = reader.readLine();
			System.out.println("Client-Server connection test: " + test);
			
			Scanner sc = new Scanner(System.in);
			
			
			Thread readerThread = new Thread(new IncomingReader());
			readerThread.start();
			
			
			while(true) {
				
				System.out.println("Please input the message");
				String msg = sc.nextLine();	
				writer.println(msg);
				writer.flush();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public class IncomingReader implements Runnable {
		public void run() {
			String incomingMsg;
			try {
				
				while ((incomingMsg = reader.readLine()) != null) {
					System.out.println(incomingMsg);
					
				} // close while
			} catch (Exception ex) {ex.printStackTrace();}
		} // close run
	} // close inner class
}
