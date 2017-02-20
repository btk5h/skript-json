# skript-json

> Idiomatic JSON integration for Skript. Convert JSON into list variables and vice versa.

## Syntax

### Effect `Map JSON to Variable`

Copies JSON formatted text into a list variable. Existing entries in the list variable are *not* 
deleted.

#### Syntax

`(map|copy) [the] json [(of|from)] %text% to [the] [var[iable]] %list variable%"`

---

### Expression `JSON Form` => `text`

Converts a list variable into JSON form.

#### Syntax

`[the] [serialized] json (form|representation) of %list variable%` or `%list variable%'[s] 
[serialized] json (form|representation)`

---

## Structure

If the following JSON were mapped to a variable `{json::*}`

```json
{
  "foo": 15,
  "bar": "test",
  "baz": {
    "foobar": [
      {
        "quux": false
      }
    ]
  }
}
```

the following assertions would be true:

```
{json::foo} is 15
{json::bar} is "test"
{json::baz} is true                    # special case
{json::baz::foobar} is true            # special case
{json::baz::foobar::1} is true         # special case
{json::baz::foobar::1::quux} is false
```

The variables marked `# special case` contain nested JSON objects and are true so Skript can 
properly loop through the variable. These variables (like `{json::baz}`, not list variables like 
`{json::baz::*}`) can be deleted and the structure will still properly serialize into JSON.

## Contributing

Feel free to submit pull requests, just make sure your changes are consistent with 
[Google's Java code style](https://google.github.io/styleguide/javaguide.html)!
