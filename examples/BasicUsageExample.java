package examples;

import ke.co.statum.sdk.StatumClient;
import ke.co.statum.sdk.config.StatumConfig;
import ke.co.statum.sdk.exceptions.ApiException;
import ke.co.statum.sdk.model.AccountDetailsResponse;
import ke.co.statum.sdk.model.ApiResponse;

/**
 * Basic usage examples for the Statum Java SDK.
 */
public class BasicUsageExample {

    public static void main(String[] args) {
        // Initialize the client with your credentials
        StatumConfig config = new StatumConfig(
                System.getenv("STATUM_CONSUMER_KEY"),
                System.getenv("STATUM_CONSUMER_SECRET"));
        StatumClient client = new StatumClient(config);

        // Example 1: Send Airtime
        sendAirtimeExample(client);

        // Example 2: Send SMS
        sendSmsExample(client);

        // Example 3: Get Account Details
        getAccountDetailsExample(client);
    }

    private static void sendAirtimeExample(StatumClient client) {
        System.out.println("\n=== Sending Airtime ===");
        try {
            ApiResponse response = client.getAirtimeService()
                    .sendAirtime("254712345678", "100");

            System.out.println("✅ Airtime sent successfully!");
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Description: " + response.description());
            System.out.println("Request ID: " + response.requestId());

        } catch (ApiException e) {
            System.err.println("❌ Failed to send airtime: " + e.getMessage());
        }
    }

    private static void sendSmsExample(StatumClient client) {
        System.out.println("\n=== Sending SMS ===");
        try {
            ApiResponse response = client.getSmsService()
                    .sendSms(
                            "254712345678",
                            "STATUM",
                            "Hello from Statum Java SDK! This is a test message.");

            System.out.println("✅ SMS sent successfully!");
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Description: " + response.description());
            System.out.println("Request ID: " + response.requestId());

        } catch (ApiException e) {
            System.err.println("❌ Failed to send SMS: " + e.getMessage());
        }
    }

    private static void getAccountDetailsExample(StatumClient client) {
        System.out.println("\n=== Getting Account Details ===");
        try {
            AccountDetailsResponse account = client.getAccountService()
                    .getAccountDetails();

            System.out.println("✅ Account details retrieved!");
            System.out.println("Organization: " + account.organization().name());
            System.out.println("Balance: KES " + account.organization().details().availableBalance());
            System.out.println("Location: " + account.organization().details().location());
            System.out.println("Email: " + account.organization().details().officeEmail());
            System.out.println("Mobile: " + account.organization().details().officeMobile());

            System.out.println("\nService Accounts:");
            account.organization().accounts().forEach(serviceAccount -> {
                System.out.println("  - " + serviceAccount.serviceName() + ": " + serviceAccount.account());
            });

        } catch (ApiException e) {
            System.err.println("❌ Failed to get account details: " + e.getMessage());
        }
    }
}
