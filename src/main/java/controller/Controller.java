package controller;

import model.Client;
import model.Queue;
import model.QueuesManager;
import validator.InputValidator;
import view.UserInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import java.util.Comparator;

public class Controller implements Runnable {

    private int arrivalMin;
    private int arrivalMax;
    private int serviceMin;
    private int serviceMax;
    private int simulationTime;
    private int N;
    private int Q;
    private float averageWaiting;
    private float averageServiceTime;
    private int peakHour = 0;


    private UserInterface userInterface;

    private List<Client> randomGeneratedClients;

    private QueuesManager queuesManager;

    private FileWriter fw;

    private Thread t;

    private InputValidator inputValidator;

    public Controller(){
        userInterface = new UserInterface();
        randomGeneratedClients = new ArrayList<Client>();
        startSimulation();

    }

    /**
     * Method that activates the action listener for the START button.
     * The file is open for writing, the input is taken from the interface and validated.
     * Then the main thread is started.
     */
    private void startSimulation(){
        userInterface.startActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userInterface.clean();
                try {
                    fw = new FileWriter("log.txt");
                    fw.write("");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    inputValidator = new InputValidator();
                    inputValidator.validate(userInterface.getNumberOfClientsText(),userInterface.getNumberOfQueuesText(),
                            userInterface.getArrivalTimeMin(),userInterface.getArrivalTimeMax(),userInterface.getServiceTimeMin(),
                            userInterface.getServiceTimeMax(),userInterface.getTimeSimulationText());
                    arrivalMax = Integer.parseInt(userInterface.getArrivalTimeMax());
                    arrivalMin = Integer.parseInt(userInterface.getArrivalTimeMin());
                    serviceMax = Integer.parseInt(userInterface.getServiceTimeMax());
                    serviceMin = Integer.parseInt(userInterface.getServiceTimeMin());
                    N = Integer.parseInt(userInterface.getNumberOfClientsText());
                    Q = Integer.parseInt(userInterface.getNumberOfQueuesText());
                    simulationTime = Integer.parseInt(userInterface.getTimeSimulationText());
                    t = new Thread(Controller.this);
                    t.start();
                }catch (RuntimeException exception){
                    userInterface.displayErrorMessage(exception);
                }

            }
        });
    }

    /**
     * Method to add client to the log
     * @param client the client to be displayed
     * @param builder the string which represents the client, will be appended to the final string
     */
    private void displayClient(Client client, StringBuilder builder){
        builder.append("( ");
        builder.append(client.getId());
        builder.append(" ");
        builder.append(client.getArrivalTime());
        builder.append(" ");
        builder.append(client.getServiceTime());
        builder.append(" )");

    }

    /**
     * This method updates the user interface: the clients that are still in the clients list are displayed as "Waiting
     * clients", for each queue the the current processing client s displayed, then the rest of the clients if there are any
     * Also, the service time of the current processing clients are decremented in this method
     * @param currentTime the current time of the simulation
     */
    private void updateInterface(Integer currentTime){
        userInterface.setCurrentTime(currentTime);
        StringBuilder builder = new StringBuilder();
        builder.append("Simulation time : ");
        builder.append(currentTime);
        builder.append("\n");
        builder.append("waiting clients : ");
        for( Client client : randomGeneratedClients){
            displayClient(client,builder);
        }
        builder.append("\n");
        for( Queue queue : queuesManager.getQueues()){
            boolean noClients = true;
            builder.append("Queue ");
            builder.append(queue.getQueueNumber());
            builder.append(": ");
            Client client = queue.getCurrentClient();
            if(client != null){
               if(client.getServiceTime() > 0){
                   noClients = false;
                   displayClient(client,builder);
                   client.setServiceTime(client.getServiceTime() - 1);
                }
            }
            if(queue.getClients().length > 0){
                for(Client client1 : queue.getClients()){
                   displayClient(client1,builder);
                }
                noClients = false;
            }
            if(noClients){
                builder.append("no clients");
            }
            builder.append("\n");
        }
        builder.append("\n");
        try {
            fw.append(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        userInterface.updateLog(builder.toString());
    }

    /**
     * Method updates the analysis log in the interface, the average waiting time, the average service time, and the
     * peak hour
     */
    private void updateAnalysis(){
        StringBuilder builder = new StringBuilder();
        builder.append("\naverage waiting time : ");
        builder.append(averageWaiting);
        builder.append("\npeak hour : ");
        builder.append(peakHour);
        builder.append("\naverage service time : ");
        builder.append(averageServiceTime);
        try {
            fw.append(builder.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        userInterface.updateAnalysis(builder.toString());
    }

    /**
     * Method to run the thread with the condition that the current time < the simulation time
     * The queue manager is created and also the queues, the queues are started.
     * At each time, the clients that have the time equal to the current time are added to the queue with the minimum
     * waiting time.
     * Also at each time, the total number of clients is calculated, so the peak hour can be found.
     * After the simulation time, the queues are stopped and the average waiting time is calculated.
     */
    @Override
    public void run(){
        queuesManager = new QueuesManager(Q);
        generateClients();
        Integer currentTime = 0;
        Integer maxSize = 0;
        while(currentTime <= simulationTime){
            Iterator<Client> iterator = randomGeneratedClients.iterator();
            while(iterator.hasNext()){
                Client client = iterator.next();
                if(client.getArrivalTime() == currentTime){
                    queuesManager.dispatchClient(queuesManager.minimWaitingQueue(),client);
                    iterator.remove();
                }
            }
            Integer size = 0;
            for(Queue queue : queuesManager.getQueues()){
                if(queue.getWaitingTime().intValue() > 0){
                    queue.setWaitingTime(queue.getWaitingTime().decrementAndGet());
                }
                size += queue.getSize();
                size++;
            }
            if(size > maxSize ){
                maxSize = size;
                peakHour = currentTime;
            }
            updateInterface(currentTime);
            currentTime++;
            try{
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        queuesManager.stopQ();
        averageWaiting = (float) queuesManager.getTotalWaiting() / N;
        updateAnalysis();
    }

    /**
     * Method to generate a random number in a given (min,max) range
     * @param min lower bound of interval
     * @param max upper bound of interval
     * @return the random generated number
     */
    private int randomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    /**
     * Method to generate an array of N clients with random arrival and service times
     */
    private void generateClients(){
        Float totalServiceTime = 0.f;
        for(int i = 1; i<= N ;i++ ){
            int arrivalTime = randomNumber(arrivalMin, arrivalMax);
            int serviceTime = randomNumber(serviceMin,serviceMax);
            totalServiceTime += serviceTime;
            Client client = new Client( i, arrivalTime, serviceTime);
            randomGeneratedClients.add(client);
        }
        averageServiceTime = totalServiceTime/N;
        randomGeneratedClients.sort(Comparator.comparing(Client::getArrivalTime));
    }


}

