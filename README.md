# QR Code Generator API 🎯

A flexible, fault-tolerant QR code generator built with Spring Boot and ZXing.

This REST API generates QR code images from text input and supports dynamic size, format, and error correction levels. Designed to be clean, testable, and easily extendable.

---

## 🚀 Features

- `GET /api/health`  
  Simple health check endpoint (returns `200 OK` with `"OK"` body)

- `GET /api/qrcode`  
  Generates a QR code based on query parameters

---

## 🔧 Query Parameters

| Name        | Required | Default | Description |
|-------------|----------|---------|-------------|
| `contents`  | ✅ yes    | –       | The text or data to encode in the QR code |
| `size`      | ❌ no     | 250     | Image size in pixels (150–350 allowed) |
| `type`      | ❌ no     | png     | Image format: `png`, `jpeg`, or `gif` |
| `correction`| ❌ no     | L       | Error correction level: `L`, `M`, `Q`, or `H` |

---

## ✅ Examples

- **Health Check**  
  `GET /api/health`  
  → Response: `200 OK`  
  → Body: `OK`

- **Basic QR Code (defaults)**  
  `GET /api/qrcode?contents=hello`  
  → 250×250 PNG image of `"hello"`

- **Custom Size & Type**  
  `GET /api/qrcode?contents=https://example.com&size=300&type=jpeg`

- **Custom Error Correction**  
  `GET /api/qrcode?contents=abc&correction=H`

---

## 🧠 Error Handling

Returns `400 Bad Request` with a JSON error message.  
Validation priority:

1. Invalid `contents`
2. Invalid `size`
3. Invalid `correction`
4. Invalid `type`

Example:
```json
{ "error": "Contents cannot be null or blank" }
```

---

## 🧱 Tech Stack

- Java 17+
- Spring Boot
- ZXing (QR code generation)
- Maven / Gradle

---

## 🔄 How to Run Locally

### With Gradle
```bash
./gradlew bootRun
```

### With Maven
```bash
./mvnw spring-boot:run
```

Then visit:

- Health check:  
  [http://localhost:8080/api/health](http://localhost:8080/api/health)

- QR code generation:  
  [http://localhost:8080/api/qrcode?contents=hello](http://localhost:8080/api/qrcode?contents=hello)

---


## 🖼️ Example Output

![Example QR Code](https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=https://example.com)
