using System;
using System.IO;
using System.Net.Sockets;

class client
{
    static void Main(string[] args)
    {
        String input;

        using (TcpClient tcpClient =
                new TcpClient("localhost", 1857))
        using (NetworkStream networkStream =
                tcpClient.GetStream())
        using (StreamReader streamReader =
                new StreamReader(networkStream))
        {
            input = streamReader.ReadToEnd();
        }

        Console.WriteLine("Received data: " + input + "\n");
    }
}