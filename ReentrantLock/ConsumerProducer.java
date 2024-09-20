package ReentrantLock;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

class Producer {
    private Queue<Integer> Q;
    private ReentrantLock lock;

    Producer(Queue<Integer> Q, ReentrantLock lock) {
        this.Q = Q;
        this.lock = lock;
    }

    void produce(int x) {
        try {
            lock.lock();

            System.out.println("Producing Item[" + x + "] ...");
            Q.add(x);
            System.out.println("Produced Item[" + x + "] SUCCESSFULLY");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}

class Consumer {
    private Queue<Integer> Q;
    private ReentrantLock lock;

    Consumer(Queue<Integer> Q, ReentrantLock lock) {
        this.Q = Q;
        this.lock = lock;
    }

    void consume() {
        try {
            lock.lock();

            System.out.println("Consuming Item ...");
            int x = Q.poll();
            System.out.println("Consumed Item[" + x + "] SUCCESSFULLY");
        } catch(Exception e) {
            System.out.println(e.getMessage());
        } finally {
            lock.unlock();
        }
    }

}

public class ConsumerProducer {
    public static void main(String[] args) {
        Queue<Integer> Q  = new LinkedList<>();
        ReentrantLock lock = new ReentrantLock();

        Producer producer = new Producer(Q, lock);
        Consumer consumer = new Consumer(Q, lock);
        
        final int n = 10;

        Thread producerThread = new Thread(() -> { 
            for(int i = 0; i < n; i++) {
                producer.produce(i);

                try {
                    Thread.sleep(1000); 
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });

        Thread consumerThread = new Thread(() -> {
            for(int i = 0; i < n; i++) {
                consumer.consume();

                try {
                    Thread.sleep(1000); 
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });

        producerThread.start();
        consumerThread.start();

        try {
            producerThread.join();
            consumerThread.join();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}