package main;

import data.ClackData;
import data.FileClackData;
import java.io.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Scanner;

/**
 * The ClackClient class represents the client user. A ClackClient object contains the username,
 * host name of the server connected to, port number connected to, and a boolean designating
 * whether the connection is open or not. The ClackClient object will also have two ClackData
 * objects representing data sent to the server and data received from the server.
 *
 * @author Xinchao Song
 */
public class ClackClient {
    private static final int DEFAULT_PORT = 7000;  // The default port number

    private String userName;  // A string representing the name of the client
    private String hostName;  // A string representing the name of the computer representing the server
    private int port; // An integer representing the port number on the server connected to
    private boolean closeConnection; // A boolean representing whether the connection is closed or not
    private ClackData dataToSendToServer; // A ClackData object representing the data sent to the server
    private ClackData dataToReceiveFromServer; // A ClackData object representing the data received from the server
    private Scanner inFromStd;
    private final static String CONSTANT_KEY= "RAMP";
    private ObjectInputStream inFromServer;
    private ObjectOutputStream outToServer;
    /**
     * The constructor to set up the username, host name, and port.
     * The connection should be set to be open (closeConnection = false).
     * Should set dataToSendToServer and dataToReceiveFromServer as null.
     *
     * @param userName a string representing the username of the client
     * @param hostName a string representing the host name of the server
     * @param port     an int representing the port number on the server connected to
     */
    public ClackClient(String userName, String hostName, int port) {
        this.userName = userName;
        this.hostName = hostName;
        this.port = port;
        this.closeConnection = false;
        this.dataToSendToServer = null;
        this.dataToReceiveFromServer = null;
        this.inFromServer = null;
        this.outToServer = null;
        if(userName==null){
            throw new IllegalArgumentException("Invalid Argument for user name");
        }
        if(hostName==null){
            throw new IllegalArgumentException("Invalid Argument for host name");
        }
        if(port<1024)
        {
            throw new IllegalArgumentException("Port number is not large enough");
        }
    }

    /**
     * The constructor to set up the port to the default port number 7000.
     * The default port number should be set up as constant (e.g., DEFAULT_PORT).
     * This constructor should call another constructor.
     *
     * @param userName a string representing the username of the client
     * @param hostName a string representing the host name of the server
     */
    public ClackClient(String userName, String hostName) {
        this(userName, hostName, DEFAULT_PORT);
    }

    /**
     * The constructor that sets the host name to be "localhost"
     * (i.e., the server and the client programs run on the same computer).
     * This constructor should call another constructor.
     *
     * @param userName a string representing the username of the client
     */
    public ClackClient(String userName) {
        this(userName, "localhost");
    }

    /**
     * The default constructor that sets the anonymous user.
     * This constructor should call another constructor.
     */
    public ClackClient() {
        this("Anon");
    }

    /**
     * Starts the client.
     * Does not return anything.
     * For now, it should have no code, just a declaration.
     */
    public void start() {
        InputStreamReader inputStream = new InputStreamReader(System.in);
        Scanner inFromStd = new Scanner(inputStream);
        while(!closeConnection) {
            readClientData();
            dataToReceiveFromServer = dataToSendToServer;
            printData();
        }
        this.inFromStd.close();
    }

    /**
     * Reads the data from the client.
     * Does not return anything.
     * For now, it should have no code, just a declaration.
     */
    public void readClientData() {
            String input= inFromStd.next();
            if (input.equals ("DONE")) {
                this.closeConnection = true;
            }
            else if((input.substring(0,8)).equals("SENDFILE")){
                String filename= inFromStd.next();
                dataToSendToServer= new FileClackData(this.userName,filename, dataToSendToServer.CONSTANT_SENDFILE);
            }
            else if (input.equals("LISTUSERS")){
                System.out.println("Unable to perform at this time");
            }
            else {
                while (inFromStd.hasNext()) {
                    input = input + inFromStd.next();
                }
                dataToSendToServer = new data.MessageClackData(this.userName, input, dataToSendToServer.CONSTANT_SENDMESSAGE);
            }
    }

    /**
     * Sends data to server.
     * Does not return anything.
     * For now, it should have no code, just a declaration.
     */
    public void sendData() {
    }

    /**
     * Receives data from the server.
     * Does not return anything.
     * For now, it should have no code, just a declaration.
     */
    public void receiveData() {
    }

    /**
     * Prints the received data to the standard output.
     * For now, it should have no code, just a declaration.
     */
    public void printData() {
        System.out.println("Username: " +dataToReceiveFromServer.getUserName()+ "Type: "+ dataToReceiveFromServer.getType() + "Date: " +dataToReceiveFromServer.getDate());
    }

    /**
     * Returns the username.
     *
     * @return this.userName
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Returns the host name.
     *
     * @return this.hostName
     */
    public String getHostName() {
        return this.hostName;
    }

    /**
     * Returns the port.
     *
     * @return this.port
     */
    public int getPort() {
        return this.port;
    }

    @Override
    public int hashCode() {
        // The following is only one of many possible implementations to generate the hash code.
        // See the hashCode() method in other classes for some different implementations.

        int result = 23;

        // It is okay to select only some instance variables to calculate the hash code
        // but must use the same instance variables with equals() to maintain consistency.
        result = 31 * result + Objects.hashCode(this.userName);
        result = 31 * result + Objects.hashCode(this.hostName);
        result = 31 * result + this.port;
        result = 31 * result + Objects.hashCode(this.closeConnection);
        result = 31 * result + Objects.hashCode(this.dataToSendToServer);
        result = 31 * result + Objects.hashCode(this.dataToReceiveFromServer);

        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ClackClient)) {
            return false;
        }

        // Casts other to be a ClackClient to access its instance variables.
        ClackClient otherClackClient = (ClackClient) other;

        // Compares all comparable instance variables of both ClackClient objects that determine uniqueness.
        // It is okay to select only some instance variables for comparison but must use the same
        // instance variables with hashCode() to maintain consistency.
        return this.userName.equals(otherClackClient.userName) &&
                this.hostName.equals(otherClackClient.hostName) &&
                this.port == otherClackClient.port &&
                this.closeConnection == otherClackClient.closeConnection &&
                Objects.equals(this.dataToSendToServer, otherClackClient.dataToSendToServer) &&
                Objects.equals(this.dataToReceiveFromServer, otherClackClient.dataToReceiveFromServer);
    }

    @Override
    public String toString() {
        // Should return a full description of the class with all instance variables.
        return "This instance of ClackClient has the following properties:\n"
                + "Username: " + this.userName + "\n"
                + "Host name: " + this.hostName + "\n"
                + "Port number: " + this.port + "\n"
                + "Connection status: " + (this.closeConnection ? "Closed" : "Open") + "\n"
                + "Data to send to the server: " + this.dataToSendToServer + "\n"
                + "Data to receive from the server: " + this.dataToReceiveFromServer + "\n";
    }
}
