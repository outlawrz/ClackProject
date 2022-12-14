package main;

import data.ClackData;
import data.FileClackData;
import data.MessageClackData;
import data.ListUsersClackData;

import java.io.*;
import java.net.*;
import java.lang.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Time;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

/**
 * The ClackClient class represents the client user. A ClackClient object contains the username,
 * host name of the server connected to, port number connected to, and a boolean designating
 * whether the connection is open or not. The ClackClient object will also have two ClackData
 * objects representing data sent to the server and data received from the server.
 *
 * @author Xinchao Song
 */
public class ClackClient {
    private static final int DEFAULT_PORT = 1738;  // The default port number

    private String userName;  // A string representing the name of the client
    private String hostName;  // A string representing the name of the computer representing the server
    private int port; // An integer representing the port number on the server connected to
    private boolean closeConnection; // A boolean representing whether the connection is closed or not
    private ClackData dataToSendToServer; // A ClackData object representing the data sent to the server
    private ClackData dataToReceiveFromServer; // A ClackData object representing the data received from the server
    private Scanner inFromStd;
    private final static String CONSTANT_KEY= "RAMP";
    private ObjectOutputStream outToServer=null;
    private ObjectInputStream inFromServer=null;

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
        if(userName==null) {
            throw new IllegalArgumentException("Invalid Argument for user name");
        }
        if(hostName==null) {
            throw new IllegalArgumentException("Invalid Argument for host name");
        }
        if(port<1024) {
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
     */
    public void start() {
        try {
            Socket skt = new Socket(this.hostName , this.port);
            System.out.println("Connected.\n-");

            inFromServer = new ObjectInputStream(skt.getInputStream());
            outToServer = new ObjectOutputStream(skt.getOutputStream());

            ClientSideServerListener listener = new ClientSideServerListener(this);
            Thread listenerThread = new Thread(listener);
            listenerThread.start();
            inFromStd = new Scanner(System.in);

            while(!closeConnection) {
                    this.readClientData();
                    this.sendData();
                    System.out.println("-");
            }

            inFromStd.close();
            skt.close();
        } catch(UnknownHostException uhe) {
            System.err.println("Unknown host");
        } catch(SecurityException se) {
            System.err.println("Untrusted subclass tried to override security-sensitive methods");
        } catch(IllegalStateException ise) {
            System.err.println("Attempted to perform search operations after scanner had closed");
        } catch(NullPointerException npe) {
            System.err.println("Stream is null");
        } catch(StreamCorruptedException sce) {
            System.err.println("Stream header is incorrect");
        } catch(NoRouteToHostException nrthe) {
            System.err.println("Could not find host");
        } catch(ConnectException ce) {
            System.err.println("Connection refused");
        } catch(IOException ioe) {
            System.err.println("Error in closing or reading input or output stream");
        }
    }

    public void readClientData() {
        String input = inFromStd.next();
        if (input.equals("LOGOUT") || input.equals("DONE")) {
            closeConnection = true;
            dataToSendToServer = new MessageClackData(this.userName, "", dataToSendToServer.CONSTANT_LOGOUT);
        } else if(input.length() >= 8 && input.substring(0,7).equals("SENDFILE")) {
            String filename = inFromStd.next();
            dataToSendToServer = new FileClackData(this.userName, filename, dataToSendToServer.CONSTANT_SENDFILE);
        } else if (input.equals("LISTUSERS")) {
            dataToSendToServer = new ListUsersClackData(userName, ClackData.CONSTANT_LISTUSERS);
        } else {
            input += inFromStd.nextLine();
            dataToSendToServer = new data.MessageClackData(this.userName, input, dataToSendToServer.CONSTANT_SENDMESSAGE);
        }
    }

    public void sendData() {
        try {
            outToServer.writeObject(dataToSendToServer);
        } catch (IOException ioe) {
            System.err.println("Error in writing to stream or closing stream when sending to server");
        }
    }

    public void receiveData() {
        try {
            dataToReceiveFromServer = (ClackData) inFromServer.readObject();
        } catch (IOException ioe) {
            System.err.println("Error in reading or closing the stream");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Error in finding object from stream");
        }
    }

    /**
     * Prints the received data to the standard output.
     */
    public void printData() {
            if (dataToReceiveFromServer.getType() == ClackData.CONSTANT_LOGOUT) {
                System.out.println("Logging out.");
            } else {
                System.out.println("Username: " + dataToReceiveFromServer.getUserName() + ", Type: " + dataToReceiveFromServer.getType() + ", Date: " + dataToReceiveFromServer.getDate());
                System.out.println(dataToReceiveFromServer.getData());
            }
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

    public boolean getCloseConnection() {
        return this.closeConnection;
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
    public static void main(String args[])
    {
        try {
            System.out.println("Type username@hostname:port or press enter to use defaults: ");
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in));
            String line = bufferedreader.readLine();
            if (line.isEmpty()) {
                ClackClient client = new ClackClient();
                System.out.println("Using username: Anon, hostname: localhost, port: " + DEFAULT_PORT);
                client.start();
            } else if(line.contains("@")&& line.contains(":")) {
                String uname= line.substring(0,line.indexOf("@"));
                String hname= line.substring(line.indexOf("@")+1,line.indexOf(":"));
                int portnum= parseInt(line.substring(line.indexOf(":")+1));
                ClackClient client = new ClackClient(uname, hname, portnum);
                System.out.println("Using Username: " + uname + ", Hostname: " + hname + ", Port: " + portnum);
                client.start();
            } else if(line.contains("@")) {
                String uname= line.substring(0,line.indexOf("@"));
                String hname= line.substring(line.indexOf("@")+1);
                ClackClient client = new ClackClient(uname, hname);
                System.out.println("Using Username: " + uname + ", Hostname: " + hname + ", Port: " + DEFAULT_PORT);
                client.start();
            }
            else {
                ClackClient client = new ClackClient(line);
                System.out.println("Using Username: " + line + ", Hostname: localhost ," + "Port: " + DEFAULT_PORT);
                client.start();
            }
        } catch (IOException ioe) {
            System.err.println("Error reading from buffer");
        }
    }
}
