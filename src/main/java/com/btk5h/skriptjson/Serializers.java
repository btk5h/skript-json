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

import org.json.simple.JSONObject;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import ch.njol.skript.registrations.Classes;
import ch.njol.skript.variables.SerializedVariable;

public class Serializers {
  private static Map<String, Serializer> serializers = new HashMap<>();

  private static class Serializer {
    Function<Object, JSONObject> serializer;
    Function<JSONObject, Object> deserializer;

    Serializer(Function<Object, JSONObject> serializer, Function<JSONObject, Object> deserializer) {
      this.serializer = serializer;
      this.deserializer = deserializer;
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> void register(Class<T> cls, Function<T, JSONObject> serializer,
                                  Function<JSONObject, T> deserializer) {
    serializers.put(cls.getName(), new Serializer((Function<Object, JSONObject>) serializer,
        (Function<JSONObject, Object>) deserializer));
  }

  @SuppressWarnings("unchecked")
  public static JSONObject serialize(Object o) {
    JSONObject obj;
    String cls = o.getClass().getName();

    if (serializers.containsKey(cls)) {
      obj = serializers.get(cls).serializer.apply(o);
      obj.put("__javaclass__", cls);
    } else {
      obj = new JSONObject();
      SerializedVariable.Value value = Classes.serialize(o);

      if (value == null) {
        return null;
      }

      obj.put("__skriptclass__", value.type);
      obj.put("value", Base64.getEncoder().encodeToString(value.data));
    }

    return obj;
  }

  public static Object deserialize(JSONObject obj) {
    String cls = (String) obj.get("__javaclass__");

    if (cls != null && serializers.containsKey(cls)) {
      return serializers.get(cls).deserializer.apply(obj);
    } else {
      String type = (String) obj.get("__skriptclass__");
      String content = (String) obj.get("value");

      if (type == null || content == null) {
        return null;
      }

      return Classes.deserialize(type, Base64.getDecoder().decode(content));
    }
  }
}
