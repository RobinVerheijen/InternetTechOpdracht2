import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class Automaat {

	private static final int id = 10;
	private String address;
	private static BufferedReader inFromUser;
	private static DataOutputStream outToServer;
	private static BufferedReader inFromServer;
	private static Socket clientSocket;
	private static String pasnummer;


	public static void main(String[] args) throws Exception {

		String sentence;

		String pincode = null;

		System.out.println("Vul uw pasnummer in: ");
		inFromUser = new BufferedReader(new InputStreamReader(System.in));
		clientSocket = new Socket("localhost",8080);
		outToServer = new DataOutputStream(clientSocket.getOutputStream());
		inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		//pasnummer uitlezen
		pasnummer = inFromUser.readLine();

		//voordat het pasnummer wordt opgestuurd de handshake uitvoeren

		//vragen aan de bank of het wel de bank is
		outToServer.writeBytes("bankcontrole" + "\n");

		//reactie van de bank uitlezen, als het het juiste id is stuurt de automaat de informatie op

		sentence = inFromServer.readLine();
		String[] bankcheck = sentence.split(" ");

		if(bankcheck[0].equals("bankid"))	{
			if(bankcheck[1].equals("20"))	{
				outToServer.writeBytes("automaatid "+ id + "\n");
			} else {
				System.out.println("Fout met bankconnectie!");
				clientSocket.close();
			}
		}

		String authenticatie = inFromServer.readLine();

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
				pincode = inFromUser.readLine();
				outToServer.writeBytes("pincode " + pasnummer + " " + pincode + "\n");
			} else	{
				System.out.println("fout pasnummer, de verbinding wordt verbroken");
				clientSocket.close();
			}
		}
		
		String[] pincodeAuthenticatie = inFromServer.readLine().split(" ");
		pinCodeCheck(pincodeAuthenticatie);

		
		String keuze = inFromUser.readLine();
		String choice = checkChoice(keuze);
		if (choice.equals("2")) {
			System.out.println("Sessie geslaagd, prettige dag verder");
			clientSocket.close();
		} else {
			System.out.println("Wat wilt u nu doen?\n" +
					"1. Geld opnemen\n" +
					"2. Stoppen");
			keuze = inFromUser.readLine();
			checkChoice2(keuze);
			clientSocket.close();
		}
	}

	private static String checkChoice(String choice) throws IOException	{
		if(choice.equals("1"))	{
			outToServer.writeBytes("saldo\n");

			String saldo = inFromServer.readLine();

			System.out.println("Uw huidige saldo bedraagt: " + saldo);


		} else if (choice.equals("2"))	{
			opnemen();
		} else	{
			System.out.println("Onjuiste keuze, maak een keuze uit 1 of 2");
			choice = inFromUser.readLine();
			checkChoice(choice);
		}
		return choice;
	}

	private static void checkChoice2(String choice) throws IOException	{
		if(choice.equals("1"))	{
			opnemen();
			System.out.println("Sessie be‘indigd, prettige dag verder");
		} else if (choice.equals("2"))	{
			System.out.println("Sessie be‘indigd, prettige dag verder");
		} else	{
			System.out.println("Onjuiste keuze, maak een keuze uit 1 of 2");
			choice = inFromUser.readLine();
			checkChoice2(choice);
		}
	}
	
	private static void opnemen() throws IOException {
		System.out.println("Welk bedrag wilt u opnemen: ");
		String opname = inFromUser.readLine();

		outToServer.writeBytes("opname " + opname + "\n");

		String opnameStatus = inFromServer.readLine();
		String[] opnameStatusSplit = opnameStatus.split(" ");

		if (opnameStatusSplit[1].equals("true")){
			System.out.println("Geslaagd");
		} else {
			System.out.println("Te weinig saldo!, Sessie wordt be‘indigd.");
		}
	}
	private static void pinCodeCheck(String[] pincodeAuthenticatie) throws IOException	{

		System.out.println(pincodeAuthenticatie[0]);
		System.out.println(pincodeAuthenticatie[1]);

		if(pincodeAuthenticatie[0].equals("pincode"))	{
			if(pincodeAuthenticatie[1].equals("1"))	{
				System.out.println("Maak uw keuze: \n" +
						"1. Saldo opvragen\n" +
						"2. Geld opnemen");
			} else	if(pincodeAuthenticatie[1].equals("3")){
				String resterendePogingen = pincodeAuthenticatie[3];
				System.out.println("Onjuiste pincode, u heeft " + resterendePogingen + " pogingen over, probeer het opnieuw\n" +
						"Pincode: ");
				String pincode = inFromUser.readLine();
				outToServer.writeBytes("pincode " + pasnummer + " " + pincode + "\n");
				pinCodeCheck(inFromServer.readLine().split(" "));
			} else if(pincodeAuthenticatie[1].equals("2")) {
			System.out.println("foute pincode, de verbinding wordt verbroken");
			clientSocket.close();
			}
		}	
	}
}

