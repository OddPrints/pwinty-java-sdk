package uk.co.mattburns.pwinty.v2_3;

import org.joda.time.DateTime;

import java.util.Date;

public class Log {
    private String title;
    private String message;
    private String created;

    /**
     * no-arg constructor included just for the sake of gson serialisation purposes.
     */
    public Log() {}

    public Log(String title, String message, String created) {
        this.title = title;
        this.message = message;
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public DateTime getCreated() {
        return new DateTime(created);
    }

    public String getCreatedString() {
        return getCreated().toString("yyyy-MM-dd HH:mm:ss");
    }
}
