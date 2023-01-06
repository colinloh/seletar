package com.canabrix.flightlog.foreflight;

import com.canabrix.flightlog.FlightlogParseException;
import com.canabrix.flightlog.Person;
import com.canabrix.flightlog.PersonName;
import com.canabrix.flightlog.PersonRole;
import com.canabrix.flightlog.foreflight.PersonParser;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PersonParserTest {

    @Test
    public void testPersonNameParsing() throws FlightlogParseException {
        PersonName p = PersonParser.parseAsPersonName("Anna");
        assertThat(p.getReferenceName())
                .isEqualTo("Anna")
        ;

        p = PersonParser.parseAsPersonName("Anna Fasar");
        assertThat(p.getReferenceName())
                .isEqualTo("Anna")
        ;
        assertThat(p.getSurname())
                .isEqualTo("Fasar")
        ;
    }

    @Test
    public void testPersonParsing() throws FlightlogParseException {

        String s = "Anna Fasar";
        Person p = PersonParser.parseAsPerson(s);
        assertThat(p.getName().getSurname()).isEqualTo("Fasar");
        assertThat(p.getName().getReferenceName()).isEqualTo("Anna");
        assertThat(p.getRole()).isNull();
        assertThat(p.getEmail()).isNull();

        s = "Kendrick;Flight Attendant";
        p = PersonParser.parseAsPerson(s);
        assertThat(p.getName().getSurname()).isNull();
        assertThat(p.getName().getReferenceName()).isEqualTo("Kendrick");
        assertThat(p.getRole()).isEqualTo(PersonRole.FLIGHT_ATTENDANT);
        assertThat(p.getEmail()).isNull();

        s = "Meiling Xu;Examiner";
        p = PersonParser.parseAsPerson(s);
        assertThat(p.getName().getSurname()).isEqualTo("Xu");
        assertThat(p.getName().getReferenceName()).isEqualTo("Meiling");
        assertThat(p.getRole()).isEqualTo(PersonRole.EXAMINER);
        assertThat(p.getEmail()).isNull();
    }
}
