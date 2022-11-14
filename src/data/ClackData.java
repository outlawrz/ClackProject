package data;

import java.io.Serializable;
import java.util.Date;

/**
 * Class ClackData is a superclass that represents the data sent between the client and the
 * server. An object of type ClackData consists of the username of the client user, the date
 * and time at which the data was sent and the data itself, which can either be a message
 * (MessageClackData) or the name and contents of a file (FileClackData). Note that ClackData
 * should not be instantiable.
 *
 * @author xinchaosong
 */
public abstract class ClackData implements Serializable {
    /**
     * For giving a listing of all users connected to this session.
     */
    public static final int CONSTANT_LISTUSERS = 0;

    /**
     * For logging out, i.e., close this client's connection.
     */
    public static final int CONSTANT_LOGOUT = 1;

    /**
     * For sending a message.
     */
    public static final int CONSTANT_SENDMESSAGE = 2;

    /**
     * For sending a file.
     */
    public static final int CONSTANT_SENDFILE = 3;

    /**
     * A string representing the name of the client user.
     */
    protected String userName;

    /**
     * An integer representing the kind of data exchanged between the client and the server.
     */
    protected int type;

    /**
     * A Date object representing the date and time when ClackData object is created.
     */
    protected Date date;

    /**
     * The constructor to set up the instance variable username and type.
     * The instance variable date should be created automatically here.
     *
     * @param userName a string representing the name of the client user
     * @param type     an int representing the data type
     */
    public ClackData(String userName, int type) {
        this.userName = userName;
        this.type = type;
        this.date = new Date();
    }

    /**
     * The constructor to create an anonymous user, whose name should be "Anon".
     * This constructor should call another constructor.
     *
     * @param type an int representing the data type
     */
    public ClackData(int type) {
        this("Anon", type);
    }

    /**
     * The default constructor.
     * This constructor should call another constructor.
     * type should get defaulted to CONSTANT_LOGOUT.
     */
    public ClackData() {
        this(CONSTANT_LOGOUT);
    }

    /**
     * Returns the type.
     *
     * @return this.type
     */
    public int getType() {
        return this.type;
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
     * Returns the date.
     *
     * @return this.date
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * The abstract method to return the data contained in this class
     * (contents of instant message or contents of a file).
     *
     * @return data
     */
    public abstract String getData();
    protected String encrypt( String inputStringtoEncrypt, String key ){
        String resultString = "";
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        int ind = 0;

        if(key == null){ key = "a"; }

        if(inputStringtoEncrypt == null){ return ""; }

        for(int i = 0; i < inputStringtoEncrypt.length(); i++){
            String s = inputStringtoEncrypt.split("")[i];
            int k = alphabet.indexOf(key.toLowerCase().split("")[ind % key.length()]);

            if(alphabet.contains(s)){
                resultString += alphabet.split("")[(alphabet.indexOf(s) + k) % 26];
                ++ind;
            } else if (alphabet.toUpperCase().contains(s)) {
                resultString += alphabet.split("")[(alphabet.toUpperCase().indexOf(s) + k) % 26];
                ++ind;
            } else {
                resultString += s;
            }
        }

        return resultString;
    }

    protected String decrypt( String inputStringtoDecrypt, String key ){
        String resultString = "";
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        int ind = 0;

        if(key == null){ key = "a"; }

        if(inputStringtoDecrypt == null){ return ""; }

        for(int i = 0; i < inputStringtoDecrypt.length(); i++){
            String s = inputStringtoDecrypt.split("")[i];
            int k = alphabet.indexOf(key.toLowerCase().split("")[ind % key.length()]);

            if(alphabet.contains(s)){
                if(alphabet.indexOf(s) - k < 0){
                    resultString += alphabet.split("")[26 + (alphabet.indexOf(s) - k)];
                } else {
                    resultString += alphabet.split("")[(alphabet.indexOf(s) - k) % 26];
                }
                ++ind;
            } else if (alphabet.toUpperCase().contains(s)) {
                if(alphabet.toUpperCase().indexOf(s) - k < 0){
                    resultString += alphabet.toUpperCase().split("")[26 + (alphabet.toUpperCase().indexOf(s) - k)];
                } else {
                    resultString += alphabet.toUpperCase().split("")[(alphabet.toUpperCase().indexOf(s) - k) % 26];
                }
                ++ind;
            } else {
                resultString += s;
            }
        }

        return resultString;
    }

}
