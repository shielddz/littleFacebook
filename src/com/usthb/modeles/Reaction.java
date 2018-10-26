package com.usthb.modeles;

import java.io.Serializable;

public class Reaction implements Serializable {
    public String username;
    private TypeReaction reaction;

    public Reaction(String username, TypeReaction reaction) {
        this.username = username;
        this.reaction = reaction;
    }

    public TypeReaction getReaction() {
        return reaction;
    }
}
