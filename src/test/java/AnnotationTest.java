import com.carpcap.hvp.groups.CGet;
import com.carpcap.hvp.groups.CPost;
import com.carpcap.hvp.groups.CPostDef;
import com.carpcap.hvp.utils.CValid;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * AnnotationTest - 正确数据与错误数据的完整验证测试
 *
 * @author CarpCap
 */
public class AnnotationTest {

    private static int testCount = 0;
    private static int passCount = 0;
    private static int failCount = 0;

    public static void main(String[] args) {
        System.out.println("=== hibernate-validator-plus Annotation Test ===\n");

        testAllValidData();
        testNameNotBlank();
        testPhoneCN();
        testPhoneUS();
        testPhoneJP();
        testPhoneKR();
        testPhoneUK();
        testIpv4();
        testIpv6();
        testDomain();
        testIdCard();
        testAccount();
        testPassword();
        testDateRangeLocalDate();
        testDateRangeString();
        testDateRangeLocalDateTime();
        testDateRangeInstant();
        testDateRangeZonedDateTime();
        testPlateNumber();
        testFileName();
        testFile();
        testUrl();
        testBankCard();
        testMoney();
        testMacAddress();
        testPostCodeCN();
        testPostCodeUS();
        testPostCodeJP();
        testPostCodeUK();
        testPostCodeKR();
        testGroupInheritance();

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

    private static User freshUser() {
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
        u.setLpn("粤B39006");
        u.setFileName("test.png");
        u.setFile(new File("src/test/resource/3.png"));
        u.setPhone("13375483434");
        u.setPhoneUS("2125551234");
        u.setPhoneJP("09012345678");
        u.setPhoneKR("01012345678");
        u.setPhoneUK("07123456789");
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

    // ==================== All valid data ====================

    private static void testAllValidData() {
        System.out.println("\n--- [All Valid Data] ---");
        User u = freshUser();
        pass("Default group", CValid.tryValidate(u));
        pass("CPost group", CValid.tryValidate(u, CPost.class));
        pass("CGet group", CValid.tryValidate(u, CGet.class));
    }

    // ==================== Name (@NotBlank, groups=CPost) ====================

    private static void testNameNotBlank() {
        System.out.println("\n--- [Name @NotBlank] ---");
        User u = freshUser();
        u.setName("");
        fail("name empty (CPost)", CValid.tryValidate(u, CPost.class));

        u.setName(null);
        fail("name null (CPost)", CValid.tryValidate(u, CPost.class));

        u.setName(null);
        pass("name null, Default group", CValid.tryValidate(u));
    }

    // ==================== Phone CN @CPhone(region="CN", groups=CPost, allowNull=false) ====================

    private static void testPhoneCN() {
        System.out.println("\n--- [Phone CN @CPhone(region=CN)] ---");
        User u = freshUser();

        u.setPhone("13375483434");
        pass("CN phone 133...", CValid.tryValidate(u, CPost.class));
        u.setPhone("15912345678");
        pass("CN phone 159...", CValid.tryValidate(u, CPost.class));
        u.setPhone("19912345678");
        pass("CN phone 199...", CValid.tryValidate(u, CPost.class));

        u.setPhone("12375483434");
        fail("CN phone starts with 12", CValid.tryValidate(u, CPost.class));
        u.setPhone("1337548343");
        fail("CN phone too short (10 digits)", CValid.tryValidate(u, CPost.class));
        u.setPhone("133754834345");
        fail("CN phone too long (12 digits)", CValid.tryValidate(u, CPost.class));
        u.setPhone("1337548343a");
        fail("CN phone has letter", CValid.tryValidate(u, CPost.class));
        u.setPhone(null);
        fail("CN phone null (allowNull=false)", CValid.tryValidate(u, CPost.class));
    }

    // ==================== Phone US @CPhone(region="US", groups=CPost, allowNull=false) ====================

    private static void testPhoneUS() {
        System.out.println("\n--- [Phone US @CPhone(region=US)] ---");
        User u = freshUser();

        u.setPhoneUS("2125551234");
        pass("US phone 2125551234", CValid.tryValidate(u, CPost.class));
        u.setPhoneUS("+12125551234");
        pass("US phone +12125551234", CValid.tryValidate(u, CPost.class));
        u.setPhoneUS("1-212-555-1234");
        pass("US phone 1-212-555-1234", CValid.tryValidate(u, CPost.class));
        u.setPhoneUS("(212) 555-1234");
        pass("US phone (212) 555-1234", CValid.tryValidate(u, CPost.class));
        u.setPhoneUS("5551234567");
        pass("US phone 5551234567 (no 1 prefix)", CValid.tryValidate(u, CPost.class));

        u.setPhoneUS("12345");
        fail("US phone too short", CValid.tryValidate(u, CPost.class));
        u.setPhoneUS("212555123");
        fail("US phone 9 digits", CValid.tryValidate(u, CPost.class));
        u.setPhoneUS("212-555-12a4");
        fail("US phone has letter", CValid.tryValidate(u, CPost.class));
        u.setPhoneUS(null);
        fail("US phone null (allowNull=false)", CValid.tryValidate(u, CPost.class));
    }

    // ==================== Phone JP @CPhone(region="JP", groups=CPost, allowNull=false) ====================

    private static void testPhoneJP() {
        System.out.println("\n--- [Phone JP @CPhone(region=JP)] ---");
        User u = freshUser();

        u.setPhoneJP("09012345678");
        pass("JP phone 09012345678 (mobile)", CValid.tryValidate(u, CPost.class));
        u.setPhoneJP("08012345678");
        pass("JP phone 08012345678 (mobile)", CValid.tryValidate(u, CPost.class));
        u.setPhoneJP("0312345678");
        pass("JP phone 0312345678 (Tokyo)", CValid.tryValidate(u, CPost.class));
        u.setPhoneJP("0120123456");
        pass("JP phone 0120123456 (toll-free)", CValid.tryValidate(u, CPost.class));

        u.setPhoneJP("090123456");
        fail("JP phone too short", CValid.tryValidate(u, CPost.class));
        u.setPhoneJP("110");
        fail("JP phone too short (110)", CValid.tryValidate(u, CPost.class));
        u.setPhoneJP("0901234567a");
        fail("JP phone has letter", CValid.tryValidate(u, CPost.class));
        u.setPhoneJP("2125551234");
        fail("JP phone does not start with 0", CValid.tryValidate(u, CPost.class));
        u.setPhoneJP(null);
        fail("JP phone null (allowNull=false)", CValid.tryValidate(u, CPost.class));
    }

    // ==================== Phone KR @CPhone(region="KR", groups=CPost, allowNull=false) ====================

    private static void testPhoneKR() {
        System.out.println("\n--- [Phone KR @CPhone(region=KR)] ---");
        User u = freshUser();

        u.setPhoneKR("01012345678");
        u.setPhoneUK("07123456789");
        pass("KR phone 01012345678 (mobile)", CValid.tryValidate(u, CPost.class));
        u.setPhoneKR("0111234567");
        pass("KR phone 0111234567 (old mobile)", CValid.tryValidate(u, CPost.class));
        u.setPhoneKR("01612345678");
        pass("KR phone 01612345678 (mobile)", CValid.tryValidate(u, CPost.class));

        u.setPhoneKR("0123456789");
        fail("KR phone starts with 01[0-9] only", CValid.tryValidate(u, CPost.class));
        u.setPhoneKR("010123456");
        fail("KR phone too short", CValid.tryValidate(u, CPost.class));
        u.setPhoneKR("010123456789");
        fail("KR phone too long", CValid.tryValidate(u, CPost.class));
        u.setPhoneKR("0101234a567");
        fail("KR phone has letter", CValid.tryValidate(u, CPost.class));
        u.setPhoneKR(null);
        fail("KR phone null (allowNull=false)", CValid.tryValidate(u, CPost.class));
    }

    // ==================== Phone UK @CPhone(region="UK", groups=CPost, allowNull=false) ====================

    private static void testPhoneUK() {
        System.out.println("\n--- [Phone UK @CPhone(region=UK)] ---");
        User u = freshUser();

        u.setPhoneUK("07123456789");
        pass("UK phone 07123456789 (mobile)", CValid.tryValidate(u, CPost.class));
        u.setPhoneUK("02079460958");
        pass("UK phone 02079460958 (London)", CValid.tryValidate(u, CPost.class));
        u.setPhoneUK("01632960001");
        pass("UK phone 01632960001 (geographic)", CValid.tryValidate(u, CPost.class));
        u.setPhoneUK("08001111111");
        pass("UK phone 08001111111 (freephone)", CValid.tryValidate(u, CPost.class));

        u.setPhoneUK("071234567");
        fail("UK phone too short (9 digits)", CValid.tryValidate(u, CPost.class));
        u.setPhoneUK("071234567890");
        fail("UK phone too long (12 digits)", CValid.tryValidate(u, CPost.class));
        u.setPhoneUK("12345678901");
        fail("UK phone does not start with 0", CValid.tryValidate(u, CPost.class));
        u.setPhoneUK("0712345678a");
        fail("UK phone has letter", CValid.tryValidate(u, CPost.class));
        u.setPhoneUK(null);
        fail("UK phone null (allowNull=false)", CValid.tryValidate(u, CPost.class));
    }

    private static void testIpv4() {
        System.out.println("\n--- [IPv4 @CIpv4] ---");
        User u = freshUser();

        u.setIp("192.168.1.1");
        pass("ip 192.168.1.1", CValid.tryValidate(u, CPost.class));
        u.setIp("255.255.255.255");
        pass("ip 255.255.255.255", CValid.tryValidate(u, CPost.class));

        u.setIp("256.1.2.3");
        fail("ip 256...", CValid.tryValidate(u, CPost.class));
        u.setIp("192.168.1");
        fail("ip missing octet", CValid.tryValidate(u, CPost.class));
        u.setIp("abc.def.ghi.jkl");
        fail("ip non-numeric", CValid.tryValidate(u, CPost.class));
    }

    // ==================== IPv6 @CIpv6(groups=CPost) ====================

    private static void testIpv6() {
        System.out.println("\n--- [IPv6 @CIpv6] ---");
        User u = freshUser();

        u.setIp6("::1");
        pass("ip6 ::1", CValid.tryValidate(u, CPost.class));
        u.setIp6("2001:db8::");
        pass("ip6 2001:db8::", CValid.tryValidate(u, CPost.class));

        u.setIp6("not-an-ip");
        fail("ip6 random string", CValid.tryValidate(u, CPost.class));
    }

    // ==================== Domain @CDomain(groups=CPost) ====================

    private static void testDomain() {
        System.out.println("\n--- [Domain @CDomain] ---");
        User u = freshUser();

        u.setDomain("example.com");
        pass("domain example.com", CValid.tryValidate(u, CPost.class));
        u.setDomain("www.google.com");
        pass("domain www.google.com", CValid.tryValidate(u, CPost.class));

        u.setDomain("example");
        fail("domain no TLD", CValid.tryValidate(u, CPost.class));
    }

    // ==================== ID Card @CIdCard ====================

    private static void testIdCard() {
        System.out.println("\n--- [ID Card @CIdCard] ---");
        User u = freshUser();

        u.setIdCard("110101199001010015");
        pass("idCard 18 digits correct check", CValid.tryValidate(u));
        u.setIdCard("110101900101001");
        pass("idCard 15 digits", CValid.tryValidate(u));

        u.setIdCard("123456789012345678");
        fail("idCard wrong province", CValid.tryValidate(u));
        u.setIdCard("110101199001010014");
        fail("idCard wrong check digit", CValid.tryValidate(u));
        u.setIdCard("12345");
        fail("idCard too short", CValid.tryValidate(u));
    }

    // ==================== Account @CAccount ====================

    private static void testAccount() {
        System.out.println("\n--- [Account @CAccount] ---");
        User u = freshUser();

        u.setUser("abcde");
        pass("account min length 5", CValid.tryValidate(u));
        u.setUser("a123456789012345");
        pass("account max length 16", CValid.tryValidate(u));
        u.setUser("a_b_c_d_e");
        pass("account underscores", CValid.tryValidate(u));

        u.setUser("abcd");
        fail("account too short <5", CValid.tryValidate(u));
        u.setUser("a1234567890123456");
        fail("account too long >16", CValid.tryValidate(u));
        u.setUser("1abcde");
        fail("account starts with digit", CValid.tryValidate(u));
        u.setUser("abc@de");
        fail("account special char", CValid.tryValidate(u));
    }

    // ==================== Password @CPassword ====================

    private static void testPassword() {
        System.out.println("\n--- [Password @CPassword] ---");
        User u = freshUser();

        u.setPasswd("abc123");
        pass("password min 6 letter+digit", CValid.tryValidate(u));
        u.setPasswd("a1b2c3d4e5f6g7h8i");
        pass("password 18 chars", CValid.tryValidate(u));

        u.setPasswd("abc12");
        fail("password too short <6", CValid.tryValidate(u));
        u.setPasswd("abcdef");
        fail("password no digit", CValid.tryValidate(u));
        u.setPasswd("123456");
        fail("password no letter", CValid.tryValidate(u));
        u.setPasswd("a1b2c3d4e5f6g7h8i9j");
        fail("password too long >18", CValid.tryValidate(u));
    }

    // ==================== DateRange LocalDate d1 ====================

    private static void testDateRangeLocalDate() {
        System.out.println("\n--- [DateRange d1 min=2022-06-01 max=2022-06-30] ---");
        User u = freshUser();

        u.setD1(LocalDate.of(2022, 6, 1));
        pass("d1 min boundary", CValid.tryValidate(u));
        u.setD1(LocalDate.of(2022, 6, 30));
        pass("d1 max boundary", CValid.tryValidate(u));

        u.setD1(LocalDate.of(2022, 5, 31));
        fail("d1 before min", CValid.tryValidate(u));
        u.setD1(LocalDate.of(2022, 7, 1));
        fail("d1 after max", CValid.tryValidate(u));
    }

    // ==================== DateRange String d2 ====================

    private static void testDateRangeString() {
        System.out.println("\n--- [DateRange d2 min=2022-04-01 max=2022-06-30] ---");
        User u = freshUser();

        u.setD2("2022-04-01");
        pass("d2 min boundary", CValid.tryValidate(u));
        u.setD2("2022-06-30 23:59:59");
        pass("d2 max boundary", CValid.tryValidate(u));

        u.setD2("2022-03-31");
        fail("d2 before min", CValid.tryValidate(u));
        u.setD2("2022-07-01");
        fail("d2 after max", CValid.tryValidate(u));
    }

    // ==================== DateRange LocalDateTime d3 ====================

    private static void testDateRangeLocalDateTime() {
        System.out.println("\n--- [DateRange d3 min=2022-08-01 00:30:00 max=2022-08-30 12:30:00 allowNull=false] ---");
        User u = freshUser();

        u.setD3(LocalDateTime.of(2022, 8, 1, 0, 30));
        pass("d3 min boundary", CValid.tryValidate(u));
        u.setD3(LocalDateTime.of(2022, 8, 30, 12, 30));
        pass("d3 max boundary", CValid.tryValidate(u));

        u.setD3(LocalDateTime.of(2022, 8, 1, 0, 29, 59));
        fail("d3 before min", CValid.tryValidate(u));
        u.setD3(LocalDateTime.of(2022, 8, 31, 0, 0));
        fail("d3 after max (day after)", CValid.tryValidate(u));
        u.setD3(null);
        fail("d3 null (allowNull=false)", CValid.tryValidate(u));
    }

    // ==================== DateRange Instant d4 ====================

    private static void testDateRangeInstant() {
        System.out.println("\n--- [DateRange d4 Instant min=2022-06-01 max=2022-06-30] ---");
        User u = freshUser();

        u.setD4(Instant.parse("2022-06-01T00:00:00Z"));
        pass("d4 Instant min boundary", CValid.tryValidate(u));
        u.setD4(Instant.parse("2022-06-30T15:59:59Z"));
        pass("d4 Instant max boundary", CValid.tryValidate(u));

        u.setD4(Instant.parse("2022-05-31T15:59:59Z"));
        fail("d4 Instant before min", CValid.tryValidate(u));
        u.setD4(Instant.parse("2022-07-01T00:00:00Z"));
        fail("d4 Instant after max", CValid.tryValidate(u));
    }

    // ==================== DateRange ZonedDateTime d5 ====================

    private static void testDateRangeZonedDateTime() {
        System.out.println("\n--- [DateRange d5 ZonedDateTime min=2022-07-01 00:30:00 max=2022-07-30 12:30:00 allowNull=false] ---");
        User u = freshUser();

        u.setD5(ZonedDateTime.parse("2022-07-01T00:30:00+08:00[Asia/Shanghai]"));
        pass("d5 ZonedDateTime min boundary", CValid.tryValidate(u));
        u.setD5(ZonedDateTime.parse("2022-07-30T12:30:00+08:00[Asia/Shanghai]"));
        pass("d5 ZonedDateTime max boundary", CValid.tryValidate(u));

        u.setD5(ZonedDateTime.parse("2022-07-01T00:29:59+08:00[Asia/Shanghai]"));
        fail("d5 ZonedDateTime before min", CValid.tryValidate(u));
        u.setD5(ZonedDateTime.parse("2022-07-31T00:00:00+08:00[Asia/Shanghai]"));
        fail("d5 ZonedDateTime after max", CValid.tryValidate(u));
        u.setD5(null);
        fail("d5 ZonedDateTime null (allowNull=false)", CValid.tryValidate(u));
    }

    // ==================== Plate Number @CPlateNumber(groups=CPost) ====================

    private static void testPlateNumber() {
        System.out.println("\n--- [Plate @CPlateNumber] ---");
        User u = freshUser();

        u.setLpn("京A12345");
        pass("lpn Beijing", CValid.tryValidate(u, CPost.class));
        u.setLpn("沪B67890");
        pass("lpn Shanghai", CValid.tryValidate(u, CPost.class));
        u.setLpn("粤B39006");
        pass("lpn Guangdong", CValid.tryValidate(u, CPost.class));

        u.setLpn("12A3456");
        fail("lpn first two not letters", CValid.tryValidate(u, CPost.class));
        u.setLpn("粤12345");
        fail("lpn no letter after province", CValid.tryValidate(u, CPost.class));
    }

    // ==================== File Name @CFile(groups=CPost, suffix=jpg/jpeg/png) ====================

    private static void testFileName() {
        System.out.println("\n--- [FileName @CFile suffix=jpg/jpeg/png] ---");
        User u = freshUser();

        u.setFileName("photo.jpg");
        pass("fileName .jpg", CValid.tryValidate(u, CPost.class));
        u.setFileName("photo.jpeg");
        pass("fileName .jpeg", CValid.tryValidate(u, CPost.class));
        u.setFileName("photo.png");
        pass("fileName .png", CValid.tryValidate(u, CPost.class));

        u.setFileName("photo.gif");
        fail("fileName .gif not allowed", CValid.tryValidate(u, CPost.class));
        u.setFileName("no_extension");
        fail("fileName no extension", CValid.tryValidate(u, CPost.class));
    }

    // ==================== File @CFile(groups=CPost, size<=200KB, allowNull=false) ====================

    private static void testFile() {
        System.out.println("\n--- [File @CFile size<=200KB allowNull=false] ---");
        User u = freshUser();

        u.setFile(new File("src/test/resource/3.png"));
        pass("file 3.png valid", CValid.tryValidate(u, CPost.class));

        u.setFile(new File("nonexistent.png"));
        fail("file not exists", CValid.tryValidate(u, CPost.class));
        u.setFile(null);
        fail("file null (allowNull=false)", CValid.tryValidate(u, CPost.class));
    }

    // ==================== URL @CUrl(groups=CGet) ====================

    private static void testUrl() {
        System.out.println("\n--- [URL @CUrl] ---");

        User u = freshUser();
        u.setUrl("http://example.com");
        pass("url http://example.com", CValid.tryValidate(u, CGet.class));

        u = freshUser();
        u.setUrl("https://www.google.com");
        pass("url https://www.google.com", CValid.tryValidate(u, CGet.class));

        u = freshUser();
        u.setUrl("http://127.0.0.1:2333");
        pass("url 127.0.0.1 port", CValid.tryValidate(u, CGet.class));

        u = freshUser();
        u.setUrl("ftp://files.example.com");
        fail("url ftp not allowed", CValid.tryValidate(u, CGet.class));

        u = freshUser();
        u.setUrl("not-a-url");
        fail("url random string", CValid.tryValidate(u, CGet.class));
    }

    // ==================== Bank Card @CBankCard(groups=CGet) ====================

    private static void testBankCard() {
        System.out.println("\n--- [BankCard @CBankCard] ---");

        User u = freshUser();
        u.setBankCard("4111111111111111");
        pass("bankCard Luhn-valid", CValid.tryValidate(u, CGet.class));

        u = freshUser();
        u.setBankCard("4111 1111 1111 1111");
        pass("bankCard with spaces", CValid.tryValidate(u, CGet.class));

        u = freshUser();
        u.setBankCard("1234567890123456");
        fail("bankCard fails Luhn", CValid.tryValidate(u, CGet.class));

        u = freshUser();
        u.setBankCard("1234");
        fail("bankCard too short", CValid.tryValidate(u, CGet.class));

        u = freshUser();
        u.setBankCard("abcd1234efgh5678");
        fail("bankCard has letters", CValid.tryValidate(u, CGet.class));
    }

    // ==================== Money @CMoney(groups=CGet) ====================

    private static void testMoney() {
        System.out.println("\n--- [Money @CMoney] ---");

        User u = freshUser();
        u.setMoneyStr("123.45");
        pass("moneyStr valid", CValid.tryValidate(u, CGet.class));

        u = freshUser();
        u.setMoneyStr("abc");
        fail("moneyStr not a number", CValid.tryValidate(u, CGet.class));

        u = freshUser();
        u.setMoneyStr("123.4567");
        fail("moneyStr too many decimals", CValid.tryValidate(u, CGet.class));

        u = freshUser();
        u.setMoneyInt(100);
        pass("moneyInt 100", CValid.tryValidate(u, CGet.class));

        u = freshUser();
        u.setMoneyInt(0);
        pass("moneyInt 0", CValid.tryValidate(u, CGet.class));

        u = freshUser();
        u.setMoneyBig(new BigDecimal("22.11"));
        pass("moneyBig 22.11", CValid.tryValidate(u, CGet.class));

        u = freshUser();
        u.setMoneyBig(new BigDecimal("0.00"));
        pass("moneyBig 0.00", CValid.tryValidate(u, CGet.class));

        u = freshUser();
        u.setMoneyBig(new BigDecimal("999999999.99"));
        pass("moneyBig large", CValid.tryValidate(u, CGet.class));
    }

    // ==================== MAC Address @CMacAddress(groups=CGet, allowNull=false) ====================

    private static void testMacAddress() {
        System.out.println("\n--- [MAC @CMacAddress] ---");

        User u = freshUser();
        u.setMac("AA:BB:CC:DD:EE:FF");
        pass("mac colon uppercase", CValid.tryValidate(u, CGet.class));

        u = freshUser();
        u.setMac("AA-BB-CC-DD-EE-FF");
        pass("mac hyphen separated", CValid.tryValidate(u, CGet.class));

        u = freshUser();
        u.setMac("AABBCCDDEEFF");
        pass("mac no separator", CValid.tryValidate(u, CGet.class));

        u = freshUser();
        u.setMac("aa:bb:cc:dd:ee:ff");
        pass("mac lowercase", CValid.tryValidate(u, CGet.class));

        u = freshUser();
        u.setMac("GG:HH:II:JJ:KK:LL");
        fail("mac invalid hex chars", CValid.tryValidate(u, CGet.class));

        u = freshUser();
        u.setMac("AA:BB:CC:DD:EE");
        fail("mac only 5 bytes", CValid.tryValidate(u, CGet.class));

        u = freshUser();
        u.setMac("AA:BB:CC:DD:EE:FF:GG");
        fail("mac 7 bytes", CValid.tryValidate(u, CGet.class));

        u = freshUser();
        u.setMac(null);
        fail("mac null (allowNull=false)", CValid.tryValidate(u, CGet.class));
    }

    // ==================== Group Inheritance CPostDef ====================

    private static void testGroupInheritance() {
        System.out.println("\n--- [Group Inheritance CPostDef = CPost + Default] ---");

        User u = freshUser();
        pass("CPostDef all valid", CValid.tryValidate(u, CPostDef.class));

        u.setPhone("11111111111");
        fail("CPostDef invalid phone", CValid.tryValidate(u, CPostDef.class));

        u = freshUser();
        u.setIdCard("12345");
        fail("CPostDef invalid idCard (Default)", CValid.tryValidate(u, CPostDef.class));
    }

    // ==================== PostCode CN @CPostCode(region="CN", groups=CPost, allowNull=false) ====================

    private static void testPostCodeCN() {
        System.out.println("\n--- [PostCode CN @CPostCode(region=CN)] ---");
        User u = freshUser();

        u.setPostCodeCN("518057");
        pass("CN postcode 518057", CValid.tryValidate(u, CPost.class));
        u.setPostCodeCN("100010");
        pass("CN postcode 100010", CValid.tryValidate(u, CPost.class));

        u.setPostCodeCN("12345");
        fail("CN postcode too short (5 digits)", CValid.tryValidate(u, CPost.class));
        u.setPostCodeCN("1234567");
        fail("CN postcode too long (7 digits)", CValid.tryValidate(u, CPost.class));
        u.setPostCodeCN("12a456");
        fail("CN postcode has letter", CValid.tryValidate(u, CPost.class));
        u.setPostCodeCN("100-001");
        fail("CN postcode with hyphen", CValid.tryValidate(u, CPost.class));
        u.setPostCodeCN(null);
        fail("CN postcode null (allowNull=false)", CValid.tryValidate(u, CPost.class));
    }

    // ==================== PostCode US @CPostCode(region="US", groups=CPost, allowNull=false) ====================

    private static void testPostCodeUS() {
        System.out.println("\n--- [PostCode US @CPostCode(region=US)] ---");
        User u = freshUser();

        u.setPostCodeUS("10001");
        pass("US postcode 10001 (5-digit)", CValid.tryValidate(u, CPost.class));
        u.setPostCodeUS("90210");
        pass("US postcode 90210 (5-digit)", CValid.tryValidate(u, CPost.class));
        u.setPostCodeUS("10001-1234");
        pass("US postcode 10001-1234 (ZIP+4)", CValid.tryValidate(u, CPost.class));

        u.setPostCodeUS("1234");
        fail("US postcode too short (4 digits)", CValid.tryValidate(u, CPost.class));
        u.setPostCodeUS("123456");
        fail("US postcode 6 digits", CValid.tryValidate(u, CPost.class));
        u.setPostCodeUS("10001-123");
        fail("US postcode ZIP+3", CValid.tryValidate(u, CPost.class));
        u.setPostCodeUS("10-001");
        fail("US postcode with hyphen in wrong position", CValid.tryValidate(u, CPost.class));
        u.setPostCodeUS(null);
        fail("US postcode null (allowNull=false)", CValid.tryValidate(u, CPost.class));
    }

    // ==================== PostCode JP @CPostCode(region="JP", groups=CPost, allowNull=false) ====================

    private static void testPostCodeJP() {
        System.out.println("\n--- [PostCode JP @CPostCode(region=JP)] ---");
        User u = freshUser();

        u.setPostCodeJP("100-0001");
        pass("JP postcode 100-0001", CValid.tryValidate(u, CPost.class));
        u.setPostCodeJP("530-0001");
        pass("JP postcode 530-0001", CValid.tryValidate(u, CPost.class));

        u.setPostCodeJP("100-000");
        fail("JP postcode too short (7 chars)", CValid.tryValidate(u, CPost.class));
        u.setPostCodeJP("1000-000");
        fail("JP postcode 4-3 format", CValid.tryValidate(u, CPost.class));
        u.setPostCodeJP("100 0001");
        fail("JP postcode with space instead of hyphen", CValid.tryValidate(u, CPost.class));
        u.setPostCodeJP("1000001");
        fail("JP postcode no hyphen", CValid.tryValidate(u, CPost.class));
        u.setPostCodeJP(null);
        fail("JP postcode null (allowNull=false)", CValid.tryValidate(u, CPost.class));
    }

    // ==================== PostCode UK @CPostCode(region="UK", groups=CPost, allowNull=false) ====================

    private static void testPostCodeUK() {
        System.out.println("\n--- [PostCode UK @CPostCode(region=UK)] ---");
        User u = freshUser();

        u.setPostCodeUK("SW1A 1AA");
        pass("UK postcode SW1A 1AA", CValid.tryValidate(u, CPost.class));
        u.setPostCodeUK("M1 1AE");
        pass("UK postcode M1 1AE", CValid.tryValidate(u, CPost.class));
        u.setPostCodeUK("EC1A 1BB");
        pass("UK postcode EC1A 1BB", CValid.tryValidate(u, CPost.class));

        u.setPostCodeUK("SW1A1AA");
        pass("UK postcode SW1A1AA (no space)", CValid.tryValidate(u, CPost.class));

        u.setPostCodeUK("12345");
        fail("UK postcode all digits", CValid.tryValidate(u, CPost.class));
        u.setPostCodeUK("SW1A-1AA");
        fail("UK postcode with hyphen", CValid.tryValidate(u, CPost.class));
        u.setPostCodeUK("SW1A  1AA");
        fail("UK postcode double space", CValid.tryValidate(u, CPost.class));
        u.setPostCodeUK(null);
        fail("UK postcode null (allowNull=false)", CValid.tryValidate(u, CPost.class));
    }

    // ==================== PostCode KR @CPostCode(region="KR", groups=CPost, allowNull=false) ====================

    private static void testPostCodeKR() {
        System.out.println("\n--- [PostCode KR @CPostCode(region=KR)] ---");
        User u = freshUser();

        u.setPostCodeKR("04524");
        pass("KR postcode 04524", CValid.tryValidate(u, CPost.class));
        u.setPostCodeKR("03187");
        pass("KR postcode 03187", CValid.tryValidate(u, CPost.class));

        u.setPostCodeKR("1234");
        fail("KR postcode too short (4 digits)", CValid.tryValidate(u, CPost.class));
        u.setPostCodeKR("123456");
        fail("KR postcode too long (6 digits)", CValid.tryValidate(u, CPost.class));
        u.setPostCodeKR("12a45");
        fail("KR postcode has letter", CValid.tryValidate(u, CPost.class));
        u.setPostCodeKR("045-24");
        fail("KR postcode with hyphen", CValid.tryValidate(u, CPost.class));
        u.setPostCodeKR(null);
        fail("KR postcode null (allowNull=false)", CValid.tryValidate(u, CPost.class));
    }
}
