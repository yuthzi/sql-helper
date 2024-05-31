package com.yuth.sql.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * json转ts
 * 
 * @author yuth
 * @since 2024-05-06 17:27:34
 */
public class Json2TSHelper {

    private static final String sep = "\n";


    public static void arrayToTs(String path) throws IOException {
        byte[] bs = Files.readAllBytes(Paths.get(path));
        List<JSONObject> arr = JSON.parseArray(new String(bs), JSONObject.class);
        StringBuilder s = new StringBuilder();
        s.append("export const data = ").append(sep);
        toTs(s, arr, 0);

        System.out.println(s.toString());
    }

    @SuppressWarnings("unchecked")
    private static void toTs(StringBuilder s, Object v, int level) {

        if (v instanceof Map) {
            indent(s, level);
            s.append("{").append(sep);
            for (Entry<String, Object> etr : ((JSONObject) v).entrySet()) {
                indent(s, level + 1);

                s.append(etr.getKey()).append(": ");
                toTs(s, etr.getValue(), 0);
                s.append(',');
                s.append(sep);
            }
            indent(s, level);
            s.append("}");
        } else if (v instanceof List) {
            indent(s, level);
            s.append("[").append(sep);
            List<Object> arr = (List<Object>) v;
            for (int i = 0; i < arr.size(); ++i) {
                toTs(s, arr.get(i), level + 1);
                s.append(",");

                s.append(sep);
            }

            indent(s, level);
            s.append("]");
        } else if (v instanceof String) {
            s.append("\'").append(v).append("\'");
        } else {
            s.append(v);
        }

    }

    private static void indent(StringBuilder s, int level) {
        for (int i = 0; i < level; i++) {
            s.append('\t');
        }
    }

    public static void main(String[] args) throws IOException {
        arrayToTs("./data/json.json");
    }
}
