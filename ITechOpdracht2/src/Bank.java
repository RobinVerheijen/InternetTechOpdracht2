import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Bank {

	public static void main(String argv[]) throws Exception {
		
		String clientSentence;
		
		ServerSocket welcomeSocket = new ServerSocket(8080);
		while(true) {
			Socket connectionSocket = welcomeSocket.accept();
			
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			
			
			clientSentence = inFromClient.readLine();
			
			System.out.println(clientSentence);
			
//			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

			PrintWriter writer = new PrintWriter(connectionSocket.getOutputStream(), true);
			writer.println(clientSentence.toUpperCase());
//			outToClient.writeBytes(clientSentence.toUpperCase());
			
			//connectionSocket.close();
		}
		
	}
	
}
