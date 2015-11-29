package knowyc.com.knowyc.model;

/**
 * Created by Sumod on 28-Nov-15.
 */
public class Message {

    private double id;
    private String message_text;
    private boolean read_flag;
    private boolean if_outgoing;

    public Message() {
    }

    public Message(double id, boolean if_outgoing, String message_text, boolean read_flag) {
        this.id = id;
        this.if_outgoing = if_outgoing;
        this.message_text = message_text;
        this.read_flag = read_flag;
    }

    public Message(double id, boolean read_flag, String text) {
        this.id = id;
        this.read_flag = read_flag;
        this.message_text = text;
    }

    public void setId(double id) {
        this.id = id;
    }

    public void setRead_flag(boolean read_flag) {
        this.read_flag = read_flag;
    }


    public void setIf_outgoing(boolean if_outgoing) {
        this.if_outgoing = if_outgoing;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public boolean isIf_outgoing() {
        return if_outgoing;
    }

    public String getMessage_text() {
        return message_text;
    }

    public double getId() {
        return id;
    }

    public boolean isRead_flag() {
        return read_flag;
    }



}
