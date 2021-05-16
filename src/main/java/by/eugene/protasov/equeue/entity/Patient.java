package by.eugene.protasov.equeue.entity;

import by.eugene.protasov.equeue.util.Logger;

public class Patient implements Runnable {

    private String name;
    private PatientStatus patientStatus;

    public Patient(String name) {
        this.name = name;
        patientStatus = PatientStatus.IN_QUEUE;
    }

    @Override
    public void run() {
        while (patientStatus != PatientStatus.INSPECTED) {
            if (patientStatus == PatientStatus.IN_QUEUE) {
                Hospital.getHospital().addPatientToTheSmallestQueue(this);
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

    public void setPatientStatus(PatientStatus patientStatus) {
        this.patientStatus = patientStatus;
    }

    public String getName() {
        return name;
    }
}
