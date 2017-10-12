package dgim;

import java.io.*;
import java.util.ArrayList;

class Bucket1 {
    public int timestamp;
    public int size;

    public Bucket1(int timestamp, int size) {
        this.timestamp = timestamp;
        this.size = size;
    }
}

public class DGIMAlgo {

    // window length
    private int N;
    private ArrayList<Bucket> items;

    public DGIMAlgo(int N) {
        this.N = N;
        this.items = new ArrayList<>();
    }

    public void createBucket(Bucket b, int currentTime) {
        ArrayList<Bucket> temp = new ArrayList<>();
        int length = items.size();
        for (int i = 0; i < length; i++) {
            if (items.get(i).size == b.size) {
                temp.add(items.get(i));
                if (temp.size() == 2) {
                    break;
                }
            }
        }
        
        items.add(b);

        if (temp.size() != 0 && temp.size() != 1) {
            items.remove(temp.get(0));
            items.remove(temp.get(1));
            int timestamp = Math.max(temp.get(0).timestamp, temp.get(1).timestamp);
            int size = temp.get(0).size * 2;
            Bucket newBucket = new Bucket(timestamp, size);
            createBucket(newBucket, currentTime);
        }
    }

    public void ClearOldBuckets(int currentTime) {
        int difference = currentTime - N;
        ArrayList<Bucket> itemsTmp = new ArrayList<>();

        for (Bucket b : items) {
            if(b.timestamp > difference) {
                itemsTmp.add(b);
            }
        }
        // Override items with itemsTmp
        items = itemsTmp;
    }    

    public int calcOneCount(int k, int currentTime) {
        int difference = currentTime - k;
        int length = items.size();
        int sum = 0;
        int max = 0;
        for (int i = 0; i < length; i++) {
            Bucket b = items.get(i);
            if (b.timestamp >= difference) {
                sum += b.size;
                if (b.size > max) {
                    max = b.size;
                }
            }
        }
        if (max == 1) {
            max++;
        }
        return sum - max / 2;
    }


    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // int N = Integer.parseInt(br.readLine());
        // Sample Size
        int N = 1000;
        String line = "111100011000101101001011001100100011111001010000000110101000110110110101001011110101000000011101101110110001010010000101000110000110101011111010001011001011011100101110000100000010010110010111011100";
        // K
        int K;
        // Start Time
        int time = 0;
        // Object to start the objDGIM
        DGIMAlgo objDGIM = new DGIMAlgo(N);
        /*
        while ((line = br.readLine()) != null) {

            if (line.startsWith("X")) {

                System.out.println("Enter the value of K: ");
                // K = Integer.parseInt(line.split(" ")[1]);
                K = Integer.parseInt(br.readLine());

                int est = objDGIM.calcOneCount(K, time);
                System.out.println(est);

            }            
            else {

                int length = line.length();
                for (int i = 0; i < length; i++) {
                    char c = line.charAt(i);
                    objDGIM.ClearOldBuckets(time);

                    // Check for New Line character 
                    if (c == 49) {
                        // Create Bucket
                        objDGIM.createBucket(new Bucket(time, 1), time);
                    }
                    time++;
                }
            }
        }
        */
        // For every bit in the stream input, caputr ethe One and insert the time component
        int length = line.length();
        for (int i = 0; i < length; i++) {
            // char c = line.charAt(i);
            
            objDGIM.ClearOldBuckets(time);
            objDGIM.createBucket(new Bucket(time, 1), time);
            time++;
        }

        System.out.println("Enter the value of K: ");
        // K = Integer.parseInt(line.split(" ")[1]);
        K = Integer.parseInt(br.readLine());

        int est = objDGIM.calcOneCount(K, time);
        System.out.println(est);
            
        br.close();
    }
}
