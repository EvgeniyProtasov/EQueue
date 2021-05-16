package by.eugene.protasov.equeue.entity;

import by.eugene.protasov.equeue.util.Logger;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Hospital {

    static {
        HOSPITAL_INSTANCE = new Hospital();
    }

    private static final Hospital HOSPITAL_INSTANCE;

    private LinkedBlockingQueue<Patient> patientsInQueue = new LinkedBlockingQueue<>();
    private ConcurrentHashMap<Doctor, LinkedBlockingQueue<Patient>> doctorsAndTheirPatientsQueue = new ConcurrentHashMap<>();

    public static Hospital getHospital() {
        return HOSPITAL_INSTANCE;
    }

    private LinkedBlockingQueue<Patient> getTheSmallestQueue() {
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

    public synchronized void addPatientToTheSmallestQueue(Patient patient) {
        if (!doctorsAndTheirPatientsQueue.isEmpty()) {
            LinkedBlockingQueue<Patient> smallestQueue = getTheSmallestQueue();
            LinkedBlockingQueue<Patient> currentPatientQueue = getPatientQueue(patient);

            if (currentPatientQueue != null) {
                if (currentPatientQueue != smallestQueue) {
                    long indexOfPatientInQueue = currentPatientQueue.stream().filter(patientsInQueue -> patientsInQueue != patient).count();
                    if (smallestQueue.size() < indexOfPatientInQueue) {
                        currentPatientQueue.remove(patient);
                        Logger.print(
                                String.format("Patient %s changed the queue", patient.getName())
                        );
                        addPatientToTheQueue(patient, smallestQueue);
                    }
                }
            } else {
                addPatientToTheQueue(patient, smallestQueue);
            }
        }
    }

    private void addPatientToTheQueue(Patient patient, LinkedBlockingQueue<Patient> queue) {
        try {
            queue.put(patient);
            Logger.print(
                    String.format("Patient %s was added to the queue", patient.getName())
            );
        } catch (InterruptedException e) {
            Logger.print("Cannot added patient to queue");
        }
    }

    private LinkedBlockingQueue<Patient> getPatientQueue(Patient patient) {
        return doctorsAndTheirPatientsQueue.values().stream()
                .filter(queue -> queue.contains(patient))
                .findFirst()
                .orElse(null);
    }
}
