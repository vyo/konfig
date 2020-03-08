# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.2.1] - 2017-06-20

### Fixed

 - Dropped the superfluous `main` function

## [0.2.0] - 2017-06-20

### Added

Configuration files can now be loaded from custom non-classpath locations by
specifying the environment variable `KONFIG_URL` and providing a valid URL
pointing to the external configuration file.

Supports the following protcols:
 - HTTP
 - HTTPS
 - FILE, with absolute and relative paths

## [0.1.0] - 2017-06-20

### Added

Inject type-safe configuration parameters by means of Kotlin's field delegation:
 - Type-safe configuration injection via `by Konfig` and `by Konfig.nested("key path")`
 - Configuration file parsing for `JSON`, `YML` and `properties` files
 - Configuration maps support both nested and flat (dot-notation) access
 - Configuration file loading for files on the classpath (`konfig.{json,yml,yaml,properties}`)

