package main;

public class ClientSideServerListener implements Runnable{
    private ClackClient client;

    ClientSideServerListener(ClackClient client){
        this.client = client;
    }

    @Override
    public void run(){
        while (client.getCloseConnection()!=true){
            client.receiveData();
            client.printData();
        }
    }
}
