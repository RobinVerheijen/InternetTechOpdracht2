import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Het automaat. Client. 
 *
 */
public class Automaat {

	private static final int id = 10;
	private static BufferedReader inFromUser;
	private static DataOutputStream outToServer;
	private static BufferedReader inFromServer;
	private static Socket clientSocket;
	private static String pasnummer;

	/**
	 * uitvoerende code, kijk in code voor meer info.
	 * @param argv
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		String sentence;
		String pincode = null;

		//user vragen om pincode
		System.out.println("Vul uw pasnummer in: ");
		inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		//verbinding, reader en writer openen
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

		// als bank = bank en automaat = automaat goed is, pasnummer opsturen
		if(authenticatie.equals("1"))	{
			outToServer.writeBytes("pasnummer " + pasnummer + "\n");
		} else if(authenticatie.equals("2"))	{
			System.out.println("authenticatie fout!");
		}

		//controle of pas goed is, en om pincode vragen als goed is.
		String[] pasnummerAuthenticatie = inFromServer.readLine().split(" ");

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
		
		//contole of pincode goed is.
		String[] pincodeAuthenticatie = inFromServer.readLine().split(" ");
		pinCodeCheck(pincodeAuthenticatie);

		//als goed is, user vragen of hij geld wil opnemen of saldo wil bekijken.
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

	/**
	 * aan de hand van de keuze van de user, het saldo weergeven, of geld opnemen.
	 * @param choice String keuze
	 * @return String keuze
	 * @throws IOException
	 */
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

	/**
	 * als saldo is opgevraagd, nogmaals vragen of user wil opnemen of niet.
	 * @param choice String keuze
	 * @throws IOException
	 */
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
	
	/**
	 * User vragen hoeveel hij wil opnemen, en dat uitvoeren. foutmelding indien niet genoeg saldo.
	 * @throws IOException
	 */
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
	
	/**
	 * methode om bij te houden wat er met het aantal pincode pogingen gebeurt.
	 * @param pincodeAuthenticatie String[] gesplitte reactie van bank.
	 * @throws IOException
	 */
	private static void pinCodeCheck(String[] pincodeAuthenticatie) throws IOException	{

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

