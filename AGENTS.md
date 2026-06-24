# Hibernate Validator Plus — 项目知识库

## 项目概述

Hibernate Validator Plus（简称 HVP）是基于 **Hibernate Validator 6.2.5.Final** 的增强校验框架，提供更丰富、实用的校验注解、分组校验机制以及统一的校验工具类。项目由作者 **CarpCap** 开发，采用 Apache License 2.0 开源协议。

- **Maven 坐标**: `com.carpcap:hibernate-validator-plus:1.2.2-SNAPSHOT`
- **JDK 版本**: >= JDK 8
- **核心依赖**: hibernate-validator 6.2.5.Final, hutool-core 5.8.41, jakarta.el 3.0.4
- **自动注册**: 使用 Google Auto Service (`@AutoService`) 自动注册 ConstraintValidator 实现
- **不强制依赖 Spring**: 可独立使用，也可与 Spring MVC / Spring Boot 集成
- **示例项目**: https://github.com/carpcap/hibernate-validator-plus-demo

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
│   │   │   ├── annotation/               # 校验注解定义 (15个)
│   │   │   ├── constraintvalidators/     # 校验器实现 (23个)
│   │   │   ├── groups/                   # 校验分组接口 (16个)
│   │   │   └── utils/                    # 工具类 (2个)
│   │   └── resources/
│   │       └── ValidationMessages*.properties  # 国际化消息 (17种语言)
│   └── test/
│       ├── java/
│       │   ├── AnnotationTest.java       # 完整测试类
│       │   └── User.java                 # 测试用实体
│       └── resource/
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
| @CEmail | 邮箱格式验证 | RFC 标准校验（复用 Hutool） |
| @CIdCard | 中国身份证号验证 | 支持 15/18 位格式 |
| @CPhone | 手机号验证 | region 参数支持 CN/US/JP/KR 等多国号码 |
| @CIpv4 | IPv4 地址验证 | 标准 IPv4 正则 |
| @CIpv6 | IPv6 地址验证 | 通过 InetAddress 原生解析 |
| @CDomain | 域名格式验证 | 支持中文域名 |
| @CPlateNumber | 中国车牌号验证 | 支持新能源/普通车牌 |
| @CFile | 文件验证 | fileNameSuffix（后缀限制）, fileSize（大小，默认 1MB） |
| @CUrl | URL 验证 | protocols（协议白名单）, allowLocalhost, allowIp |
| @CBankCard | 银行卡号验证 | Luhn 算法校验, allowedPrefixes/forbiddenPrefixes |
| @CMoney | 金额格式验证 | min/max, 整数/小数位数, 货币符号, 千分位 |
| @CDateRange | 日期范围验证 | min/max 日期, format, 支持 String/Date/LocalDate/LocalDateTime/Instant/ZonedDateTime |
| @CMacAddress | MAC 地址验证 | allowLowercase, allowEui64, allowOmittingLeadingZero |

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
| CIdCardValidator | @CIdCard | 扩展正则校验 |
| CPlateNumberValidator | @CPlateNumber | 扩展正则校验 |
| CUrlValidator | @CUrl | 使用 java.net.URL 解析 + 正则回退 |
| CBankCardValidator | @CBankCard | Luhn 算法校验，前缀黑白名单 |
| CMoneyValidator | @CMoney | 复杂金额格式校验（符号/千分位/小数位） |
| CMacAddressValidator | @CMacAddress | 动态构建正则，支持多种格式 |
| CIpv6Validator | @CIpv6 | 使用 InetAddress.getByName() 原生解析 |
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

每个 `*Def` 接口都继承自对应的业务接口 + `javax.validation.groups.Default`，使得使用该分组时既验证业务组内的约束，也验证未指定分组的约束。

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
支持 **17 种语言**：英语（默认）、简体中文、繁体中文、日语、韩语、法语、德语、西班牙语、俄语、阿拉伯语、印地语、意大利语、荷兰语、葡萄牙语、泰语、越南语。

消息支持 Hibernate Validator 的 Expression Language 插值表达式。

---

## 版本历史

### 1.2.x 系列
- **1.2.2-SNAPSHOT**: 当前开发版本
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

- **Maven 构建**: 编译目标 Java 1.8，源码编码 UTF-8
- **发布到 Maven Central**: 通过 Sonatype Central Publishing Plugin 发布，自动打包源码 + Javadoc + GPG 签名
- **测试**: 运行 AnnotationTest 的 main 方法即可执行全部测试用例

---

## 关键设计决策

1. **零硬编码注册**: 使用 Google Auto Service SPI 自动注册 ConstraintValidator，注解不用指定验证器类
2. **两级校验器**: CValid 维护两个 Validator 实例（全量 + 快速失败），用户根据场景选择
3. **统一的 null 处理**: CValidNullUtil 提供统一判空逻辑，所有验证器都通过它处理 allowNull
4. **抽象基类模式**: 减少重复代码，正则类/日期类/文件类各有抽象基类
5. **分组继承链**: 每个业务分组有对应的 *Def 接口继承 Default，解决分组校验时默认约束不被执行的常见问题
6. **轻量无侵入**: 不强制依赖 Spring，但完全兼容 Spring 生态
