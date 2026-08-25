# Hibernate Validator Plus 使用文档

[返回项目首页](../readme.md) · [English](usage_en.md) · [版本日志](versions_v2.md)

> 本文档以 `jakarta.validation.*`为案例，如果使用的是1.x版本请使用`javax.validation.*`。

## 文档导航

- [1. 引入依赖](#1-引入依赖)
- [2. 分组校验教程](#2-分组校验教程)
- [3. 手动校验教程](#3-手动校验教程)
- [4. Spring 校验器配置](#4-spring-校验器配置)
- [5. Spring MVC 自动校验](#5-spring-mvc-自动校验)
- [6. 注解使用说明](#6-注解使用说明)

## 1. 引入依赖

### JDK 11+

```xml
<dependency>
    <groupId>com.carpcap</groupId>
    <artifactId>hibernate-validator-plus</artifactId>
    <version>2.4.0</version>
</dependency>
```

2.x 使用 `jakarta.validation.*`，适用于 Spring Boot 3、Jakarta EE 10 等项目。

### JDK 8+

```xml
<dependency>
    <groupId>com.carpcap</groupId>
    <artifactId>hibernate-validator-plus</artifactId>
    <version>1.4.0</version>
</dependency>
```

1.x 使用 `javax.validation.*`，适用于 Spring Boot 2,jdk8+ 项目。

---

## 2. Group 分组

### 2.1 Group 说明

框架在 `com.carpcap.hvp.groups` 包中预置了 8 组业务分组，每组包含基础接口和 `*Def` 接口，主要用于业务区分：

| 基础分组 | Def 分组 | 场景 |
|----------|----------|------|
| `CCreate` | `CCreateDef` | 创建 |
| `CQuery` | `CQueryDef` | 查询 |
| `CUpdate` | `CUpdateDef` | 更新 |
| `CDelete` | `CDeleteDef` | 删除 |
| `CGet` | `CGetDef` | GET 请求 |
| `CPost` | `CPostDef` | POST 请求 |
| `CPut` | `CPutDef` | PUT 请求 |
| `CPatch` | `CPatchDef` | PATCH 请求 |

### 2.2 Group 使用

通过 `groups` 属性指定约束生效的分组，未指定分组（默认 `Default`）的约束在所有场景都会生效：

```java
public class User {

    // 默认分组：所有场景都校验
    @CAccount
    private String account;

    // 仅 CPost 分组生效
    @NotBlank(groups = CPost.class)
    private String name;

    // 仅 CGet 分组生效，且不允许为 null
    @CPhone(region = "CN", groups = CGet.class, allowNull = false)
    private String phone;
}
```

### 2.3 Group Def

`*Def` 接口继承了对应的业务分组和 `jakarta.validation.groups.Default`，例如：

```java
public interface CPostDef extends CPost, Default {
}
```

使用 `CPostDef` 校验时，**既校验 CPost 分组的约束，也校验未指定分组的约束（Default）**。

---

## 3. CValid 手动校验

`com.carpcap.hvp.utils.CValid` 提供手动校验方法，内部持有两个 Validator：

- `validator`：全量校验器，收集所有违规信息
- `fastValidator`：快速失败校验器（遇到第一个错误立即返回）

### 3.1 CValid 方法总览

以 `try` 开头的方法不会抛出校验失败异常，而是通过返回值提供校验结果；其中 `tryValidate*` 返回错误信息列表，`tryFastValidate*` 返回首条错误信息，校验通过时返回空列表或 `null`。

| 方法 | 校验模式 | 失败行为 | 返回 |
|------|----------|----------|------|
| `validate(obj, ...groups)` | 快速失败 | 抛出 `ValidationException` | `void` |
| `tryValidate(obj, ...groups)` | 全量校验 | 不抛异常 | `List<String>` |
| `tryFastValidate(obj, ...groups)` | 快速失败 | 不抛异常 | `String` |
| `validateProperty(obj, property, ...groups)` | 快速失败 | 抛出 `ValidationException` | `void` |
| `tryValidateProperty(obj, property, ...groups)` | 全量校验 | 不抛异常 | `List<String>` |
| `tryFastValidateProperty(obj, property, ...groups)` | 快速失败 | 不抛异常 | `String` |

### 3.2 CValid 使用示例

```java
import com.carpcap.hvp.utils.CValid;
import com.carpcap.hvp.groups.CPost;
import com.carpcap.hvp.groups.CPostDef;

public class Demo {

    public static void main(String[] args) {
        User user = new User();
        user.setName("张三");

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
```

---

## 4. Spring 配置校验器（推荐/可选）

`@Primary`指定默认使用的`Bean`，将`spring`默认的校验器改为了`fail_fast`模式

将`CValid`中的`FastValidator`与`Validator`设置为`Spring`的`Validator`，这样使用`CValid`校验时让`spring mvc i18n`生效.

```java
import com.carpcap.hvp.utils.CValid;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidatorConfig {

    /**
     * Registers a fail-fast validator as a default bean
     * and replaces the fail-fast validator used by CValid.
     *
     * @author CarpCap
     */
    @Bean
    @Primary
    public LocalValidatorFactoryBean defaultValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.getValidationPropertyMap().put("hibernate.validator.fail_fast", "true");
        CValid.setFastValidator(validator);
        return validator;
    }

    /**
     * Replaces the default validator used by CValid.
     *
     * @author CarpCap
     */
    @Bean
    public LocalValidatorFactoryBean normalValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        CValid.setValidator(validator);
        return validator;
    }
}
```


---

## 5. Spring MVC 自动校验


### 5.1 Controller 示例

使用 `@Validated(分组.class)` + `@RequestBody` 自动校验请求体：

```java
import com.carpcap.hvp.groups.CGetDef;
import com.carpcap.hvp.groups.CPostDef;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    // 创建用户：只校验 CPostDef 分组的约束
    @PostMapping("/create")
    public String create(@Validated(CPostDef.class) @RequestBody User user) {
        return "ok";
    }

    // 查询用户：只校验 CGetDef 分组的约束
    @GetMapping("/get")
    public String get(@Validated(CGetDef.class) @RequestBody User user, HttpServletRequest request) {
        return "ok";
    }
}
```

### 5.2 全局异常处理

自动校验失败会抛出 `MethodArgumentNotValidException`，建议配置全局异常处理器统一返回错误信息，这里返回String 可以根据需求返回统一格式：

```java
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder message = new StringBuilder();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            message.append(fieldError.getField())
                    .append("--")
                    .append(fieldError.getDefaultMessage())
                    .append("\n");
        }
        return message.toString();
    }



    @ExceptionHandler(ValidationException.class)
    public String handleValidationException(ValidationException ex) {
        return ex.getMessage();
    }
}
```


---

## 6. 注解使用说明

所有 HVP 注解均支持 `message`、`groups`、`payload` 和 `allowNull`，并支持重复标注。`allowNull = false` 表示 `null` 不通过；以下示例仅列出关键属性。

### 6.1 账号和字符串

```java
import com.carpcap.hvp.annotation.CAccount;
import com.carpcap.hvp.annotation.CPassword;
import com.carpcap.hvp.annotation.CStrAllow;
import com.carpcap.hvp.annotation.CStrDeny;

public class AccountForm {
    @CAccount(min = 6, max = 20, allowNull = false) private String account;
    @CPassword(min = 8, regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$") private String password;
    @CStrAllow({"DRAFT", "PUBLISHED"}) private String status;
    @CStrDeny({"root", "admin"}) private String nickname;
}
```

`CAccount` 和 `CPassword` 可通过 `regexp` 覆盖默认规则；`CStrAllow` 与 `CStrDeny` 的 `value` 是必填字符串集合。

### 6.2 身份和地区格式

```java
import com.carpcap.hvp.annotation.CIdCard;
import com.carpcap.hvp.annotation.CPassport;
import com.carpcap.hvp.annotation.CPhone;
import com.carpcap.hvp.annotation.CPostCode;

public class IdentityForm {
    @CIdCard(region = "CN", allowNull = false) private String idCard;
    @CPassport(region = "US") private String passport;
    @CPhone(region = "JP") private String phone;
    @CPostCode(region = "UK") private String postCode;
}
```

上述注解支持 `CN`、`US`、`JP`、`KR`、`UK`；`regexp`（`CPhone`、`CIdCard`、`CPassport`、`CPostCode` 支持）优先于 `region`。

### 6.3 网络、域名和地址

```java
import com.carpcap.hvp.annotation.CDomain;
import com.carpcap.hvp.annotation.CEmail;
import com.carpcap.hvp.annotation.CIpv4;
import com.carpcap.hvp.annotation.CIpv6;
import com.carpcap.hvp.annotation.CMacAddress;
import com.carpcap.hvp.annotation.CUrl;

public class NetworkForm {
    @CIpv4(allowNull = false) private String ipv4;
    @CIpv6 private String ipv6;
    @CDomain(level = 2) private String domain;
    @CEmail(listMode = CEmail.ListMode.BLACKLIST, domains = {"example.com"}) private String email;
    @CUrl(protocols = {"https"}, allowLocalhost = false, allowIp = false) private String callbackUrl;
    @CMacAddress(allowEui64 = true, allowLowercase = false) private String macAddress;
}
```

`CDomain` 和 `CEmail` 默认不允许只有一个标签的顶级域名；需要时设置 `allowTld = true`。`CEmail.domains` 会匹配指定域名及其子域名。

### 6.4 日期、金额和银行卡

```java
import com.carpcap.hvp.annotation.CBankCard;
import com.carpcap.hvp.annotation.CDateRange;
import com.carpcap.hvp.annotation.CMoney;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentForm {
    @CDateRange(min = "2026-01-01", max = "2026-12-31") private LocalDate payDate;
    @CMoney(min = 0.01, max = 999999.99, decimalPlaces = 2, allowCurrencySymbol = false)
    private BigDecimal amount;
    @CBankCard(allowedPrefixes = {"62"}, allowSpaces = true) private String bankCard;
}
```

`CDateRange` 支持 `String`、`Date`、`LocalDate`、`LocalDateTime`、`Instant` 和 `ZonedDateTime`；`format` 为空时自动解析。`CBankCard` 默认执行 Luhn 校验；`CMoney` 支持数字、字符串与 `BigDecimal`。

### 6.5 文件、车牌和 JSON

```java
import com.carpcap.hvp.annotation.CFile;
import com.carpcap.hvp.annotation.CJson;
import com.carpcap.hvp.annotation.CPlateNumber;
import java.io.File;

public class ResourceForm {
    @CFile(fileNameSuffix = {"png", "jpg"}, fileSize = 2 * 1024 * 1024L) private File image;
    @CPlateNumber private String plateNumber;
    @CJson(type = CJson.Type.OBJECT, allowNull = false) private String metadata;
}
```

`CFile` 对 `File` 校验后缀和大小，对 `String` 仅校验文件名后缀；`fileSize` 单位为字节。`CJson` 默认 `STRUCT`，只接受对象或数组；可使用 `OBJECT`、`ARRAY`、`VALUE` 或 `ANY` 限制根节点类型。
