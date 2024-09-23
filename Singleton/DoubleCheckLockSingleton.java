/*
* Improvement of SyncronizedSingleton
*/

import java.util.*;

class Singleton {
    private static Singleton obj; //NOTE : obj is static

    private Singleton() {}

    public static Singleton getInstance() {
        if(obj == null) { //CHECK - 1
            synchronized(Singleton.class) { //syncronized only when obj is null -> only one time
                if(obj == null) { //CHECK - 2
                    obj = new Singleton();
                }
            }
        }

        return obj;
    }
}

public class DoubleCheckLockSingleton {
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
