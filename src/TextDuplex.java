/*
 * TextDuplex.java
 */

/**
 *
 * @author  abj
 */
public class TextDuplex {
    
    public static void main (String[] args){
        
        AudioReceiverThread receiver = new AudioReceiverThread();
        AudioSenderThread sender = new AudioSenderThread();
        
        receiver.start();
        sender.start();

        
    }
    
}
