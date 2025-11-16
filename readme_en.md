

# Hibernate Validator Plus 

🌍 选择语言/Select Language:

- [中文](readme.md)
- [English](readme_en.md)

---

Hibernate Validator Plus is an enhanced validation toolkit based on **Hibernate Validator**, providing rich validation annotations, flexible group validations, and unified validation utilities.



📦 **Features:**
- Built-in commonly used validation annotations (account, password, email, ID card, IPv4, etc.)
- Multiple built-in validation group schemes
- Lightweight architecture with minimal dependencies
- Supports both Spring MVC automatic validation and manual validation via utility classes


---

## 📘 Annotation Overview

Location: [`annotation`](src/main/java/com/carpcap/hvp/annotation)

| Annotation        | Purpose             | Description                                                    |
|------------------|----------------------|----------------------------------------------------------------|
| `@CAccount`      | Account validation   | Starts with a letter, 5–16 chars, alphanumeric + `_`          |
| `@CPassword`     | Password validation  | Starts with a letter, 6–18 chars, letters/digits/underscore   |
| `@CIdCard`       | ID card validation   | Supports common CN ID formats                                  |
| `@CPhone`        | Phone validation     | Mainland China mobile numbers                                  |
| `@CEmail`        | Email validation     | RFC-compliant email rule                                       |
| `@CFile`         | File validation      | Default max size 1 MB, file suffix supported                   |
| `@CPlateNumber`  | Plate number check   | Supports both new & old CN vehicle plates                      |
| `@CIPv4`         | IPv4 validation      | Standard IPv4 address format                                   |
| `@CDateRange`    | Date range check     | `min` start date, `max` end date                               |

---

## 📂 Validation Groups

Location: [`groups`](src/main/java/com/carpcap/hvp/groups)

| Group Name      | Purpose                   |
|-----------------|---------------------------|
| `@CCreate`      | Create operation          |
| `@CCreateDef`   | Create + default checks   |
| `@CQuery`       | Query operation           |
| `@CQueryDef`    | Query + default checks    |
| ...             | More extensions supported |


---

## 🛠 Usage Example

Demo project:  
🔗 https://github.com/carpcap/hibernate-validator-plus-demo

### 1. Maven Dependency

```xml
<dependency>
    <groupId>com.carpcap</groupId>
    <artifactId>hibernate-validator-plus</artifactId>
    <version>1.1.0</version>
</dependency>
```


### Validate according to different groups (5 built-in groups)

Declare validation groups:

![Group Declaration](docs/img0.png)

Automatic validation through Spring MVC:

![Spring MVC Validation](docs/img1.png)

Manual validation using the provided utility class. Validation failures will throw `ValidationException`:

![Manual Validation](docs/img2.png)




## 📜 License

This project is released under the **Apache License 2.0**.

You may, under the terms of the license:

✔ Use it commercially for free

✔ Modify the source code

✔ Distribute source or binary packages

✔ Use it for secondary development

✔ Use it privately

❗ You must **retain the original author (CarpCap) copyright notice**

You can find the full license text in the project's root directory (`LICENSE`), or visit:

[https://www.apache.org/licenses/LICENSE-2.0](https://www.apache.org/licenses/LICENSE-2.0)
