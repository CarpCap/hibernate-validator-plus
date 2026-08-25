# Hibernate Validator Plus Usage Documentation

[Project Home](../readme_en.md) · [中文](usage.md) · [Changelog](versions_v2_en.md)

> This guide uses `jakarta.validation.*` as an example. If you are using the 1.x version, use `javax.validation.*` instead.

## Contents

- [1. Add Dependencies](#1-add-dependencies)
- [2. Group Validation](#2-group-validation)
- [3. Manual Validation with CValid](#3-manual-validation-with-cvalid)
- [4. Spring Validator Configuration (Recommended/Optional)](#4-spring-validator-configuration-recommendedoptional)
- [5. Spring MVC Automatic Validation](#5-spring-mvc-automatic-validation)

## 1. Add Dependencies

### JDK 11+

```xml
<dependency>
    <groupId>com.carpcap</groupId>
    <artifactId>hibernate-validator-plus</artifactId>
    <version>2.4.0</version>
</dependency>
```

2.x uses `jakarta.validation.*` and is intended for projects such as Spring Boot 3 and Jakarta EE 10.

### JDK 8+

```xml
<dependency>
    <groupId>com.carpcap</groupId>
    <artifactId>hibernate-validator-plus</artifactId>
    <version>1.4.0</version>
</dependency>
```

1.x uses `javax.validation.*` and is intended for Spring Boot 2 and JDK 8+ projects.

---

## 2. Group Validation

### 2.1 Group Overview

The framework provides eight built-in business groups in `com.carpcap.hvp.groups`. Each group has a base interface and a corresponding `*Def` interface for distinguishing business scenarios:

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

### 2.2 Using Groups

Use the `groups` attribute to specify which group a constraint belongs to. Constraints without an explicit group (the default `Default` group) are validated in every scenario:

```java
public class User {
    // Default group: validated in every scenario
    @CAccount
    private String account;

    // Applies only to the CPost group
    @NotBlank(groups = CPost.class)
    private String name;

    // Applies only to the CGet group; null is not allowed
    @CPhone(region = "CN", groups = CGet.class, allowNull = false)
    private String phone;
}
```

### 2.3 Group Def

Each `*Def` interface extends its corresponding business group and `jakarta.validation.groups.Default`, for example:

```java
public interface CPostDef extends CPost, Default {
}
```

When validating with `CPostDef`, **both the constraints in the CPost group and the constraints without an explicit group (Default) are validated**.

---

## 3. Manual Validation with CValid

`com.carpcap.hvp.utils.CValid` provides manual validation methods and internally holds two Validator instances:

- `validator`: full validation, collecting all violation messages
- `fastValidator`: fail-fast validation, returning immediately after the first error

### 3.1 CValid Method Overview

Methods beginning with `try` do not throw validation-failure exceptions; they provide the result through their return value. `tryValidate*` methods return a list of error messages, while `tryFastValidate*` methods return the first error message. On success, they return an empty list or `null`, respectively.

| Method | Validation Mode | On Failure | Return |
|------|----------|----------|------|
| `validate(obj, ...groups)` | Fail-fast | Throws `ValidationException` | `void` |
| `tryValidate(obj, ...groups)` | Full validation | No exception thrown | `List<String>` |
| `tryFastValidate(obj, ...groups)` | Fail-fast | No exception thrown | `String` |
| `validateProperty(obj, property, ...groups)` | Fail-fast | Throws `ValidationException` | `void` |
| `tryValidateProperty(obj, property, ...groups)` | Full validation | No exception thrown | `List<String>` |
| `tryFastValidateProperty(obj, property, ...groups)` | Fail-fast | No exception thrown | `String` |

### 3.2 CValid Usage Example

```java
import com.carpcap.hvp.utils.CValid;
import com.carpcap.hvp.groups.CPost;
import com.carpcap.hvp.groups.CPostDef;

public class Demo {
    public static void main(String[] args) {
        User user = new User();
        user.setName("Zhang San");

        // 1. Validate the default group: throws ValidationException on failure
        CValid.validate(user);
        // 2. Validate a specified group: throws ValidationException on failure
        CValid.validate(user, CPost.class);
        // 3. Full validation: returns all error messages without throwing
        List<String> errors = CValid.tryValidate(user, CPostDef.class);
        if (!errors.isEmpty()) System.out.println(errors);
        // 4. Fail-fast validation: returns only the first error message
        String error = CValid.tryFastValidate(user, CPost.class);
        // 5. Validate a single property
        CValid.validateProperty(user, "name", CPost.class);
        // 6. Validate a property and return all error messages
        List<String> propertyErrors = CValid.tryValidateProperty(user, "phone", CPost.class);
    }
}
```

---

## 4. Spring Validator Configuration (Recommended/Optional)

`@Primary` marks the default bean and changes Spring's default validator to fail-fast mode. Setting CValid's `fastValidator` and `validator` to Spring validators also enables Spring MVC i18n messages when validating through CValid.

```java
import com.carpcap.hvp.utils.CValid;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidatorConfig {
    /** Registers a fail-fast validator as the default bean and for CValid. */
    @Bean
    @Primary
    public LocalValidatorFactoryBean defaultValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.getValidationPropertyMap().put("hibernate.validator.fail_fast", "true");
        CValid.setFastValidator(validator);
        return validator;
    }

    /** Replaces the normal validator used by CValid. */
    @Bean
    public LocalValidatorFactoryBean normalValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        CValid.setValidator(validator);
        return validator;
    }
}
```

---

## 5. Spring MVC Automatic Validation

### 5.1 Controller Example

Use `@Validated(Group.class)` together with `@RequestBody` to automatically validate a request body:

```java
import com.carpcap.hvp.groups.CGetDef;
import com.carpcap.hvp.groups.CPostDef;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    // Create a user: validate only constraints in the CPostDef group
    @PostMapping("/create")
    public String create(@Validated(CPostDef.class) @RequestBody User user) { return "ok"; }

    // Query a user: validate only constraints in the CGetDef group
    @GetMapping("/get")
    public String get(@Validated(CGetDef.class) @RequestBody User user, HttpServletRequest request) { return "ok"; }
}
```

### 5.2 Global Exception Handling

Automatic validation failures throw `MethodArgumentNotValidException`. Configure a global exception handler to return error messages consistently. This example returns a `String`; adapt it to your response format as needed:

```java
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder message = new StringBuilder();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            message.append(fieldError.getField()).append("--")
                    .append(fieldError.getDefaultMessage()).append("\n");
        }
        return message.toString();
    }

    @ExceptionHandler(ValidationException.class)
    public String handleValidationException(ValidationException ex) { return ex.getMessage(); }
}
```
