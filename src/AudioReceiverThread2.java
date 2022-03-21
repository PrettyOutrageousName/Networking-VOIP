import CMPC3M06.AudioPlayer;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class AudioReceiverThread2 implements Runnable {

    static DatagramSocket receiving_socket;
    static AudioPlayer player;
    int key = 15;
    short authenticationKey = 10;

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {


        try {
            player = new AudioPlayer();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }


        //***************************************************
        //Port to open socket on
        int PORT = 55555;
        //***************************************************

        //***************************************************
        //Open a socket to receive from on port PORT

        //DatagramSocket receiving_socket;
        try {
            receiving_socket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            System.out.println("ERROR: TextReceiver: Could not open UDP socket to receive from.");
            e.printStackTrace();
            System.exit(0);
        }
        //***************************************************

        //***************************************************
        //Main loop.


        boolean running = true;

        while (running) {

            try {
                byte[] buffer = new byte[512]; // Create our byte array/buffer
                DatagramPacket packet = new DatagramPacket(buffer, 0, 512); // Create/Setup our Datagram Packet to receive data
              // ByteBuffer block = ByteBuffer.wrap(buffer)
                receiving_socket.receive(packet);
               ByteBuffer wrapped = ByteBuffer.wrap(buffer);


                player.playBlock(buffer);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
                /*
                byte[] buffer = new byte[514]; // Create our byte array/buffer

                DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length); // Create/Setup our Datagram Packet to receive data
                receiving_socket.receive(packet); // Receive data

                ByteBuffer unwrapDecrypt = ByteBuffer.allocate(buffer.length);   // Create buffer for Final Decrypted data
                ByteBuffer cipherText = ByteBuffer.wrap(buffer); // Add encrypted data to our temp buffer to decrypt

                // Start Decryption
                for(int j = 0; j < buffer.length/4; j++) {
                    int fourByte = cipherText.getInt();
                    fourByte = fourByte ^ key; // XOR decrypt
                    unwrapDecrypt.putInt(fourByte);
                }
                // Finish Decryption


                if(unwrapDecrypt.getShort(0) == authenticationKey ){  // Check if header matches our key

                    byte[] decryptedBlock = Arrays.copyOfRange(unwrapDecrypt.array(), 2, 514); // Extract Only Voip bytes from our decrypted array

                    player.playBlock(decryptedBlock); // Play Decrypted Block
                   // System.out.println("Good Header");

                } else{
                    System.out.println("Bad Header");
                }

            } catch (IOException e) {
                e.printStackTrace();

            }
        }
            //Close the socket
            receiving_socket.close();
            //***************************************************
        }
    }*/
