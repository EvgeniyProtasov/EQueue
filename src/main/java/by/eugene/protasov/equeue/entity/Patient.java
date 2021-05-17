package by.eugene.protasov.equeue.entity;

import by.eugene.protasov.equeue.util.Logger;

import java.util.concurrent.LinkedBlockingQueue;

public class Patient implements Runnable {

    private String name;
    private PatientStatus patientStatus;

    public Patient(String name) {
        this.name = name;
        patientStatus = PatientStatus.IN_QUEUE;
    }


    public void setPatientStatus(PatientStatus patientStatus) {
        this.patientStatus = patientStatus;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        while (patientStatus != PatientStatus.INSPECTED) {
            if (patientStatus == PatientStatus.IN_QUEUE) {
                findTheSmallestQueueAndAdd(this);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Logger.print(
                String.format("Patient %s has gone from hospital", name)
        );
    }

    public synchronized void findTheSmallestQueueAndAdd(Patient patient) {
        Hospital hospital = Hospital.getHospital();
        if (!hospital.isHospitalActive()) {
            LinkedBlockingQueue<Patient> smallestQueue = hospital.getTheSmallestQueue();
            LinkedBlockingQueue<Patient> currentPatientQueue = hospital.getPatientsQueue(patient);

            if (currentPatientQueue != null) {
                if (currentPatientQueue != smallestQueue) {
                    long indexOfPatientInQueue = currentPatientQueue.stream().filter(patientsInQueue -> patientsInQueue != patient).count();
                    if (smallestQueue.size() < indexOfPatientInQueue) {
                        currentPatientQueue.remove(patient);
                        Logger.print(
                                String.format("Patient %s changed the queue", patient.getName())
                        );
                        joinToTheQueue(patient, smallestQueue);
                    }
                }
            } else {
                joinToTheQueue(patient, smallestQueue);
            }
        }
    }

    private void joinToTheQueue(Patient patient, LinkedBlockingQueue<Patient> queue) {
        try {
            queue.put(patient);
            Logger.print(
                    String.format("Patient %s joined to the queue", patient.getName())
            );
        } catch (InterruptedException e) {
            Logger.print("Cannot added patient to queue");
        }
    }
}
