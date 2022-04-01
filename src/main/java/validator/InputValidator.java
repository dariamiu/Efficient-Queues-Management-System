package validator;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputValidator {

    public List<String> readColumns(String inputFilePath){

        List<String> columns = new ArrayList<>();
        try{

            File inputF = new File(inputFilePath);
            InputStream inputFS = new FileInputStream(inputF);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));

            String firstLine = br.readLine();
            columns = Arrays.asList(firstLine.split(","));

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return columns ;
    }
    /**
     *
     */
    /**
     * Method to check is the inputs are integers
     * @param s the string which needs to be checked if ot contains an integer
     * @return the parsed string
     */
    private int validateInt(String s){
        int number;
        try{
            number = Integer.parseInt(s);
        }catch (NumberFormatException e){
            throw new RuntimeException("some input contains a non-integer value");
        }
        return number;

    }

    /**
     * The method checks if the input is valid: no empty fields, no other characters than integer numbers, conditions for
     * the intervals and the simulation time to be valid
     * @param N the string written in the GUI as the number of clients
     * @param Q the string written in GUI as the number of queues
     * @param arrivalMin the string written in GUI as arrival min
     * @param arrivalMax the string written in GUI as arrival max
     * @param serviceMin the string written in GUI as service min
     * @param serviceMax the string written in GUI as arrival max
     * @param timeSimulation the string written in GUI as time simulation
     */
    public void validate(String N, String Q, String arrivalMin, String arrivalMax, String serviceMin, String serviceMax, String timeSimulation){
        if(N.isEmpty() || Q.isEmpty() || arrivalMax.isEmpty() || arrivalMin.isEmpty() || serviceMax.isEmpty() || serviceMin.isEmpty() || timeSimulation.isEmpty()){
            throw new RuntimeException("some inputs are empty!");
        }
        if( validateInt(N) <= 0 ||
                validateInt(Q) <= 0 ||
                validateInt(arrivalMax) <=0 ||
                validateInt(arrivalMin) <=0 ||
                validateInt(serviceMax) <=0 ||
                validateInt(serviceMin) <= 0 ||
                validateInt(timeSimulation) <= 0){
            throw new RuntimeException("inputs can't be <= 0!");
        }

        if(Integer.parseInt(arrivalMax) < Integer.parseInt(arrivalMin)){
            throw new RuntimeException("invalid arrival interval");
        }

        if(Integer.parseInt(serviceMax) < Integer.parseInt(serviceMin)){
            throw new RuntimeException("invalid service interval");
        }

        if(Integer.parseInt(serviceMax) > Integer.parseInt(timeSimulation) || Integer.parseInt(arrivalMax) >= Integer.parseInt(timeSimulation)
        || Integer.parseInt(arrivalMin) >Integer.parseInt(timeSimulation) || Integer.parseInt(serviceMin) > Integer.parseInt(timeSimulation)){
            throw new RuntimeException("invalid interval for the time simulation");
        }
    }
}
