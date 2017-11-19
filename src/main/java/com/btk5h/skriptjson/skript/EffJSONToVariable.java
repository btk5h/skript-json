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

package com.btk5h.skriptjson.skript;

import com.btk5h.skriptjson.Serializers;
import com.btk5h.skriptjson.SkriptUtil;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.util.Locale;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;

@Name("Map JSON to Variable")
@Description("Copies JSON formatted text into a list variable. Existing entries in the list variable are not deleted." +
		"If the following JSON were mapped to a variable `{json::*}`" +
		"\n```json" +
		"\n{" +
		"\n  \"foo\": 15," +
		"\n  \"bar\": \"test\","
		"\n  \"baz\": {" +
		"\n    \"foobar\": [" +
		"\n      {" +
		"\n        \"quux\": false" +
		"\n      }" +
		"\n    ]" +
		"\n  }" +
		"\n}" +
		"\n```" +
		"\nthe following assertions would be true:" +
		"\n```" +
		"\n{json::foo} is 15" +
		"\n{json::bar} is \"test\"" +
		"\n{json::baz} is true                    # special case" +
		"\n{json::baz::foobar} is true            # special case" +
		"\n{json::baz::foobar::1} is true         # special case" +
		"\n{json::baz::foobar::1::quux} is false" +
		"\n```" +
		"\nThe variables marked `# special case` contain nested JSON objects and are true so Skript can properly loop through the variable. These variables (like `{json::baz}`, not list variables like `{json::baz::*}`) can be deleted and the structure will still properly serialize into JSON.")
@Examples({
		"set {_input} to \"{\"\"age\"\": 15, \"\"catchphrase\"\": \"\"heeyaw\"\", \"\"nicknames\"\": [\"\"ftaang\"\", \"\"bleh\"\"]}\"",
		"map json {_input} to {_json::*}"
})
@Since("1.0.0")
public class EffJSONToVariable extends Effect {

  static {
    Skript.registerEffect(EffJSONToVariable.class,
        "(map|copy) [the] json [(of|from)] %string% to [the] [var[iable]] %objects%");
  }

  private Expression<String> json;
  private VariableString var;
  private boolean isLocal;

  @Override
  protected void execute(Event e) {
    String json = this.json.getSingle(e);
    String var = this.var.toString(e).toLowerCase(Locale.ENGLISH);

    if (json == null) {
      return;
    }

    try {
      Object parsed = new JSONParser().parse(json);
      mapFirst(e, var.substring(0, var.length() - 3), parsed);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
  }

  private void setVariable(Event e, String name, Object obj) {
    Variables.setVariable(name.toLowerCase(Locale.ENGLISH), obj, e, isLocal);
  }

  private void mapFirst(Event e, String name, Object obj) {
    if (obj instanceof JSONObject) {
      handleObject(e, name, (JSONObject) obj);
    } else if (obj instanceof JSONArray) {
      handleArray(e, name, (JSONArray) obj);
    } else {
      setVariable(e, name, obj);
    }
  }

  private void map(Event e, String name, Object obj) {
    if (obj instanceof JSONObject) {
      if (((JSONObject) obj).containsKey("__javaclass__")
          || ((JSONObject) obj).containsKey("__skriptclass__")) {
        setVariable(e, name, Serializers.deserialize(((JSONObject) obj)));
      } else {
        setVariable(e, name, true);
        handleObject(e, name, (JSONObject) obj);
      }
    } else if (obj instanceof JSONArray) {
      setVariable(e, name, true);
      handleArray(e, name, (JSONArray) obj);
    } else {
      setVariable(e, name, obj);
    }
  }

  @SuppressWarnings("unchecked")
  private void handleObject(Event e, String name, JSONObject obj) {
    obj.keySet().forEach(key -> map(e, name + Variable.SEPARATOR + key, obj.get(key)));
  }

  @SuppressWarnings("unchecked")
  private void handleArray(Event e, String name, JSONArray obj) {
    for (int i = 0; i < obj.size(); i++) {
      map(e, name + Variable.SEPARATOR + (i + 1), obj.get(i));
    }
  }

  @Override
  public String toString(@Nullable Event e, boolean debug) {
    return json.toString(e, debug) + " => " + var.toString(e, debug);
  }

  @SuppressWarnings({"unchecked", "Duplicates"})
  @Override
  public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed,
                      SkriptParser.ParseResult parseResult) {
    json = (Expression<String>) exprs[0];
    Expression<?> expr = exprs[1];
    if (expr instanceof Variable) {
      Variable<?> varExpr = (Variable<?>) expr;
      if (varExpr.isList()) {
        var = SkriptUtil.getVariableName(varExpr);
        isLocal = varExpr.isLocal();
        return true;
      }
    }
    Skript.error(expr + " is not a list variable");
    return false;
  }
}
