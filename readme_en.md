# Hibernate Validator Plus Enhanced Version 🔧

Select Language:

- [English](readme_en.md)
- [中文](readme.md)

---

An extension toolkit based on **Hibernate Validator**, providing additional commonly used annotations, group validation, utility classes, and more. It helps developers validate Java objects or request parameters more efficiently.

The dependency modules are lightweight, and the overall architecture is clean and efficient.
<img src="docs/img.png" width="500" style="border: 2px solid #ddd; border-radius: 8px;">

---

## 🛠 Usage Examples

[https://github.com/carpcap/hibernate-validator-plus-demo](https://github.com/carpcap/hibernate-validator-plus-demo)

### Validate according to different groups (5 built-in groups)

Declare validation groups:

![Group Declaration](docs/img0.png)

Automatic validation through Spring MVC:

![Spring MVC Validation](docs/img1.png)

Manual validation using the provided utility class. Validation failures will throw `ValidationException`:

![Manual Validation](docs/img2.png)

## 📦 Quick Integration

Add the following dependency in your Maven project:

```xml
<dependency>
    <groupId>com.carpcap</groupId>
    <artifactId>hibernate-validator-plus</artifactId>
    <version>1.0.1</version>
</dependency>
```

## 📌 Version History

### 1.0.1

* Added license plate validation and file type validation

### 1.0.0

* Added 7 custom annotations for validating date, ID card, phone number, password, etc.
* Added 5 default groups for different validation scenarios
* Added a utility package for manual validation
* Added `AnnotationTest` for quickly understanding project features

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
