import com.carpcap.hvp.User2;
import com.carpcap.hvp.annotation.CAccount;
import com.carpcap.hvp.groups.*;
import com.carpcap.hvp.utils.CValid;

import jakarta.validation.ValidationException;
import java.io.File;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * AnnotationTwoTest - Advanced dimension tests
 * CValid API variants, all groups, annotation attributes, @Repeatable, custom regex, edge cases
 *
 * @author CarpCap
 */
public class AnnotationTest3 {

    public static void main(String[] args) {
        User3 user = new User3();
        user.setName("张三");
        user.setEmail("qweqw@cc.qq.com");

        // 1. 默认分组校验：失败抛 ValidationException
        CValid.validate(user);

        // 2. 指定分组校验：失败抛 ValidationException
        CValid.validate(user, CPost.class);

        // 3. 全量校验：返回所有错误信息，不抛异常
        List<String> errors = CValid.tryValidate(user, CPostDef.class);
        if (!errors.isEmpty()) {
            System.out.println(errors);
        }

        // 4. 快速失败校验：只返回第一条错误信息
        String error = CValid.tryFastValidate(user, CPost.class);

        // 5. 校验单个属性
        CValid.validateProperty(user, "name", CPost.class);

        // 6. 校验单个属性：返回错误信息列表
        List<String> propertyErrors = CValid.tryValidateProperty(user, "phone", CPost.class);
    }
}
