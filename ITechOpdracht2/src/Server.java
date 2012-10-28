
import java.io.*;
import java.net.*;

public class Server {

	public static void main(String[] args) throws Exception {
		ServerSocket welcomeSocket = new ServerSocket(8080);
		String fileName = null;

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

			if(words[0].equals("GET"))	{
				fileName = words[1];
				System.out.println("FILENAME: " + fileName);
				file = new File("." + fileName);

			} else {
				fileName = "No GET Request";
				file = new File("400.html");
				String header = "HTTP/1.0 400 BAD REQUEST" +
						"\nContent-Type: text/html" +
						"\nContent-Length: " + file.length() + "\n" +
						"\n";

				byte [] mybytearray  = new byte [(int)file.length()];
				FileInputStream fis = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(fis);
				bis.read(mybytearray,0,mybytearray.length);
				bis.close();

				outToClient.writeUTF(header);
				outToClient.write(mybytearray,0,mybytearray.length);
				outToClient.flush();
				outToClient.close();
			}

			if(file.exists())	{
				System.out.println("Bestand bestaat");

				String header = "HTTP/1.0 200 OK" +
						"\nContent-Type: text/html" +
						"\nContent-Length: " + file.length() + "\n" +
						"\n";

				byte [] mybytearray  = new byte [(int)file.length()];
				FileInputStream fis = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(fis);
				bis.read(mybytearray,0,mybytearray.length);
				bis.close();

				outToClient.writeUTF(header);
				outToClient.write(mybytearray,0,mybytearray.length);
				outToClient.flush();
				outToClient.close();

			} else	{
				System.out.println("Bestand bestaat niet");

				file = new File("404.html");
				String header = "HTTP/1.0 404 NOT FOUND" +
						"\nContent-Type: text/html" +
						"\nContent-Length: " + file.length() + "\n" +
						"\n";

				byte [] mybytearray  = new byte [(int)file.length()];
				FileInputStream fis = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(fis);
				bis.read(mybytearray,0,mybytearray.length);
				bis.close();

				outToClient.writeUTF(header);
				outToClient.write(mybytearray,0,mybytearray.length);
				outToClient.flush();
				outToClient.close();
			}
		}
	}
}

