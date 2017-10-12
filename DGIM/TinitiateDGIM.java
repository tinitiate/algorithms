package dgim;

import java.io.Serializable;
import java.util.Date;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

// Bucket Class
//
public class TinitiateDGIM implements Serializable{

    // private static final long serialVersionUID = 1L;

    public static Deque<Bucket> queue = new LinkedList<Bucket>();
    public int N;

    public TinitiateDGIM(int N) {
        this.N = N*1000;
    }

    public int getCount() {

        int ans = 0;
        int addValue=0;
        Iterator<Bucket> iter = queue.iterator();
        long currTime = new Date().getTime();

        while(iter.hasNext()) {
        	
            Bucket bucket = iter.next();
            if((bucket.timestamp + N) > currTime) {
                addValue = bucket.size;
                ans+=addValue;
            }
            else
                break;
        }

        ans-=addValue/2;
        return ans;
    }
    

    public void add() {

        long currTime = new Date().getTime();
        Bucket bucket = new Bucket(1, currTime);

        queue.addFirst(bucket);

        if((currTime - queue.getLast().timestamp) > N) {
            queue.removeLast();
        }
        
        Iterator<Bucket> iter = queue.iterator();
        
        if(needMerge(iter)) {
            Iterator<Bucket> iter1 = queue.iterator();
            merge(iter1);
        }
    }
    

    private static void merge(Iterator<Bucket> iter) {

        iter.next();
        Bucket next = iter.next();
        Bucket nextToNext = iter.next();
        int firstValue = next.getSize();

        next.setSize(next.getSize() + nextToNext.getSize());
        iter.remove();
        Iterator<Bucket> iter1 = getIterator(firstValue);

        if(needMerge(iter1)){
            Iterator<Bucket> iter2 = getIterator(firstValue);
            merge(iter2);
        }
    }
    
    private static Iterator<Bucket> getIterator(int firstValue) {

        Iterator<Bucket> iter = queue.iterator();

        while(iter.hasNext()) {
            Bucket bucket = iter.next();
            if(bucket.getSize()==firstValue)
                return iter;
        }

        return null;
    }


    private static  boolean needMerge(Iterator<Bucket> iter) {
        Bucket head,next,nextToNext;
        if(iter.hasNext()){
            head = iter.next();
            if(iter.hasNext()){
                next = iter.next();
                if(next.getSize()!=head.getSize())
                    return false;
                else{
                    if(iter.hasNext()){
                        nextToNext = iter.next();
                        if(next.getSize()==nextToNext.getSize())
                            return true;
                        else
                            return false;
                    }
                    else
                        return false;
                }
            }
            else
                return false;
        }
        else    
            return false;
    }

    
    
    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        TinitiateDGIM dgim = new TinitiateDGIM(3);

        for(int i =0;i<30;i++){
            dgim.add();
            Thread.sleep(100);
        }

        Iterator<Bucket> iter = queue.iterator();

        while(iter.hasNext())
            System.out.println(iter.next());
            System.out.println(dgim.getCount());
    }

}