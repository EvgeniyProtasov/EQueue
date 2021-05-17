package by.eugene.protasov.equeue.entity;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Hospital {

    static {
        HOSPITAL_INSTANCE = new Hospital();
    }

    private static final Hospital HOSPITAL_INSTANCE;

    private ConcurrentHashMap<Doctor, LinkedBlockingQueue<Patient>> doctorsAndTheirPatientsQueue = new ConcurrentHashMap<>();

    public static Hospital getHospital() {
        return HOSPITAL_INSTANCE;
    }

    public boolean isHospitalActive() {
        return doctorsAndTheirPatientsQueue.isEmpty();
    }

    public LinkedBlockingQueue<Patient> getTheSmallestQueue() {
        return doctorsAndTheirPatientsQueue.values().stream()
                .min(Comparator.comparing(LinkedBlockingQueue::size)).get();
    }

    public LinkedBlockingQueue<Patient> getDoctorsQueue(Doctor doctor) {
        LinkedBlockingQueue<Patient> doctorsQueue;
        if (!doctorsAndTheirPatientsQueue.containsKey(doctor)) {
            doctorsQueue = new LinkedBlockingQueue<>();
            doctorsAndTheirPatientsQueue.put(doctor, doctorsQueue);
        } else {
            doctorsQueue = doctorsAndTheirPatientsQueue.get(doctor);
        }
        return doctorsQueue;
    }

    public LinkedBlockingQueue<Patient> getPatientsQueue(Patient patient) {
        return doctorsAndTheirPatientsQueue.values().stream()
                .filter(queue -> queue.contains(patient))
                .findFirst()
                .orElse(null);
    }
}
