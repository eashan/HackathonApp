package knowyc.com.knowyc.activities.model;

/**
 * Created by Sumod on 28-Nov-15.
 */
public class Message {

    private double id;
    private String text;
    private boolean read_flag;

    public Message() {
    }

    public Message(double id, boolean read_flag, String text) {
        this.id = id;
        this.read_flag = read_flag;
        this.text = text;
    }

    public void setId(double id) {
        this.id = id;
    }

    public void setRead_flag(boolean read_flag) {
        this.read_flag = read_flag;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getId() {
        return id;
    }

    public boolean isRead_flag() {
        return read_flag;
    }

    public String getText() {
        return text;
    }


}
