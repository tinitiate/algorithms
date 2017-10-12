package dgim;

/**
 * @author TINITIATE.COM
 *
 * > TOPIC : JAVA Communication
 * 
 * > NOTES : 1) Java provides mechanisms for threads to communicate with each other, this is
 *              useful where there are parent-child threads, event-based operations with threads.
 *           2) The major hurdle is to avoid polling in inter-thread communication.
 *           3) wait(): This method tells the calling thread to give up the monitor and
 *                      go to sleep until some other thread enters the same monitor and calls notify().
 *           4) notify(): This method wakes up the first thread that called wait() on the same object.
 *           5) notifyAll(): This method wakes up all the threads that called wait()
 *                           on the same object. The highest priority thread will run first.
 *
 */

/*
 *  Consider a "Factory - Consumer" model,
 *
 *  Factory  : Manufactures products
 *  Consumer : Gets the products from the factory.
 *
 */ 
class FactoryConsumer extends Thread {

    // This variable indicates if the product is ready or not.
    // Initially the Factory has no products, hence set to false
    boolean ProductReady = false;
    int i;
    // Product Identifier, Protected
    // This is the internal ID
    protected int ProductID = 0;

    // The Product that is exposed to the consumers 
    int     Product      = 0;
    String  ConsumerName = "";

    synchronized void ProductMachine() {
        // This method (a machine) makes a product
        // Factory keeps on producing products.
        // i.e. creates a new productID
        // wait for 5 Seconds
        while (true) {
            try {
                // Produce a Product only if the "ProductReady" is false
                if(Product != 0)
                    wait();
                     // For every iteration reset the Product value to ZERO.
                    Product = 0;

                    Thread.sleep(5000);
                    // Creates a Product.
                    ProductID++;
                    Product = ProductID;
                    //ProductReady = true;
                    System.out.println("#Factory Message#: Product# " + Product + " Ready for consumption.");
                    // Notify all threads waiting to consume the Product value.
                    notifyAll();
            }
            catch (InterruptedException e)
            { e.printStackTrace(); }
        }
    }

    synchronized void ConsumeProducts() {
        try {
            // if ProductReady is false, then wait.
            while (true) {
                // All threads calling this method need to wait
                // as data for their consumption is not ready yet
                if (Product == 0) {
                    System.out.println("Consumer " + ConsumerName + " is waiting.");
                    wait();
                }
                else{
                    System.out.println("#Consumer Message#: Product# " + Product + " Consumed by: " +  ConsumerName);
                    Product = 0;
                    notifyAll();
                }
            }
        }
        catch(InterruptedException e) {
            System.out.println("InterruptedException caught");
        }
    }
 }

class ThreadCommunication extends Thread {
    FactoryConsumer FC_Obj;
    String ConsumerName = "";
    String Thread_Type  = "";

    // Constructor, to indicate to use the same singleton object.
    // maintain only single instantiation of the class Factory, factoryObj
    // across all the threads
    public ThreadCommunication(FactoryConsumer FC_Obj, String ThreadName, String ThreadType) {
        this.FC_Obj       = FC_Obj;
        //this.FC_Obj.
        ConsumerName = ThreadName;
        this.Thread_Type  = ThreadType;
    }

    // The Run method calling the consumeProducts()
    // Using the thread type to call the appropriate method
    public void run() {
        if (Thread_Type == "Consumer") {   
            this.FC_Obj.ConsumerName = ConsumerName;
            this.FC_Obj.ConsumeProducts(); 
        }
        if (Thread_Type == "Factory") {   
            //this.FC_Obj.ConsumerName = ConsumerName;
            this.FC_Obj.ProductMachine();
        }
    }

    public static void main(String args[])  throws InterruptedException {

        try {
            FactoryConsumer factoryObj = new FactoryConsumer();

            // Create two new thread for Consumer
            ThreadCommunication ConsumerThread1 = new ThreadCommunication(factoryObj,"ConsumerThread1","Consumer");
            ConsumerThread1.start();

            // Create a new thread for the Factory 
            ThreadCommunication FactoryThread = new ThreadCommunication(factoryObj,"FactoryThread","Factory");
            FactoryThread.start();

        }
        catch(Exception e)
        { e.printStackTrace(); }
    }
}