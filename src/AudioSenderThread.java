import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

import CMPC3M06.AudioRecorder;
import javax.sound.sampled.LineUnavailableException;


public class AudioSenderThread implements Runnable{

    static DatagramSocket sending_socket;
    static AudioRecorder recorder;
    int key = 15;



    public void start(){
        Thread thread = new Thread(this);
	thread.start();
    }
    
    public void run (){
        try {
            recorder = new AudioRecorder();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
     
        //***************************************************
        //Port to send to
        int PORT = 55555;
        //IP ADDRESS to send to
        InetAddress clientIP = null;
	try {
		clientIP = InetAddress.getByName("localhost");  //CHANGE localhost to IP or NAME of client machine
	} catch (UnknownHostException e) {
                System.out.println("ERROR: TextSender: Could not find client IP");
		e.printStackTrace();
                System.exit(0);
	}
        //***************************************************
        
        //***************************************************
        //Open a socket to send from
        //We dont need to know its port number as we never send anything to it.
        //We need the try and catch block to make sure no errors occur.
        
        //DatagramSocket sending_socket;
        try{
		sending_socket = new DatagramSocket();
	} catch (SocketException e){
                System.out.println("ERROR: TextSender: Could not open UDP socket to send from.");
		e.printStackTrace();
                System.exit(0);
	}
        //***************************************************
        
        //***************************************************
        //Main loop.
        
        boolean running = true;
        
        while (running){
            try{
                byte[] buffer = recorder.getBlock(); // encrypt this



                //Encryption block
                ByteBuffer unwrapEncrypt = ByteBuffer.allocate(buffer.length); //byte buffer



                ByteBuffer plainText = ByteBuffer.allocate(514); // adding buffer to new bytebuffer "plainText"
                short authenticationKey = 10; //set key
                plainText.putShort(authenticationKey);
                plainText.wrap(buffer);
                ////////////ENCRYPT
                for( int j = 0; j < buffer.length/4; j++) {
                    int fourByte = plainText.getInt();
                    fourByte = fourByte ^ key; // XOR operation with key
                    unwrapEncrypt.putInt(fourByte);
                }
                ///////////ENCRYPT


                ////////finished
                byte[] encryptedBlock = unwrapEncrypt.array(); // finished encrypted block
                ///////finished


                ///////header add








                DatagramPacket packet = new DatagramPacket(encryptedBlock, encryptedBlock.length, clientIP, PORT);


                sending_socket.send(packet);
              //  System.out.println( "I am sent buffer : " + Arrays.toString(buffer));
                //System.out.println("I am sent encrypted : " + Arrays.toString(encryptedBlock));
                    
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Close the socket
        sending_socket.close();
    }
} 
