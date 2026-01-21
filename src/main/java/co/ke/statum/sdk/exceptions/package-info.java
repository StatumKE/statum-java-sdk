/**
 * Exception classes for SDK error handling.
 *
 * <h2>Exception Hierarchy</h2>
 * 
 * <pre>
 * {@link co.ke.statum.sdk.exceptions.ApiException} (base)
 * ├── {@link co.ke.statum.sdk.exceptions.AuthenticationException} (HTTP 401)
 * ├── {@link co.ke.statum.sdk.exceptions.AuthorizationException} (HTTP 403)
 * ├── {@link co.ke.statum.sdk.exceptions.ValidationException} (HTTP 422)
 * └── {@link co.ke.statum.sdk.exceptions.NetworkException} (Network/timeout errors)
 * </pre>
 *
 * @since 1.0.0
 */
package co.ke.statum.sdk.exceptions;
