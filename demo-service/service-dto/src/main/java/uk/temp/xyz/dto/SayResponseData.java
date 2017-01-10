package uk.temp.xyz.dto;

import java.util.Date;

/**
 * Created by krupagj on 10/01/2017.
 */
public class SayResponseData {

    private String message;
    private final Date timestamp;


    public SayResponseData() {
        this.timestamp = new Date();
    }

    public SayResponseData(String message) {
        this();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
