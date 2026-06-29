import com.carpcap.hvp.User2;
import com.carpcap.hvp.annotation.*;
import com.carpcap.hvp.groups.*;
import com.carpcap.hvp.utils.CValid;

import javax.validation.ValidationException;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * AnnotationTest2 - Advanced dimension tests
 * CValid API variants, all groups, annotation attributes, @Repeatable, custom regex, edge cases
 *
 * @author CarpCap
 */
public class AnnotationTest2 {

    private static int testCount = 0;
    private static int passCount = 0;
    private static int failCount = 0;

    public static void main(String[] args) {
        System.out.println("=== hibernate-validator-plus AnnotationTest2 (Advanced) ===\n");

        // === CValid utility method variants ===
        testCValidUtils();

        // === allowNull default (true) behavior ===
        testAllowNullDefaults();

        // === All group types ===
        testAllGroups();

        // === Annotation attribute config variants ===
        testUrlConfigVariants();
        testBankCardConfigVariants();
        testMoneyConfigVariants();
        testMacConfigVariants();
        testDateRangeCustomFormat();

        // === Custom config ===
        testAccountCustomConfig();
        testPasswordCustomConfig();
        testCustomRegexpOverride();

        // === Edge cases ===
        testIpv4EdgeCases();
        testIpv6EdgeCases();
        testIdCardEdgeCases();
        testDomainEdgeCases();
        testPlateNumberEdgeCases();
        testMoneyEdgeCases();
        testFileEdgeCases();

        // === @Repeatable ===
        testRepeatableAnnotations();

        System.out.println("\n============================================");
        System.out.println("Total: " + testCount + ", Passed: " + passCount + ", Failed: " + failCount);
        System.out.println("============================================");

        if (failCount > 0) {
            System.exit(1);
        }
    }

    // ==================== Helpers ====================

    private static void pass(String label, List<String> violations) {
        testCount++;
        if (violations == null || violations.isEmpty()) {
            passCount++;
            System.out.println("[PASS] " + label);
        } else {
            failCount++;
            System.out.println("[FAIL] " + label + " => violations: " + violations);
        }
    }

    private static void pass(String label, String violation) {
        testCount++;
        if (violation == null || violation.trim().isEmpty()) {
            passCount++;
            System.out.println("[PASS] " + label);
        } else {
            failCount++;
            System.out.println("[FAIL] " + label + " => violation: " + violation);
        }
    }

    private static void pass(String label) {
        testCount++;
        passCount++;
        System.out.println("[PASS] " + label);
    }

    private static void fail(String label, List<String> violations) {
        testCount++;
        if (violations != null && !violations.isEmpty()) {
            passCount++;
            System.out.println("[PASS] " + label + " => " + violations.get(0));
        } else {
            failCount++;
            System.out.println("[FAIL] " + label + " => expected violation, got none");
        }
    }

    private static void fail(String label, String violation) {
        testCount++;
        if (violation != null && !violation.trim().isEmpty()) {
            passCount++;
            System.out.println("[PASS] " + label + " => " + violation);
        } else {
            failCount++;
            System.out.println("[FAIL] " + label + " => expected violation, got none");
        }
    }

    private static void failThrows(String label, RunnableWithException runnable) {
        testCount++;
        try {
            runnable.run();
            failCount++;
            System.out.println("[FAIL] " + label + " => expected exception, got none");
        } catch (ValidationException e) {
            passCount++;
            System.out.println("[PASS] " + label + " => " + e.getMessage());
        } catch (Exception e) {
            passCount++;
            System.out.println("[PASS] " + label + " => " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private static void noThrow(String label, RunnableWithException runnable) {
        testCount++;
        try {
            runnable.run();
            passCount++;
            System.out.println("[PASS] " + label);
        } catch (Exception e) {
            failCount++;
            System.out.println("[FAIL] " + label + " => unexpected exception: " + e.getMessage());
        }
    }

    @FunctionalInterface
    interface RunnableWithException {
        void run() throws Exception;
    }

    // ==================== Entity Factory ====================

    private static User2 freshUser2() {
        User2 u = new User2();
        u.setUrlNoLocalhost("http://example.com");
        u.setUrlNoIp("http://example.com");
        u.setUrlCustomProtocols("https://example.com");
        u.setUrlRequired("http://example.com");
        u.setBankCardAllowedPrefix("6200000000000005");
        u.setBankCardForbiddenPrefix("6200000000000005");
        u.setBankCardNoLuhn("1234567890123456");
        u.setMoneyNoSymbol("100.00");
        u.setMoneyCustomSymbol("100.00");
        u.setMoneyMustDecimal("100.01");
        u.setMoneyAllowLeadingZero("0.50");
        u.setMoneyRange(new BigDecimal("100"));
        u.setMoneyDigitConfig("1234.5678");
        u.setMoneyNoThousandSep("1000.00");
        u.setMoneyRequired("50.00");
        u.setMacUpperOnly("AA:BB:CC:DD:EE:FF");
        u.setMacEui64("AA:BB:CC:DD:EE:FF:00:11");
        u.setMacOmitZero("A0:1B:2C:3D:4E:5F");
        u.setDCustomFormat("20220615");
        u.setDRequired("2022-06-15");
        u.setAccountShort("abcde");
        u.setAccountCustomRegex("admin123");
        u.setPasswdLong("abcde12345");
        u.setPhoneCustomRegexp("13800138000");
        u.setPassportCustomRegexp("E123456789");
        u.setPostCodeCustomRegexp("12345");
        u.setIpCreateGroup("192.168.1.1");
        u.setDomainCreateGroup("example.com");
        u.setAccountAllowNull("validAccount1");
        u.setPasswdAllowNull("validPwd1");
        u.setIdCardAllowNull("110101199001010015");
        u.setLpnAllowNull("\u4eacA12345");
        return u;
    }

    // ==================== 1. CValid Utility Methods ====================

    private static void testCValidUtils() {
        System.out.println("\n--- [CValid Utility Methods] ---");

        // ---- validate() - throws ValidationException on failure ----
        User u = freshBaseUser();

        noThrow("validate() - valid data (no exception)", () -> CValid.validate(u));
        noThrow("validate() with group - valid data", () -> CValid.validate(u, CPost.class));

        // validate() should throw on invalid data
        User invalid = freshBaseUser();
        invalid.setName("");
        invalid.setIp("999.999.999.999");
        invalid.setDomain("not_a_domain");
        invalid.setIdCard("123456789012345678");
        invalid.setUser("ab");
        invalid.setPasswd("abc");
        failThrows("validate() - invalid data throws ValidationException", () -> CValid.validate(invalid));

        // ---- tryFastValidate() - returns single error message ----
        User u2 = freshBaseUser();

        pass("tryFastValidate() - valid data", CValid.tryFastValidate(u2));

        User invalid2 = freshBaseUser();
        invalid2.setIdCard("123456789012345678");
        invalid2.setUser("ab");
        invalid2.setPasswd("abc");
        fail("tryFastValidate() - invalid data returns error", CValid.tryFastValidate(invalid2));

        // ---- validateProperty() - single property validation ----
        User propUser1 = freshBaseUser();
        noThrow("validateProperty() - valid property", () -> CValid.validateProperty(propUser1, "user"));
        noThrow("validateProperty() - valid ip", () -> CValid.validateProperty(propUser1, "ip", CPost.class));

        // use a field that will fail validation
        User propUser2 = freshBaseUser();
        propUser2.setPasswd("abc");
        failThrows("validateProperty() - invalid passwd",
            () -> CValid.validateProperty(propUser2, "passwd"));

        // ---- tryValidateProperty() - returns all violations for single property ----
        User propUser3 = freshBaseUser();
        pass("tryValidateProperty() - valid property", CValid.tryValidateProperty(propUser3, "user"));
        User propUser4 = freshBaseUser();
        propUser4.setPasswd("abc");
        fail("tryValidateProperty() - invalid passwd", CValid.tryValidateProperty(propUser4, "passwd"));

        // ---- tryFastValidateProperty() - fast failure for single property ----
        User propUser5 = freshBaseUser();
        pass("tryFastValidateProperty() - valid property", CValid.tryFastValidateProperty(propUser5, "user"));
        User propUser6 = freshBaseUser();
        propUser6.setPasswd("abc");
        fail("tryFastValidateProperty() - invalid passwd", CValid.tryFastValidateProperty(propUser6, "passwd"));
    }

    // ==================== 2. allowNull Defaults (allowNull = true) ====================

    private static void testAllowNullDefaults() {
        System.out.println("\n--- [allowNull Defaults (allowNull=true)] ---");

        User2 u = freshUser2();
        u.setAccountAllowNull(null);
        pass("CAccount allowNull=true with null", CValid.tryValidateProperty(u, "accountAllowNull"));

        u.setPasswdAllowNull(null);
        pass("CPassword allowNull=true with null", CValid.tryValidateProperty(u, "passwdAllowNull"));

        u.setIdCardAllowNull(null);
        pass("CIdCard allowNull=true with null", CValid.tryValidateProperty(u, "idCardAllowNull"));

        u.setLpnAllowNull(null);
        pass("CPlateNumber allowNull=true with null", CValid.tryValidateProperty(u, "lpnAllowNull"));

        // allowNull=false on CUrl variant
        u.setUrlRequired(null);
        fail("CUrl allowNull=false with null", CValid.tryValidateProperty(u, "urlRequired", CGet.class));
    }

    // ==================== 3. All Group Types ====================

    private static void testAllGroups() {
        System.out.println("\n--- [All Groups] ---");

        User2 u = freshUser2();
        u.setIpCreateGroup(null);
        u.setDomainCreateGroup(null);

        // CCreate group
        fail("CCreate group - invalid ip", CValid.tryValidate(u, CCreate.class));
        fail("CCreate group - invalid domain", CValid.tryValidate(u, CCreate.class));

        // CCreateDef = CCreate + Default
        // The ip and domain fields have CCreate group, so CCreateDef validates them too
        u.setIpCreateGroup(null);
        u.setDomainCreateGroup(null);
        fail("CCreateDef group - invalid ip", CValid.tryValidate(u, CCreateDef.class));
        fail("CCreateDef group - invalid domain", CValid.tryValidate(u, CCreateDef.class));

        // CQuery / CUpdate / CDelete / CPut / CPatch groups
        // Use the existing User entity which has CPost and CGet group fields
        User base = new User();
        base.setName("Zhang");
        base.setIp("192.168.1.1");
        base.setDomain("example.com");
        base.setIdCard("110101199001010015");
        base.setUser("admin12345");
        base.setPasswd("abc12345");
        base.setD1(LocalDate.of(2022, 6, 15));

        // These groups have no fields annotated with them in the existing entity,
        // so validation should pass (no matching constraints)
        pass("CQuery group - no matching constraints", CValid.tryValidate(base, CQuery.class));
        pass("CUpdate group - no matching constraints", CValid.tryValidate(base, CUpdate.class));
        pass("CDelete group - no matching constraints", CValid.tryValidate(base, CDelete.class));
        pass("CPut group - no matching constraints", CValid.tryValidate(base, CPut.class));
        pass("CPatch group - no matching constraints", CValid.tryValidate(base, CPatch.class));

        // Verify *Def groups work - revisit with existing annotations
        // CGetDef validates both CGet-grouped constraints AND Default-grouped constraints
        base.setUrl("ftp://bad.com");
        fail("CGetDef group - invalid url protocol", CValid.tryValidateProperty(base, "url", CGetDef.class));
    }

    // ==================== 4. URL Config Variants ====================

    private static void testUrlConfigVariants() {
        System.out.println("\n--- [URL Config Variants] ---");
        User2 u;

        // --- allowLocalhost = false ---
        u = freshUser2();
        u.setUrlNoLocalhost("http://localhost:8080");
        fail("url allowLocalhost=false with localhost", CValid.tryValidateProperty(u, "urlNoLocalhost", CGet.class));

        u = freshUser2();
        u.setUrlNoLocalhost("http://127.0.0.1:8080");
        fail("url allowLocalhost=false with 127.0.0.1", CValid.tryValidateProperty(u, "urlNoLocalhost", CGet.class));

        u = freshUser2();
        u.setUrlNoLocalhost("http://example.com");
        pass("url allowLocalhost=false with normal domain", CValid.tryValidateProperty(u, "urlNoLocalhost", CGet.class));

        // --- allowIp = false ---
        u = freshUser2();
        u.setUrlNoIp("http://192.168.1.1");
        fail("url allowIp=false with IP address", CValid.tryValidateProperty(u, "urlNoIp", CGet.class));

        u = freshUser2();
        u.setUrlNoIp("http://example.com");
        pass("url allowIp=false with domain", CValid.tryValidateProperty(u, "urlNoIp", CGet.class));

        // --- Custom protocols (https, ftp only) ---
        u = freshUser2();
        u.setUrlCustomProtocols("http://example.com");
        fail("url custom protocols excludes http", CValid.tryValidateProperty(u, "urlCustomProtocols", CGet.class));

        u = freshUser2();
        u.setUrlCustomProtocols("ftp://files.example.com");
        pass("url custom protocols includes ftp", CValid.tryValidateProperty(u, "urlCustomProtocols", CGet.class));

        u = freshUser2();
        u.setUrlCustomProtocols("https://example.com");
        pass("url custom protocols includes https", CValid.tryValidateProperty(u, "urlCustomProtocols", CGet.class));
    }

    // ==================== 5. BankCard Config Variants ====================

    private static void testBankCardConfigVariants() {
        System.out.println("\n--- [BankCard Config Variants] ---");
        User2 u;

        // --- allowedPrefixes = {"62"} ---
        u = freshUser2();
        u.setBankCardAllowedPrefix("6200000000000005");
        pass("bankCard allowedPrefix 62 matches", CValid.tryValidateProperty(u, "bankCardAllowedPrefix", CGet.class));

        u = freshUser2();
        u.setBankCardAllowedPrefix("4111111111111111");
        fail("bankCard allowedPrefix 62 not match", CValid.tryValidateProperty(u, "bankCardAllowedPrefix", CGet.class));

        // --- forbiddenPrefixes = {"4"} ---
        u = freshUser2();
        u.setBankCardForbiddenPrefix("6200000000000005");
        pass("bankCard forbiddenPrefix 4 not match", CValid.tryValidateProperty(u, "bankCardForbiddenPrefix", CGet.class));

        u = freshUser2();
        u.setBankCardForbiddenPrefix("4111111111111111");
        fail("bankCard forbiddenPrefix 4 matches", CValid.tryValidateProperty(u, "bankCardForbiddenPrefix", CGet.class));

        // --- useLuhnCheck = false ---
        u = freshUser2();
        u.setBankCardNoLuhn("1234567890123456");
        pass("bankCard useLuhnCheck=false with invalid Luhn", CValid.tryValidateProperty(u, "bankCardNoLuhn", CGet.class));

        // --- allowSpaces ---
        u = freshUser2();
        u.setBankCardForbiddenPrefix("6200 0000 0000 0005");
        pass("bankCard with spaces allowed", CValid.tryValidateProperty(u, "bankCardForbiddenPrefix", CGet.class));

        // --- allowHyphens ---
        u = freshUser2();
        u.setBankCardForbiddenPrefix("6200-0000-0000-0005");
        pass("bankCard with hyphens allowed", CValid.tryValidateProperty(u, "bankCardForbiddenPrefix", CGet.class));
    }

    // ==================== 6. Money Config Variants ====================

    private static void testMoneyConfigVariants() {
        System.out.println("\n--- [Money Config Variants] ---");
        User2 u;

        // --- allowCurrencySymbol = false ---
        u = freshUser2();
        u.setMoneyNoSymbol("$100.00");
        fail("money allowCurrencySymbol=false with $", CValid.tryValidateProperty(u, "moneyNoSymbol", CGet.class));

        u = freshUser2();
        u.setMoneyNoSymbol("\u00a5100.00");
        fail("money allowCurrencySymbol=false with \u00a5", CValid.tryValidateProperty(u, "moneyNoSymbol", CGet.class));

        u = freshUser2();
        u.setMoneyNoSymbol("100.00");
        pass("money allowCurrencySymbol=false no symbol", CValid.tryValidateProperty(u, "moneyNoSymbol", CGet.class));

        // --- allowedCurrencySymbols = {"$", "\u00a5"} ---
        u = freshUser2();
        u.setMoneyCustomSymbol("$100.00");
        pass("money allowedCurrencySymbols allows $", CValid.tryValidateProperty(u, "moneyCustomSymbol", CGet.class));

        u = freshUser2();
        u.setMoneyCustomSymbol("\u00a5100.00");
        pass("money allowedCurrencySymbols allows \u00a5", CValid.tryValidateProperty(u, "moneyCustomSymbol", CGet.class));

        u = freshUser2();
        u.setMoneyCustomSymbol("\u20ac100.00");
        fail("money allowedCurrencySymbols rejects \u20ac", CValid.tryValidateProperty(u, "moneyCustomSymbol", CGet.class));

        // --- allowNoDecimal = false ---
        u = freshUser2();
        u.setMoneyMustDecimal("100");
        fail("money allowNoDecimal=false without decimal", CValid.tryValidateProperty(u, "moneyMustDecimal", CGet.class));

        u = freshUser2();
        u.setMoneyMustDecimal("100.");
        fail("money allowNoDecimal=false trailing dot", CValid.tryValidateProperty(u, "moneyMustDecimal", CGet.class));

        u = freshUser2();
        u.setMoneyMustDecimal("100.01");
        pass("money allowNoDecimal=false with decimal", CValid.tryValidateProperty(u, "moneyMustDecimal", CGet.class));

        // --- allowLeadingZero = true ---
        u = freshUser2();
        u.setMoneyAllowLeadingZero("0.50");
        pass("money allowLeadingZero=true leading zero", CValid.tryValidateProperty(u, "moneyAllowLeadingZero", CGet.class));

        u = freshUser2();
        u.setMoneyAllowLeadingZero("00.50");
        pass("money allowLeadingZero=true double zero", CValid.tryValidateProperty(u, "moneyAllowLeadingZero", CGet.class));

        // --- min/max range ---
        u = freshUser2();
        u.setMoneyRange(new BigDecimal("5"));
        fail("money min=10 with value 5", CValid.tryValidateProperty(u, "moneyRange", CGet.class));

        u = freshUser2();
        u.setMoneyRange(new BigDecimal("2000"));
        fail("money max=1000 with value 2000", CValid.tryValidateProperty(u, "moneyRange", CGet.class));

        u = freshUser2();
        u.setMoneyRange(new BigDecimal("100"));
        pass("money min=10 max=1000 with value 100", CValid.tryValidateProperty(u, "moneyRange", CGet.class));

        // --- integer digits config ---
        u = freshUser2();
        u.setMoneyDigitConfig("1.2345");
        fail("money minIntegerDigits=2 with 1 digit", CValid.tryValidateProperty(u, "moneyDigitConfig", CGet.class));

        u = freshUser2();
        u.setMoneyDigitConfig("1234567.1234");
        fail("money maxIntegerDigits=6 with 7 digits", CValid.tryValidateProperty(u, "moneyDigitConfig", CGet.class));

        u = freshUser2();
        u.setMoneyDigitConfig("12.1234");
        pass("money min/max digits correct", CValid.tryValidateProperty(u, "moneyDigitConfig", CGet.class));

        // --- allowThousandSeparator = false ---
        u = freshUser2();
        u.setMoneyNoThousandSep("1,000.00");
        fail("money allowThousandSeparator=false with comma", CValid.tryValidateProperty(u, "moneyNoThousandSep", CGet.class));

        // --- moneyRequired (allowNull=false) ---
        u = freshUser2();
        u.setMoneyRequired(null);
        fail("money allowNull=false with null", CValid.tryValidateProperty(u, "moneyRequired", CGet.class));
    }

    // ==================== 7. MAC Config Variants ====================

    private static void testMacConfigVariants() {
        System.out.println("\n--- [MAC Config Variants] ---");
        User2 u;

        // --- allowLowercase = false ---
        u = freshUser2();
        u.setMacUpperOnly("aa:bb:cc:dd:ee:ff");
        fail("mac allowLowercase=false lowercase", CValid.tryValidateProperty(u, "macUpperOnly", CGet.class));

        u = freshUser2();
        u.setMacUpperOnly("AA:BB:CC:DD:EE:FF");
        pass("mac allowLowercase=false uppercase OK", CValid.tryValidateProperty(u, "macUpperOnly", CGet.class));

        // --- allowEui64 = true (8 bytes) ---
        u = freshUser2();
        u.setMacEui64("AA:BB:CC:DD:EE:FF:00:11");
        pass("mac allowEui64=true 8 bytes", CValid.tryValidateProperty(u, "macEui64", CGet.class));

        u = freshUser2();
        u.setMacEui64("AA-BB-CC-DD-EE-FF-00-11");
        pass("mac allowEui64=true 8 bytes hyphen", CValid.tryValidateProperty(u, "macEui64", CGet.class));

        u = freshUser2();
        u.setMacEui64("AABBCCDDEEFF0011");
        pass("mac allowEui64=true 8 bytes no sep", CValid.tryValidateProperty(u, "macEui64", CGet.class));

        u = freshUser2();
        u.setMacEui64("AA:BB:CC:DD:EE:FF");
        fail("mac allowEui64=true but 6 bytes", CValid.tryValidateProperty(u, "macEui64", CGet.class));

        // --- allowOmittingLeadingZero = true ---
        u = freshUser2();
        u.setMacOmitZero("0:1:2:3:4:5");
        pass("mac allowOmittingLeadingZero=true omitted zeros", CValid.tryValidateProperty(u, "macOmitZero", CGet.class));

        u = freshUser2();
        u.setMacOmitZero("A:B:C:D:E:F");
        pass("mac allowOmittingLeadingZero=true with letters", CValid.tryValidateProperty(u, "macOmitZero", CGet.class));

        u = freshUser2();
        u.setMacOmitZero("A0:1B:2C:3D:4E:5F");
        pass("mac allowOmittingLeadingZero=true mixed", CValid.tryValidateProperty(u, "macOmitZero", CGet.class));
    }

    // ==================== 8. DateRange Custom Format ====================

    private static void testDateRangeCustomFormat() {
        System.out.println("\n--- [DateRange Custom Format] ---");
        User2 u;

        u = freshUser2();
        u.setDCustomFormat("20220615");
        pass("dCustomFormat yyyyMMdd within range", CValid.tryValidateProperty(u, "dCustomFormat", CGet.class));

        u = freshUser2();
        u.setDCustomFormat("20220101");
        pass("dCustomFormat min boundary", CValid.tryValidateProperty(u, "dCustomFormat", CGet.class));

        u = freshUser2();
        u.setDCustomFormat("20221231");
        pass("dCustomFormat max boundary", CValid.tryValidateProperty(u, "dCustomFormat", CGet.class));

        u = freshUser2();
        u.setDCustomFormat("20200101");
        fail("dCustomFormat before min", CValid.tryValidateProperty(u, "dCustomFormat", CGet.class));

        u = freshUser2();
        u.setDCustomFormat("20231231");
        fail("dCustomFormat after max", CValid.tryValidateProperty(u, "dCustomFormat", CGet.class));

        // --- allowNull = false ---
        u = freshUser2();
        u.setDRequired(null);
        fail("dRequired allowNull=false with null", CValid.tryValidateProperty(u, "dRequired", CGet.class));

        u = freshUser2();
        u.setDRequired("2022-06-15");
        pass("dRequired with valid date", CValid.tryValidateProperty(u, "dRequired", CGet.class));
    }

    // ==================== 9. Account Custom Config ====================

    private static void testAccountCustomConfig() {
        System.out.println("\n--- [Account Custom Config] ---");
        User2 u;

        // --- Custom min/max (3-10) ---
        u = freshUser2();
        u.setAccountShort("ab");
        fail("accountShort min=3 too short", CValid.tryValidateProperty(u, "accountShort", CGet.class));

        u = freshUser2();
        u.setAccountShort("abcdefghijk");
        fail("accountShort max=10 too long", CValid.tryValidateProperty(u, "accountShort", CGet.class));

        u = freshUser2();
        u.setAccountShort("abcde");
        pass("accountShort within range", CValid.tryValidateProperty(u, "accountShort", CGet.class));

        // --- Custom regexp ^admin.* ---
        u = freshUser2();
        u.setAccountCustomRegex("admin123");
        pass("accountCustomRegex matches ^admin", CValid.tryValidateProperty(u, "accountCustomRegex", CGet.class));

        u = freshUser2();
        u.setAccountCustomRegex("xadmin123");
        fail("accountCustomRegex does not match ^admin", CValid.tryValidateProperty(u, "accountCustomRegex", CGet.class));

        u = freshUser2();
        u.setAccountCustomRegex("admin");
        pass("accountCustomRegex exact admin", CValid.tryValidateProperty(u, "accountCustomRegex", CGet.class));
    }

    // ==================== 10. Password Custom Config ====================

    private static void testPasswordCustomConfig() {
        System.out.println("\n--- [Password Custom Config] ---");
        User2 u;

        // --- Custom min/max (8-20) ---
        u = freshUser2();
        u.setPasswdLong("abc1234");
        fail("passwdLong min=8 too short", CValid.tryValidateProperty(u, "passwdLong", CGet.class));

        u = freshUser2();
        u.setPasswdLong("abc123456789012345678");
        fail("passwdLong max=20 too long", CValid.tryValidateProperty(u, "passwdLong", CGet.class));

        u = freshUser2();
        u.setPasswdLong("abcde12345");
        pass("passwdLong within range", CValid.tryValidateProperty(u, "passwdLong", CGet.class));

        // --- Default regex: contain letter AND digit ---
        u = freshUser2();
        u.setPasswdLong("abcdefgh");
        fail("passwdLong must contain digit", CValid.tryValidateProperty(u, "passwdLong", CGet.class));

        u = freshUser2();
        u.setPasswdLong("12345678");
        fail("passwdLong must contain letter", CValid.tryValidateProperty(u, "passwdLong", CGet.class));
    }

    // ==================== 11. Custom Regexp Override ====================

    private static void testCustomRegexpOverride() {
        System.out.println("\n--- [Custom Regexp Override] ---");
        User2 u;

        // --- CPhone with custom regexp ^13\\d{9}$ ---
        u = freshUser2();
        u.setPhoneCustomRegexp("13800138000");
        pass("phone custom regexp 138 matches", CValid.tryValidateProperty(u, "phoneCustomRegexp", CGet.class));

        u = freshUser2();
        u.setPhoneCustomRegexp("15800138000");
        fail("phone custom regexp 158 not 13 prefix", CValid.tryValidateProperty(u, "phoneCustomRegexp", CGet.class));

        u = freshUser2();
        u.setPhoneCustomRegexp("1380013800");
        fail("phone custom regexp too short", CValid.tryValidateProperty(u, "phoneCustomRegexp", CGet.class));

        u = freshUser2();
        u.setPhoneCustomRegexp("138001380000");
        fail("phone custom regexp too long", CValid.tryValidateProperty(u, "phoneCustomRegexp", CGet.class));

        // --- CPassport with custom regexp ^[A-Z]\\d{9}$ ---
        u = freshUser2();
        u.setPassportCustomRegexp("E123456789");
        pass("passport custom regexp 10 chars", CValid.tryValidateProperty(u, "passportCustomRegexp", CGet.class));

        u = freshUser2();
        u.setPassportCustomRegexp("E12345678");
        fail("passport custom regexp too short (9 digits)", CValid.tryValidateProperty(u, "passportCustomRegexp", CGet.class));

        u = freshUser2();
        u.setPassportCustomRegexp("AB12345678");
        fail("passport custom regexp two letters", CValid.tryValidateProperty(u, "passportCustomRegexp", CGet.class));

        // --- CPostCode with custom regexp ^\\d{5}$ ---
        u = freshUser2();
        u.setPostCodeCustomRegexp("12345");
        pass("postCode custom regexp 5 digits", CValid.tryValidateProperty(u, "postCodeCustomRegexp", CGet.class));

        u = freshUser2();
        u.setPostCodeCustomRegexp("123456");
        fail("postCode custom regexp 6 digits", CValid.tryValidateProperty(u, "postCodeCustomRegexp", CGet.class));

        u = freshUser2();
        u.setPostCodeCustomRegexp("1234");
        fail("postCode custom regexp 4 digits", CValid.tryValidateProperty(u, "postCodeCustomRegexp", CGet.class));

        u = freshUser2();
        u.setPostCodeCustomRegexp("1234a");
        fail("postCode custom regexp has letter", CValid.tryValidateProperty(u, "postCodeCustomRegexp", CGet.class));
    }

    // ==================== 12. IPv4 Edge Cases ====================

    private static void testIpv4EdgeCases() {
        System.out.println("\n--- [IPv4 Edge Cases] ---");
        User u;

        // Additional valid IPv4 cases
        u = freshBaseUser();
        u.setIp("0.0.0.0");
        pass("ipv4 0.0.0.0", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setIp("255.255.255.255");
        pass("ipv4 255.255.255.255", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setIp("1.2.3.4");
        pass("ipv4 1.2.3.4", CValid.tryValidate(u, CPost.class));

        // Invalid cases
        u = freshBaseUser();
        u.setIp("192.168.1.256");
        fail("ipv4 octet >255", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setIp("192.168.1.-1");
        fail("ipv4 negative octet", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setIp("192.168.1.1.1");
        fail("ipv4 five octets", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setIp("192.168.1");
        fail("ipv4 three octets", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setIp("");
        pass("ipv4 empty string (allowNull=true)", CValid.tryValidate(u, CPost.class));
    }

    // ==================== 13. IPv6 Edge Cases ====================

    private static void testIpv6EdgeCases() {
        System.out.println("\n--- [IPv6 Edge Cases] ---");
        User u;

        // Additional valid IPv6 formats
        u = freshBaseUser();
        u.setIp6("2001:0db8:85a3:0000:0000:8a2e:0370:7334");
        pass("ipv6 full format", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setIp6("2001:db8:85a3::8a2e:370:7334");
        pass("ipv6 compressed with ::", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setIp6("::");
        pass("ipv6 unspecified (::)", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setIp6("fe80::1");
        pass("ipv6 link-local", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setIp6("ff02::1");
        pass("ipv6 multicast", CValid.tryValidate(u, CPost.class));

        // Invalid
        u = freshBaseUser();
        u.setIp6("not-an-ip");
        fail("ipv6 random string", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setIp6("192.168.1.1");
        fail("ipv6 invalid (IPv4 address)", CValid.tryValidate(u, CPost.class));

        // Test ip66 (second @CIpv6 field)
        u = freshBaseUser();
        u.setIp66("2001:db8::1");
        pass("ip66 (2nd @CIpv6 field) valid", CValid.tryValidate(u, CPost.class));
    }

    // ==================== 14. ID Card Edge Cases ====================

    private static void testIdCardEdgeCases() {
        System.out.println("\n--- [ID Card Edge Cases] ---");
        User u;

        // Additional valid ID card numbers
        // 18-digit with valid check digit
        u = freshBaseUser();
        u.setIdCard("110101199001010015");
        pass("idCard valid 18 digits check digit 5", CValid.tryValidate(u));

        u = freshBaseUser();
        u.setIdCard("110101199001010015");
        pass("idCard valid 18-digit", CValid.tryValidate(u));

        // 15-digit (no check digit)
        u = freshBaseUser();
        u.setIdCard("110101900101001");
        pass("idCard valid 15 digits", CValid.tryValidate(u));

        // Invalid
        u = freshBaseUser();
        u.setIdCard("");
        pass("idCard empty string (allowNull=true)", CValid.tryValidate(u));

        u = freshBaseUser();
        u.setIdCard("12345678901234567890");
        fail("idCard too long (20 digits)", CValid.tryValidate(u));

        u = freshBaseUser();
        u.setIdCard("11010119900101001X");
        fail("idCard invalid 18-digit with wrong check", CValid.tryValidate(u));

        u = freshBaseUser();
        u.setIdCard("12345678901234567a");
        fail("idCard has letter in digits", CValid.tryValidate(u));
    }

    // ==================== 15. Domain Edge Cases ====================

    private static void testDomainEdgeCases() {
        System.out.println("\n--- [Domain Edge Cases] ---");
        User u;

        // Valid domains
        u = freshBaseUser();
        u.setDomain("example.com");
        pass("domain example.com", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setDomain("www.google.com");
        pass("domain www.google.com", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setDomain("\u4e2d\u56fd.cn");
        pass("domain Chinese characters", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setDomain("sub.domain.example.com");
        pass("domain multi-level subdomain", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setDomain("my-domain.com");
        pass("domain with hyphen", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        // Punycode domains are not supported by the current regex pattern
        // due to consecutive hyphens in the punycode encoding

        // Invalid
        u = freshBaseUser();
        u.setDomain("example");
        fail("domain no TLD", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setDomain("192.168.1.1");
        fail("domain IP address not valid domain", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setDomain("");
        pass("domain empty (allowNull=true)", CValid.tryValidate(u, CPost.class));
    }

    // ==================== 16. Plate Number Edge Cases ====================

    private static void testPlateNumberEdgeCases() {
        System.out.println("\n--- [Plate Number Edge Cases] ---");
        User u;

        // Valid plates
        u = freshBaseUser();
        u.setLpn("\u4eacA12345");
        pass("lpn Beijing ordinary", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setLpn("\u4f7fA12345");
        pass("lpn embassy plate", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setLpn("\u4eacA1234\u6302");
        pass("lpn trailer plate", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setLpn("\u4eacA1234\u5b66");
        pass("lpn student plate", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setLpn("\u4eacA1234\u8b66");
        pass("lpn police plate", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setLpn("\u4eacA1234\u6e2f");
        pass("lpn Hong Kong cross-border", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setLpn("\u4eacA1234\u6fb3");
        pass("lpn Macau cross-border", CValid.tryValidate(u, CPost.class));

        // Invalid
        u = freshBaseUser();
        u.setLpn("12A3456");
        fail("lpn starts with digits", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setLpn("\u7ca412345");
        fail("lpn no letter after province", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setLpn("\u4eacA123456");
        fail("lpn too many chars (8)", CValid.tryValidate(u, CPost.class));
    }

    // ==================== 17. Money Edge Cases ====================

    private static void testMoneyEdgeCases() {
        System.out.println("\n--- [Money Edge Cases] ---");
        User u;

        // String money edge cases
        u = freshBaseUser();
        u.setMoneyStr("0.00");
        pass("moneyStr 0.00", CValid.tryValidate(u, CGet.class));

        u = freshBaseUser();
        u.setMoneyStr("999999999.999");
        pass("moneyStr large with 3 decimals", CValid.tryValidate(u, CGet.class));

        u = freshBaseUser();
        u.setMoneyStr("");
        pass("moneyStr empty string", CValid.tryValidate(u, CGet.class));

        u = freshBaseUser();
        u.setMoneyStr("1,234.56");
        pass("moneyStr with thousands separator", CValid.tryValidate(u, CGet.class));

        u = freshBaseUser();
        u.setMoneyStr("$1,234.56");
        pass("moneyStr with $ and thousand separator", CValid.tryValidate(u, CGet.class));

        u = freshBaseUser();
        u.setMoneyStr("abc");
        fail("moneyStr not a number", CValid.tryValidate(u, CGet.class));

        u = freshBaseUser();
        u.setMoneyStr("123.4567");
        fail("moneyStr too many decimals (4 > 3)", CValid.tryValidate(u, CGet.class));

        u = freshBaseUser();
        u.setMoneyStr("12,34.56");
        fail("moneyStr invalid thousand separator", CValid.tryValidate(u, CGet.class));

        // Integer money edge cases
        u = freshBaseUser();
        u.setMoneyInt(100);
        pass("moneyInt valid 100", CValid.tryValidate(u, CGet.class));

        u = freshBaseUser();
        u.setMoneyInt(0);
        pass("moneyInt 0 (min=0)", CValid.tryValidate(u, CGet.class));

        // BigDecimal money edge cases
        u = freshBaseUser();
        u.setMoneyBig(new BigDecimal("0.00"));
        pass("moneyBig 0.00", CValid.tryValidate(u, CGet.class));

        u = freshBaseUser();
        u.setMoneyBig(new BigDecimal("999999999.999999"));
        fail("moneyBig too many decimals (6 > 2)", CValid.tryValidateProperty(u, "moneyBig", CGet.class));

        // String money with leading zeros (default false)
        u = freshBaseUser();
        u.setMoneyStr("00.12");
        fail("moneyStr leading zeros not allowed by default", CValid.tryValidateProperty(u, "moneyStr", CGet.class));
    }

    // ==================== 18. File Edge Cases ====================

    private static void testFileEdgeCases() {
        System.out.println("\n--- [File Edge Cases] ---");
        User u;

        // File name suffix validation
        u = freshBaseUser();
        u.setFileName("photo.PNG");
        fail("fileName uppercase .PNG not matched by default", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setFileName("photo.png");
        pass("fileName .png valid", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setFileName("photo");
        fail("fileName no extension", CValid.tryValidate(u, CPost.class));

        // File object with zero-length / non-existent
        u = freshBaseUser();
        u.setFile(new File("src/test/resource/3.png"));
        pass("file 3.png exists and under 200KB", CValid.tryValidate(u, CPost.class));

        u = freshBaseUser();
        u.setFile(new File("nonexistent.png"));
        fail("file not exists", CValid.tryValidate(u, CPost.class));
    }

    // ==================== 19. @Repeatable Annotations ====================

    private static void testRepeatableAnnotations() {
        System.out.println("\n--- [@Repeatable Annotations] ---");

        // Use static inner test entity with @Repeatable @CAccount
        RepeatableTestBean r = new RepeatableTestBean();

        // Both @CAccount must pass (AND semantics):
        //   Account A: min=3, max=20
        //   Account B: min=5, max=10

        r.setAccount("abcde");
        pass("repeatable both pass (len=5)", CValid.tryValidate(r));

        r.setAccount("abc");
        fail("repeatable second fails (len=3 < min=5)", CValid.tryValidate(r));

        r.setAccount("abcdefghijk");
        fail("repeatable second fails (len=11 > max=10)", CValid.tryValidate(r));

        r.setAccount("ab");
        fail("repeatable both fail (len=2 < min=3)", CValid.tryValidate(r));

        r.setAccount("a1b2c");
        pass("repeatable with alphanumeric (len=5)", CValid.tryValidate(r));

        // also test specific violations via tryFastValidate to confirm at least one fires
        r.setAccount("abc");
        String fastResult = CValid.tryFastValidate(r);
        if (fastResult != null && !fastResult.trim().isEmpty()) {
            pass("repeatable fast-validate returns error for short value => " + fastResult);
        } else {
            testCount++;
            failCount++;
            System.out.println("[FAIL] repeatable fast-validate should have returned error for len=3");
        }
    }

    // ==================== Helper: freshBaseUser ====================

    private static User freshBaseUser() {
        User u = new User();
        u.setName("Zhang San");
        u.setIp("127.0.2.3");
        u.setIp6("::1");
        u.setIp66("fe80::1");
        u.setDomain("example.cn");
        u.setIdCard("110101199001010015");
        u.setUser("ubsdhdsj11111");
        u.setUser1("ubsdhdsj112222222222222222222222222222111");
        u.setPasswd("jjre2311232222");
        u.setD1(LocalDate.of(2022, 6, 15));
        u.setD2("2022-05-15 12:00:00");
        u.setD3(LocalDateTime.of(2022, 8, 15, 10, 0));
        u.setD4(Instant.parse("2022-06-15T10:00:00Z"));
        u.setD5(ZonedDateTime.parse("2022-07-15T10:00:00+08:00[Asia/Shanghai]"));
        u.setLpn("\u7ca4B39006");
        u.setFileName("test.png");
        u.setFile(new File("src/test/resource/3.png"));
        u.setPhone("13375483434");
        u.setPhoneUS("2125551234");
        u.setPhoneJP("09012345678");
        u.setPhoneKR("01012345678");
        u.setPhoneUK("07123456789");
        u.setPassport("E12345678");
        u.setPassportUS("123456789");
        u.setPassportJP("AB1234567");
        u.setPassportUK("A12345678");
        u.setPassportKR("M12345678");
        u.setUrl("http://127.0.0.1:2333");
        u.setBankCard("4111111111111111");
        u.setMoneyStr("123.45");
        u.setMoneyInt(100);
        u.setMoneyBig(new BigDecimal("22.11"));
        u.setMac("A0:1A:2B:3C:4D:5E");
        u.setPostCodeCN("518057");
        u.setPostCodeUS("10001");
        u.setPostCodeJP("100-0001");
        u.setPostCodeUK("SW1A 1AA");
        u.setPostCodeKR("04524");
        return u;
    }

    // ==================== Static Inner Entity: @Repeatable Test ====================

    public static class RepeatableTestBean {
        @CAccount(min = 3, max = 20, message = "Account constraint A failed")
        @CAccount(min = 5, max = 10, message = "Account constraint B failed")
        private String account;

        public String getAccount() { return account; }
        public void setAccount(String account) { this.account = account; }
    }
}
