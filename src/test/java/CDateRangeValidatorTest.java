import com.carpcap.hvp.annotation.CDateRange;
import com.carpcap.hvp.utils.CValid;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * CDateRange 多类型、格式和边界回归测试。
 */
public class CDateRangeValidatorTest {

    private static final Validator VALIDATOR = CValid.getValidator();
    private static final ZoneId SYSTEM_ZONE = ZoneId.systemDefault();

    @Test
    public void shouldValidateDateOnlyStringBoundaries() {
        assertValuesValid("dateOnly",
                "2022-06-01",
                "2022-06-15",
                "2022-06-30",
                "2022-06-30 23:59:59");
        assertValuesInvalid("dateOnly",
                "2022-05-31 23:59:59",
                "2022-07-01");
    }

    @Test
    public void shouldPreserveExactTimeBoundaries() {
        assertValuesValid("dateTime",
                "2022-06-01 08:30:00",
                "2022-06-15 12:00:00",
                "2022-06-30 18:45:00");
        assertValuesInvalid("dateTime",
                "2022-06-01 08:29:59",
                "2022-06-30 18:45:01");
    }

    @Test
    public void shouldValidateOpenEndedRangesAndNullPolicy() {
        assertValuesValid("minOnly", "2022-06-01", "2099-12-31");
        assertValuesInvalid("minOnly", "2022-05-31");

        assertValuesValid("maxOnly", "1900-01-01", "2022-06-30 23:59:59");
        assertValuesInvalid("maxOnly", "2022-07-01");

        assertValueValid("nullable", null);
        assertValueInvalid("required", null);
        assertValueValid("required", "2022-06-15");
    }

    @Test
    public void shouldValidateCustomFormatsStrictly() {
        assertValuesValid("compactDate", "20220101", "20220615", "20221231");
        assertValuesInvalid("compactDate", "20211231", "20230101");

        assertValuesValid("compactDateTime",
                "20220801003000",
                "20220815120000",
                "20220830123000");
        assertValuesInvalid("compactDateTime",
                "20220801002959",
                "20220830123001");
    }

    @Test
    public void shouldAutoDetectCommonDateFormats() {
        assertValuesValid("autoCompactDate", "20220101", "20220615", "20221231");
        assertValuesInvalid("autoCompactDate", "20211231", "20230101");

        assertValuesValid("autoCompactDateTime", "20220801003000", "20220830123000");
        assertValuesInvalid("autoCompactDateTime", "20220801002959", "20220830123001");

        assertValuesValid("autoCompactMillis", "20220801003000123", "20220830123000456");
        assertValuesInvalid("autoCompactMillis", "20220801003000122", "20220830123000457");

        assertValuesValid("minuteDateTime", "2022-06-01 08:30", "2022-06-30 18:45");
        assertValuesInvalid("minuteDateTime", "2022-06-01 08:29", "2022-06-30 18:46");

        assertValuesValid("millisDateTime",
                "2022-06-01 08:30:00.123",
                "2022-06-30 18:45:00.456");
        assertValuesInvalid("millisDateTime",
                "2022-06-01 08:30:00.122",
                "2022-06-30 18:45:00.457");

        assertValuesValid("slashDate", "2022/06/01", "2022/06/30 23:59:59");
        assertValuesInvalid("slashDate", "2022/05/31", "2022/07/01");

        assertValuesValid("slashDateTime", "2022/06/01 08:30:00", "2022/06/30 18:45:00");
        assertValuesInvalid("slashDateTime", "2022/06/01 08:29:59", "2022/06/30 18:45:01");

        assertValuesValid("chineseDate", "2022年06月01日", "2022年06月30日 23:59:59");
        assertValuesInvalid("chineseDate", "2022年05月31日", "2022年07月01日");

        assertValuesValid("chineseDateTime",
                "2022年06月01日 08时30分00秒",
                "2022年06月30日 18时45分00秒");
        assertValuesInvalid("chineseDateTime",
                "2022年06月01日 08时29分59秒",
                "2022年06月30日 18时45分01秒");

        assertValuesValid("isoOffset",
                "2022-06-01T08:30:00+08:00",
                "2022-06-30T18:45:00+08:00");
        assertValuesInvalid("isoOffset",
                "2022-06-01T08:29:59+08:00",
                "2022-06-30T18:45:01+08:00");
    }

    @Test
    public void shouldValidateIsoAndRfcFormats() {
        assertValuesValid("isoMillis",
                "2022-06-01T00:00:00.000Z",
                "2022-06-15T12:30:45.123Z",
                "2022-06-30T23:59:59.999Z");
        assertValuesInvalid("isoMillis",
                "2022-05-31T23:59:59.999Z",
                "2022-07-01T00:00:00.000Z");

        assertValuesValid("isoSeconds",
                "2022-06-01T00:00:00Z",
                "2022-06-30T23:59:59Z");
        assertValuesInvalid("isoSeconds", "2022-07-01T00:00:00Z");

        assertValuesValid("rfcDateTime",
                "Wed, 01 Jun 2022 00:00:00 GMT",
                "Thu, 30 Jun 2022 23:59:59 GMT");
        assertValuesInvalid("rfcDateTime", "Fri, 01 Jul 2022 00:00:00 GMT");
    }

    @Test
    public void shouldValidateAllSupportedTemporalTypes() {
        assertValuesValid("legacyDate",
                toDate(2022, 6, 1, 0, 0, 0),
                toDate(2022, 6, 30, 23, 59, 59));
        assertValuesInvalid("legacyDate",
                toDate(2022, 5, 31, 23, 59, 59),
                toDate(2022, 7, 1, 0, 0, 0));

        assertValuesValid("localDate",
                LocalDate.of(2022, 6, 1),
                LocalDate.of(2022, 6, 30));
        assertValuesInvalid("localDate",
                LocalDate.of(2022, 5, 31),
                LocalDate.of(2022, 7, 1));

        assertValuesValid("localDateTime",
                LocalDateTime.of(2022, 6, 1, 8, 30),
                LocalDateTime.of(2022, 6, 30, 18, 45));
        assertValuesInvalid("localDateTime",
                LocalDateTime.of(2022, 6, 1, 8, 29, 59),
                LocalDateTime.of(2022, 6, 30, 18, 45, 1));

        assertValuesValid("instant",
                toInstant(2022, 6, 1, 0, 0, 0),
                toInstant(2022, 6, 30, 23, 59, 59));
        assertValuesInvalid("instant",
                toInstant(2022, 5, 31, 23, 59, 59),
                toInstant(2022, 7, 1, 0, 0, 0));

        assertValuesValid("zonedDateTime",
                ZonedDateTime.of(2022, 6, 1, 8, 30, 0, 0, SYSTEM_ZONE),
                ZonedDateTime.of(2022, 6, 30, 18, 45, 0, 0, SYSTEM_ZONE));
        assertValuesInvalid("zonedDateTime",
                ZonedDateTime.of(2022, 6, 1, 8, 29, 59, 0, SYSTEM_ZONE),
                ZonedDateTime.of(2022, 6, 30, 18, 45, 1, 0, SYSTEM_ZONE));
    }

    @Test
    public void shouldApplyEveryRepeatedConstraint() {
        assertValuesValid("repeatedRange", "2022-04-01", "2022-06-15", "2022-09-30");
        assertValuesInvalid("repeatedRange", "2022-03-31", "2022-10-01");
    }

    @Test
    public void shouldRejectMalformedValuesAndConfiguration() {
        assertThrows(RuntimeException.class,
                () -> validateValue("dateOnly", "2022-02-30"));

        assertThrows(RuntimeException.class,
                () -> validateValue("dateOnly", "2022-06-01abc"));
        assertThrows(RuntimeException.class,
                () -> validateValue("invalidConfiguration", "2022-06-15"));
    }

    private static void assertValuesValid(String property, Object... values) {
        for (Object value : values) {
            assertValueValid(property, value);
        }
    }

    private static void assertValuesInvalid(String property, Object... values) {
        for (Object value : values) {
            assertValueInvalid(property, value);
        }
    }

    private static void assertValueValid(String property, Object value) {
        assertTrue(property + " 应校验通过: " + value, validateValue(property, value).isEmpty());
    }

    private static void assertValueInvalid(String property, Object value) {
        assertFalse(property + " 应校验失败: " + value, validateValue(property, value).isEmpty());
    }

    private static Set<ConstraintViolation<DateRangeCases>> validateValue(String property, Object value) {
        return VALIDATOR.validateValue(DateRangeCases.class, property, value);
    }

    private static Date toDate(int year, int month, int day, int hour, int minute, int second) {
        return Date.from(toInstant(year, month, day, hour, minute, second));
    }

    private static Instant toInstant(int year, int month, int day, int hour, int minute, int second) {
        return LocalDateTime.of(year, month, day, hour, minute, second)
                .atZone(SYSTEM_ZONE)
                .toInstant();
    }

    private static class DateRangeCases {

        @CDateRange(min = "2022-06-01", max = "2022-06-30")
        private String dateOnly;

        @CDateRange(min = "2022-06-01 08:30:00", max = "2022-06-30 18:45:00")
        private String dateTime;

        @CDateRange(min = "2022-06-01")
        private String minOnly;

        @CDateRange(max = "2022-06-30")
        private String maxOnly;

        @CDateRange(min = "2022-06-01", max = "2022-06-30")
        private String nullable;

        @CDateRange(min = "2022-06-01", max = "2022-06-30", allowNull = false)
        private String required;

        @CDateRange(min = "20220101", max = "20221231", format = "yyyyMMdd")
        private String compactDate;

        @CDateRange(min = "20220801003000", max = "20220830123000", format = "yyyyMMddHHmmss")
        private String compactDateTime;

        @CDateRange(min = "20220101", max = "20221231")
        private String autoCompactDate;

        @CDateRange(min = "20220801003000", max = "20220830123000")
        private String autoCompactDateTime;

        @CDateRange(min = "20220801003000123", max = "20220830123000456")
        private String autoCompactMillis;

        @CDateRange(min = "2022-06-01 08:30", max = "2022-06-30 18:45")
        private String minuteDateTime;

        @CDateRange(min = "2022-06-01 08:30:00.123", max = "2022-06-30 18:45:00.456")
        private String millisDateTime;

        @CDateRange(min = "2022/06/01", max = "2022/06/30")
        private String slashDate;

        @CDateRange(min = "2022/06/01 08:30:00", max = "2022/06/30 18:45:00")
        private String slashDateTime;

        @CDateRange(min = "2022年06月01日", max = "2022年06月30日")
        private String chineseDate;

        @CDateRange(min = "2022年06月01日 08时30分00秒", max = "2022年06月30日 18时45分00秒")
        private String chineseDateTime;

        @CDateRange(min = "2022-06-01T08:30:00+08:00", max = "2022-06-30T18:45:00+08:00")
        private String isoOffset;

        @CDateRange(min = "2022-06-01T00:00:00.000Z", max = "2022-06-30T23:59:59.999Z")
        private String isoMillis;

        @CDateRange(min = "2022-06-01T00:00:00Z", max = "2022-06-30T23:59:59Z")
        private String isoSeconds;

        @CDateRange(min = "Wed, 01 Jun 2022 00:00:00 GMT", max = "Thu, 30 Jun 2022 23:59:59 GMT")
        private String rfcDateTime;

        @CDateRange(min = "2022-06-01", max = "2022-06-30")
        private Date legacyDate;

        @CDateRange(min = "2022-06-01", max = "2022-06-30")
        private LocalDate localDate;

        @CDateRange(min = "2022-06-01 08:30:00", max = "2022-06-30 18:45:00")
        private LocalDateTime localDateTime;

        @CDateRange(min = "2022-06-01", max = "2022-06-30")
        private Instant instant;

        @CDateRange(min = "2022-06-01 08:30:00", max = "2022-06-30 18:45:00")
        private ZonedDateTime zonedDateTime;

        @CDateRange(min = "2022-01-01", max = "2022-12-31")
        @CDateRange(min = "2022-04-01", max = "2022-09-30")
        private String repeatedRange;

        @CDateRange(min = "not-a-date", max = "2022-06-30")
        private String invalidConfiguration;
    }
}
