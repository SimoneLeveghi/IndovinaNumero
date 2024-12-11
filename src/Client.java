import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private String serverHostname;
    private int porta;

    private Socket clientSocket;
    private BufferedReader userInput;
    private BufferedReader serverInput;
    private DataOutputStream clientOutput;
    private String userInputString;

    public Client() {
        serverHostname = "localhost";
        userInputString = "";
        porta = 1234;
    }

    public void connetti() {
        System.out.println("Client in esecuzione");
        try {
            userInput = new BufferedReader(new InputStreamReader(System.in));
            clientSocket = new Socket(serverHostname, porta);
            clientOutput = new DataOutputStream(clientSocket.getOutputStream());
            serverInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.out.println("Host sconosciuto.");
        } catch(Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante la connessione.");
            System.exit(1);
        }
    }

    public void comunica() {
        try {
            while(serverInput != null) {
                System.out.print("Stringa da inviare al server: ");
                userInputString = userInput.readLine();

                clientOutput.writeBytes(userInputString + '\n');
                System.out.println("Risposta dal server: " + serverInput.readLine());
            }
            System.out.println("Gioco terminato.");
            System.out.println("Connessione terminata.");
            clientSocket.close();
        } catch(Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante la connessione.");
            System.exit(1);
        }
    }


    public static void main(String[] args) {
        Client client = new Client();
        client.connetti();
        client.comunica();
    }
}