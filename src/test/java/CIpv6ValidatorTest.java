import com.carpcap.hvp.constraintvalidators.CIpv6Validator;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * IPv6 字面量解析回归测试。
 */
public class CIpv6ValidatorTest {

    @Test
    public void shouldAcceptPureIpv6Literals() {
        assertTrue(CIpv6Validator.isIPv6("2001:0db8:85a3:0000:0000:8a2e:0370:7334"));
        assertTrue(CIpv6Validator.isIPv6("2001:db8:85a3::8a2e:370:7334"));
        assertTrue(CIpv6Validator.isIPv6("::"));
        assertTrue(CIpv6Validator.isIPv6("::1"));
        assertTrue(CIpv6Validator.isIPv6("2001:db8::"));
    }

    @Test
    public void shouldRejectValuesThatAreNotPureIpv6Literals() {
        // 域名必须直接失败，不能交给 DNS 解析。
        assertFalse(CIpv6Validator.isIPv6("ipv6.google.com"));
        assertFalse(CIpv6Validator.isIPv6("localhost"));
        assertFalse(CIpv6Validator.isIPv6("::ffff:192.168.1.1"));
        assertFalse(CIpv6Validator.isIPv6("fe80::1%eth0"));
        assertFalse(CIpv6Validator.isIPv6("2001:db8:::1"));
        assertFalse(CIpv6Validator.isIPv6("1:2:3:4:5:6:7:8:9"));
    }
}
