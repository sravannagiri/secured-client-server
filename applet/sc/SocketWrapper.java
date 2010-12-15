//package scsa.applet;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

class SocketWrapper {

	private Socket socket;
	
	private BufferedWriter writer;
	
	public SocketWrapper(Socket s) {
		this.socket = s;
		try {
			
			this.writer = new BufferedWriter( 
					new OutputStreamWriter( socket.getOutputStream() ) );
			
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("[SocketWrapper ERROR]: " + 
					e.getMessage());
		}
	}
	
	public void write(String s) {
		try {
			
			byte[] b = s.getBytes("ISO-8859-1");
			
			socket.getOutputStream().write(b, 0, s.length());

			this.writer.flush();

		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("[SocketWrapper ERROR]: " + 
					e.getMessage());
		}
	}
	
	public String read() {
		
		String line = null;
		try {
			
			byte b[] = new byte[2048];
			
			int len = socket.getInputStream().read(b, 0, 2048);
			line = (new String(b, "ISO-8859-1")).substring(0, len);
			
			//System.out.println("bytes read: " + len + " (" + line.length() + ")");
				
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("[SocketWrapper ERROR]: " + 
					e.getMessage());
		}

		return line;
	}
}
