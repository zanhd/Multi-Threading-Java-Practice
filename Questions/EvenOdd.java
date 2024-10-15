import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EvenOdd {

    public static void main(String[] args) {

        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        final int n = 10;

        Runnable evenTask = () -> {
            lock.lock();

            for(int i = 2; i < n; i += 2) {
                try { condition.await(); } catch (Exception e) {}

                System.out.print(i + " ");

                condition.signal();
            }

            lock.unlock();
        };
        
        Runnable oddTask = () -> {
            lock.lock();

            for(int i = 1; i < n; i += 2) {
                System.out.print(i + " ");
                
                condition.signal();

                try { condition.await(); } catch (Exception e) {}
            }

            lock.unlock();
        };

        Thread evenThread = new Thread(evenTask);
        Thread oddThread = new Thread(oddTask);

        evenThread.start();
        oddThread.start();

        try {
            evenThread.join();
            oddThread.join();
        } catch (Exception e) {
            System.out.println("EXCEPTION : " + e.getMessage());
        }
    }
    
}