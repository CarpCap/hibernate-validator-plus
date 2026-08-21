
# 📌 Changelog (English Version)


## Version 1.3.x Series


---

### 1.3.1

No longer dependency on hutool.


---

### 1.3.0

> **🎉 New Features**

| Annotation | Description |
|------------|-------------|
| `@CEmail` | Added email format validation with `allowNull`, domain blacklists/whitelists, and a maximum domain-level limit |

`@CEmail` domain lists are case-insensitive and match both the configured domain and all its subdomains. It includes messages in all 17 i18n bundles and reports distinct errors for invalid format, blacklist, whitelist, and domain-level failures.

> **🔄 Enhancements**

| Feature | Update Notes |
|---------|--------------|
| `@CIdCard` | Added CN/US/JP/KR/UK regional rules and custom `regexp` support; the CN rule now accepts only the 18-digit format, including a final `X/x` |
| Regional validation | `region` values for `@CPhone`, `@CPassport`, `@CPostCode`, and `@CIdCard` are now case-insensitive and trimmed; regional formats are aligned with the test matrix |
| Configuration validation | Without a custom regular expression, an unsupported `region` now throws `ConstraintDeclarationException` instead of being silently accepted |

> **🐞 Bug Fixes**

| Feature | Fix |
|---------|-----|
| `@CIpv6` | Replaced hostname/DNS resolution with pure IPv6 literal parsing, removing network dependency and hostname false positives |
| `@CDateRange` | Fixed precise `max` times being extended to the end of the day; only date-only upper bounds are now expanded |
| `@CFile` | Fixed missing files being treated as empty values; nonexistent paths and directories are now rejected |
| `@CMoney` | Fixed blank strings not following the configured `allowNull` behavior |
| `@CIdCard` | Fixed Chinese 18-digit ID numbers ending in `X/x` being rejected |
| Regional formats | Fixed and refined CN/US/JP/KR/UK rules for phone numbers, passports, postcodes, and ID numbers |

> **🛠 Build and Tests**

* 🧪 Added JUnit 4 automation so `mvn test` runs the basic, advanced, regional matrix, IPv6, and email validation suites
* ✅ The basic and advanced suites cover 446 internal checks, with another 120 regional-format scenarios in the matrix
* 📌 Pinned `maven-compiler-plugin` to version 3.13.0 for reproducible builds

> **⚠️ Compatibility Note**

* `@CIdCard(region = "CN")` no longer accepts legacy 15-digit Chinese ID numbers and validates only the 18-digit format

---

## Version 1.2.x Series

---
### 1.2.3

> **🔄 Update**

| Features | Update Notes |
|----------|-------------|
| `CValid` | Added getter and setter methods for the default `validator` and fail-fast `fastValidator`, allowing custom `Validator` instances to be injected in Spring Boot environments |

---
### 1.2.2


> **🎉 New Feature**

| Annotations | Description |
|-------------|-------------|
| `@CPassport` | Passport number validation, supports CN/US/JP/UK/KR formats |
| `@CPostCode` | Postal code validation, supports CN/US/JP/UK/KR formats |

> **🔄 Update**

| Features | Update Notes |
|----------|-------------|
| `@CPhone` | Added `region` parameter for multi-country phone validation (CN/US/JP/KR/UK) |
| `@CDateRange` | Added `Instant` and `ZonedDateTime` type support |

> **🛠 Improvements**

* 💾 i18n message files converted to Unicode encoding, fixing garbled characters in non-UTF-8 environments
* ✏️ Improved code comments and test cases

---

### 1.2.1

> **🔄Update content**

| Features | Update Notes |
|-------------|-------------------------------------|
| `@CDateRange` | Provides LocalDateTime type support and fixes time zone issues. <br/> max date automatically supplements the last time. For example: if you enter 2022-06-30, the judgment will be 2022-06-30 23:59:59.|
| Field allowNull | All annotations are added with the null field allowNull, which defaults to true and is allowed to be empty |

---


### 1.2.0


> **🆕 New Feature**

| Annotations            | Description            |
|------------------|------------------------|
| `@CBankCard`     | BankCard Validation    |
| `@CUrl`          | URL      Validation    |
| `@CMoney`        | Money     Validation   |
| `@CMacAddress`   | Mac Address Validation |
| `@CIpv6`         | IPv6 Validation        |

> **🔄 Update**

| Annotations              | Description                                        |
|--------------------------|-------------------------------------------------|
| `@Account` / `@Password` | Added `min` and `max` parameters, which can be used to control the minimum-maximum length |
| `@Domain`                | Domain name rules are enhanced to support Chinese domain names and `-` identifiers         |



## Version 1.1.x Series

---

### 1.1.4

> **New Features**

* ✨ Added fast validation method in `CValid`
* 🔒 Provides more convenient and safer validation results

---

### 1.1.3

> **Internationalization**

* 🌐 Added i18n multi-language support

---

### 1.1.2

> **Dependency Upgrades**

* ⬆️ `hibernate-validator`: 6.2.0.Final → **6.2.5.Final**
* ⬆️ `hutool-core`: 5.8.40 → **5.8.41**

---

### 1.1.1

> **Bug Fixes**

* 🐞 Fixed group inheritance issue

---

### 1.1.0 (Not Recommended)

> ❌ **Contains group inheritance bug — not recommended**

* 🗂 Updated project structure
* 🏷 Renamed some annotations

---

## Version 1.0.x Series

---

### 1.0.1

> **Enhancements**

* 🚗 Added license plate validation
* 📄 Added file validation

---

### 1.0.0

> **Initial Release**

* 🏷 Provided first 7 custom validation annotations
* 🔗 Included 5 default validation groups
* 🧰 Added manual validation utility class
* 🧪 Included test examples

---

*If you need a version without emojis, a more compact variant, or a different visual style (badges, color blocks, or plain text), tell me which and I will generate it.*
