import java.lang.reflect.Array;
import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

import CMPC3M06.AudioPlayer;
import javax.sound.sampled.LineUnavailableException;

public class AudioReceiverThread implements Runnable {

    static DatagramSocket receiving_socket;
    static AudioPlayer player;
    int key = 15;

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
                //Receive a DatagramPacket (note that the string cant be more than 512 chars)
                byte[] buffer = new byte[514];



                DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length);

                receiving_socket.receive(packet);



                ByteBuffer unwrapDecrypt = ByteBuffer.allocate(buffer.length);   // Create buffer for Cipher
                ByteBuffer cipherText = ByteBuffer.wrap(buffer);





                for(int j = 0; j < buffer.length/4; j++) {
                    int fourByte = cipherText.getInt();
                    fourByte = fourByte ^ key; // XOR decrypt
                    unwrapDecrypt.putInt(fourByte);
                }
                byte[] decryptedBlock = unwrapDecrypt.array();


                ByteBuffer voippacket = ByteBuffer.allocate(buffer.length);
                voippacket.put(decryptedBlock);
                short authenticationKey = 10;

                if(voippacket.getShort() == authenticationKey ){

                    System.out.println("GOOOD HEADER");
                } else{
                    System.out.println("bad header!!!!!!!!!");
                }





                //   System.out.println( "I am received buffer : " + Arrays.toString(buffer));
              //  System.out.println("I am rec decrypted : " + Arrays.toString(decryptedBlock));




                // System.out.println("Playing Audio...");
                player.playBlock(decryptedBlock);

            } catch (IOException e) {
                e.printStackTrace();

            }
        }
            //Close the socket
            receiving_socket.close();
            //***************************************************
        }
    }
