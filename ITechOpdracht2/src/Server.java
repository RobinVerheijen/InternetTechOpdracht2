
import java.io.*;
import java.net.*;

public class Server {

	public static void main(String[] args) throws Exception {
		ServerSocket welcomeSocket = new ServerSocket(8080);

		while(true)	{
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(
					new InputStreamReader(connectionSocket.getInputStream()));
			String s = null;	
			s = inFromClient.readLine();
			String sentence = s;  
			String[] words = sentence.split(" ");  

			DataOutputStream outToClient = new DataOutputStream(
					connectionSocket.getOutputStream());

			File file;
		}
	}
}

