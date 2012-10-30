import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Bank {

	private static final int id = 20;

	public static void main(String argv[]) throws Exception {

		ArrayList<Rekening> rekeningen = new ArrayList<Rekening>();
		Rekening rekening = new Rekening("123456789", "9876", 500.0);
		rekeningen.add(rekening);
		Rekening werkRekening = null;

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
				if(sentence[1].equals("10"))	{
					writer.println("1");
				} else	{
					writer.println("2");
				}
			}

			String pasnummer = inFromClient.readLine();

			String[] automaatpasnummer = pasnummer.split(" ");

			System.out.println(automaatpasnummer[0]);
			System.out.println(automaatpasnummer[1]);

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
			
			String pincode = inFromClient.readLine();

			String[] automaatpincode = pincode.split(" ");

			System.out.println(automaatpincode[0]);
			System.out.println(automaatpincode[1]);
			System.out.println(automaatpincode[2]);

			if(automaatpincode[0].equals("pincode"))	{

				for (int i = 0; i<rekeningen.size(); i++) {
					Rekening testRekening = rekeningen.get(i);
					if (testRekening.getPasnummer().equals(automaatpincode[1])){
						werkRekening = testRekening;
					}
				}
				
				if (werkRekening.getPincode().equals(automaatpincode[2])) {
					writer.println("pincode " + 1);
				} else {
					writer.println("pincode " + 2);
				}
			}

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
			
			writer.close();
			inFromClient.close();
			connectionSocket.close();
			break;
		}

	}

}
