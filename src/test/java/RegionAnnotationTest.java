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
 * 地区注解格式测试。
 * 样例只覆盖标准填写格式和明显格式错误，不校验号码真实性。
 */
public class RegionAnnotationTest {

    @Test
    public void testPhoneRegions() throws Exception {
        List<String> errors = new ArrayList<>();
        check(errors, "phoneCN",
            values("13800138000", "16612345678", "19912345678"),
            values("1380013800", "138001380000", "1380013800A"));
        check(errors, "phoneUS",
            values("2125551234", "4155552671", "2025550175"),
            values("1235551234", "2121551234", "212555123"));
        check(errors, "phoneJP",
            values("09012345678", "08012345678", "07012345678"),
            values("0312345678", "090-1234-5678", "0901234567"));
        check(errors, "phoneKR",
            values("01012345678", "01112345678", "01612345678"),
            values("01212345678", "010-1234-5678", "0101234567"));
        check(errors, "phoneUK",
            values("07123456789", "07911123456", "07700900123"),
            values("02079460958", "07123 456789", "0712345678"));
        assertNoErrors("手机号地区规则", errors);
    }

    @Test
    public void testPassportRegions() throws Exception {
        List<String> errors = new ArrayList<>();
        check(errors, "passportCN",
            values("E12345678", "G12345678", "D12345678"),
            values("e12345678", "123456789", "E1234567"));
        check(errors, "passportUS",
            values("123456789", "987654321", "100000001"),
            values("A12345678", "12345678", "1234567890"));
        check(errors, "passportJP",
            values("TR1234567", "A1234567", "AB0000001"),
            values("tr1234567", "ABC1234567", "TR123456"));
        check(errors, "passportKR",
            values("M12345678", "S12345678", "123456789"),
            values("m12345678", "M1234567", "AB1234567"));
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
            values("00000", "1000000", "10000A"));
        check(errors, "postCodeUS",
            values("10001", "90210", "30301"),
            values("0000", "1000A", "10001-123"));
        check(errors, "postCodeJP",
            values("1000001", "5300001", "0600005"),
            values("000-0000", "100000", "100000A"));
        check(errors, "postCodeKR",
            values("04524", "03187", "63584"),
            values("0000", "999999", "045-24"));
        check(errors, "postCodeUK",
            values("M11AE", "W1A1HQ", "GIR0AA"),
            values("EC1A 1B", "EC1A  1BB", "12345"));
        assertNoErrors("邮政编码地区规则", errors);
    }

    @Test
    public void testIdCardRegions() throws Exception {
        List<String> errors = new ArrayList<>();
        check(errors, "idCardCN",
            values("110101199001010015", "11010519491231002X", "440524188001010014"),
            values("1101011990010100X1", "11010120230229001Y", "110101900101001"));
        check(errors, "idCardUS",
            values("212345678", "457555462", "001010001"),
            values("457-55-5462", "21234567", "21234567A"));
        check(errors, "idCardJP",
            values("123456789018", "987654321093", "100000000005"),
            values("12345678901", "98765432109A", "100-000-0005"));
        check(errors, "idCardKR",
            values("9001011234568", "0002293234563", "8512312123455"),
            values("900101-1234568", "900101123456", "900101123456A"));
        check(errors, "idCardUK",
            values("AB123456C", "CE123456D", "HN123456A"),
            values("ab123456C", "AB12345C", "AB1234567"));
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
