package rayservice;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class MessagePack {
    private List<Message> messages = new ArrayList<Message>();
    private List<String> los = new ArrayList<String>();

    public MessagePack() {}

    public MessagePack(List<Message> messages) {
        this.messages.addAll(messages);
        los.add("a");
        los.add("b");
    }

    public List<Message> getContent() {
        return messages;
    }

    public void addContent(List<Message> messages) {
        this.messages.addAll(messages);
    }

    public void clearPack() {
        messages = new ArrayList<Message>();
    }
}
