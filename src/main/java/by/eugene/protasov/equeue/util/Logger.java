package by.eugene.protasov.equeue.util;

public class Logger {

    public static synchronized void print(String message) {
        System.out.println(message);
    }
}
