# Error Code Format and Field Definitions

### Format:

```
[ModuleCode][ErrorTypeCode][DetailCode]
```

**Example:** `GW020001`

* `GW` — Gateway module
* `02` — Authentication error type
* `0001` — Specific error detail code

---

## 1. Module Codes (`ModuleCode`) — 2 characters

| Code | Module Name | Description                                       |
| ---- | ----------- | ------------------------------------------------- |
| CM   | Common      | Common/general shared module                      |
| AU   | Auth        | Authentication & user management                  |
| CO   | Console     | Admin console and management                      |
| GW   | Gateway     | API gateway, routing, rate limiting               |
| CN   | Connector   | Connector module (LLM calling, usage stats, etc.) |

---

## 2. Error Type Codes (`ErrorTypeCode`) — 2 digits

| Code | Error Type Description                     |
| ---- | ------------------------------------------ |
| 01   | Parameter validation error                 |
| 02   | Authentication or authorization error      |
| 03   | Resource not found error                   |
| 04   | Business logic error                       |
| 05   | External service call failure              |
| 06   | Internal system error                      |
| 07   | Rate limiting or circuit breaker triggered |
| 08   | Third-party dependency failure             |

---

## 3. Detail Codes (`DetailCode`) — 4 digits

* **Module-specific error identifiers**
* Each module + error type pair can have its own detail codes starting from `0001` and incrementing upwards.

### Example for Gateway module `GW` + Auth error `02`:

| Detail Code | Meaning                     |
| ----------- | --------------------------- |
| 0001        | Invalid or expired token    |
| 0002        | API key missing or disabled |
| 0003        | Unauthorized access         |

### Example for Auth module `AU` + Parameter validation error `01`:

| Detail Code | Meaning                    |
| ----------- | -------------------------- |
| 0001        | Username parameter missing |
| 0002        | Password too short         |

---

# Summary Example

| Full Error Code | Meaning                                             |
| --------------- | --------------------------------------------------- |
| `AU020001`      | Auth module, authentication error, invalid token    |
| `GW070001`      | Gateway module, rate limiting error, limit exceeded |
| `CM060001`      | Common module, internal system error                |

---

### Usage

* These codes are consistent across your system for easy tracing and internationalization.
* Each error code corresponds to an i18n message key like `AU020001` for localization.
* HTTP status codes are mapped per error type or detailed error.

---