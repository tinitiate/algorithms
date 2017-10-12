package dgim;

import java.util.ArrayList;

class Bucket {
    public int timestamp;
    public int size;

    public Bucket(int timestamp, int size) {
        this.timestamp = timestamp;
        this.size = size;
    }
}


public class DGIM {

    // window length
    private int N;
    private ArrayList<Bucket> items;
    public int OneCount;

    // Clears the Bucket
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

    // Clear old buckets, when the window slides
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

    // Count the number of 1s in the given N data of K count
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


    public DGIM(int N, int k, String DATA) {

        this.N = N;
        this.items = new ArrayList<>();
        String line = DATA;
        int K = k;
        // Start Time
        int time = 0;

        // For every bit in the stream input, capture the One and insert the time component
        int length = line.length();
        for (int i = 0; i < length; i++) {
            this.ClearOldBuckets(time);
            this.createBucket(new Bucket(time, 1), time);
            time++;
        }

        this.OneCount = this.calcOneCount(K, time);
    }
}
