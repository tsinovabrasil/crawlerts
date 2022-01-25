package br.com.leodevelb.crawlerts.utils;

import br.com.leodevelb.crawlerts.models.Beat;
import br.com.leodevelb.crawlerts.models.Device;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Util {

    public static final String NAME_DOC_ID = "id_doc";
    private static final SimpleDateFormat DATE_FORMAT_US = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss.000");

    public static String getDatetimeForElasticsearch(Date date) {
        String dateFormated = DATE_FORMAT_US.format(date) + "T" + TIME_FORMAT.format(date) + "Z";
        return dateFormated;
    }

    public static String getDatetimeNowForElasticsearch() {
        return Instant.now().toString();
    }

    public static int getSecondsByPeriod(Date dStart, Date dEnd) {
        DateTime start = new DateTime(dStart);
        DateTime end = new DateTime(dEnd);
        int seconds = Seconds.secondsBetween(start, end).getSeconds();
        return seconds;
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);

        return bd.doubleValue();

    }

    public static Object executeScript(String script, Object value) throws Exception {

        Binding binding = new Binding();
        GroovyShell shell = new GroovyShell(binding);

        Object valueScript;

        if (value instanceof String) {

            valueScript = "\"" + value.toString() + "\"";

        } else if (value instanceof Integer || value instanceof Double || value instanceof Float || value instanceof Long) {

            valueScript = value;

        } else {
            throw new Exception("only strings, integers, doubles, floats, and longs are accepted");

        }

        script = script.replace("{{value}}", valueScript.toString());

        Object response = shell.evaluate(script);

        return response;

    }

    public static String buildId(String id, JSONObject json) throws Exception {

        id = getIdDoc(id, json);

        Iterator it = json.keys();

        while (it.hasNext()) {

            String key = it.next().toString();
            Object value = json.get(key);

            if (value instanceof JSONObject) {
                id = buildId(id, (JSONObject) value);
            }

        }

        return id;

    }

    private static String getIdDoc(String idDocActionUpdateLast, JSONObject document) throws Exception {

        Iterator it = document.keys();

        while (it.hasNext()) {

            String key = it.next().toString();
            Object value = document.get(key);

            if (idDocActionUpdateLast.contains("{" + key + "}")) {
                idDocActionUpdateLast = idDocActionUpdateLast.replace("{" + key + "}", (value + ""));
            }

        }

        return idDocActionUpdateLast;

    }

    public static String prettyPrinter(String value) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    mapper.readValue(value, Object.class));
        } catch (Exception ex) {
            return value;
        }
    }

    public static String prettyPrinter(JSONObject value) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    mapper.readValue(value.toString(), Object.class));
        } catch (Exception ex) {
            return value.toString();
        }
    }

    public static String prettyPrinter(Object value) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    value);
        } catch (Exception ex) {
            return value.toString();
        }
    }

    public static String fromHex(String s) {
        try {
            byte bs[] = new byte[s.length() / 2];
            for (int i = 0; i < s.length(); i += 2) {
                bs[i / 2] = (byte) Integer.parseInt(s.substring(i, i + 2), 16);
            }
            return new String(bs, StandardCharsets.ISO_8859_1);
        } catch (Exception ex) {
            return "";
        }
    }

    public static void addAppendFields(JSONObject obj, Device nobreak) {

        Iterator<Map.Entry<String, JsonNode>> it;

        if (nobreak != null && nobreak.getAppendFields() != null) {

            it = nobreak.getAppendFields().fields();

            while (it.hasNext()) {
                Map.Entry<String, JsonNode> node = it.next();
                try {
                    if (null == node.getValue().getNodeType()) {
                        obj.put(node.getKey(), node.getValue());
                    } else {
                        switch (node.getValue().getNodeType()) {
                            case OBJECT:
                                obj.put(node.getKey(), new JSONObject(node.getValue().toString()));
                                break;
                            case ARRAY:
                                obj.put(node.getKey(), new JSONArray(node.getValue().toString()));
                                break;
                            case BOOLEAN:
                                obj.put(node.getKey(), node.getValue().asBoolean());
                                break;
                            case NULL:
                                obj.put(node.getKey(), JSONObject.NULL);
                                break;
                            case NUMBER:
                                obj.put(node.getKey(), node.getValue().asDouble());
                                break;
                            case STRING:
                                obj.put(node.getKey(), node.getValue().asText());
                                break;
                            default:
                                obj.put(node.getKey(), node.getValue());
                                break;
                        }
                    }
                } catch (JSONException ex) {
                }
            }

        }

    }
    
    public static JSONObject getJson(JSONObject jsonValuesDefault, JSONObject jsonValues) throws JSONException {

        JSONObject json = new JSONObject();

        Iterator it = jsonValuesDefault.keys();
        while (it.hasNext()) {
            String key = it.next().toString();
            json.put(key, jsonValuesDefault.get(key));
        }

        it = jsonValues.keys();
        while (it.hasNext()) {
            String key = it.next().toString();
            json.put(key, jsonValues.get(key));
        }

        return json;
    }
    
    public static JSONObject buildJsonDefault(Device device, Beat beat) {

        JSONObject jsonDefault = new JSONObject();

        try {

            JSONObject jsonDevice = new JSONObject();
            jsonDevice.put("ip", device.getHost());
            jsonDefault.put("device", jsonDevice);

            jsonDefault.put("timestamp", Util.getDatetimeNowForElasticsearch());

            JSONObject jsonMetadata = new JSONObject();
            jsonMetadata.put("beat", beat.getName());
            jsonMetadata.put("version", beat.getVersion());
            jsonDefault.put("@metadata", jsonMetadata);

            JSONObject jsonBeat = new JSONObject();
            jsonBeat.put("name", beat.getName());
            jsonBeat.put("version", beat.getVersion());
            jsonDefault.put("beat", jsonBeat);

            jsonDefault.put("tags", beat.getTags());

            jsonDefault.put("client_name", beat.getClientName());
            jsonDefault.put("client_id", beat.getClientId());
            
            jsonDefault.put("manufacturer", device.getManufacturer());
            jsonDefault.put("category", device.getCategory());

            Util.addAppendFields(jsonDefault, device);

        } catch (JSONException ex) {
        }

        return jsonDefault;

    }

}
