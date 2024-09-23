import java.util.*;

class Singleton {
    private static Singleton obj; //NOTE : obj is static

    private Singleton() {}

    //syncronized getInstance() Method
    synchronized public static Singleton getInstance() {
        if(obj == null) {
            obj = new Singleton();
        }

        return obj;
    }
}

public class SyncronizedSingleton {
    public static void main(String[] args) {
        Thread oneThread = new Thread(() -> System.out.println(Singleton.getInstance()));
        Thread secondThread = new Thread(() -> System.out.println(Singleton.getInstance()));

        oneThread.start();
        secondThread.start();

        try {
            oneThread.join();
            secondThread.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}