package com.canabrix.flightlog;

import org.apache.commons.lang3.StringUtils;

public class PersonName {

    private String surname;
    private String referenceName;
    private String otherNames;

    private PersonName() {
    }

    /**
     * @param surname Family name or last Name
     * @param referenceName First name or how you'd address informally
     * @param otherNames Middle names and other words that appear in the birth cert
     */
    public PersonName(
            String surname,
            String referenceName,
            String otherNames
    ) {
        this.surname = surname;
        this.referenceName = referenceName;
        this.otherNames = otherNames;
    }

    public String getSurname() {
        return surname;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public String getOtherNames() {
        return otherNames;
    }

}
