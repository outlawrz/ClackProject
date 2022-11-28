package main;

import data.ClackData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class ServerSideClientIO implements Runnable{
    private boolean closeConnection;
    private data.ClackData dataToReceiveFromClient;
    private data.ClackData dataToSendToClient;
    private ObjectInputStream inFromClient;
    private ObjectOutputStream outToClient;
    private ClackServer server;
    private Socket clientSocket;

    public ServerSideClientIO(ClackServer server, Socket clientSocket){
        this.server=server;
        this.clientSocket=clientSocket;
        this.closeConnection=false;
        this.dataToReceiveFromClient=null;
        this.dataToSendToClient=null;
        this.inFromClient =null;
        this.outToClient =null;
    }

    public void run(){
        try{
            outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            inFromClient = new ObjectInputStream(clientSocket.getInputStream());
            while(!closeConnection){
                receiveData();
                this.server.broadcast(dataToSendToClient);
            }
        }catch(IOException ioe){
            System.err.println("Error reading input or output stream");
            closeConnection=true;
        }
    }

    public void receiveData(){
        try {
            dataToReceiveFromClient = (ClackData) inFromClient.readObject();
            if (dataToReceiveFromClient.getType() == ClackData.CONSTANT_LOGOUT) {
                this.server.remove(this);
            } else {
                System.out.println(dataToReceiveFromClient);
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
            //outToClient.close();
        } catch (IOException ioe) {
            System.err.println("Error in writing to stream or closing stream");
            closeConnection = true;
        }
    }

    public void setDataToSendToClient(data.ClackData dataToSendToClient){
        this.dataToSendToClient = dataToSendToClient;
    }
}
