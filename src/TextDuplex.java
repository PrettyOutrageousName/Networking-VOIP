/*
 * TextDuplex.java
 */

/**
 *
 * @author  abj
 */
public class TextDuplex {
    
    public static void main (String[] args){
        
        AudioReceiverThread2 receiver = new AudioReceiverThread2();
        AudioSenderThread2 sender = new AudioSenderThread2();
        
        receiver.start();
        sender.start();

        
    }
    
}
