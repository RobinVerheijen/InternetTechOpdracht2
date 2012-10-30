import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;


public class Automaat {

	private static final int id = 1;
	private String address;
	
	public static void main(String[] args) throws Exception {
		
		String sentence;
		
		String pasnummer;
		
		System.out.println("Vul uw pasnummer in: ");
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		Socket clientSocket = new Socket("localhost",8080);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
		//pasnummer uitlezen
		pasnummer = inFromUser.readLine();
		
		System.out.println(pasnummer);
		//voordat het pasnummer wordt opgestuurd de handshake uitvoeren
		
		//vragen aan de bank of het wel de bank is
		outToServer.writeBytes("bankcontrole" + "\n");
		
		//reactie van de bank uitlezen, als het het juiste id is stuurt de automaat de informatie op
				
		sentence = inFromServer.readLine();
		String[] bankcheck = sentence.split(" ");
		
		System.out.println(bankcheck[0]);
		System.out.println(bankcheck[1]);

		if(bankcheck[0].equals("bankid"))	{
			if(bankcheck[1].equals("2"))	{
				outToServer.writeBytes("automaatid "+ id + "\n");
			}
		}
		
		String authenticatie = inFromServer.readLine();
		System.out.println("AUTHENTICATIE" + authenticatie);
		
		if(authenticatie.equals("1"))	{
			System.out.println("authenticatie correct");
			outToServer.writeBytes("pasnummer " + pasnummer + "\n");
		} else if(authenticatie.equals("2"))	{
			System.out.println("authenticatie fout");
		}
		
		String[] pasnummerAuthenticatie = inFromServer.readLine().split(" ");
		
		System.out.println(pasnummerAuthenticatie[0]);
		System.out.println(pasnummerAuthenticatie[1]);
		
		if(pasnummerAuthenticatie[0].equals("pasnummer"))	{
			if(pasnummerAuthenticatie[1].equals("1"))	{
				System.out.println("pincode: ");
			} else	{
				System.out.println("fout pasnummer, de verbinding wordt verbroken");
				clientSocket.close();
			}
		}

		
		
		
		
	}
}
