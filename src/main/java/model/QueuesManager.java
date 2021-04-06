package model;

import java.util.ArrayList;
import java.util.List;

public class QueuesManager {

    private List<Queue> queues;
    private int nrQueues;
    private int totalWaiting;

    /**
     * Constructor for queue manager in which all the queues are started
     * @param nrQueues is the number of queues in the simulation
     */
    public QueuesManager( int nrQueues){
        queues = new ArrayList<>();
        this.nrQueues = nrQueues;
        for( int i = 0; i < nrQueues ; i++){
            queues.add(new Queue(i+1));
            Thread thread = new Thread(queues.get(i));
            thread.start();

        }
    }

    /**
     * Method that adds client to the chosen queue
     * The time that the client has to wait for is added to the total waiting time (for computing average waiting)
     * @param i the queue in which the client should be enqueued
     * @param client the client to be enqueued
     */
    public void dispatchClient(int i, Client client){
        this.totalWaiting += queues.get(i).getWaitingTime().intValue();
        queues.get(i).addClient(client);
    }

    public List<Queue> getQueues() {
        return queues;
    }

    /**
     * the total waiting time is computed as the sum of the waiting times of each client that ever was at a queue
     * @return
     */
    public int getTotalWaiting() {
        return totalWaiting;
    }

    /**
     * determines the queue where the waiting time is minimum
     * @return the index of the queue with the minimum waiting time
     */
    public int minimWaitingQueue(){
        int min = 1000000;
        int minIndex = 0;
        for( Queue queue :queues){
            if(queue.getWaitingTime().intValue() < min){
                min = queue.getWaitingTime().intValue();
                minIndex = queue.getQueueNumber() - 1;
            }
        }
        return minIndex;
    }

    /**
     * stops all the queues
     */
    public void stopQ(){
        for(Queue queue : queues){
            queue.setRunning(false);
            //System.out.println(queue.getRunning());
        }
    }
}
