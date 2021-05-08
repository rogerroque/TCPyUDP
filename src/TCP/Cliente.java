package TCP;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.Socket;
import java.util.Random;

public class Cliente extends Thread {

    private String hostname;
    private int port;
    private List<Integer> numberList;
    private Llista lista;
    private Llista recivedList;
    Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Random random = new Random();

    public Cliente(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void run() {

        numberList = new ArrayList<>();
        numberList.add(random.nextInt(100) + 1);
        numberList.add(random.nextInt(100) + 1);
        numberList.add(random.nextInt(100) + 1);
        numberList.add(random.nextInt(100) + 1);
        numberList.add(random.nextInt(100) + 1);
        numberList.add(random.nextInt(100) + 1);
        numberList.add(random.nextInt(100) + 1);

        lista = new Llista("Roger", numberList);

        System.out.println("Antes");
        System.out.println(numberList);


        System.out.println("\nDespues");
        try {
            socket = new Socket(InetAddress.getByName(hostname), port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream.writeObject(lista);
            recivedList = (Llista) inputStream.readObject();
            getRequest(recivedList);
            close(socket);

        } catch (UnknownHostException ex) {
            System.out.println("Error de connexió. No existeix el host: " + ex.getMessage());
        } catch (IOException | ClassNotFoundException exx) {
            System.out.println("Error de connexió indefinit: " + exx.getMessage());
        }

    }

    public void getRequest(Llista llista) {

        System.out.println("Nombre: " + llista.getNom());
        System.out.println("Lista ordenada ");
        System.out.println(llista.getNumberList());

    }

    private void close(Socket socket) throws IOException {

        if (socket != null && !socket.isClosed()) {
            if (!socket.isInputShutdown()) {
                socket.shutdownInput();
            }
            if (!socket.isOutputShutdown()) {
                socket.shutdownOutput();
            }
            socket.close();
        }
    }

    public static void main(String[] args) {
        Cliente cliente = new Cliente("localhost", 5558);
        cliente.start();
    }
}
