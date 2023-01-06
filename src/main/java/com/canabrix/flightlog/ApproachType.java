package com.canabrix.flightlog;

import javax.crypto.spec.GCMParameterSpec;

public enum ApproachType {

    // See https://epicflightacademy.com/approach/ for reference

    // VISUAL, We will omit non-instrument approaches
    // CONTACT, We will omit non-instrument approaches
    ILS_LOC,
    PAR,
    ASR,
    VOR,
    NDB,
    RNAV,
    LDA,
    SDF,
    VISUAL
}
