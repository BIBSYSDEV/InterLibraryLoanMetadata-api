package no.unit;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LibraryBeanTest {

    public Clock initClock(LocalDate date) {
        return Clock.fixed(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()
                , ZoneId.systemDefault());
    }

    @Test
    public void IsOpenShoudBeFalseWhenInTheMiddleOfClosedDates() {
        Clock clock = initClock(LocalDate.of(2016, 5, 15));
        LibraryBean bean = new LibraryBean();
        bean.setStengt_fra("2016-05-10");
        bean.setStengt_til("2016-05-20");
        boolean open = bean.isOpenAtDate(LocalDate.now(clock));
        assertEquals(false, open);
    }

    @Test
    public void IsOpenShoudBeFalseWhenInTheBeginningOfClosedDates() {
        Clock clock = initClock(LocalDate.of(2016, 5, 10));
        LibraryBean bean = new LibraryBean();
        bean.setStengt_fra("2016-05-10");
        bean.setStengt_til("2016-05-20");
        boolean open = bean.isOpenAtDate(LocalDate.now(clock));
        assertEquals(false, open);
    }

    @Test
    public void IsOpenShoudBeFalseWhenAtTheEndOfClosedDates() {
        Clock clock = initClock(LocalDate.of(2016, 5, 20));
        LibraryBean bean = new LibraryBean();
        bean.setStengt_fra("2016-05-10");
        bean.setStengt_til("2016-05-20");
        boolean open = bean.isOpenAtDate(LocalDate.now(clock));
        assertEquals(false, open);
    }

    @Test
    public void IsOpenShoudBeTrueWhenAfterTheEndOfClosedDates() {
        Clock clock = initClock(LocalDate.of(2016, 5, 25));
        LibraryBean bean = new LibraryBean();
        bean.setStengt_fra("2016-05-10");
        bean.setStengt_til("2016-05-20");
        boolean open = bean.isOpenAtDate(LocalDate.now(clock));
        assertEquals(true, open);
    }

    @Test
    public void IsOpenShoudBeTrueWhenBeforeTheBeginningOfClosedDates() {
        Clock clock = initClock(LocalDate.of(2016, 5, 5));
        LibraryBean bean = new LibraryBean();
        bean.setStengt_fra("2016-05-10");
        bean.setStengt_til("2016-05-20");
        boolean open = bean.isOpenAtDate(LocalDate.now(clock));
        assertEquals(true, open);
    }

    @Test
    public void IsOpenShoudBeTrueWhenNoDatesAreAvailable() {
        Clock clock = initClock(LocalDate.of(2016, 5, 5));
        LibraryBean bean = new LibraryBean();
        boolean open = bean.isOpenAtDate(LocalDate.now(clock));
        assertEquals(true, open);
    }

    @Test
    public void IsOpenShoudBWorkWhenOnlyBeginningDateIsAvailable() {
        Clock clock = initClock(LocalDate.of(2016, 5, 5));
        LibraryBean bean = new LibraryBean();
        bean.setStengt_fra("2016-05-05");
        boolean open = bean.isOpenAtDate(LocalDate.now(clock));
        assertEquals(false, open);
    }

    @Test
    public void IsOpenShoudBWorkWhenOnlyEndDateIsAvailable2() {
        Clock clock = initClock(LocalDate.of(2016, 5, 5));
        LibraryBean bean = new LibraryBean();
        bean.setStengt_til("2016-05-06");
        boolean open = bean.isOpenAtDate(LocalDate.now(clock));
        assertEquals(false, open);
    }

    @Test
    public void IsOpenShoudBeFalseIfClosedTagHasValue() {

        Clock clock = initClock(LocalDate.of(2016, 5, 15)); //any date
        LibraryBean bean = new LibraryBean();
        bean.setStengt("X");
        boolean open = bean.isOpenAtDate(LocalDate.now(clock));

        assertEquals(false, open);
        bean.setStengt("S");

        assertEquals(false, open);

        bean.setStengt("5493885034");
        assertEquals(false, open);
    }


}
