package model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Queue implements Runnable {

    private BlockingQueue<Client> clients;
    private AtomicInteger waitingTime;
    private Integer queueNumber;
    private AtomicBoolean running;
    private Client currentClient;


    public Queue(Integer number){
        queueNumber = number;
        clients = new LinkedBlockingQueue<Client>();
        waitingTime = new AtomicInteger(0);
        running = new AtomicBoolean(true);
    }

    public AtomicBoolean getRunning() {
        return running;
    }

    public void setRunning(boolean run) {
        this.running.set(run);
    }

    public Integer getQueueNumber() {
        return queueNumber;
    }


    /**
     * method to add client in the queue, also increase the waiting time of the queue with the service time of the
     * added client
     * @param client the client to be added
     */
    public void addClient(Client client){
        clients.add(client);
        this.waitingTime.addAndGet(client.getServiceTime());
    }

    public AtomicInteger getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int time) {
       this.waitingTime.set(time);
    }

    public Client getCurrentClient() {
        return currentClient;
    }

    public void setCurrentClientToNull() {
        this.currentClient = null;
    }

    /**
     * Function that runs the thread
     * while the thread its running the function dequeues the first member of the queue and puts the thread to sleep for
     * its service time seconds.
     * The take() method waits until there is a client in the queue so it can dequeue it.
     */
    @Override
    public void run() {
        while (running.get()) {
                try {
                    this.currentClient = this.clients.take();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
                   try {
                       Thread.sleep(currentClient.getServiceTime() * 1000);
                   } catch (Exception e) {
                       System.out.println(e.getMessage());
               }
        }
    }

    /**
     * method that return the clients that are waiting in this queue
     * @return an array of clients
     */
    public Client[] getClients(){
        Client[] clientsArray = new Client[clients.size()];
        clients.toArray(clientsArray);
        return clientsArray;
    }

    /**
     *
     * @return the current size of the queue
     */
    public Integer getSize(){
        return clients.size();
    }

}
