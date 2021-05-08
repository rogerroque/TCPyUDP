package TCP;

import java.net.Socket;
import java.io.IOException;
import java.net.ServerSocket;


public class Server {

    private int port;
    private ServerSocket serverSocket;
    private Socket socket;

    public Server(int port ) {
        this.port = port;
    }

    public void listen() throws IOException {

        serverSocket = new ServerSocket(port);
        while(true) {
            socket = serverSocket.accept();
            ThreadServidor FilServidor = new ThreadServidor(socket);
            Thread client = new Thread(FilServidor);
            client.start();
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(5558);
        server.listen();

    }

}
