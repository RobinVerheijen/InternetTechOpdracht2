import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * De bank. Server.
 *
 */
public class Bank {

	private static final int id = 20;
	private static ArrayList<Rekening> rekeningen;
	private static Rekening werkRekening = null;
	private static PrintWriter writer;
	private static int resterendePogingen;
	private static BufferedReader inFromClient;
	private static Socket connectionSocket;

	/**
	 * uitvoerende code, kijk in de code voor meer info.
	 * @param argv
	 * @throws Exception
	 */
	public static void main(String argv[]) throws Exception {

		//Lijst met bankrekeningen + testrekening aanmaken
		rekeningen = new ArrayList<Rekening>();
		Rekening rekening = new Rekening("123456789", "9876", 500.0);
		rekeningen.add(rekening);
		
		//Resterende pogingen voor pincode.
		resterendePogingen = 3;

		//Socket van de server
		ServerSocket welcomeSocket = new ServerSocket(8080);

		while(true) {
			
			//connectie openen, en de reader en writer.
			connectionSocket = welcomeSocket.accept();
			inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			writer = new PrintWriter(connectionSocket.getOutputStream(), true);

			//Uitlezen wat de automaat heeft opgestuurd
			String bankcontrole = inFromClient.readLine();

			//Als de automaat om een "bankcontrole" heeft gevraagd stuurt de bank zijn id terug
			if(bankcontrole.equals("bankcontrole"))	{
				writer.println("bankid "+ id);
			}

			//Als het id van de bank is goedegekeurd stuurt de automaat zijn id op en wordt nagekeken.
			String automaatcontrole = inFromClient.readLine();
			String[] sentence = automaatcontrole.split(" ");

			if(sentence[0].equals("automaatid"))	{
				if(sentence[1].equals("10"))	{
					writer.println("1");
				} else	{
					writer.println("2");
					writer.close();
					inFromClient.close();
					connectionSocket.close();
					break;
				}
			}

			//pasnummer ontvangen en nakijken
			String pasnummer = inFromClient.readLine();
			String[] automaatpasnummer = pasnummer.split(" ");

			if(automaatpasnummer[0].equals("pasnummer"))	{

				boolean pasnummerAuth = false;

				for (int i = 0; i<rekeningen.size(); i++) {
					Rekening testRekening = rekeningen.get(i);
					if (testRekening.getPasnummer().equals(automaatpasnummer[1])){
						pasnummerAuth = true;
					}
				}
				if (pasnummerAuth)	{
					writer.println("pasnummer " + 1);

				} else {
					writer.println("pasnummer " + 2);
				}
			}

			//pincode ontvangen en nakijken.
			String pincode = inFromClient.readLine();
			checkPincode(pincode);
			if (resterendePogingen == 0){
				break;
			}

			//kijken of de automaat het saldo wil of geld wil opnemen en dat uitvoeren
			String keuze = inFromClient.readLine();
			String[] keuzesplit = keuze.split(" ");

			if(keuzesplit[0].equals("saldo"))	{
				writer.println(werkRekening.getSaldo());
			} else if (keuzesplit[0].equals("opname")) {
				if (werkRekening.getSaldo() > Integer.parseInt(keuzesplit[1])){
					werkRekening.setSaldo(werkRekening.getSaldo() - Integer.parseInt(keuzesplit[1]));
					writer.println("opname true");
					writer.close();
					inFromClient.close();
					connectionSocket.close();
					break;
				} else {
					writer.println("opname false");
				}
			}

			//controleren en uitvoeren van geld opnemen
			String keuze2 = inFromClient.readLine();
			String[] keuzesplit2 = keuze2.split(" ");

			if(keuzesplit2[0].equals("saldo"))	{
				writer.println(werkRekening.getSaldo());
			} else if (keuzesplit2[0].equals("opname")) {
				if (werkRekening.getSaldo() > Integer.parseInt(keuzesplit2[1])){
					werkRekening.setSaldo(werkRekening.getSaldo() - Integer.parseInt(keuzesplit2[1]));
					writer.println("opname true");
				} else {
					writer.println("opname false");
				}
			}
			
			//verbingding sluiten
			writer.close();
			inFromClient.close();
			connectionSocket.close();
			break;
		}
	}
	
	/**
	 * Methode om bij te houden hoeveel pincode pogingen de user nog heeft.
	 * @param pincode String pincode
	 * @throws IOException
	 */
	private static void checkPincode(String pincode) throws IOException	{
		String[] automaatpincode = pincode.split(" ");

		if(automaatpincode[0].equals("pincode"))	{

			for (int i = 0; i< rekeningen.size(); i++) {
				Rekening testRekening = rekeningen.get(i);
				if (testRekening.getPasnummer().equals(automaatpincode[1])){
					werkRekening = testRekening;
				}
			}

			if (werkRekening.getPincode().equals(automaatpincode[2])) {
				writer.println("pincode " + 1);
			} else if(resterendePogingen > 0){
				resterendePogingen -= 1;
				writer.println("pincode " + 3 + " pogingen " + resterendePogingen);
				String poging = inFromClient.readLine();
				checkPincode(poging);
			} else if(resterendePogingen == 0)	{
				writer.println("pincode " + 2);
				writer.close();
				inFromClient.close();
				connectionSocket.close();
			}
		}
	}
}
