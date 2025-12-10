

# Hibernate Validator Plus 

![Java Version](https://img.shields.io/badge/Java-%3E%3D8-orange?logo=openjdk)
![Hibernate Validator Version](https://img.shields.io/badge/validator-6.2.5.Final-green?logo=hibernate)
![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)

🌍 选择语言/Select Language:

- [中文](readme.md)
- [English](readme_en.md)





Hibernate Validator Plus is an enhanced validation framework based on **Hibernate Validator**, providing rich validation annotations, flexible group validations, and unified validation utilities.



📦 **Features:**
- Built-in commonly used validation annotations (account, password, email, ID card, IPv4, etc.)
- Multiple built-in validation group schemes
- International i18n, supports Chinese, English, Japanese, Chinese, Russian, French, Spanish, etc..
- Built-in allowable null judgment
- It remains fully compatible with the Hibernate Validator native framework and can directly use all its built-in validation functions.
- There are fewer dependencies, the architecture is more lightweight, and there is no forced dependence on other frameworks (such as spring).
- Supports the direct introduction of Spring MVC and Spring Boot projects, can automatically verify, and provides manual calling of tool classes.
- Support jdk8 or jdk8 or above version



## 📌 Versions
[versions_en.md](docs/versions_en.md)


## 📘 Annotation Overview

Location: [`annotation`](src/main/java/com/carpcap/hvp/annotation)

| Annotation      | Purpose             | Description                                                                                                                |
|-----------------|---------------------|----------------------------------------------------------------------------------------------------------------------------|
| `@CAccount`     | Account validation  | Starts with a letter, 5–16 chars, alphanumeric + `_`                                                                       |
| `@CPassword`    | Password validation | 6–18 chars, with at least 1 letter and at least 1 number.                                                                  |
| `@CIdCard`      | ID card validation  | Supports common CN ID formats                                                                                              |
| `@CPhone`       | Phone validation    | Mainland China mobile numbers                                                                                              |
| `@CEmail`       | Email validation    | RFC-compliant email rule                                                                                                   |
| `@CFile`        | File validation     | Default max size 1 MB, file suffix supported                                                                               |
| `@CPlateNumber` | Plate number check  | Supports both new & old CN vehicle plates                                                                                  |
| `@CIpv4`        | Ipv4 validation     | Standard IPv4 address format                                                                                               |
| `@CIpv6`         | IPv6 Validation        | Standard IPv6 address format                                                                                               |
| `@CDateRange`   | Date range check    | `min` start date, `max` end date ，recommend format：yyyy-MM-dd HH:mm:ss                                                           |
| `@CBankCard`     | BankCard Validation    | Bank card number verification uses the Luhn algorithm by default, and you can specify the interception card number prefix. |
| `@CUrl`          | URL      Validation    | URL format verification                                                                                                    |
| `@CMoney`        | Money     Validation   | Amount format verification<br/> Supports verification of amount formats of numbers, strings or BigDecimal types            |
| `@CMacAddress`   | Mac Address Validation | Mac Address Validation                                                                                                     |


## 📂 Validation Groups

Location: [`groups`](src/main/java/com/carpcap/hvp/groups)

| Group Name      | Purpose                   |
|-----------------|---------------------------|
| `@CCreate`      | Create operation          |
| `@CCreateDef`   | Create + default checks   |
| `@CQuery`       | Query operation           |
| `@CQueryDef`    | Query + default checks    |
| ...             | More extensions supported |

## 🔧 CValid Utility Class

`CValid` provides multiple validation capabilities, including standard validation(Full Check), fast-fail validation, property-level validation, and group-based validation.

Exception Class：ValidationException

| Method Type                                                                   | Validation Mode | Failure Behavior | Return Type    |
|-------------------------------------------------------------------------------|-----------------|------------------|----------------|
| `validate(Object object) `         <br/> `validate(Object object, Class<?>... groups) `                                             | Fast-Fail ⚡     | Throws Exception ❗ | void           |
| `tryValidate(Object object)`                 <br/>`tryValidate(Object object, Class<?>... groups)`                                     | Full Check      | No Exception     | List<String>   |
| `tryFastValidate(Object object) `    <br/>`tryFastValidate(Object object, Class<?>... groups) `                                              | Fast-Fail ⚡     | No Exception     | String         |
| `validateProperty(Object object, String propertyName, Class<?>... groups)`    | Fast-Fail ⚡     | Throws Exception ❗ | void           |
| `tryValidateProperty(Object object, String propertyName, Class<?>... groups)` | Full Check      | No Exception     | List<String>   |
| `tryFastValidateProperty(Object object, String propertyName, Class<?>... groups)`                                                   | Fast-Fail ⚡     | No Exception     | String         |



## 🛠 Usage Example

Demo Spring Boot project:  
🔗 https://github.com/carpcap/hibernate-validator-plus-demo

### 1. Maven Dependency

```xml
<dependency>
    <groupId>com.carpcap</groupId>
    <artifactId>hibernate-validator-plus</artifactId>
    <version>1.2.1</version>
</dependency>
```


### Validate according to different groups

Declare validation groups:

![Group Declaration](docs/img0.png)

Automatic validation through Spring MVC:

![Spring MVC Validation](docs/img1.png)

Manual validation using the provided utility class. Validation failures will throw `ValidationException`:

![Manual Validation](docs/img2.png)




## 📜 License

This project is released under the **Apache License 2.0**.

You can find the full license text in the project's root directory (`LICENSE`), or visit:

[https://www.apache.org/licenses/LICENSE-2.0](https://www.apache.org/licenses/LICENSE-2.0)
