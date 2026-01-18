# Statum Java SDK Examples

This directory contains practical examples demonstrating how to use the Statum Java SDK.

## Getting Your API Credentials

Before running these examples, you need API credentials:

1. Visit the [Statum Dashboard](https://app.statum.co.ke/user)
2. Sign in or create an account
3. Navigate to API Management
4. Copy your Consumer Key and Consumer Secret

For more information, see the [official API documentation](https://docs.statum.co.ke).

```bash
export STATUM_CONSUMER_KEY="your_consumer_key"
export STATUM_CONSUMER_SECRET="your_consumer_secret"
```

### Compile and Run

```bash
# Navigate to the SDK root directory
cd /path/to/statum-java-sdk

# Compile the examples
javac -cp "target/classes:target/dependency/*" examples/*.java

# Run an example
java -cp "target/classes:target/dependency/*:examples" examples.BasicUsageExample
```

## Available Examples

### 1. BasicUsageExample.java

Demonstrates basic usage of all three APIs:
- Sending airtime
- Sending SMS
- Retrieving account details

**Run:**
```bash
java -cp "target/classes:target/dependency/*:examples" examples.BasicUsageExample
```

### 2. ErrorHandlingExample.java

Shows comprehensive error handling including:
- Handling all exception types (Authentication, Authorization, Validation, Network)
- Extracting validation error details
- Implementing retry logic with exponential backoff

**Run:**
```bash
java -cp "target/classes:target/dependency/*:examples" examples.ErrorHandlingExample
```

## Example Output

### Successful Airtime Transaction

```
=== Sending Airtime ===
✅ Airtime sent successfully!
Status Code: 200
Description: Transaction processed successfully
Request ID: req-abc123def456
```

### Validation Error

```
❌ Validation Error:
   Message: Validation failed
   Field Errors:
     - phone_number: Phone number must be in format 254XXXXXXXXX
     - amount: Amount must be a positive number
```

## Integration Patterns

### Spring Boot

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

### Singleton Pattern (Plain Java)

```java
public class StatumClientFactory {
    private static final StatumClient INSTANCE;
    
    static {
        StatumConfig config = new StatumConfig(
            System.getenv("STATUM_CONSUMER_KEY"),
            System.getenv("STATUM_CONSUMER_SECRET")
        );
        INSTANCE = new StatumClient(config);
    }
    
    public static StatumClient getInstance() {
        return INSTANCE;
    }
}
```

## Tips

1. **Reuse the Client**: `StatumClient` is thread-safe and expensive to create. Initialize once and reuse.

2. **Handle Exceptions**: Always wrap API calls in try-catch blocks to handle potential errors.

3. **Store Request IDs**: Keep track of `requestId` from responses for debugging and support.

4. **Use Environment Variables**: Never hardcode credentials in your source code.

5. **Implement Retry Logic**: For `NetworkException`, implement exponential backoff retry logic.

## Need Help?

- 📖 [SDK Documentation](../README.md)
- 📖 [API Documentation](https://docs.statum.co.ke)
- 🔑 [API Dashboard](https://app.statum.co.ke/user)
- 📧 support@statum.co.ke
- 🐛 [Report Issues](https://github.com/StatumKE/statum-java-sdk/issues)
