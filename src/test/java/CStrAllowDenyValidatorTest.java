import com.carpcap.hvp.annotation.CStrAllow;
import com.carpcap.hvp.annotation.CStrDeny;
import com.carpcap.hvp.utils.CValid;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** @CStrAllow 和 @CStrDeny 测试。 */
public class CStrAllowDenyValidatorTest {
    private static class Bean {
        @CStrAllow(value = {"draft", "published"}) String status;
        @CStrDeny(value = {"admin", "root"}) String role;
    }

    @Test
    public void validatesWhitelistAndBlacklist() {
        Bean bean = new Bean();
        bean.status = "draft";
        bean.role = "user";
        assertTrue(CValid.tryValidate(bean).isEmpty());
        bean.status = "deleted";
        assertFalse(CValid.tryValidateProperty(bean, "status").isEmpty());
        bean.role = "root";
        assertFalse(CValid.tryValidateProperty(bean, "role").isEmpty());
    }
}
