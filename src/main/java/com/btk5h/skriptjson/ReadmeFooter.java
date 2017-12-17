/*
 * MIT License
 *
 * Copyright (c) 2017 Bryan Terce
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.btk5h.skriptjson;

/**
 * ## Structure
 *
 * If the following JSON were mapped to a variable `{json::*}`
 *
 * ```json
 * {
 *   "foo": 15,
 *   "bar": "test",
 *   "baz": {
 *     "foobar": [
 *       {
 *         "quux": false
 *       }
 *     ]
 *   }
 * }
 * ```
 *
 * the following assertions would be true:
 *
 * ```
 * {json::foo} is 15
 * {json::bar} is "test"
 * {json::baz} is true                    # special case
 * {json::baz::foobar} is true            # special case
 * {json::baz::foobar::1} is true         # special case
 * {json::baz::foobar::1::quux} is false
 * ```
 *
 * The variables marked `# special case` contain nested JSON objects and are true so Skript can
 * properly loop through the variable. These variables (like `{json::baz}`, not list variables like
 * `{json::baz::*}`) can be deleted and the structure will still properly serialize into JSON.
 *
 * ## Contributing
 *
 * Feel free to submit pull requests, just make sure your changes are consistent with
 * [Google's Java code style](https://google.github.io/styleguide/javaguide.html)!
 *
 * @skriptdoc
 * @index -1
 */
public interface ReadmeFooter {
}
