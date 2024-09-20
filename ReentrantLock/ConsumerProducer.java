package ReentrantLock;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

class Producer {
    private Queue<Integer> Q;
    private ReentrantLock lock;
    private Condition condition;

    Producer(Queue<Integer> Q, ReentrantLock lock, Condition condition) {
        this.Q = Q;
        this.lock = lock;
        this.condition = condition;
    }

    void produce(int x) {
        try {
            lock.lock();

            System.out.println("Producing Item[" + x + "] ...");
            Q.add(x);
            System.out.println("Produced Item[" + x + "] SUCCESSFULLY");

            condition.signal();
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
    private Condition condition;

    Consumer(Queue<Integer> Q, ReentrantLock lock, Condition condition) {
        this.Q = Q;
        this.lock = lock;
        this.condition = condition;
    }

    void consume() {
        try {
            lock.lock();

            System.out.println("Consuming Item ...");
            
            if(Q.isEmpty()) {
                System.out.println("Waiting for Producer...");
                condition.await();
            }

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
        Condition condition = lock.newCondition();

        Producer producer = new Producer(Q, lock, condition);
        Consumer consumer = new Consumer(Q, lock, condition);
        
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