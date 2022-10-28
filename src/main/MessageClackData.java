package data;

import java.util.Objects;

/**
 * The child of ClackData, whose data is a message.
 *
 * @author xinchaosong
 */
public class MessageClackData extends ClackData {
    private String message;  // A string representing instant message

    /**
     * The constructor to set up the instance variables username, message, and type.
     * Should call the super constructor.
     *
     * @param userName a string representing the name of the client user
     * @param message  a string representing instant message
     * @param type     an int representing the data type
     */
    public MessageClackData(String userName, String message, int type) {
        super(userName, type);
        this.message = message;
    }

    /**
     * The default constructor.
     * This constructor should call another constructor.
     */
    public MessageClackData() {
        // It is okay to do either this:
        super(ClackData.CONSTANT_SENDMESSAGE);
        this.message = "";

        // Or this:
        // this("Anon", "", ClackData.CONSTANT_SENDMESSAGE);
    }

    /**
     * Returns the instant message.
     *
     * @return this.message
     */
    public String getData() {
        return this.message;
    }

    @Override
    public int hashCode() {
        // The following is a traditional standard way to generate the hash code.
        // This is only one of many possible implementations. See the hashCode()
        // method in other classes for some different implementations.

        int result = 29;

        // It is okay to select only some of the instance variables to calculate the hash code
        // but must use the same instance variables with equals() to maintain consistency.
        result = 37 * result + ((this.userName == null) ? 0 : this.userName.hashCode());
        result = 37 * result + this.type;
        result = 37 * result + ((this.message == null) ? 0 : this.message.hashCode());

        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MessageClackData)) {
            return false;
        }

        // Casts other to be a MessageClackData to access its instance variables.
        MessageClackData otherMessageClackData = (MessageClackData) other;

        // Compares the selected instance variables of both MessageClackData objects that determine uniqueness.
        // It is okay to select only some of the instance variables for comparison but must use the same
        // instance variables with hashCode() to maintain consistency.
        return this.userName.equals(otherMessageClackData.userName)
                && this.type == otherMessageClackData.type
                && Objects.equals(this.message, otherMessageClackData.message);
    }

    @Override
    public String toString() {
        // Should return a full description of the class with all instance variables,
        // including those in the super class.
        return "This instance of MessageClackData has the following properties:\n"
                + "Username: " + this.userName + "\n"
                + "Type: " + this.type + "\n"
                + "Date: " + this.date.toString() + "\n"
                + "Message: " + this.message + "\n";
    }
}
