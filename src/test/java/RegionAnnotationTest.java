import com.carpcap.hvp.annotation.CIdCard;
import com.carpcap.hvp.annotation.CPassport;
import com.carpcap.hvp.annotation.CPhone;
import com.carpcap.hvp.annotation.CPostCode;
import com.carpcap.hvp.utils.CValid;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * 地区规则回归测试。
 *
 * 规则参考：https://github.com/google/libphonenumber、https://travel.state.gov、
 * https://www.gov.uk/government/publications/basic-passport-checks、https://www.upu.int、
 * 中国 GB 11643、日本数字厅、韩国居民登记号及美国 SSA 公布的编号规则。
 * 样例均为公开示例或按公开规则构造的测试号码，不代表真实个人证件。
 */
public class RegionAnnotationTest {

    @Test
    public void testPhoneRegions() throws Exception {
        List<String> errors = new ArrayList<>();
        check(errors, "phoneCN",
            values("13800138000", "16612345678", "19912345678"),
            values("12800138000", "1380013800", "1380013800A"));
        check(errors, "phoneUS",
            values("2125551234", "+1 415 555 2671", "(202) 555-0175"),
            values("1235551234", "2121551234", "212555123"));
        check(errors, "phoneJP",
            values("09012345678", "090-1234-5678", "03-1234-5678"),
            values("110", "0012345678", "090123456789"));
        check(errors, "phoneKR",
            values("01012345678", "010-1234-5678", "02-123-4567"),
            values("01212345678", "010123456", "0101234A567"));
        check(errors, "phoneUK",
            values("07123456789", "02079460958", "020 7946 0958"),
            values("00123456789", "071234567", "0712345678A"));
        assertNoErrors("手机号地区规则", errors);
    }

    @Test
    public void testPassportRegions() throws Exception {
        List<String> errors = new ArrayList<>();
        check(errors, "passportCN",
            values("E12345678", "G12345678", "D12345678"),
            values("e12345678", "123456789", "E1234567"));
        check(errors, "passportUS",
            values("A12345678", "C98765432", "123456789"),
            values("a12345678", "A1234567", "1234567890"));
        check(errors, "passportJP",
            values("TR1234567", "TK9876543", "AB0000001"),
            values("tr1234567", "A12345678", "TR123456"));
        check(errors, "passportKR",
            values("M12345678", "S12345678", "D12345678"),
            values("m12345678", "M1234567", "123456789"));
        check(errors, "passportUK",
            values("123456789", "987654321", "100000001"),
            values("A12345678", "12345678", "123 456 789"));
        assertNoErrors("护照地区规则", errors);
    }

    @Test
    public void testPostCodeRegions() throws Exception {
        List<String> errors = new ArrayList<>();
        check(errors, "postCodeCN",
            values("100000", "200000", "518000"),
            values("000000", "10000", "10000A"));
        check(errors, "postCodeUS",
            values("10001", "90210", "10001-1234"),
            values("00000", "1000A", "10001-123"));
        check(errors, "postCodeJP",
            values("100-0001", "530-0001", "060-0005"),
            values("000-0000", "1000001", "100-001"));
        check(errors, "postCodeKR",
            values("04524", "03187", "63584"),
            values("00000", "99999", "045-24"));
        check(errors, "postCodeUK",
            values("M1 1AE", "W1A 1HQ", "GIR 0AA"),
            values("EC1A 1B", "EC1A  1BB", "12345"));
        assertNoErrors("邮政编码地区规则", errors);
    }

    @Test
    public void testIdCardRegions() throws Exception {
        List<String> errors = new ArrayList<>();
        check(errors, "idCardCN",
            values("110101199001010015", "11010519491231002X", "440524188001010014"),
            values("110101199001010014", "110101202302290019", "110101900101001"));
        check(errors, "idCardUS",
            values("212345678", "457-55-5462", "001-01-0001"),
            values("000-12-3456", "212-00-3456", "212-34-0000"));
        check(errors, "idCardJP",
            values("123456789018", "987654321093", "100000000005"),
            values("123456789019", "987654321092", "100000000004"));
        check(errors, "idCardKR",
            values("900101-1234568", "0002293234563", "851231-2123455"),
            values("900101-1234567", "991332-1234567", "900101-5234568"));
        check(errors, "idCardUK",
            values("AB123456C", "CE123456D", "HN123456A"),
            values("GB123456C", "BG123456C", "ZZ123456C"));
        assertNoErrors("身份号码地区规则", errors);
    }

    private static String[] values(String first, String second, String third) {
        return new String[] {first, second, third};
    }

    private static void check(List<String> errors, String property,
                              String[] validValues, String[] invalidValues) throws Exception {
        RegionBean bean = new RegionBean();
        Field field = RegionBean.class.getDeclaredField(property);
        field.setAccessible(true);

        for (String value : validValues) {
            field.set(bean, value);
            if (!CValid.tryValidateProperty(bean, property).isEmpty()) {
                errors.add(property + " 应接受：" + value);
            }
        }
        for (String value : invalidValues) {
            field.set(bean, value);
            if (CValid.tryValidateProperty(bean, property).isEmpty()) {
                errors.add(property + " 应拒绝：" + value);
            }
        }
    }

    private static void assertNoErrors(String category, List<String> errors) {
        assertTrue(category + "存在实现偏差：\n" + String.join("\n", errors), errors.isEmpty());
    }

    private static class RegionBean {
        @CPhone(region = "CN") private String phoneCN;
        @CPhone(region = "US") private String phoneUS;
        @CPhone(region = "JP") private String phoneJP;
        @CPhone(region = "KR") private String phoneKR;
        @CPhone(region = "UK") private String phoneUK;

        @CPassport(region = "CN") private String passportCN;
        @CPassport(region = "US") private String passportUS;
        @CPassport(region = "JP") private String passportJP;
        @CPassport(region = "KR") private String passportKR;
        @CPassport(region = "UK") private String passportUK;

        @CPostCode(region = "CN") private String postCodeCN;
        @CPostCode(region = "US") private String postCodeUS;
        @CPostCode(region = "JP") private String postCodeJP;
        @CPostCode(region = "KR") private String postCodeKR;
        @CPostCode(region = "UK") private String postCodeUK;

        @CIdCard(region = "CN") private String idCardCN;
        @CIdCard(region = "US") private String idCardUS;
        @CIdCard(region = "JP") private String idCardJP;
        @CIdCard(region = "KR") private String idCardKR;
        @CIdCard(region = "UK") private String idCardUK;
    }
}
