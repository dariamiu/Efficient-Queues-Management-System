package model;

/**
 * The Class Client does not contain any special methods, just getters and setters for its attributes
 */
public class Client {

    private Integer id;
    private Integer arrivalTime;
    private Integer serviceTime;

    public Client(Integer id, Integer arrivalTime, Integer processingTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = processingTime;
    }

    public Integer getId() {
        return id;
    }

    public Integer getArrivalTime() {
        return arrivalTime;
    }

    public Integer getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(Integer serviceTime) {
        this.serviceTime = serviceTime;
    }
}
