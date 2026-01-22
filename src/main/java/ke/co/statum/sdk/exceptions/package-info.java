/**
 * Exception classes for SDK error handling.
 *
 * <h2>Exception Hierarchy</h2>
 * 
 * <pre>
 * {@link ke.co.statum.sdk.exceptions.ApiException} (base)
 * ├── {@link ke.co.statum.sdk.exceptions.AuthenticationException} (HTTP 401)
 * ├── {@link ke.co.statum.sdk.exceptions.AuthorizationException} (HTTP 403)
 * ├── {@link ke.co.statum.sdk.exceptions.ValidationException} (HTTP 422)
 * └── {@link ke.co.statum.sdk.exceptions.NetworkException} (Network/timeout errors)
 * </pre>
 *
 * @since 1.0.0
 */
package ke.co.statum.sdk.exceptions;
