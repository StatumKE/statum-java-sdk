# Statum Java SDK

[![Java CI](https://github.com/StatumKE/statum-java-sdk/actions/workflows/ci.yml/badge.svg)](https://github.com/StatumKE/statum-java-sdk/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/ke.co.statum/statum-java-sdk.svg)](https://central.sonatype.com/artifact/ke.co.statum/statum-java-sdk)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Official Java SDK for the [Statum API](https://statum.co.ke). Send airtime, SMS, and manage your account programmatically. typed interface for interacting with Statum services including Airtime, SMS, and Account Management.

## Features

✅ **Java 17+** - Modern Java with Records and sealed types  
✅ **Zero Dependencies** - Uses only `java.net.http` (no OkHttp, Retrofit, etc.)  
✅ **Thread-Safe** - Reuse `StatumClient` across your entire application  
✅ **Immutable DTOs** - All data models are immutable Java records  
✅ **Type-Safe** - Compile-time type checking for all API calls  
✅ **Framework-Agnostic** - Works with Spring, Quarkus, Micronaut, Jakarta EE, or plain Java

## Getting Started

1. **Sign up for a Statum account**: [https://app.statum.co.ke](https://app.statum.co.ke)
2. **Get your API credentials** from the [Statum Dashboard](https://app.statum.co.ke/user)
3. **Read the full API documentation**: [https://docs.statum.co.ke](https://docs.statum.co.ke)
4. **Install the SDK** (see Installation below)

## Requirements

- Java 17 or higher

## Installation

### Maven

```xml
<dependency>
    <groupId>co.ke.statum</groupId>
    <artifactId>statum-java-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'co.ke.statum:statum-java-sdk:1.0.0'
```

## Quick Start

```java
import co.ke.statum.sdk.StatumClient;
import co.ke.statum.sdk.config.StatumConfig;
import co.ke.statum.sdk.model.ApiResponse;

// Get your credentials from: https://app.statum.co.ke/user
StatumConfig config = new StatumConfig("YOUR_CONSUMER_KEY", "YOUR_CONSUMER_SECRET");
StatumClient client = new StatumClient(config);

// Send airtime
ApiResponse response = client.getAirtimeService()
    .sendAirtime("254712345678", "100");

// Available fields
int statusCode = response.statusCode();      // HTTP status code (200 = success)
String description = response.description(); // Human-readable message
String requestId = response.requestId();     // Unique transaction ID for tracking

System.out.println("Success! Request ID: " + statusCode);
```

## API Usage

### Send Airtime

```java
import co.ke.statum.sdk.model.ApiResponse;

ApiResponse response = client.getAirtimeService()
    .sendAirtime("254712345678", "100");

System.out.println("Status Code: " + response.statusCode());
System.out.println("Description: " + response.description());
System.out.println("Request ID: " + response.requestId());
```

### Send SMS

```java
ApiResponse response = client.getSmsService()
    .sendSms(
        "254712345678",           // Phone number
        "STATUM",                 // Sender ID
        "Hello from Statum SDK!"  // Message
    );

// Available fields
int statusCode = response.statusCode();      // HTTP status code (200 = success)
String description = response.description(); // Human-readable message
String requestId = response.requestId();     // Unique transaction ID for tracking
if (statusCode == 200) {
    System.out.println("SMS sent successfully!");
}
```

### Get Account Details

```java
import co.ke.statum.sdk.model.AccountDetailsResponse;

AccountDetailsResponse account = client.getAccountService()
    .getAccountDetails();

System.out.println("Organization: " + account.organization().name());
System.out.println("Balance: " + account.organization().details().availableBalance());
System.out.println("Location: " + account.organization().details().location());

// Access service accounts
account.organization().accounts().forEach(serviceAccount -> {
    System.out.println(serviceAccount.serviceName() + ": " + serviceAccount.account());
});
```

## Understanding API Responses

All API responses are immutable Java records with typed fields. Here's what you get back:

### ApiResponse (Airtime & SMS)

```java
ApiResponse response = client.getAirtimeService().sendAirtime("254712345678", "100");

// Available fields
int statusCode = response.statusCode();      // HTTP status code (200 = success)
String description = response.description(); // Human-readable message
String requestId = response.requestId();     // Unique transaction ID for tracking

// Example: Check if successful
if (response.statusCode() == 200) {
    // Transaction successful
    System.out.println("Transaction ID: " + response.requestId());
} else {
    // Transaction failed (but no exception thrown)
    System.err.println("Failed: " + response.description());
}
```

### AccountDetailsResponse

```java
AccountDetailsResponse account = client.getAccountService().getAccountDetails();

// Response structure
record AccountDetailsResponse(
    int statusCode,
    String description,
    String requestId,
    Organization organization
)

// Organization details
record Organization(
    String name,
    OrganizationDetails details,
    List<ServiceAccount> accounts
)

// Organization financial and contact details
record OrganizationDetails(
    double availableBalance,        // Current account balance
    String location,                // Physical location
    String website,                 // Company website
    String officeEmail,            // Contact email
    String officeMobile,           // Contact phone
    String mpesaAccountTopUpCode   // M-Pesa paybill/till
)

// Service account mapping
record ServiceAccount(
    String account,      // Account identifier
    String serviceName   // Service type (e.g., "AIRTIME", "SMS")
)

// Real-world usage
System.out.println("Balance: KES " + account.organization().details().availableBalance());
System.out.println("Top-up code: " + account.organization().details().mpesaAccountTopUpCode());

// List all service accounts
account.organization().accounts().forEach(sa -> 
    System.out.println(sa.serviceName() + " → " + sa.account())
);
```

### Response Status Codes

The SDK handles HTTP status codes as follows:

| Status Code | Meaning | SDK Behavior |
|------------|---------|--------------|
| 200 | Success | Returns response object normally |
| 401 | Authentication failed | Throws `AuthenticationException` |
| 403 | Access denied | Throws `AuthorizationException` |
| 422 | Validation failed | Throws `ValidationException` with field errors |
| 500+ | Server error | Throws `ApiException` |
| Network errors | Connection/timeout | Throws `NetworkException` |

**Note**: For 200 status, the response object is returned. For errors (4xx/5xx), exceptions are thrown instead.

## Input Validation

The SDK performs client-side validation before making API calls:

### Phone Number Format

All phone numbers must be valid Kenyan numbers:

```java
// ✅ Valid formats
"+254712345678"  // International format with +
"254712345678"   // International format without +
"0712345678"     // Local format
"0112345678"     // Alternative prefix

// ❌ Invalid formats
"712345678"      // Missing prefix
"123456"         // Too short
"+254612345678"  // Invalid operator (must start with 7 or 1)
```

**Validation pattern**: `^(?:\+254|254|0)(7|1)[0-9]{8}$`

### Airtime Amount

Airtime amounts must be between **KES 5** and **KES 10,000**:

```java
// ✅ Valid amounts
airtimeService.sendAirtime("254712345678", "5");      // Minimum
airtimeService.sendAirtime("254712345678", "100");    // Normal
airtimeService.sendAirtime("254712345678", "10000");  // Maximum

// ❌ Invalid amounts
airtimeService.sendAirtime("254712345678", "4");      // Too low
airtimeService.sendAirtime("254712345678", "10001");  // Too high
airtimeService.sendAirtime("254712345678", "abc");    // Not numeric
```

**Validation throws `IllegalArgumentException`** with descriptive error messages.

## Error Handling

### Exception Hierarchy

```
ApiException (base)
├── AuthenticationException (HTTP 401)
├── AuthorizationException (HTTP 403)
├── ValidationException (HTTP 422)
└── NetworkException (Network/timeout errors)
```

### Handling Errors

```java
import co.ke.statum.sdk.exceptions.*;

try {
    ApiResponse response = client.getAirtimeService()
        .sendAirtime("254712345678", "100");
    
    System.out.println("Success: " + response.description());
    
} catch (AuthenticationException e) {
    System.err.println("Authentication failed. Check your credentials at: https://app.statum.co.ke/user");
    
} catch (ValidationException e) {
    System.err.println("Validation failed:");
    e.getValidationErrors().forEach((field, errors) -> {
        System.err.println("  " + field + ": " + String.join(", ", errors));
    });
    
} catch (NetworkException e) {
    System.err.println("Network error: " + e.getMessage());
    // Implement retry logic
    
} catch (ApiException e) {
    System.err.println("API error: " + e.getMessage());
}
```

## Configuration

### Basic Configuration

```java
StatumConfig config = new StatumConfig("consumerKey", "consumerSecret");
StatumClient client = new StatumClient(config);
```

> **Get your credentials** from the [Statum Dashboard](https://app.statum.co.ke/user)

### Advanced: Custom Timeout

```java
import java.time.Duration;

StatumConfig config = new StatumConfig(
    "consumerKey", 
    "consumerSecret",
    "https://api.statum.co.ke/api/v2",
    Duration.ofSeconds(60)  // Custom timeout
);
```

### Spring Boot Integration

```java
@Configuration
public class StatumConfig {
    @Bean
    public StatumClient statumClient(
        @Value("${statum.consumer-key}") String key,
        @Value("${statum.consumer-secret}") String secret
    ) {
        return new StatumClient(new StatumConfig(key, secret));
    }
}
```

## Best Practices

### 1. Reuse the Client Instance

`StatumClient` is thread-safe. Create once, reuse everywhere:

```java
// ✅ GOOD
public class App {
    private static final StatumClient CLIENT = new StatumClient(
        new StatumConfig(System.getenv("STATUM_KEY"), System.getenv("STATUM_SECRET"))
    );
}

// ❌ BAD - Creates new HttpClient every time
public void sendSms() {
    StatumClient client = new StatumClient(config);
    client.getSmsService().sendSms(...);
}
```

### 2. Store Credentials Securely

```java
// ✅ GOOD - Use environment variables
StatumConfig config = new StatumConfig(
    System.getenv("STATUM_CONSUMER_KEY"),
    System.getenv("STATUM_CONSUMER_SECRET")
);

// ❌ BAD - Hardcoded
StatumConfig config = new StatumConfig("key123", "secret456");
```

### 3. Handle Network Errors with Retry Logic

```java
public ApiResponse sendWithRetry(String phone, String amount, int maxRetries) {
    int attempt = 0;
    while (attempt < maxRetries) {
        try {
            return client.getAirtimeService().sendAirtime(phone, amount);
        } catch (NetworkException e) {
            attempt++;
            if (attempt >= maxRetries) throw e;
            Thread.sleep((long) Math.pow(2, attempt) * 1000); // Exponential backoff
        }
    }
    throw new RuntimeException("Max retries exceeded");
}
```

### 4. Track Requests with Request IDs

```java
ApiResponse response = client.getAirtimeService().sendAirtime("254712345678", "100");
logger.info("Airtime sent. RequestID: {}", response.requestId());
```

## Troubleshooting

| Issue | Solution |
|-------|----------|
| **Authentication failed (401)** | Get credentials from [Dashboard](https://app.statum.co.ke/user). Verify no typos or swapped key/secret. |
| **Network timeout** | Increase timeout: `Duration.ofSeconds(60)` |
| **Validation failed (422)** | Check phone format (`254712345678`) and amount is positive |
| **ClassNotFoundException** | Ensure `jackson-databind` is on classpath |

## Examples

See the [examples](examples/) directory for complete working examples including Spring Boot integration and advanced error handling.

## Resources

- 🌐 **Statum Website**: [https://statum.co.ke](https://statum.co.ke)
- 📖 **API Documentation**: [https://docs.statum.co.ke](https://docs.statum.co.ke)
- 🔑 **API Dashboard**: [https://app.statum.co.ke/user](https://app.statum.co.ke/user) - Manage API keys and view usage
- 📧 **Email Support**: support@statum.co.ke
- 🐛 **Report Issues**: [GitHub Issues](https://github.com/StatumKE/statum-java-sdk/issues)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

