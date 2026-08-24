
# Changelog


[Project Home](../readme_en.md) · [Usage Guide](usage_en.md) · [中文](versions_v2.md)

## Version 2.4.x Series

### 2.4.0

- To keep the minor versions synchronized with the 1.x branch, v2.4.0 provides the same features as v1.4.0.
- Added `@CStrAllow` and `@CStrDeny` for string whitelist and blacklist validation.
- Added the `allowTld` attribute to `@CDomain` and `@CEmail`; top-level domains with a single label are disallowed by default.
- Added automatic date-format detection to `@CDateRange`.
- Added `@CJson` for JSON syntax validation with object and array root-type restrictions.

---

## Version 2.0.x Series

### 2.0.0

- Raises the minimum runtime to JDK 11 while remaining compatible with newer JDK releases.
- Uses the `jakarta.validation` API.
- Upgrades the Hibernate Validator baseline to 8.x.
- Keeps the Maven coordinates `com.carpcap:hibernate-validator-plus`; the first final release is `2.0.0`.

---
