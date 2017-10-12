package dgim;

import java.io.IOException;
import java.util.Scanner;

// Runner Class
// 1. This class is the entry point to the Package
//
// 2. The input file arguments for this class are
//  2.1 HostName, This is the host name where there Bit Stream is running
//  2.2 PortNumber, This is the port number where there Bit Stream is running
//
// 3. This will spawn TWO Process(s) to read the Bit Stream data
//    3.1 The first process is the Stream Reader Class that reads the Bit data 
//        from the Bit Stream python source.
//    3.2

// Class Runner 
public class Runner extends Thread {

    public boolean CommStreamRecordFlag;
    public String hostName;
    public int port;
    
    // This method keeps on reading the Stream Thread 
    synchronized void ReadStreamThread() throws IOException {

        boolean isRunning = true;
        
        // Create StreamReader Object
        // This will keep reading the BitStream
        
        StreamReader BGObj = new StreamReader(this.hostName, this.port);

        while (true) {
            if (isRunning) {
                // Background object started running
                System.out.println("BitStream Reading Thread Started !!");
                BGObj.setRecordStreamFlg(true);
                isRunning = false;
            }
            else if (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }


    // Main Class
    public static void main(String[] args) {

        Runner Obj = new Runner();
        int N;
        int K;
        String DATA ="1111000110001011010010110011001000111110010100011100111100000011111110010101010101010101010110101010101011010101000011110000110101000110110110101001011110101000000011101101110110001010010000101000110000110101011111010001011001011011100101110000100000010010110010111011100";
        
        // Read from Arguments
        //Obj.hostName = args[0];
        //Obj.port = Integer.parseInt(args[1]);

        Scanner scanN = new Scanner(System.in);
        Scanner scanK = new Scanner(System.in);

        System.out.println("Enter value of N:");
        N = Integer.parseInt(scanN.nextLine());
        System.out.println("Enter value of K:");
        K = Integer.parseInt(scanK.nextLine());

        DGIM dObj = new DGIM(N, K, DATA);
        System.out.println("Count of 1s: " + dObj.OneCount);
        // Start Reading the input stream

        // Accept User inputs for N and K

    }
}
