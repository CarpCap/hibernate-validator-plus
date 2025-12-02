
# 📌 Changelog (English Version)


## ☕ Version 1.2.x Series

---

### 🥩1.2.1

> **🔄Update content**

| Features | Update Notes |
|-------------|-------------------------------------|
| `@CDateRange` | Provides LocalDateTime type support and fixes time zone issues. <br/> max date automatically supplements the last time. For example: if you enter 2022-06-30, the judgment will be 2022-06-30 23:59:59.|
| Field allowNull | All annotations are added with the null field allowNull, which defaults to true and is allowed to be empty |

---


### 🍕1.2.0


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



## 🎯 Version 1.1.x Series

---

### 🚀 1.1.4

> **New Features**

* ✨ Added fast validation method in `CValid`
* 🔒 Provides more convenient and safer validation results

---

### 🌍 1.1.3

> **Internationalization**

* 🌐 Added i18n multi-language support

---

### 📦 1.1.2

> **Dependency Upgrades**

* ⬆️ `hibernate-validator`: 6.2.0.Final → **6.2.5.Final**
* ⬆️ `hutool-core`: 5.8.40 → **5.8.41**

---

### 🛠 1.1.1

> **Bug Fixes**

* 🐞 Fixed group inheritance issue

---

### ⚠️ 1.1.0 (Not Recommended)

> ❌ **Contains group inheritance bug — not recommended**

* 🗂 Updated project structure
* 🏷 Renamed some annotations

---

## 📘 Version 1.0.x Series

---

### 🧩 1.0.1

> **Enhancements**

* 🚗 Added license plate validation
* 📄 Added file validation

---

### 🏁 1.0.0

> **Initial Release**

* 🏷 Provided first 7 custom validation annotations
* 🔗 Included 5 default validation groups
* 🧰 Added manual validation utility class
* 🧪 Included test examples

---

*If you need a version without emojis, a more compact variant, or a different visual style (badges, color blocks, or plain text), tell me which and I will generate it.*
