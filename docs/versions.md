# 版本日志

[返回项目首页](../readme.md) · [使用教程](usage.md) · [English](versions_en.md)

## 2.0.x 系列

### 2.0.1

- 新增 `@CStrAllow` 和 `@CStrDeny`，分别用于字符串值白名单和黑名单校验。
- `@CDomain` 和 `@CEmail` 新增 `allowTld` 属性，默认不允许只有一个标签的顶级域名。
- `@CDateRange` 增加日期格式自动识别功能。

---

### 2.0.0

- 最低运行版本调整为 JDK 11，并兼容更高版本 JDK。
- 使用 `jakarta.validation` API。
- Hibernate Validator 基线升级至 8.x。
- Maven 坐标保持 `com.carpcap:hibernate-validator-plus`，正式版本从 `2.0.0` 开始。

---
