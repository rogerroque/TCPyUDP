package UDP;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class ClientVelocimetre {
    private MulticastSocket multisocket;
    private InetSocketAddress groupMulticast;
    private boolean continueRunning = true;
    private NetworkInterface netIf;
    private int media;
    private int contador;

    public ClientVelocimetre() {
        try {
            multisocket = new MulticastSocket(51351);
            InetAddress multicastIP = InetAddress.getByName("224.0.10.50");
            groupMulticast = new InetSocketAddress(multicastIP, 51351);
            netIf = NetworkInterface.getByName("wlp0s20f3");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runClient() throws IOException {
        byte[] receivedData = new byte[4];
        multisocket.joinGroup(groupMulticast, netIf);
        while (continueRunning) {
            DatagramPacket mpacket = new DatagramPacket(receivedData, 4);
            multisocket.receive(mpacket);
            medianVelocity(mpacket.getData());
        }
        multisocket.leaveGroup(groupMulticast, netIf);
        multisocket.close();
    }

    private void medianVelocity(byte[] data) {
        System.out.println(ByteBuffer.wrap(data).getInt());
        media += ByteBuffer.wrap(data).getInt();
        contador++;

        if (contador == 5) {
            System.out.println("Media: " + media / 5);
            media = 0;
            contador = 0;
        }

    }

    public static void main(String[] args) throws IOException {
        ClientVelocimetre clientVelocimetre = new ClientVelocimetre();

        clientVelocimetre.runClient();

    }
}
