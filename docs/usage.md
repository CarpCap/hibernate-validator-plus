# Hibernate Validator Plus 使用文档

> 基于 Hibernate Validator 6.2.5 的增强校验框架，支持分组校验、Spring MVC 自动校验与手动校验工具类。

## 1. 引入最新依赖，这里以1.2.3版本为例

```xml
<dependency>
    <groupId>com.carpcap</groupId>
    <artifactId>hibernate-validator-plus</artifactId>
    <version>1.2.3</version>
</dependency>
```


---

## 2. 分组校验（Groups）

### 2.1 内置分组

框架在 `com.carpcap.hvp.groups` 包中预置了 8 组业务分组，每组包含基础接口和 `*Def` 接口：

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

### 2.2 在实体属性上标注分组

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

### 2.3 使用 Def 分组

`*Def` 接口继承了对应的业务分组和 `javax.validation.groups.Default`，例如：

```java
public interface CPostDef extends CPost, Default {
}
```

使用 `CPostDef` 校验时，**既校验 CPost 分组的约束，也校验未指定分组的约束（Default）**。

---

## 3. CValid 工具类

`com.carpcap.hvp.utils.CValid` 提供手动校验方法，内部持有两个 Validator：

- `validator`：全量校验器，收集所有违规信息
- `fastValidator`：快速失败校验器（遇到第一个错误立即返回）

### 3.1 方法总览

| 方法 | 校验模式 | 失败行为 | 返回 |
|------|----------|----------|------|
| `validate(obj, ...groups)` | 快速失败 | 抛出 `ValidationException` | `void` |
| `tryValidate(obj, ...groups)` | 全量校验 | 不抛异常 | `List<String>` |
| `tryFastValidate(obj, ...groups)` | 快速失败 | 不抛异常 | `String` |
| `validateProperty(obj, property, ...groups)` | 快速失败 | 抛出 `ValidationException` | `void` |
| `tryValidateProperty(obj, property, ...groups)` | 全量校验 | 不抛异常 | `List<String>` |
| `tryFastValidateProperty(obj, property, ...groups)` | 快速失败 | 不抛异常 | `String` |

### 3.2 使用示例

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

## 4. Spring MVC 自动校验


### 4.1 Controller 示例

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

### 4.2 全局异常处理

自动校验失败会抛出 `MethodArgumentNotValidException`，建议配置全局异常处理器统一返回错误信息：

```java
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
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

### 4.3 自定义校验器配置

在 Spring Boot 中，可以通过配置类将 Spring 管理的 `Validator` 注入 `CValid`：

```java
import com.carpcap.hvp.utils.CValid;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidatorConfig {

    /**
     * 将 fail-fast 校验器设置为默认 Bean，
     * 并替换 CValid 的快速校验器。
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
     * 替换 CValid 的默认校验器。
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

## 5. 快速上手总结

1. 引入依赖 `hibernate-validator-plus`
2. 在实体字段上使用 `@CAccount`、`@CPhone` 等注解，并通过 `groups` 指定分组
3. 手动校验：调用 `CValid.validate / tryValidate / tryFastValidate`
4. 自动校验：Controller 方法参数上使用 `@Validated(XXXDef.class) @RequestBody`
