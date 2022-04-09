import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.lang.Thread;

public class EchoServer {
    public static int clients_count= 0;
    public static void main (String[] args) {
        try {
            ServerSocket server = new ServerSocket(5566);
            while (true) {
                if(clients_count < 5){
                    Socket client = server.accept();
                    EchoHandler handler = new EchoHandler(client);
                    handler.start();
                    System.out.println("New client Connected on port: " + client.getPort());
                    clients_count++;
                    System.out.println("Total number of clients: " + clients_count+"/5");
                }
            }
        }
        catch (Exception e) {
            System.err.println("Exception caught:" + e);
        }
    }
}

class EchoHandler extends Thread {
	
	static final String OK_HEADER = "HTTP/1.1 200 OK";
	
	static final String HTML_START =
			"<html>" +
			"<title>HTTP Server in java</title>" +
			"<body>";

	static final String HTML_END =
			"</body>" +
			"</html>";
			
    Socket client;
    EchoHandler (Socket client) {
        this.client = client;
    }

    public void run () {
        try {
    	   BufferedReader inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
    	   PrintWriter outToClient = new PrintWriter(client.getOutputStream(), true);
    	   
    	   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
    	   LocalDateTime now = LocalDateTime.now();  
    	   

    	   String requestString = inFromClient.readLine();
           
    	   String localAddressString = client.getLocalAddress().getHostAddress();
    	   int localPortInt = client.getLocalPort();
    	   
    	   String responseString = 
    			   HTML_START +
    			   requestString + "</br>" + 
    			   "Date:" + now + "</br>" +
    			   "Server Address: " + localAddressString + ":" + localPortInt + "</br>" +
    			   "Client Address: " + client.getRemoteSocketAddress().toString() + "</br>" +
    			   HTML_END;
    	   
    	   String contentLengthLine = "Content-Length: " + responseString.length() + "\r\n";
    	   
    	   outToClient.println(OK_HEADER);
    	   outToClient.println("Content-Type: text/html");
    	   outToClient.println(contentLengthLine);
    	   outToClient.println(responseString);
            
        }
        catch (Exception e) {
            System.err.println("Exception caught: client disconnected.");
            System.out.println("Total number of clients: " + EchoServer.clients_count+"/5");
        }
        finally {
            try {
                client.close();
                EchoServer.clients_count--;
                System.out.println("Client Disconnected on port: " + client.getPort());
                System.out.println("Total number of clients: " + EchoServer.clients_count+"/5");
            }
            catch (Exception e ){
                System.out.println("Exception caught: " + e);
            }
        }
    }
}