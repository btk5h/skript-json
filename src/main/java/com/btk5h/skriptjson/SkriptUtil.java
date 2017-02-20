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

import java.lang.reflect.Field;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Variable;
import ch.njol.skript.lang.VariableString;

public class SkriptUtil {
  static {
    Field _VARIABLE_NAME = null;
    try {
      _VARIABLE_NAME = Variable.class.getDeclaredField("name");
      _VARIABLE_NAME.setAccessible(true);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
      Skript.error("Skript's 'variable name' method could not be resolved.");
    }
    VARIABLE_NAME = _VARIABLE_NAME;
  }

  private static final Field VARIABLE_NAME;

  public static VariableString getVariableName(Variable<?> var) {
    try {
      return (VariableString) VARIABLE_NAME.get(var);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }
}
