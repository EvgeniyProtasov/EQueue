package by.eugene.protasov.equeue.entity;

import by.eugene.protasov.equeue.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class Doctor implements Runnable {

    private String name;
    private Boolean isFree = false;
    private Patient currentPatient;
    private LinkedBlockingQueue<Patient> waitingPatients;
    private List<Patient> inspectedPatients = new ArrayList<>();

    public Doctor(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        if (waitingPatients == null) {
            waitingPatients = Hospital.getHospital().getDoctorsQueue(this);
        }
        while (!isFree || Hospital.getHospital().getDoctorsQueue(this).size() > 0) {
            getPatientFromQueue();
            if (currentPatient != null) {
                try {
                    Thread.sleep(new Random().nextInt(2000) + 1000);
                } catch (InterruptedException e) {
                    Logger.print("Doctor cannot sleep while working =)");
                }
                setPatientInspected();
            }
        }
        Logger.print(
                String.format("Doctor %s finished his work.", name) + "\n" +
                        String.format("Today were inspected %s humans", inspectedPatients.size())
        );
    }

    private void getPatientFromQueue() {
        currentPatient = waitingPatients.poll();
        if (currentPatient != null) {
            currentPatient.setPatientStatus(PatientStatus.ON_INSPECTING);
            Logger.print(
                    String.format("Doctor %s started inspection of patient %s", name, currentPatient.getName())
            );
            isFree = false;
        }
    }

    private void setPatientInspected() {
        currentPatient.setPatientStatus(PatientStatus.INSPECTED);
        inspectedPatients.add(currentPatient);
        currentPatient = null;
        isFree = true;
    }
}
