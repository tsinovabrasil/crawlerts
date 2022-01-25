package br.com.leodevelb.crawlerts.services;

import br.com.leodevelb.crawlerts.builders.WirelessAlvarionBuilder;
import br.com.leodevelb.crawlerts.models.Beat;
import br.com.leodevelb.crawlerts.models.Device;
import br.com.leodevelb.crawlerts.models.Output;
import br.com.leodevelb.crawlerts.utils.Util;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WirelessAlvarionService {

    private static final Logger LOGGER = Logger.getLogger(WirelessAlvarionService.class.getName());
    
    public static void execute(Device device, Output output, Beat beat) throws Exception {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "hdnAction=login&inpLogUser="
                + device.getUsername() + "&inpLogPass=" + device.getPassword());

        Request request = new Request.Builder()
                .url("http://" + device.getHost())
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        Response response = client.newCall(request).execute();

        String html = response.body().string();
        Document document = Jsoup.parse(html);
        
        WirelessAlvarionBuilder wirelessAlvarionBuilder = new WirelessAlvarionBuilder(document);
        
        sendWirelessLinks(device, output, beat, wirelessAlvarionBuilder);
        sendInterfaceStatistics(device, output, beat, wirelessAlvarionBuilder);

    }

    private static void sendWirelessLinks(Device device, Output output, Beat beat, WirelessAlvarionBuilder wirelessAlvarionBuilder) throws Exception {

        JSONObject jsonDefault = Util.buildJsonDefault(device, beat);
        JSONArray wirelessLinks = wirelessAlvarionBuilder.buildWirelessLinksStatistics();

        for (int i = 0; i < wirelessLinks.length(); i++) {

            JSONObject wirelessLinkJson = wirelessLinks.getJSONObject(i);

            JSONObject json = Util.getJson(jsonDefault, wirelessLinkJson);
            LOGGER.log(Level.INFO, Util.prettyPrinter(json));

            try (Socket socket = new Socket(output.getLogstash().getHost(), output.getLogstash().getPort());
                    DataOutputStream os = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {
                os.writeBytes(json.toString());
                os.flush();
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Failed to send for " + device.getHost(), ex);
            }

        }

    }

    private static void sendInterfaceStatistics(Device device, Output output, Beat beat, WirelessAlvarionBuilder wirelessAlvarionBuilder) throws Exception {

        JSONObject jsonDefault = Util.buildJsonDefault(device, beat);
        JSONArray interfaceStatistics = wirelessAlvarionBuilder.buildInterfacesStatistics();

        for (int i = 0; i < interfaceStatistics.length(); i++) {

            JSONObject interfaceStatisticsJson = interfaceStatistics.getJSONObject(i);
            
            JSONObject json = Util.getJson(jsonDefault, interfaceStatisticsJson);
            LOGGER.log(Level.INFO, Util.prettyPrinter(json));

            try (Socket socket = new Socket(output.getLogstash().getHost(), output.getLogstash().getPort());
                    DataOutputStream os = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {
                os.writeBytes(json.toString());
                os.flush();
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Failed to send for " + device.getHost(), ex);
            }

        }

    }

}
