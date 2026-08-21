# Hibernate Validator Plus Usage Documentation

> An enhanced validation framework based on Hibernate Validator 6.2.5, supporting group validation, Spring MVC automatic validation, and a manual validation utility class.

## 1. Add the Latest Dependency 

```xml
<dependency>
    <groupId>com.carpcap</groupId>
    <artifactId>hibernate-validator-plus</artifactId>
    <version>1.3.1</version>
</dependency>
```

---

## 2. Group Validation (Groups)

### 2.1 Built-in Groups

The framework provides 8 built-in business groups in the `com.carpcap.hvp.groups` package. Each group has a base interface and a `*Def` interface:

| Base Group | Def Group | Scenario |
|----------|----------|------|
| `CCreate` | `CCreateDef` | Create |
| `CQuery` | `CQueryDef` | Query |
| `CUpdate` | `CUpdateDef` | Update |
| `CDelete` | `CDeleteDef` | Delete |
| `CGet` | `CGetDef` | GET request |
| `CPost` | `CPostDef` | POST request |
| `CPut` | `CPutDef` | PUT request |
| `CPatch` | `CPatchDef` | PATCH request |

### 2.2 Annotating Groups on Entity Properties

Use the `groups` attribute to specify which group a constraint belongs to. Constraints without a group (default `Default`) are validated in all scenarios:

```java
public class User {

    // Default group: validated in all scenarios
    @CAccount
    private String account;

    // Only applies to the CPost group
    @NotBlank(groups = CPost.class)
    private String name;

    // Only applies to the CGet group, and null is not allowed
    @CPhone(region = "CN", groups = CGet.class, allowNull = false)
    private String phone;
}
```

### 2.3 Using Def Groups

A `*Def` interface extends its corresponding business group and `javax.validation.groups.Default`, for example:

```java
public interface CPostDef extends CPost, Default {
}
```

When validating with `CPostDef`, **both the constraints of the CPost group and the constraints without an explicit group (Default) are validated**.

---

## 3. CValid Utility Class

`com.carpcap.hvp.utils.CValid` provides manual validation methods and holds two Validator instances internally:

- `validator`: full validation, collects all violations
- `fastValidator`: fail-fast validation (returns immediately on the first error)

### 3.1 Method Overview

| Method | Validation Mode | On Failure | Return |
|------|----------|----------|------|
| `validate(obj, ...groups)` | Fail-fast | Throws `ValidationException` | `void` |
| `tryValidate(obj, ...groups)` | Full validation | No exception thrown | `List<String>` |
| `tryFastValidate(obj, ...groups)` | Fail-fast | No exception thrown | `String` |
| `validateProperty(obj, property, ...groups)` | Fail-fast | Throws `ValidationException` | `void` |
| `tryValidateProperty(obj, property, ...groups)` | Full validation | No exception thrown | `List<String>` |
| `tryFastValidateProperty(obj, property, ...groups)` | Fail-fast | No exception thrown | `String` |

### 3.2 Usage Examples

```java
import com.carpcap.hvp.utils.CValid;
import com.carpcap.hvp.groups.CPost;
import com.carpcap.hvp.groups.CPostDef;

public class Demo {

    public static void main(String[] args) {
        User user = new User();
        user.setName("张三");

        // 1. Validate with the default group: throws ValidationException on failure
        CValid.validate(user);

        // 2. Validate with a specific group: throws ValidationException on failure
        CValid.validate(user, CPost.class);

        // 3. Full validation: returns all error messages without throwing
        List<String> errors = CValid.tryValidate(user, CPostDef.class);
        if (!errors.isEmpty()) {
            System.out.println(errors);
        }

        // 4. Fail-fast validation: returns only the first error message
        String error = CValid.tryFastValidate(user, CPost.class);

        // 5. Validate a single property
        CValid.validateProperty(user, "name", CPost.class);

        // 6. Validate a single property: returns a list of error messages
        List<String> propertyErrors = CValid.tryValidateProperty(user, "phone", CPost.class);
    }
}
```

---

## 4. Spring MVC Automatic Validation

### 4.1 Controller Example

Use `@Validated(Group.class)` + `@RequestBody` to automatically validate the request body:

```java
import com.carpcap.hvp.groups.CGetDef;
import com.carpcap.hvp.groups.CPostDef;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    // Create user: only validates constraints of the CPostDef group
    @PostMapping("/create")
    public String create(@Validated(CPostDef.class) @RequestBody User user) {
        return "ok";
    }

    // Get user: only validates constraints of the CGetDef group
    @GetMapping("/get")
    public String get(@Validated(CGetDef.class) @RequestBody User user, HttpServletRequest request) {
        return "ok";
    }
}
```

### 4.2 Global Exception Handling

Automatic validation failures throw `MethodArgumentNotValidException`. It is recommended to configure a global exception handler to return error messages uniformly:

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

### 4.3 Custom Validator Configuration

In Spring Boot, you can inject Spring-managed `Validator` instances into `CValid` through a configuration class:

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

## 5. Quick Start Summary

1. Add the dependency `hibernate-validator-plus`
2. Add Custom Validator Configuration `ValidatorConfig`
3. Annotate entity fields with `@CAccount`, `@CPhone`, etc., and specify groups via `groups`
4. Manual validation: call `CValid.validate / tryValidate / tryFastValidate`
5. Automatic validation: use `@Validated(XXXDef.class) @RequestBody` on Controller method parameters
