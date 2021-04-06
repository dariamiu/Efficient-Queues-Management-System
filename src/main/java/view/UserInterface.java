package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UserInterface extends JFrame {

    private JTextField numberOfClientsText = new JTextField();
    private JTextField numberOfQueuesText = new JTextField();
    private JTextField timeSimulationText = new JTextField();
    private JTextField arrivalTimeMin = new JTextField();
    private JTextField arrivalTimeMax = new JTextField();
    private JTextField serviceTimeMin = new JTextField();
    private JTextField serviceTimeMax = new JTextField();
    private JTextArea currentTime = new JTextArea(1,1);

    private JButton start = new JButton("START");

    private JScrollPane logs = new JScrollPane();

    private JTextArea textArea = new JTextArea(20, 20);

    private JTextArea analysis = new JTextArea(20,20);


    /**
     * set the aspect of the JFrame
     */
    public UserInterface(){
        this.setTitle("Queue Simulator");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(218,189,232));
        panel.setLayout(null);
        initializePanel(panel);
        this.setContentPane(panel);
        this.setVisible(true);
    }

    /**
     * Method to initialize all the components in the panel, set their sizes, positions and color
     * @param panel the panel in the JFrame
     */
    private void initializePanel(JPanel panel){

        JLabel numberOfClients = new JLabel("Number of clients");
        JLabel numberOfQueues = new JLabel("Number of queues");
        JLabel timeSimulation = new JLabel("Simulation time");
        JLabel arrivalTimeInterval = new JLabel("Arrival time interval");
        JLabel serviceTimeInterval = new JLabel("Service time interval");
        JLabel analysisLabel = new JLabel("Analysis results");
        JLabel events = new JLabel("Log of events");

        numberOfClients.setBounds(10,10,130,30);
        numberOfClientsText.setBounds(150, 10, 40,30);

        numberOfQueues.setBounds(10,70,130,30);
        numberOfQueuesText.setBounds(150,70,40,30);

        arrivalTimeInterval.setBounds(220, 10,130,30);
        arrivalTimeMin.setBounds(360, 10,40,30);
        arrivalTimeMax.setBounds(420,10,40,30);

        serviceTimeInterval.setBounds(220,70,130,30);
        serviceTimeMin.setBounds(360, 70, 40, 30);
        serviceTimeMax.setBounds(420,70,40,30);

        timeSimulation.setBounds(500,10,130,30);
        timeSimulationText.setBounds(620,10,40,30);

        start.setBounds(500,70,200,30);

        analysisLabel.setBounds(10,140,200,30);
        analysis.setBounds(10,190,270,100);

        currentTime.setBounds(130,310,30,30);
        currentTime.setBackground(Color.white);

        events.setBounds(10,310,130,30);

        panel.add(numberOfClients);
        panel.add(numberOfQueues);
        panel.add(numberOfClientsText);
        panel.add(numberOfQueuesText);

        panel.add(timeSimulation);
        panel.add(timeSimulationText);
        panel.add(arrivalTimeInterval);
        panel.add(serviceTimeInterval);
        panel.add(arrivalTimeMax);
        panel.add(arrivalTimeMin);
        panel.add(serviceTimeMin);
        panel.add(serviceTimeMax);
        panel.add(start);
        panel.add(analysisLabel);
        panel.add(currentTime);
        panel.add(events);
        panel.add(analysis);

        logs = new JScrollPane(textArea);
        logs.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        logs.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        logs.setBounds(10,360,770,170);
        panel.add(logs);
    }

    /**
     * Method used to remove the content of the text areas, used in controller
     */
    public void clean(){
        textArea.setText("");
        analysis.setText("");
    }

    /**
     * Method to add text to the log text in real time, used in controller
     * @param content text to add to log
     */
    public void updateLog(String content){
        textArea.setText(content);
    }

    /**
     * Method used to add the analysis results after the simulation is ended, used in controller
     * @param content text to add to analysis
     */
    public void updateAnalysis(String content){
        analysis.setText(content);
    }

    /**
     * getter for the number of clients, used in controller
     * @return the number of clients
     */

    public String getNumberOfClientsText() {
        return numberOfClientsText.getText();
    }

    public String getNumberOfQueuesText() {
        return numberOfQueuesText.getText();
    }


    public String getTimeSimulationText() {
        return timeSimulationText.getText();
    }


    public String getArrivalTimeMin() {
        return arrivalTimeMin.getText();
    }


    public String getArrivalTimeMax() {
        return arrivalTimeMax.getText();
    }

    public String getServiceTimeMin() {
        return serviceTimeMin.getText();
    }

    public String getServiceTimeMax() {
        return serviceTimeMax.getText();
    }

    /**
     * Method for adding the action listener to button START, will be used in the controller
     * @param actionListener the action given to the button START
     */
    public void startActionListener(final ActionListener actionListener){
        start.addActionListener(actionListener);
    }

    public void setCurrentTime(Integer currentTime) {
        this.currentTime.setText(currentTime.toString());
    }

    /**
     * Method to display the errors as messages to the user when the validation of the input fails
     * @param exception message about the error
     */
    public void displayErrorMessage(Exception exception) {
        if (exception != null) {
            String message = exception.getMessage();
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
