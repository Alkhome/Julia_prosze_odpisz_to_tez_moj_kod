import java.io.*;
import java.net.*;
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
    Socket client;
    EchoHandler (Socket client) {
        this.client = client;
    }

    public void run () {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter writer = new PrintWriter(client.getOutputStream(), true);

            while (true) {
                // TODO change response of a server to the client
                String line = reader.readLine();
                System.out.println(line);
                writer.println("Server Response: " + line);

                if (client.getInputStream().read() == -1){
                    break;
                }
            }
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