package lab2;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

class NewClient extends Thread{
	Socket socket;
	BufferedReader reader;
	public NewClient(Socket sock) {
		socket = sock;
	} // close constructor
	
	@Override
	public void run() {
		String message;
		String hello;
		
		try {		
			InetAddress addr = InetAddress.getLocalHost();
			String ip=addr.getHostAddress().toString();
					
			hello = "HELO text\nIP: " + ip + 
					"\nPort: " + socket.getPort() + 
					"\nStudentID: 15067025";
			
			InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
			reader = new BufferedReader(streamReader);
			
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			String test = "succeed";
			writer.println(test);
			writer.flush();
			System.out.println("Establish the connection");
			
			while((message = reader.readLine())!=null) {
				if(message.equals("KILL_SERVICE")) {
					System.out.println("Closing socket" + socket.getPort());
					socket.close();
				} else if (message.equals("HELO text")) {
					System.out.println(hello);
					writer.println(hello);
					writer.flush();
				} else
					System.out.println("Get message back from client:" + message);
			}
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("There is an exception on Port:" + socket.getPort());
		}
	}
} // close inner class NewClient

public class Server {
	
	public static void main(String[] args) {
		try {
			
			ServerSocket serverSock = new ServerSocket(9312);
			ExecutorService pool = new ThreadPoolExecutor (0, 50, 
					60L, TimeUnit.SECONDS, 
					new SynchronousQueue<Runnable>(), 
					//new ArrayBlockingQueue<Runnable>(2),
					//new NoBlockingQueue<Runnable>(0), 
					new ThreadPoolExecutor.DiscardPolicy());
			System.out.println("create the pool");
			while (true) {
				Socket sock = serverSock.accept();
				Thread readerThread = new NewClient(sock);
				pool.execute(readerThread);
	
			}
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	} // close main method	
} // close public class Server
