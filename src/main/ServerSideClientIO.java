package main;

import data.ClackData;
import data.MessageClackData;
import data.FileClackData;
import data.ListUsersClackData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class ServerSideClientIO implements Runnable{
    private boolean closeConnection;
    private data.ClackData dataToReceieveFromClient;
    private data.ClackData dataToSendToClient;
    private ObjectInputStream inFromClient;
    private ObjectOutputStream outToClient;
    private ClackServer server;
    private Socket clientSocket;

    public ServerSideClientIO(ClackServer server, Socket clientSocket){
        this.server = server;
        this.clientSocket = clientSocket;
        this.closeConnection = false;
        this.dataToReceieveFromClient = null;
        this.dataToSendToClient = null;
        this.inFromClient = null;
        this.outToClient = null;
    }

    @Override
    public void run(){
        try{
            outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            inFromClient = new ObjectInputStream(clientSocket.getInputStream());
            while(!closeConnection){
                this.receiveData();
                this.server.LUClackData.addUser(dataToReceieveFromClient.getUserName());
                if (dataToReceieveFromClient instanceof ListUsersClackData) {
                    setDataToSendToClient(new MessageClackData("Server", this.server.LUClackData.getData(), ClackData.CONSTANT_LISTUSERS));
                    System.out.println(this.server.LUClackData.getData());
                } else {
                    this.server.broadcast(dataToReceieveFromClient);
                }
                this.server.broadcast(dataToSendToClient);
                this.sendData();
            }
        }catch(IOException ioe){
            System.err.println("Error reading input or output stream");
            closeConnection=true;
        }
    }

    public void receiveData(){
        try {
            dataToReceieveFromClient = (ClackData) inFromClient.readObject();
            if (dataToReceieveFromClient.getType() == ClackData.CONSTANT_LOGOUT) {
                this.server.broadcast(dataToSendToClient);
                closeConnection = true;
                this.sendData();
                server.remove(this);
                System.out.println(dataToReceieveFromClient.getUserName() + " logging out");
            } else {
                System.out.println(dataToReceieveFromClient);
            }
        } catch (IOException ioe) {
            System.err.println("Error in reading or closing the stream");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Error in finding object from stream");
        }
    }

    public void sendData(){
        try {
            outToClient.writeObject(dataToSendToClient);
        } catch (IOException ioe) {
            System.err.println("Error in writing to stream or closing stream");
            closeConnection = true;
        }
    }

    public void setDataToSendToClient(data.ClackData dataToSendToClient){
        this.dataToSendToClient = dataToSendToClient;
    }
}
