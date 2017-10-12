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
    public String DATA;
    public int N;
    public StreamReader BGObj;

    public boolean getCommStreamRecordFlag() { return this.CommStreamRecordFlag; };
    
    public void setCommStreamRecordFlag(boolean CommStreamRecordFlag) {
        this.CommStreamRecordFlag = CommStreamRecordFlag;
    }

    // Create StreamReader Object
    // Single object for all the runs
    public Runner(String hostName, int port) {
        BGObj = new StreamReader(hostName, port);    
    }
    
    
    // This method keeps on reading the Stream Thread 
    public void ReadStreamThread() throws IOException {

        // System.out.println("CommStreamRecordFlag is true");
        BGObj.setRecordStreamFlg(getCommStreamRecordFlag());
        
        // System.out.println("ReadStreamThread Status of getCommStreamRecordFlag() " + String.valueOf(getCommStreamRecordFlag()));
        
        
        // Wait till the "BitStreamData" string is == N length
        while(BGObj.getBitStreamData().length() <= N) {
            // System.out.println("RecordStreamFlg status " + BGObj.RecordStreamFlg);
            System.out.println("Current DATA length: " + BGObj.getBitStreamData().length());
            // System.out.println("Current DATA: " + BGObj.getBitStreamData());

            // Wait for 1 sec until the BitStreamData appended is equal to N
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
        BGObj.setRecordStreamFlg(false);
        DATA = BGObj.getBitStreamData(); 
        setCommStreamRecordFlag(false);
    }


    public void run() {
        try {
            System.out.println("BitStream Reading Thread Started !!");
            BGObj.ReadStream();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    
    // Main Class
    public static void main(String[] args) throws IOException {



        // Step 1.
        // Create the Runner Object
        // Read from Class commandline Arguments for HostName and Port to lister for bitstream data
        // Runner Obj = new Runner(args[0], args[1]);
        Runner Obj = new Runner("localhost",9090);
        
        // Step 2.
        // Read Values of N and K from user input
        Scanner scanN = new Scanner(System.in);
        Scanner scanK = new Scanner(System.in);

        System.out.println("Enter value of N:");
        Obj.N = Integer.parseInt(scanN.nextLine());
        System.out.println("Enter value of K:");
        int K = Integer.parseInt(scanK.nextLine());

        
        // Step 3.
        // Initialize
        // Set CommStreamRecordFlag to False
        Obj.setCommStreamRecordFlag(false);
        
        // Start the Thread to read bit stream data
        Obj.start();

        // wait for Three Seconds to make sure thread start timing doesnt interfere with calculation
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) { e.printStackTrace(); }

        // Start Data Stream recording
        Obj.setCommStreamRecordFlag(true);

        // Read the Data from the Thread
        Obj.ReadStreamThread();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) { e.printStackTrace(); }

        System.out.println("Data: " + Obj.DATA);
        
        // Step 4
        // Run the DGIM
        DGIM dObj = new DGIM(Obj.N, K, Obj.DATA);
        System.out.println("Count of 1s: " + dObj.OneCount);
        
        // String DATA ="1111000110001011010010110011001000111110010100011100111100000011111110010101010101010101010110101010101011010101000011110000110101000110110110101001011110101000000011101101110110001010010000101000110000110101011111010001011001011011100101110000100000010010110010111011100";
        
        // Read from Arguments
        //Obj.hostName = args[0];
        //Obj.port = Integer.parseInt(args[1]);

        /*
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
        */
    }
}
