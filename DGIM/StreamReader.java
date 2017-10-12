package dgim;

// StreamReader
// 1. The Stream Reader Class
// 2. 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class StreamReader extends Socket {

    
    public String hostName;
    public int port;

    // This flag is set false initially, if this is true then the bitstream 
    //  will be stored to the bitstream string field.
    public boolean RecordStreamFlg;
    // This field will store the bitstreamData 
    public String BitStreamData = "";

    // Method to set the status of recordStreamFlg
    public void setRecordStreamFlg(boolean RecordStreamFlg) {
        this.RecordStreamFlg = RecordStreamFlg;
    }
    
    // Method to return the "bitstreamData" 
    public String getBitStreamData() {
        return this.BitStreamData;
    }

    public void ReadStream() throws UnknownHostException, IOException {

        Socket s = new Socket(hostName, port); // constructor call to the Socket Class
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

        // Keep reading the server    
        System.out.println("Connected to Host and running ReadStream");
        // System.out.println("RecordStreamFlg status " + RecordStreamFlg);
        while(true) {
            // System.out.println("RecordStreamFlg status " + RecordStreamFlg);
            String bitData = br.readLine();
            if (RecordStreamFlg) {
                BitStreamData = BitStreamData + bitData;
                System.out.println(BitStreamData);
            }
        }
    }

    // Constructor of the class
    public StreamReader(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    // Main Test
    // public static void main(String[] args) throws IOException {
    //     StreamReader o = new StreamReader("localhost", 9090);
    //     o.setRecordStreamFlg(true);
    //     o.ReadStream();
    // }
}