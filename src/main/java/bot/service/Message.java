package bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

public class Message {

    public static final String MESSAGE = "MESSAGE";
    public static final String PHOTO = "PHOTO";
    public static final String DOCUMENT = "DOCUMENT";

    private SendMessage sendMessage;
    private SendPhoto sendPhoto;
    private SendDocument sendDocument;

    private String type;

    private boolean markable;

    public boolean isMarkable() {
        return markable;
    }

    public void setMarkable(boolean markable) {
        this.markable = markable;
    }

    private String process;

    public String getProcess(){
        return process;
    }

    public Message(String type, boolean markable, String process){
        this.type = type;
        this.markable = markable;
        this.process = process;
        switch (type){
            case MESSAGE:
                this.sendMessage = new SendMessage();
                break;

            case PHOTO:
                this.sendPhoto = new SendPhoto();
                break;

            case DOCUMENT:
                this.sendDocument = new SendDocument();
                break;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SendMessage getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(SendMessage sendMessage) {
        this.sendMessage = sendMessage;
    }

    public SendPhoto getSendPhoto() {
        return sendPhoto;
    }

    public void setSendPhoto(SendPhoto sendPhoto) {
        this.sendPhoto = sendPhoto;
    }

    public SendDocument getSendDocument() {
        return sendDocument;
    }

    public void setSendDocument(SendDocument sendDocument) {
        this.sendDocument = sendDocument;
    }



}
