/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.io.Serializable;

/**
 *
 * @author sg
 */
public final class Envelope implements Serializable {
    private final String message;
    private final String desc;
    
    public Envelope(String message, String desc) {
        this.message = message;
        this.desc = desc;
    }
    
    public Envelope(String message) {
        this(message, "");
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }
    
    @Override
    public String toString() {
        return "Envelope(" + desc + ", " + message + ")";
    }
}
