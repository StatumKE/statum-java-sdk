/**
 * Statum Java SDK - Official Java client for the Statum API.
 *
 * <p>
 * This SDK provides a simple, type-safe interface for interacting with Statum
 * services
 * including Airtime, SMS, and Account Management.
 * </p>
 *
 * <h2>Quick Start</h2>
 * 
 * <pre>{@code
 * StatumConfig config = new StatumConfig("consumerKey", "consumerSecret");
 * StatumClient client = new StatumClient(config);
 *
 * // Send airtime
 * ApiResponse response = client.getAirtimeService().sendAirtime("254712345678", "100");
 * }</pre>
 *
 * <h2>Main Classes</h2>
 * <ul>
 * <li>{@link ke.co.statum.sdk.StatumClient} - Main entry point</li>
 * <li>{@link ke.co.statum.sdk.config.StatumConfig} - Configuration</li>
 * <li>{@link ke.co.statum.sdk.services.AirtimeService} - Airtime
 * operations</li>
 * <li>{@link ke.co.statum.sdk.services.SmsService} - SMS operations</li>
 * <li>{@link ke.co.statum.sdk.services.AccountService} - Account
 * operations</li>
 * </ul>
 *
 * @since 1.0.0
 * @see <a href="https://docs.statum.co.ke">Statum API Documentation</a>
 */
package ke.co.statum.sdk;
