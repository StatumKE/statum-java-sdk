# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.3] - 2026-01-22

### Fixed
- Restored stable test mocking logic in `HttpClientProviderTest` and `HttpClientProviderBugFixTest` to prevent `NullPointerException` during test execution.

## [1.0.2] - 2026-01-22

### Added
- Regression tests for 422 error handling with mixed JSON types.
- Regression tests for `AccountDetailsResponse` JSON mapping.

### Fixed
- Robust error handling for 422 Unprocessable Entity responses. The SDK now safely parses validation errors even when mixed with other data types (e.g., integers) or nested under a `validation_errors` key.
- Fixed `ClassCastException` in `HttpClientProvider.handleError`.
- Updated documentation (README) to use the correct package coordinates `ke.co.statum`.

## [1.0.1] - 2026-01-22

### Changed
- Refactored `groupId` and package structure from `co.ke.statum` to `ke.co.statum` to align with Maven Central requirements.
- Updated project metadata and CI workflows.

## [1.0.0] - 2026-01-18

### Added
- Initial release of the Statum Java SDK.
- Support for Airtime API (`sendAirtime`).
- Support for SMS API (`sendSms`).
- Support for Account Details API (`getAccountDetails`).
- Exception handling for 401, 403, 422, and network errors.
- Thread-safe `StatumClient`.
- JUnit 5 tests.
