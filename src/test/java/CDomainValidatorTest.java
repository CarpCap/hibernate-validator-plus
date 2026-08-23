import com.carpcap.hvp.annotation.CDomain;
import com.carpcap.hvp.utils.CValid;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** @CDomain 层级和格式测试。 */
public class CDomainValidatorTest {
    private static class Bean {
        @CDomain(level = -1) String unlimited;
        @CDomain(level = 0, allowTld = true) String tldOnly;
        @CDomain(level = 1) String secondLevel;
        @CDomain(level = 2) String thirdLevel;
        @CDomain(allowNull = false) String required;
    }

    @Test
    public void validatesConfiguredLevels() {
        Bean bean = new Bean();
        bean.unlimited = "a.b.c.example.com";
        bean.tldOnly = "com";
        bean.secondLevel = "outlook.com";
        bean.thirdLevel = "eeo.outlook.com";
        bean.required = "ok.com";
        assertTrue(CValid.tryValidate(bean).isEmpty());
        bean.tldOnly = "outlook.com";
        assertFalse(CValid.tryValidateProperty(bean, "tldOnly").isEmpty());
        bean.secondLevel = "eeo.outlook.com";
        assertFalse(CValid.tryValidateProperty(bean, "secondLevel").isEmpty());
        bean.thirdLevel = "a.eeo.outlook.com";
        assertFalse(CValid.tryValidateProperty(bean, "thirdLevel").isEmpty());
    }

    @Test
    public void validatesFormatAndNullPolicy() {
        Bean bean = new Bean();
        bean.unlimited = "-bad.com";
        assertFalse(CValid.tryValidateProperty(bean, "unlimited").isEmpty());
        bean.unlimited = "192.168.1.1";
        assertFalse(CValid.tryValidateProperty(bean, "unlimited").isEmpty());
        bean.unlimited = "192.168.1.1.2.2";
        assertFalse(CValid.tryValidateProperty(bean, "unlimited").isEmpty());
        bean.unlimited = "中国.cn";
        assertTrue(CValid.tryValidateProperty(bean, "unlimited").isEmpty());
        bean.required = null;
        assertFalse(CValid.tryValidateProperty(bean, "required").isEmpty());
    }

    @Test
    public void rejectsTldByDefault() {
        Bean bean = new Bean();
        bean.unlimited = "com";
        assertFalse(CValid.tryValidateProperty(bean, "unlimited").isEmpty());
    }
}
