

# Hibernate Validator Plus 

![Java Version](https://img.shields.io/badge/Java-8%20%7C%2011%2B-orange?logo=openjdk)
![Hibernate Validator Version](https://img.shields.io/badge/validator-6.2.x%20%7C%208.x-green?logo=hibernate)
![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)

**Language:** [中文](readme.md) · [English](readme_en.md)

Hibernate Validator Plus extends **Hibernate Validator** with practical annotations, validation groups, and unified utilities for Java objects and request parameters.

---

## Core Capabilities

- Built-in commonly used validation annotations (account, password, ID card, IPv4, etc.)
- Multiple built-in validation group schemes
- Internationalized messages for Chinese, English, Japanese, Russian, French, Spanish, and more
- A consistent null-value policy across all extension annotations
- Full compatibility with Hibernate Validator's built-in constraints
- A lightweight dependency footprint with no mandatory Spring dependency
- Direct integration with Spring MVC and Spring Boot, plus utilities for manual validation
- JDK 8 and JDK 11+ are maintained as separate version lines; see the version policy below

---

## Version Policy

| Version line | JDK     | Validation API | Hibernate Validator | Recommended for |
|--------------|---------|----------------|---------------------|-----------------|
| [2.x](https://github.com/CarpCap/hibernate-validator-plus/tree/2.x) | JDK 11+ | `jakarta.validation` | 8.x | Spring Boot 3, Jakarta EE 10, or projects using `jakarta.validation` |
| [1.x](https://github.com/CarpCap/hibernate-validator-plus/tree/1.x) | JDK 8+  | `javax.validation` | 6.2.x | Spring Boot 2 or projects using `javax.validation` |

---

## Documentation

- [2.x Usage Guide (JDK 11+ / jakarta.validation)](docs/usage_en.md)
- [1.x Usage Guide (JDK 8 / javax.validation)](https://github.com/CarpCap/hibernate-validator-plus/blob/1.x/docs/usage_en.md)
- [2.x Change Log](docs/versions_v2_en.md)
- [1.x Change Log](docs/versions_v1_en.md)

---

## Annotation Overview

Location: [`annotation`](src/main/java/com/carpcap/hvp/annotation)

| Annotation      | Purpose             | Description                                                                                                                |
|-----------------|---------------------|----------------------------------------------------------------------------------------------------------------------------|
| `@CAccount`     | Account validation  | Starts with a letter, 5–16 chars, alphanumeric + `_`                                                                       |
| `@CPassword`    | Password validation | 6–18 chars, with at least 1 letter and at least 1 number.                                                                  |
| `@CIdCard`      | ID number validation | Supports CN/US/JP/KR/UK via region (CN by default); regexp overrides the regional rule                                    |
| `@CPhone`       | Phone validation    | Mainland China mobile numbers; supports region parameter switching CN/US/JP/UK/KR                                                                                           |
| `@CPassport`    | Passport validation | Default: CN passport format; supports region parameter switching CN/US/JP/UK/KR                                            |
| `@CPostCode`    | Postcode validation | Default: CN postcode format; supports region parameter switching CN/US/JP/UK/KR                                            |
| `@CEmail`       | Email validation    | Supports domain blacklists, whitelists, and maximum subdomain level                                                       |
| `@CPlateNumber` | Plate number check  | Supports both new & old CN vehicle plates                                                                                  |
| `@CFile`        | File validation     | Default max size 1 MB, file suffix supported                                                                               |
| `@CBankCard`     | BankCard Validation    | Bank card number verification uses the Luhn algorithm by default, and you can specify the interception card number prefix. |
| `@CMoney`        | Money     Validation   | Amount format verification<br/> Supports verification of amount formats of numbers, strings or BigDecimal types            |
| `@CDateRange`   | Date range check    | `min` start date, `max` end date ，recommend format：yyyy-MM-dd HH:mm:ss                                                     |
| `@CStrAllow`     | String whitelist      | String value must be in the configured allowed list                                                                      |
| `@CStrDeny`      | String blacklist      | String value must not be in the configured forbidden list                                                                |
| `@CJson`         | JSON validation       | Validates JSON strings; accepts objects or arrays by default and can restrict the root to an object, array, or primitive |
| `@CIpv4`        | Ipv4 validation     | Standard IPv4 address format                                                                                               |
| `@CIpv6`         | IPv6 Validation        | Standard IPv6 address format                                                                                               |
| `@CUrl`          | URL      Validation    | URL format verification                                                                                                    |
| `@CMacAddress`   | Mac Address Validation | Mac Address Validation                                                                                                     |


---

## Validation Groups

Location: [`groups`](src/main/java/com/carpcap/hvp/groups)

| Group Name      | Purpose                   |
|-----------------|---------------------------|
| `@CCreate`      | Create operation          |
| `@CCreateDef`   | Create + default checks   |
| `@CQuery`       | Query operation           |
| `@CQueryDef`    | Query + default checks    |
| ...             | More extensions supported |

---

## CValid Utility Class

`CValid` provides multiple validation capabilities, including standard validation(Full Check), fast-fail validation, property-level validation, and group-based validation.

In the Spring Boot environment, it is recommended to replace the built-in validator of CValid with fastValidator. There are examples in the tutorial documentation.

Exception Class：ValidationException

| Method Type                                                                   | Validation Mode | Failure Behavior | Return Type    |
|-------------------------------------------------------------------------------|-----------------|------------------|----------------|
| `validate(Object object) `         <br/> `validate(Object object, Class<?>... groups) `                                             | Fast-Fail ⚡     | Throws Exception ❗ | void           |
| `tryValidate(Object object)`                 <br/>`tryValidate(Object object, Class<?>... groups)`                                     | Full Check      | No Exception     | List<String>   |
| `tryFastValidate(Object object) `    <br/>`tryFastValidate(Object object, Class<?>... groups) `                                              | Fast-Fail ⚡     | No Exception     | String         |
| `validateProperty(Object object, String propertyName, Class<?>... groups)`    | Fast-Fail ⚡     | Throws Exception ❗ | void           |
| `tryValidateProperty(Object object, String propertyName, Class<?>... groups)` | Full Check      | No Exception     | List<String>   |
| `tryFastValidateProperty(Object object, String propertyName, Class<?>... groups)`                                                   | Fast-Fail ⚡     | No Exception     | String         |

---

## License

This project is released under the **Apache License 2.0**.

You can find the full license text in the project's root directory (`LICENSE`), or visit:

[https://www.apache.org/licenses/LICENSE-2.0](https://www.apache.org/licenses/LICENSE-2.0)
