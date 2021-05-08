package TCP;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ThreadServidor implements Runnable {

    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Llista outMessage;
    private Llista inMessage;
    private boolean acabat;

    public ThreadServidor(Socket clientSocket) {
        this.clientSocket = clientSocket;
        acabat = false;
    }

    @Override
    public void run() {
        try {

            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());

            while (!acabat) {
                inMessage = (Llista) in.readObject();
                outMessage = generaResposta(inMessage);
                out.writeObject(outMessage);

            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
        }
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Llista generaResposta(Llista llista) {

        Set<Integer> sortedAndDistinct = new TreeSet<>(llista.getNumberList());
        List<Integer> arrayList = new ArrayList<>(sortedAndDistinct);
        Llista lista = new Llista(llista.getNom(), arrayList);
        acabat = false;

        return lista;
    }

}
