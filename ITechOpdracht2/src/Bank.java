import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Bank {
	
	private static final int id = 2;

	public static void main(String argv[]) throws Exception {
		
		String clientSentence;
		
		ServerSocket welcomeSocket = new ServerSocket(8080);
		while(true) {
			Socket connectionSocket = welcomeSocket.accept();
			
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			
			PrintWriter writer = new PrintWriter(connectionSocket.getOutputStream(), true);

			//Uitlezen wat de automaat heeft opgestuurd
			String bankcontrole = inFromClient.readLine();
			
			System.out.println(bankcontrole);
			//Als de automaat om een "bankcontrole" heeft gevraagd stuurt de bank zijn id terug
			if(bankcontrole.equals("bankcontrole"))	{
				writer.println("bankid "+ id);
			}
			
			//Als het id van de bank is goedegekeurd stuurt de automaat zijn id op
			String automaatcontrole = inFromClient.readLine();

			String[] sentence = automaatcontrole.split(" ");
			
			System.out.println(sentence[0]);
			System.out.println(sentence[1]);
			if(sentence[0].equals("automaatid"))	{
				if(sentence[1].equals("1"))	{
					clientSentence = "1";
					writer.println("1");
				} else	{
					clientSentence = "2";
					writer.println("2");
				}
			}
			
			String pasnummer = inFromClient.readLine();
			
			System.out.println(pasnummer);
			String[] automaatpasnummer = pasnummer.split(" ");
			
			System.out.println(automaatpasnummer[0]);
			System.out.println(automaatpasnummer[1]);
			
			if(automaatpasnummer[0].equals("pasnummer"))	{
				if(!automaatpasnummer[1].equals("123456789"))	{
					writer.println("pasnummer " + 1);
				}
			}
			
			
//			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());


//			outToClient.writeBytes(clientSentence.toUpperCase());
			
			//connectionSocket.close();
		}
		
	}
	
}
