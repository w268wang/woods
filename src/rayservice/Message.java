package rayservice;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class Message {
    private String sender;
    private String receiver;
    private long time;
    private String content; 
    private int timeZone = 5;

    @SuppressWarnings("unused")
    private Message() {
        this.sender = "";
        this.receiver = "";
        this.content = "";
        time = 0;
        timeZone = 5;
    }

    public Message(String sender, String receiver,
            String content) {
        this(sender, receiver, content, 5, System.currentTimeMillis());
    }

    public Message(String sender, String receiver,
            String content, int timeZone) {
        this(sender, receiver, content, timeZone, System.currentTimeMillis());
    }
    
    @JsonCreator
    public Message(@JsonProperty("sender") String sender,
            @JsonProperty("receiver") String receiver,
            @JsonProperty("content") String content,
            @JsonProperty("timeZone") int timeZone,
            @JsonProperty("time") long time) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.time = time;
        this.timeZone = timeZone;
    }

    @Override
    public String toString() {
      return sender + "->" + receiver + "time: " + time + "content: " + content;  
    }
    public String getSender() {
        return sender;
    }
    public String getReceiver() {
        return receiver;
    }
    public String getContent() {
        return content;
    }
    public int getTimeZone() {
        return timeZone;
    }
    public long getTime() {
        return time;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setTimeZone(int timeZone) {
        this.timeZone = timeZone;
    }
    public void setTime(long time) {
        this.time = time;
    }
}
