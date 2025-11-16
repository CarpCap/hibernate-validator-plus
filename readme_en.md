# Hibernate Validator Plus Enhanced Version 🔧

选择语言/Select Language:

- [中文](readme.md)
- [English](readme_en.md)

---

An extension toolkit based on **Hibernate Validator**, providing additional commonly used annotations, group validation, utility classes, and more. It helps developers validate Java objects or request parameters more efficiently.

The dependency modules are lightweight, and the overall architecture is clean and efficient.

<img src="docs/img.png" width="500" style="border: 2px solid #ddd; border-radius: 8px;">


    * Added 9 custom annotations for validating date, ID card, phone number, password, plater number，file type.
    * Added 10 default groups for different validation scenarios
    * Added a utility package for manual validation
    * Added `AnnotationTest` for quickly understanding project features


---

## 🛠 Usage Examples

[https://github.com/carpcap/hibernate-validator-plus-demo](https://github.com/carpcap/hibernate-validator-plus-demo)

Add the following dependency in your Maven project:

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
