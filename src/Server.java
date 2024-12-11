import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Random;

public class Server {
    private ServerSocket server;
    private Socket client;
    private final int porta;
    private final int numIndov;

    public Server() {
        Random r = new Random();

        server = null;
        client = null;
        porta = 1234;
        numIndov = r.nextInt(1000);
    }
    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
        @Override
        public void run () {
            try {
                BufferedReader clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream serverOutput = new DataOutputStream(clientSocket.getOutputStream());
                int tentativo = -1;
                System.out.println("NumIndov: " + numIndov);

                while (tentativo != numIndov) {
                    String message = clientInput.readLine();
                    System.out.println("Messaggio ricevuto da client: " + message);

                    try {
                        tentativo = Integer.parseInt(message);
                        serverOutput.writeBytes((tentativo < numIndov? "minore" : "maggiore") + "\n");
                    }
                    catch (Exception e) {
                        serverOutput.writeBytes("Valore non valido" + "\n");
                    }

                    serverOutput.flush();
                }
                serverOutput.writeBytes("Numero indovinato\n");
            } catch (IOException e) {
                System.out.println("Comunicazione interrotta con il client.");
            }
        }
    }

    public void attendi() {
        try {
            server = new ServerSocket(porta);
            server.setReuseAddress(true);
            System.out.println("Server in attesa di connessioni.");

            while(true) {
                client = server.accept();
                System.out.println("Nuova connessione: " + client);

                Thread clientHandler = new Thread(new ClientHandler(client));
                clientHandler.start();
            }

        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Errore durante l'istanza del server.");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.attendi();
    }
}