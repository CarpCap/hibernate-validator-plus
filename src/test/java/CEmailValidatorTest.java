import com.carpcap.hvp.annotation.CEmail;
import com.carpcap.hvp.utils.CValid;
import org.junit.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 邮箱格式、域名名单与层级限制测试。
 */
public class CEmailValidatorTest {

    @Test
    public void shouldValidateBasicEmailFormatAndNullPolicy() {
        EmailBean bean = new EmailBean();
        assertTrue(isValid(bean, "email"));
        assertFalse(isValid(bean, "requiredEmail"));

        bean.email = "user.name+tag@example.com";
        assertTrue(isValid(bean, "email"));
        bean.email = "user..name@example.com";
        assertFalse(isValid(bean, "email"));
        bean.email = "user@-example.com";
        assertFalse(isValid(bean, "email"));
        bean.email = "user@example";
        assertTrue(isValid(bean, "email"));
    }

    @Test
    public void shouldApplyBlacklistToDomainAndSubdomains() {
        EmailBean bean = new EmailBean();
        bean.blacklistEmail = "user@outlook.com";
        assertFalse(isValid(bean, "blacklistEmail"));
        bean.blacklistEmail = "user@ee.gmail.com";
        assertFalse(isValid(bean, "blacklistEmail"));
        bean.blacklistEmail = "user@notgmail.com";
        assertTrue(isValid(bean, "blacklistEmail"));
        bean.blacklistEmail = "user@example.com";
        assertTrue(isValid(bean, "blacklistEmail"));
    }

    @Test
    public void shouldApplyWhitelistToDomainAndSubdomains() {
        EmailBean bean = new EmailBean();
        bean.whitelistEmail = "user@example.com";
        assertTrue(isValid(bean, "whitelistEmail"));
        bean.whitelistEmail = "user@service.example.com";
        assertTrue(isValid(bean, "whitelistEmail"));
        bean.whitelistEmail = "user@example.org";
        assertFalse(isValid(bean, "whitelistEmail"));
    }

    @Test
    public void shouldLimitMaximumLevel() {
        EmailBean bean = new EmailBean();
        bean.levelEmail = "user@com";
        assertTrue(isValid(bean, "levelEmail"));
        bean.levelEmail = "user@outlook.com";
        assertTrue(isValid(bean, "levelEmail"));
        bean.levelEmail = "user@eeo.outlook.com";
        assertTrue(isValid(bean, "levelEmail"));
        bean.levelEmail = "user@service.eeo.outlook.com";
        assertFalse(isValid(bean, "levelEmail"));
    }

    @Test
    public void shouldSelectMessageByFailureReason() {
        EmailBean bean = new EmailBean();

        bean.blacklistEmail = "user@gmail.com";
        assertMessageTemplate(bean, "blacklistEmail", "{com.carpcap.hvp.annotation.CEmail.blacklist.message}");

        bean.whitelistEmail = "user@example.org";
        assertMessageTemplate(bean, "whitelistEmail", "{com.carpcap.hvp.annotation.CEmail.whitelist.message}");

        bean.levelEmail = "user@service.eeo.outlook.com";
        assertMessageTemplate(bean, "levelEmail", "{com.carpcap.hvp.annotation.CEmail.level.message}");

        bean.customMessageEmail = "user@gmail.com";
        assertMessageTemplate(bean, "customMessageEmail", "自定义邮箱提示");
    }

    @Test
    public void shouldInterpolateChineseFailureMessages() {
        Locale originalLocale = Locale.getDefault();
        Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            EmailBean bean = new EmailBean();

            bean.blacklistEmail = "user@gmail.com";
            assertMessage(validator, bean, "blacklistEmail", "邮箱域名被禁止");

            bean.whitelistEmail = "user@example.org";
            assertMessage(validator, bean, "whitelistEmail", "邮箱域名不在允许的白名单中");

            bean.levelEmail = "user@service.eeo.outlook.com";
            assertMessage(validator, bean, "levelEmail", "邮箱域名超过允许的最大层级 2");
        } finally {
            Locale.setDefault(originalLocale);
        }
    }

    private static boolean isValid(EmailBean bean, String property) {
        return CValid.tryValidateProperty(bean, property).isEmpty();
    }

    private static void assertMessageTemplate(EmailBean bean, String property, String expected) {
        Set<ConstraintViolation<EmailBean>> violations = CValid.getValidator().validateProperty(bean, property);
        assertEquals(1, violations.size());
        assertEquals(expected, violations.iterator().next().getMessageTemplate());
    }

    private static void assertMessage(Validator validator, EmailBean bean, String property, String expected) {
        Set<ConstraintViolation<EmailBean>> violations = validator.validateProperty(bean, property);
        assertEquals(1, violations.size());
        assertEquals(expected, violations.iterator().next().getMessage());
    }

    private static class EmailBean {
        @CEmail
        private String email;

        @CEmail(allowNull = false)
        private String requiredEmail;

        @CEmail(listMode = CEmail.ListMode.BLACKLIST, domains = {"outlook.com", "gmail.com"})
        private String blacklistEmail;

        @CEmail(listMode = CEmail.ListMode.WHITELIST, domains = "example.com")
        private String whitelistEmail;

        @CEmail(level = 2)
        private String levelEmail;

        @CEmail(listMode = CEmail.ListMode.BLACKLIST, domains = "gmail.com", message = "自定义邮箱提示")
        private String customMessageEmail;
    }
}
