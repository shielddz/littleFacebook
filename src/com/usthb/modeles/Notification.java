package com.usthb.modeles;

import java.io.Serializable;

public class Notification implements Serializable {
    private StringBuilder information;
    private TypeNotification type;
    private boolean lue;

    public Notification(StringBuilder information, TypeNotification type) {
        this.information = information;
        this.type = type;
        this.lue = false;
    }

    public StringBuilder getInformation() {
        return information;
    }

    public TypeNotification getType() {
        return type;
    }

    public boolean isLue() {
        return lue;
    }

    public void setLue(boolean lue) {
        this.lue = lue;
    }
}
