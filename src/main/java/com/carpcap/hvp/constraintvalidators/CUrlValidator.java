package com.carpcap.hvp.constraintvalidators;

import cn.hutool.core.lang.Validator;
import com.carpcap.hvp.annotation.CUrl;
import com.carpcap.hvp.utils.CValidNullUtil;
import com.google.auto.service.AutoService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * URL格式验证器
 *
 * @author CarpCap
 */
@AutoService(ConstraintValidator.class)
public class CUrlValidator implements ConstraintValidator<CUrl, CharSequence> {

    private Set<String> protocols;
    private boolean allowLocalhost;
    private boolean allowIp;
    private final Pattern IP_PATTERN = Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

    @Override
    public void initialize(CUrl constraintAnnotation) {
        protocols = new HashSet<>(Arrays.asList(constraintAnnotation.protocols()));
        allowLocalhost = constraintAnnotation.allowLocalhost();
        allowIp = constraintAnnotation.allowIp();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        int vn = CValidNullUtil.validNull(value, context);
        if (0 != vn) {
            return vn == 1;
        }




        String urlStr = value.toString().trim();

        try {
            URL url = new URL(urlStr);

            // 验证协议
            if (!protocols.contains(url.getProtocol())) {
                return false;
            }

            String host = url.getHost();

            // 检查是否是localhost
            if (!allowLocalhost && ("localhost".equals(host) || "127.0.0.1".equals(host))) {
                return false;
            }

            // 检查是否是IP地址
            if (!allowIp && IP_PATTERN.matcher(host).matches()) {
                return false;
            }

            return true;
        } catch (MalformedURLException e) {
            // 如果不是标准URL格式，尝试使用正则表达式进行更宽松的验证
            return validateUrlPattern(urlStr);
        }
    }

    /**
     * 使用正则表达式验证URL格式
     */
    private boolean validateUrlPattern(String urlStr) {
        // 检查是否以允许的协议开头
        boolean hasValidProtocol = false;
        for (String protocol : protocols) {
            if (urlStr.startsWith(protocol + ":")) {
                hasValidProtocol = true;
                break;
            }
        }
        
        if (!hasValidProtocol) {
            return false;
        }

        // 基础URL格式正则表达式
        String urlPattern = "^[a-zA-Z]+://([\\w\\-]+(\\.[\\w\\-]+)+(/[\\w\\-\\._~:/?#[\\]@!\\$&'\\(\\)\\*\\+,;=.]+)*)?$";
        return Pattern.matches(urlPattern, urlStr);
    }
}