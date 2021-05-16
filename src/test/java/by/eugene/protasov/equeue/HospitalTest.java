package by.eugene.protasov.equeue;

import by.eugene.protasov.equeue.entity.Doctor;
import by.eugene.protasov.equeue.entity.Patient;
import org.junit.Test;

public class HospitalTest {

    @Test
    public void testHospitalProcess() throws InterruptedException {
        Thread client1 = new Thread(new Patient("Eugene"));
        Thread client2 = new Thread(new Patient("Mike"));
        Thread client3 = new Thread(new Patient("John"));
        Thread client4 = new Thread(new Patient("Tina"));
        Thread client5 = new Thread(new Patient("Joahna"));
        Thread client6 = new Thread(new Patient("Kim"));
        Thread client7 = new Thread(new Patient("Ella"));
        Thread client8 = new Thread(new Patient("Bella"));


        Thread doctor1 = new Thread(new Doctor("Mr Brown"));
        Thread doctor2 = new Thread(new Doctor("Mr Paul"));

        doctor1.start();

        client1.start();
        client2.start();
        client3.start();
        client4.start();
        client5.start();
        client6.start();
        client7.start();
        client8.start();

        Thread.sleep(500);

        doctor2.start();

        client1.join();
        client2.join();
        client3.join();
        client4.join();
        client5.join();
        client6.join();
        client7.join();
        client8.join();

        doctor1.join();
        doctor2.join();
    }
}
