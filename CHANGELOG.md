# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2026-01-18

### Added
- Initial release of the Statum Java SDK.
- Support for Airtime API (`sendAirtime`).
- Support for SMS API (`sendSms`).
- Support for Account Details API (`getAccountDetails`).
- Exception handling for 401, 403, 422, and network errors.
- Thread-safe `StatumClient`.
- JUnit 5 tests.
