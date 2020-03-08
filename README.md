Konfig
========================================

Small and simple configuration injection for Kotlin.
Inspired by Spring's `@Value` but more explicit and idiomatic.

Also, easy as pie and a tad opinionated.

## Usage

`Konfig` allows injecting configuration file content as a tree-like `Map`.

`Konfig` is a globally accessible, immutable singleton `object` implementing
the `Map`-interface and can be used as such. It is initialised on startup,
any (meta) configuration has to be done via providing environment variables.

The contents of the `Konfig` map support access via either flat dot-notation,
as used by `properties` files, as well as regular tree-like nesting access.

### Configuration file format

`Konfig` supports the following file formats:
  - `YML`
  - `JSON`
  - `Properties`

### Configuration file location

By default `Konfig` loads a `konfig.{yml,json,properties}` file from the classpath,
if present. If none is present initialisation will fail with an `Error`.

Configuration files can also be loaded from custom non-classpath locations by
specifying the environment variable `KONFIG_URL` and providing a valid URL
pointing to the external configuration file.

This mechanism supports the following protcols:
 - `HTTP`
 - `HTTPS`
 - `FILE`, with absolute and relative paths

### Flat structure

Provided one of the following is on the classpath:

`konfig.yml`

```yml
string: A string
int: 9000
double: 0.5
```

`konfig.json`

```json
{
  "string": "A string",
  "int": 9000,
  "double": 0.5
}
```

`konfig.properties`

```properties
string=A string
int=9000
double=0.5
```

Inject the parameters by way of Kotlin field delegation:

```kotlin
class Service {
    val string: String by Konfig
    val int: Int by Konfig
    val double: Double by Konfig
}
```

**Note**: This follows the same pattern of delegation as via a `Map<String, Any>`,
i.e. you have to make sure the field name and the configuration key match.

If you want to use a different field name, use `Konfig.nested("key path")` instead -
you are free to choose your field name as you wish in this case.

### Nested structure

To inject a nested, i.e. not top-level, element of your configuration,
specify the key-path to it via:

```kotlin
Konfig.nested("path.to.subtree")
```

This can even be a whole subtree, and can be used to avoid one of Spring's `@Value`
shortcomings; instead of injecting each respective value by specifying its full
key-path prefix for every single one, inject the prefix element as a `NestedKonfig` or
`Map<String, Any>`, and delegate injection to the prefix element:

`konfig.yml` (`YML` only for brevity)

```yml
complex:
  multilevel:
    structure:
      int: 9000
      double: 0.5
```

And use it like this:

```kotlin
class Service {
    val nested: NestedKonfig by Konfig.nested("complex.multilevel.structure")
    val int: Int by nested
    val double: Double by nested
}
```

Note the absence of duplicated `Konfig.nested("complex.multilevel.structure")` prefix
declarations.

### Supported types

Some of Kotlin's primitive types:
 - Boolean
 - Int
 - Double
 - String

And two collection types:
 - List<T> (where T is one of the valid primitives or collection types; `YML` and `JSON` only)
 - Map<String, T> (where T is one of the valid primitives or collections types)

### Lazy loading

**Note**: As per usual with field delegation, loading and thus also type checking
occurs lazily on first access of the field.

### Default config file precedence

Since configuration is not merged from multiple files, make sure to only
provide one configuration file format at once.

Default (classpath) configuration files are looked up in the following order:
 - YML
 - JSON
 - Properties

## Maven Repo

```xml
<dependency>
  <groupId>io.github.vyo.konfig</groupId>
  <artifactId>konfig</artifactId>
  <version>0.2.1</version>
</dependency>
```

## Dependencies

 - Jackson, plus its Kotlin module
 - Snake YAML

