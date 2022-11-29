package main;

import data.ClackData;

import java.nio.channels.IllegalBlockingModeException;
import java.util.*;
import java.net.*;
import java.io.*;
import java.lang.*;

import static java.lang.Integer.parseInt;

/**
 * The ClackServer class is a blueprint for a ClackServer object that contains information about the
 * port number that clients connect to, a boolean representing whether the server needs to be
 * closed or not, and ClackData objects representing data sent to and received from the client. The
 * server class does not need to know the host name (as the server program runs on its own computer),
 * it just needs to know what port the clients connect to. In our application, all clients will connect
 * to a single port.
 *
 * @author xinchaosong
 */
public class ClackServer {
    private static final int DEFAULT_PORT = 1738;  // The default port number

    private int port; // An integer representing the port number on the server connected to
    private boolean closeConnection; // A boolean representing whether the connection is closed or not
    private ArrayList<ServerSideClientIO> serverSideClientIOList;
    /**
     * The constructor that sets the port number.
     * Should set dataToReceiveFromClient and dataToSendToClient as null.
     *
     * @param port an int representing the port number on the server connected to
     */
    public ClackServer(int port) {
        try{
            this.port = port;
            this.closeConnection = false;
            serverSideClientIOList = new ArrayList<ServerSideClientIO>();

        } catch(IllegalArgumentException iae){
            System.err.println("Port number is less than 1024");
        }

    }

    /**
     * The default constructor that sets the port to the default port number 7000.
     * The default port number should be set up as constant (e.g., DEFAULT_PORT).
     * This constructor should call another constructor.
     */
    public ClackServer() {
        this.port = DEFAULT_PORT;
    }

    /**
     * Starts the server.
     * Does not return anything.
     * For now, it should have no code, just a declaration.
     */
    public void start() {
        try {
            ServerSocket sskt = new ServerSocket(port);
            System.out.println(sskt);
            while(!closeConnection) {
                Socket cskt = sskt.accept();
                ServerSideClientIO sscio = new ServerSideClientIO(this,cskt);
                serverSideClientIOList.add(sscio);
                Thread runningServer = new Thread(sscio);
                runningServer.start();
            }
            sskt.close();
        } catch(SecurityException se){
            System.err.println("Operation not allowed for security reasons");
        } catch(IllegalArgumentException iae){
            System.err.println("Port number not allowed");

        } catch(SocketTimeoutException ste){
            System.err.println("Socket took too long to connect");
        } catch(IllegalBlockingModeException ibme){
            System.err.println("Socket has an associated channel, not ready to connect");
        } catch(NullPointerException npe){
            System.err.println("Stream is null");
        } catch(StreamCorruptedException sce){
            System.err.println("Stream header could not be found");
        } catch(IOException ioe){
            System.err.println("Input/Output error in stream");
        }
    }

    /**
     * Receives data from client.
     */


    /**
     * Sends data to client.
     * Does not return anything.
     * For now, it should have no code, just a declaration.
     */

    public synchronized void broadcast(ClackData dataToBroadcastToClients)
    {
        for(int i=0;i<serverSideClientIOList.size();i++){
            serverSideClientIOList.get(i).setDataToSendToClient(dataToBroadcastToClients);
            serverSideClientIOList.get(i).sendData();
        }
    }

    public synchronized void remove (ServerSideClientIO serverSideClientToRemove)
    {
        for(int i=0;i<serverSideClientIOList.size();i++){
            if((this.serverSideClientIOList.get(i)).equals(serverSideClientToRemove)){
                serverSideClientIOList.remove(i);
            }
        }
    }

    /**
     * Returns the port.
     *
     * @return this.port.
     */
    public int getPort() {
        return this.port;
    }


    @Override
    public int hashCode() {
        // The following is only one of many possible implementations to generate the hash code.
        // See the hashCode() method in other classes for some different implementations.
        // It is okay to select only some of the instance variables to calculate the hash code
        // but must use the same instance variables with equals() to maintain consistency.
        return Objects.hash(this.port, this.closeConnection, this.serverSideClientIOList);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ClackServer)) {
            return false;
        }

        // Casts other to be a ClackServer to access its instance variables.
        ClackServer otherClackServer = (ClackServer) other;

        // Compares the selected instance variables of both ClackServer objects that determine uniqueness.
        // It is okay to select only some of the instance variables for comparison but must use the same
        // instance variables with hashCode() to maintain consistency.
        return this.port == otherClackServer.port
                && this.closeConnection == otherClackServer.closeConnection
                && Objects.equals(this.serverSideClientIOList, otherClackServer.serverSideClientIOList);
    }

    @Override
    public String toString() {
        // Should return a full description of the class with all instance variables.
        return "This instance of ClackServer has the following properties:\n"
                + "Port number: " + this.port + "\n"
                + "Connection status: " + (this.closeConnection ? "Closed" : "Open") + "\n";

    }
    public static void main(String args[])
    {
        try {
            System.out.println("Type port or press enter to use default port: ");
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in));
            String line = bufferedreader.readLine();
            String newline = line;
            if (newline.isEmpty()) {
                ClackServer server = new ClackServer();
                System.out.println("Using port: " + DEFAULT_PORT);
                server.start();
            } else {
                ClackServer server = new ClackServer(parseInt(newline));
                System.out.println("Using port: " + newline);
                server.start();
            }
        } catch(IOException ioe) {
            System.err.println("Error reading from buffer");
        }
    }

}
