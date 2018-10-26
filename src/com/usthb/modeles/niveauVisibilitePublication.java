package com.usthb.modeles;

import java.io.Serializable;

public enum niveauVisibilitePublication implements Serializable {
    //j'ai utilisé publique car public est un mot reservé par le langage
    prive, publiq, ami, amis, groupe;
}
