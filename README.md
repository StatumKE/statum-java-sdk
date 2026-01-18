# Statum Java SDK


The official Java SDK for the Statum API. This library provides a simple, thread-safe, and strictly typed interface for interacting with Statum services like Airtime, SMS, and Account Details.

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

## Usage

### Initialization

Create a `StatumConfig` and `StatumClient`. The client is thread-safe and should be reused across your application.

```java
import co.ke.statum.sdk.StatumClient;
import co.ke.statum.sdk.config.StatumConfig;

StatumConfig config = new StatumConfig("YOUR_CONSUMER_KEY", "YOUR_CONSUMER_SECRET");
StatumClient client = new StatumClient(config);
```

### Airtime API

```java
import co.ke.statum.sdk.model.ApiResponse;

try {
    ApiResponse response = client.getAirtimeService().sendAirtime("254712345678", "100");
    System.out.println("Status: " + response.statusCode());
    System.out.println("Description: " + response.description());
} catch (ApiException e) {
    System.err.println("Error: " + e.getMessage());
}
```

### SMS API

```java
import co.ke.statum.sdk.model.ApiResponse;

try {
    ApiResponse response = client.getSmsService().sendSms("254712345678", "STATUM", "Hello from Java SDK!");
    System.out.println("Status: " + response.statusCode());
} catch (ApiException e) {
    System.err.println("Error: " + e.getMessage());
}
```

### Account Details API

```java
import co.ke.statum.sdk.model.AccountDetailsResponse;

try {
    AccountDetailsResponse response = client.getAccountService().getAccountDetails();
    System.out.println("Balance: " + response.organization().details().availableBalance());
} catch (ApiException e) {
    System.err.println("Error: " + e.getMessage());
}
```

## Error Handling

All methods throw subclasses of `ApiException`:

- `AuthenticationException` (401): Invalid credentials.
- `AuthorizationException` (403): Access denied.
- `ValidationException` (422): Invalid input. Contains a map of validation errors.
- `NetworkException`: Network or timeout errors.
- `ApiException`: Generic API error (other 4xx/5xx status codes).

```java
try {
    client.getAirtimeService().sendAirtime("invalid", "100");
} catch (ValidationException e) {
    System.err.println("Validation errors: " + e.getValidationErrors());
} catch (AuthenticationException e) {
    System.err.println("Check your credentials!");
}
```

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for details.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
