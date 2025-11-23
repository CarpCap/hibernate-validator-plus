# Hibernate Validator Plus 

🌍 选择语言/Select Language:

- [中文](readme.md)
- [English](readme_en.md)

---

Hibernate Validator Plus 是基于 **Hibernate Validator** 的增强框架，提供了更丰富、实用的校验注解、分组校验机制以及统一的校验工具类，让 Java 对象与请求参数验证更加简洁、高效。

📦 **特性：**
- 内置常用校验注解（账号、密码、邮箱、身份证、IPv4 等）
- 内置多种数据分组校验方案
- 精简依赖，架构轻量
- 支持 Spring MVC 自动校验与工具类手动调用
- 国际化i18n，支持中文、英语、日语、汉语、俄语、法语、西班牙语等..


---

# 更新日志
[versions.md](docs/versions.md)

---

## 📘 注解说明

目录位置：[`annotation`](src/main/java/com/carpcap/hvp/annotation)

| 注解名称            | 功能说明    | 详细描述                              |
|-----------------|---------|-----------------------------------|
| `@CAccount`     | 账号格式验证  | 默认：字母开头，5–16 字符，允许字母数字下划线         |
| `@CPassword`    | 密码强度验证  | 默认：字母开头，6–18 位，仅限字母/数字/下划线        |
| `@CIdCard`      | 身份证号验证  | 默认：适配中国大陆身份证格式                    |
| `@CPhone`       | 手机号验证   | 默认：中国手机号                          |
| `@CEmail`       | 邮箱格式验证  | RFC 标准校验                          |
| `@CFile`        | 文件校验    | 默认：最大 1 MB；可指定后缀 `fileNameSuffix` |
| `@CPlateNumber` | 车牌号验证   | 默认：中国车牌                      |
| `@CIpv4`        | Ipv4 验证 | 合法 IPv4 地址格式                      |
| `@CDateRange`   | 日期范围验证  | `min` 最小日期、`max` 最大日期             |

---

## 📂 分组（Groups）

目录位置：[`groups`](src/main/java/com/carpcap/hvp/groups)

| 分组名称          | 场景说明      |
|---------------|-----------|
| `@CCreate`    | 创建数据校验    |
| `@CCreateDef` | 创建 + 默认校验 |
| `@CQuery`     | 查询数据校验    |
| `@CQueryDef`  | 查询 + 默认校验 |
| 更多请看源码......  | ......    |


---

# 🔧 CValid 工具类

`CValid` 提供普通校验、快速校验（fail-fast）、属性校验、分组校验等多种能力。

异常类：ValidationException

| 方法类型                       | 校验模式 | 失败行为 | 返回类型         |
| ------------------------- | ---- | ---- | ------------ |
| `validate`                 |快速校验 ⚡ | 抛异常❗  | void         |
| `tryValidate`               | 全量校验 | 不抛异常 | List<String> |
| `tryFastValidate`          | 快速校验  ⚡| 不抛异常 | String       |
| `validateProperty`           | 快速校验  ⚡| 抛异常❗  | void         |
| `tryValidateProperty`        | 全量校验 | 不抛异常 | List<String> |
| `tryFastValidateProperty`   | 快速校验 ⚡ | 不抛异常 | String       |



---



## 🛠 使用示例

示例项目地址：  
🔗 https://github.com/carpcap/hibernate-validator-plus-demo

### 1. Maven 依赖

```xml
<dependency>
    <groupId>com.carpcap</groupId>
    <artifactId>hibernate-validator-plus</artifactId>
    <version>1.1.4</version>
</dependency>
```

### 根据不同分组来分别校验场景

声明分组类别

<img src="docs/img0.png" width="500" style="border: 2px solid #ddd; border-radius: 8px;">

通过Spring Mvc自动校验

<img src="docs/img1.png" width="500" style="border: 2px solid #ddd; border-radius: 8px;">

调用提供的工具类进行手动校验 校验失败会抛出ValidationException

<img src="docs/img2.png" width="500" style="border: 2px solid #ddd; border-radius: 8px;">


## 📜 协议（License）

本项目基于 Apache License 2.0 开源协议发布。
你可以在遵循协议要求（例如保留版权说明、不得移除版权声明等）前提下：

✔ 允许免费商用

✔ 允许修改源代码

✔ 允许分发源码和二进制包

✔ 允许二次开发

✔ 允许私有化使用

❗ 但需保留原作者（CarpCap）版权声明

协议全文请参考项目根目录中的：
LICENSE 文件
或查看官方协议文档：
https://www.apache.org/licenses/LICENSE-2.0
