# Hibernate Validator Plus 

![Java Version](https://img.shields.io/badge/Java-8%20%7C%2011%2B-orange?logo=openjdk)
![Hibernate Validator Version](https://img.shields.io/badge/validator-6.2.x%20%7C%208.x-green?logo=hibernate)
![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)

**语言：** [中文](readme.md) · [English](readme_en.md)

Hibernate Validator Plus 是基于 **Hibernate Validator** 的增强校验框架，为 Java 对象和请求参数提供常用校验注解、分组机制及统一校验工具。

---

## 核心能力

- 内置常用校验注解（账号、密码、身份证、IPv4 等）
- 内置多种数据分组校验方案
- 提供国际化消息，覆盖中文、英语、日语、俄语、法语和西班牙语等语言
- 所有扩展注解统一支持空值策略
- 与 Hibernate Validator 原生框架保持完全兼容，可直接沿用其所有内置校验功能。
- 依赖精简，不强制依赖 Spring 等应用框架。
- 支持 Spring MVC、Spring Boot 项目直接引入，可自动校验，提供工具类手动调用。
- 同时维护 JDK 8 和 JDK 11+ 两个版本系列，详见下方版本说明

---

## 版本说明

| 版本系列 | JDK | Validation API | Hibernate Validator | 适用项目 |
|---------|-----|----------------|---------------------|----------|
| [2.x](https://github.com/CarpCap/hibernate-validator-plus/tree/2.x) | JDK 11+ | `jakarta.validation` | 8.x | Spring Boot 3、Jakarta EE 10 或使用 `jakarta.validation` 的项目 |
| [1.x](https://github.com/CarpCap/hibernate-validator-plus/tree/1.x) | JDK 8 | `javax.validation` | 6.2.x | Spring Boot 2 或使用 `javax.validation` 的项目 |

---

## 技术文档

- [2.x 使用教程（JDK 11+ / jakarta.validation）](docs/usage.md)
- [1.x 使用教程（JDK 8 / javax.validation）](https://github.com/CarpCap/hibernate-validator-plus/blob/1.x/docs/usage.md)
- [更新日志](docs/versions.md)

---

## 注解说明

目录位置：[`annotation`](src/main/java/com/carpcap/hvp/annotation)

| 注解名称            | 功能说明       | 详细描述                                           |
|-----------------|------------|------------------------------------------------|
| `@CAccount`     | 账号格式验证     | 默认：字母开头，5–16 字符，允许字母数字下划线                      |
| `@CPassword`    | 密码强度验证     | 默认：密码长度要求6-18，至少1个字母，至少1个数字。                   |
| `@CIdCard`      | 身份号码验证     | region 支持 CN/US/JP/KR/UK，默认 CN；regexp 可覆盖地区规则  |
| `@CPhone`       | 手机号验证      | 默认：中国手机号；支持 region 参数切换 CN/US/JP/KR/UK         |
| `@CPassport`    | 护照号验证      | 默认：中国护照格式；支持 region 参数切换 CN/US/JP/UK/KR        |
| `@CPostCode`    | 邮编格式验证     | 默认：中国邮编；支持 region 参数切换 CN/US/JP/UK/KR          |
| `@CEmail`       | 邮箱格式验证     | 支持域名黑白名单及最大子域层级限制.                             |
| `@CFile`        | 文件验证       | 默认：最大 1 MB；可指定后缀 `fileNameSuffix`              |
| `@CPlateNumber` | 车牌号验证      | 默认：中国车牌                                        |
| `@CIpv4`        | Ipv4 验证    | 合法 IPv4 地址格式                                   |
| `@CIpv6`        | IPv6 验证    | 合法 IPv6 地址格式                                   |
| `@CDateRange`   | 日期范围验证     | `min` 最小日期、`max` 最大日期，推荐格式：yyyy-MM-dd HH:mm:ss |
| `@CBankCard`    | 银行卡号 验证    | 银行卡号验证，默认使用Luhn算法，可以指定拦截卡号前缀.                  |
| `@CUrl`         | URL      验证 | URL格式 验证                                       |
| `@CMoney`       | 金额     验证  | 金额验证<br/> 支持验证数字、字符串或BigDecimal类型的金额格式         |
| `@CMacAddress`  | Mac地址验证    | MAC地址格式验证                                      |


---

## 分组（Groups）

目录位置：[`groups`](src/main/java/com/carpcap/hvp/groups)

| 分组名称          | 场景说明      |
|---------------|-----------|
| `@CCreate`    | 创建数据校验    |
| `@CCreateDef` | 创建 + 默认校验 |
| `@CQuery`     | 查询数据校验    |
| `@CQueryDef`  | 查询 + 默认校验 |
| 更多请看源码......  | ......    |

---

## CValid 工具类

`CValid` 提供普通校验(全量校验)、快速校验（fail-fast）、属性校验、分组校验等多种能力。

Spring boot 环境下建议替换掉CValid内置的validator与fastValidator, 教程文档有案例

异常类：ValidationException

| 方法类型                       | 校验模式 | 失败行为 | 返回类型         |
| ------------------------- | ---- | ---- | ------------ |
| `validate(Object object)`      <br/>`validate(Object object, Class<?>... groups)`             |快速校验 ⚡ | 抛异常❗  | void         |
| `tryValidate(Object object)`    <br/>     `tryValidate(Object object, Class<?>... groups)`        | 全量校验 | 不抛异常 | List<String> |
| `tryFastValidate(Object object)` <br/>  `tryFastValidate(Object object, Class<?>... groups)`        | 快速校验  ⚡| 不抛异常 | String       |
| `validateProperty(Object object, String propertyName, Class<?>... groups)`           | 快速校验  ⚡| 抛异常❗  | void         |
| `tryValidateProperty(Object object, String propertyName, Class<?>... groups)`        | 全量校验 | 不抛异常 | List<String> |
| `tryFastValidateProperty(Object object, String propertyName, Class<?>... groups)`   | 快速校验 ⚡ | 不抛异常 | String       |

---

## 开源协议

本项目基于 Apache License 2.0 开源协议发布。

协议全文请查看项目根目录中的 [LICENSE](LICENSE)，或访问 [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)。
