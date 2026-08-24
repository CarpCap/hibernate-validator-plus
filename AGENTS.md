# Hibernate Validator Plus — 项目知识库

## 协作约定

- 用户名：CarpCap；默认使用中文沟通。
- 所有文本和源码使用 UTF-8 编码。
- 新增或修改代码时，仅在必要位置添加简洁、明确的注释。
- 禁止批量删除文件或目录；禁止使用 `del /s`、`rd /s`、`rmdir /s`、`Remove-Item -Recurse`、`rm -rf`。
- 如需删除文件，只能一次删除一个已确认的明确路径；如需批量删除，停止操作并请用户手动处理。
- 生成i18n文件时要考虑编码问题，参考每个文件现有的编码

## 项目概述

Hibernate Validator Plus（简称 HVP）是基于 **Hibernate Validator** 的增强校验框架，提供更丰富、实用的校验注解、分组校验机制以及统一的校验工具类。项目由作者 **CarpCap** 开发，采用 Apache License 2.0 开源协议。项目同时维护 1.x 和 2.x 两个大版本。

| 项目 | 说明                                                                                        |
|------|-------------------------------------------------------------------------------------------|
| Maven 坐标 | `com.carpcap:hibernate-validator-plus`                                                    |
| 1.x 版本线 | `1.4.0`；JDK 8 + `javax.validation` + Hibernate Validator 6.2.x；`1.x` 分支                   |
| 2.x 版本线 | `2.4.0`；JDK 11+ + `jakarta.validation` + Hibernate Validator 8.x；`2.x` 与 `main` 同步        |
| 公共依赖 | hutool-core 5.8.41；具体依赖以对应分支 `pom.xml` 为准                                                 |
| 验证器注册 | Google Auto Service（`@AutoService`）自动注册 `ConstraintValidator` 实现                          |
| 框架集成 | 不强制依赖 Spring；支持独立使用及 Spring MVC / Spring Boot 集成                                          |
| 示例项目 | [hibernate-validator-plus-demo](https://github.com/carpcap/hibernate-validator-plus-demo) |

### 知识库导航

- [项目结构](#项目结构)
- [校验注解](#校验注解一览)
- [验证器实现](#验证器实现)
- [分组机制](#分组机制)
- [工具类](#工具类)
- [版本历史](#版本历史)
- [构建与发布](#构建与发布)
- [已知问题](#已知问题2026-08-07)

### 分支与文档规则

- `1.x` 和 `2.x` 都是持续维护的开发线，均可增加新功能、修复缺陷并发布新版本。
- `main` 与 `2.x` 保持同步；`main` 是默认展示和 2.x 稳定开发入口，`2.x` 是明确的版本线分支。
- 1.x 新功能必须保持 JDK 8 与 `javax.validation` 兼容；2.x 新功能必须保持 JDK 11+ 与 `jakarta.validation` 兼容。
- 根目录 README 同时说明两个大版本；`docs/versions*.md` 和 `docs/usage*.md` 只记录当前 2.x 的版本与使用方式。
- 1.x 的使用说明以 `1.x` 分支中的文档和 `pom.xml` 为准，不能将 2.x 的 `jakarta.validation` 示例复制到 1.x 项目。

---

## 项目结构

```
hibernate-validator-plus/
├── pom.xml                          # Maven 构建配置
├── readme.md / readme_en.md         # 中英文文档
├── LICENSE                          # Apache 2.0 许可证
├── .gitignore
├── docs/
│   ├── versions.md / versions_en.md # 中英文版本日志
│   ├── anns.png                     # 注解使用示例图
│   ├── img0.png / img1.png / img2.png
├── assets/
├── src/
│   ├── main/
│   │   ├── java/com/carpcap/hvp/
│   │   │   ├── annotation/               # 校验注解定义（20 个）
│   │   │   ├── constraintvalidators/     # 验证器源码（25 个：22 个具体实现 + 3 个抽象基类）
│   │   │   ├── groups/                   # 校验分组接口 (16个)
│   │   │   └── utils/                    # 工具类 (2个)
│   │   └── resources/
│   │       └── ValidationMessages*.properties  # 17 个 ResourceBundle 文件
│   └── test/
│       ├── java/
│       │   ├── AnnotationTest*.java      # 3 个 main 方法测试/演示类
│       │   └── User*.java                # 3 个测试实体
│       └── resource/                     # 注意：当前使用单数 resource，非 Maven 默认 resources
│           └── 3.png                     # 文件校验测试资源
```

---

## 校验注解一览

所有注解位于 `com.carpcap.hvp.annotation` 包，均支持：
- **allowNull**: 是否允许 null 值（默认 true）
- **groups**: 分组校验
- **payload**: 负载
- **@Repeatable**: 可重复标注（含内嵌 List 注解）

| 注解 | 功能 | 关键属性 |
|------|------|----------|
| @CAccount | 账号格式验证 | regexp（正则）, min/max（长度范围，默认 5-16） |
| @CPassword | 密码强度验证 | min/max（长度 6-18），默认需包含字母+数字 |
| @CIdCard | 身份号码格式验证 | region 支持 CN/US/JP/KR/UK，CN 仅支持 18 位格式 |
| @CPhone | 手机号验证 | region 参数支持 CN/US/JP/KR 等多国号码 |
| @CIpv4 | IPv4 地址验证 | 标准 IPv4 正则 |
| @CIpv6 | IPv6 地址验证 | 通过 InetAddress 原生解析 |
| @CDomain | 域名格式验证 | 支持中文域名，level 控制层级，allowTld 控制是否允许顶级域名 |
| @CEmail | 邮箱格式验证 | 支持域名黑白名单、最大子域层级及 allowTld |
| @CPlateNumber | 中国车牌号验证 | 支持新能源/普通车牌 |
| @CFile | 文件验证 | fileNameSuffix（后缀限制）, fileSize（大小，默认 1MB） |
| @CUrl | URL 验证 | protocols（协议白名单）, allowLocalhost, allowIp |
| @CBankCard | 银行卡号验证 | Luhn 算法校验, allowedPrefixes/forbiddenPrefixes |
| @CMoney | 金额格式验证 | min/max, 整数/小数位数, 货币符号, 千分位 |
| @CDateRange | 日期范围验证 | min/max 日期, format, 支持 String/Date/LocalDate/LocalDateTime/Instant/ZonedDateTime |
| @CStrAllow | 字符串白名单验证 | value 定义允许的字符串集合 |
| @CStrDeny | 字符串黑名单验证 | value 定义禁止的字符串集合 |
| @CJson | JSON 格式验证 | 校验字符串 JSON 语法，可限制对象或数组根节点 |
| @CMacAddress | MAC 地址验证 | allowLowercase, allowEui64, allowOmittingLeadingZero |
| @CPassport | 护照号验证 | region/regexp，内置 CN/US/JP/UK/KR |
| @CPostCode | 邮政编码格式验证 | region/regexp，内置 CN/US/JP/UK/KR |

### 注解设计模式

所有注解均采用 `validatedBy = { }`（空数组），实际验证器通过 `@AutoService(ConstraintValidator.class)` 自动注册。这样注解与验证器解耦，无需在注解中硬编码验证器类。

---

## 验证器实现

验证器位于 `com.carpcap.hvp.constraintvalidators` 包，通过 Google Auto Service SPI 机制自动注册。

| 验证器 | 对应注解 | 实现策略 |
|--------|----------|----------|
| AbstractCPatternValidator | 抽象基类 | 通用的正则匹配 + null 处理基类 |
| CAccountValidator | @CAccount | 继承 AbstractCPatternValidator，叠加长度范围校验 |
| CPasswordValidator | @CPassword | 继承 AbstractCPatternValidator，叠加长度范围校验 |
| CIpAddressValidator | @CIpv4 | 直接继承 AbstractCPatternValidator |
| CDomainValidator | @CDomain | 直接继承 AbstractCPatternValidator |
| CPhoneValidator | @CPhone | 扩展正则校验，支持多地区手机号模板 |
| CIdCardValidator | @CIdCard | 按 region 选择身份号码正则，regexp 优先 |
| CPlateNumberValidator | @CPlateNumber | 扩展正则校验 |
| CUrlValidator | @CUrl | 使用 java.net.URL 解析 + 正则回退 |
| CBankCardValidator | @CBankCard | Luhn 算法校验，前缀黑白名单 |
| CMoneyValidator | @CMoney | 复杂金额格式校验（符号/千分位/小数位） |
| CMacAddressValidator | @CMacAddress | 动态构建正则，支持多种格式 |
| CIpv6Validator | @CIpv6 | 使用 InetAddress.getByName() 原生解析 |
| CPassportValidator | @CPassport | 按 region 选择护照号正则，regexp 优先 |
| CPostCodeValidator | @CPostCode | 按 region 选择邮编正则，regexp 优先 |
| CDateRange*Validator (6个) | @CDateRange | 分别支持 Date/LocalDate/LocalDateTime/Instant/ZonedDateTime/String |
| AbstractCFileValidator | 抽象基类 | 文件校验基类（后缀名、大小） |
| CFileValidator | @CFile | java.io.File 类型验证 |
| CFileStrValidator | @CFile | 文件名（String）后缀验证 |

---

## 分组机制

分组接口位于 `com.carpcap.hvp.groups`，覆盖常见 CRUD + HTTP 方法场景。每组包含一个基础接口和对应的 `*Def` 接口。

| 基础分组 | Def 分组 | 继承关系 | 场景 |
|----------|----------|----------|------|
| CCreate | CCreateDef | CCreate + Default | 创建 |
| CQuery | CQueryDef | CQuery + Default | 查询 |
| CUpdate | CUpdateDef | CUpdate + Default | 更新 |
| CDelete | CDeleteDef | CDelete + Default | 删除 |
| CGet | CGetDef | CGet + Default | GET 请求 |
| CPost | CPostDef | CPost + Default | POST 请求 |
| CPut | CPutDef | CPut + Default | PUT 请求 |
| CPatch | CPatchDef | CPatch + Default | PATCH 请求 |

每个 `*Def` 接口都继承自对应的业务接口 + `Default`，使得使用该分组时既验证业务组内的约束，也验证未指定分组的约束。1.x 使用 `javax.validation.groups.Default`，2.x 使用 `jakarta.validation.groups.Default`。

---

## 工具类

### CValid

统一的校验工具类，内部持有两个 Validator 实例：
- **validator**: 默认全量校验器（收集所有违反）
- **fastValidator**: 快速失败校验器（`hibernate.validator.fail_fast=true`）

| 方法 | 校验模式 | 失败行为 | 返回类型 |
|------|----------|----------|----------|
| validate(obj, ...groups) | 快速失败 | 抛 ValidationException | void |
| tryValidate(obj, ...groups) | 全量校验 | 不抛异常 | List<String> |
| tryFastValidate(obj, ...groups) | 快速失败 | 不抛异常 | String |
| validateProperty(obj, property, ...groups) | 快速失败 | 抛 ValidationException | void |
| tryValidateProperty(obj, property, ...groups) | 全量校验 | 不抛异常 | List<String> |
| tryFastValidateProperty(obj, property, ...groups) | 快速失败 | 不抛异常 | String |

### CValidNullUtil

内部判空辅助工具，统一处理 allowNull 逻辑：
- 返回 `1`: null 值且允许为空 -> 校验通过
- 返回 `-1`: null 值且不允许为空 -> 校验失败
- 返回 `0`: 值非空 -> 需要继续后续校验

---

## 国际化（i18n）

消息文件位于 `src/main/resources/`，命名遵循 Java ResourceBundle 规范。
当前共有 **17 个 ResourceBundle 文件**：一个默认英文文件，以及 16 个语言/地区变体文件（其中中文包含 `zh`、`zh_CN`、`zh_TW`）。

消息支持 Hibernate Validator 的 Expression Language 插值表达式。

---

## 版本历史

### 2.4.x 系列
- **2.4.0**: 新增 `@CStrAllow`、`@CStrDeny`；`@CDomain`、`@CEmail` 增加 `allowTld`；`@CDateRange` 支持自动识别日期格式；完善 17 个 i18n 资源包。


### 2.0.x 系列
- **2.0.0**: 最低支持 JDK 11，迁移到 `jakarta.validation` 与 Hibernate Validator 8.x

### 1.3.x 系列
- **1.3.1**: 当前 1.x 版本，继续支持 JDK 8 与 `javax.validation`
- **1.3.0**: 新增 @CEmail，并完成 IPv6、地区规则、日期范围与文件校验等质量修复

### 1.2.x 系列
- **1.2.3**: 当前版本；CValid 的普通/快速 Validator 增加 get/set 方法，支持注入自定义实例
- **1.2.2**: 新增 @CPassport、@CPostCode；@CPhone 增加多地区支持；@CDateRange 增加 Instant/ZonedDateTime
- **1.2.1**: 修复 @CDateRange 时区问题，max 日期自动补全到 23:59:59；所有注解增加 allowNull 字段
- **1.2.0**: 新增 @CBankCard、@CUrl、@CMoney、@CMacAddress、@CIpv6；@CAccount/@CPassword 增加 min/max；@CDomain 支持中文域名

### 1.1.x 系列
- **1.1.4**: 新增 CValid 快速校验方法
- **1.1.3**: 新增国际化 i18n 支持
- **1.1.2**: 升级 hibernate-validator 至 6.2.5.Final，hutool-core 至 5.8.41
- **1.1.1**: 修复分组继承 Bug
- **1.1.0**: (不推荐) 存在分组继承 Bug

### 1.0.x 系列
- **1.0.1**: 新增车牌号、文件校验
- **1.0.0**: 初始发布，7 个注解 + 5 个分组 + 工具类

---

## 构建与发布

- **Maven 构建**: 1.x 编译目标 Java 8；2.x 编译目标 Java 11；源码编码 UTF-8
- **发布到 Maven Central**: 通过 Sonatype Central Publishing Plugin 发布，自动打包源码 + Javadoc + GPG 签名
- **Maven 测试现状**: 使用 JUnit 4 接入两个套件入口；`mvn test` 实际执行 2 个测试，内部覆盖 379 个检查
- **手工测试入口**: 仍可分别运行 `AnnotationTest`、`AnnotationTwoTest`、`AnnotationTest3` 的 main 方法；前两组当前合计 379 个计数用例
- **测试资源目录**: 当前为 `src/test/resource/`（单数），Maven 默认目录是 `src/test/resources/`

---

## 关键设计决策

1. **零硬编码注册**: 使用 Google Auto Service SPI 自动注册 ConstraintValidator，注解不用指定验证器类
2. **两级校验器**: CValid 维护两个 Validator 实例（全量 + 快速失败），用户根据场景选择
3. **统一的 null 处理**: CValidNullUtil 提供统一判空逻辑，所有验证器都通过它处理 allowNull
4. **抽象基类模式**: 减少重复代码，正则类/日期类/文件类各有抽象基类
5. **分组继承链**: 每个业务分组有对应的 *Def 接口继承 Default，解决分组校验时默认约束不被执行的常见问题
6. **轻量无侵入**: 不强制依赖 Spring，但完全兼容 Spring 生态

---




### 后续功能候选

以下注解与现有校验能力互补，可作为后续小版本功能候选：

| 候选注解 | 建议功能 | 优先级 |
|---|---|---|
| `@CJson` | 校验字符串是否为合法 JSON，可配置对象/数组根节点 | 高 |
| `@CRegex` | 独立的正则校验注解，统一 `allowNull`、消息和重复标注能力 | 高 |
| `@CContains` | 字符串必须包含指定文本、前缀或后缀，支持大小写策略 | 中 |
| `@CFileMime` | 文件扩展名之外，校验 MIME 类型或文件头魔数 | 中 |
| `@CDate` | 单日期格式、最小值、最大值校验，补充 `@CDateRange` | 中 |
| `@CListSize` | 集合、数组、Map 的元素数量范围校验 | 中 |
| `@CNumberRange` | 统一数字、字符串数字和 BigDecimal 的数值区间校验 | 中 |

