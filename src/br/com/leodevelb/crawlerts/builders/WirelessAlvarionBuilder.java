package br.com.leodevelb.crawlerts.builders;

import java.math.BigDecimal;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WirelessAlvarionBuilder {

    private final Document doc;

    public WirelessAlvarionBuilder(Document doc) {
        this.doc = doc;
    }

    public JSONArray buildInterfacesStatistics() throws JSONException {

        JSONArray list = new JSONArray();

        Element tableInterfaces = doc.getElementById("tbInterfaceStatistics");
        Elements rowsInterfaces = tableInterfaces.child(0).children();

        for (int i = 1; i < rowsInterfaces.size(); i++) {

            Element row = rowsInterfaces.get(i);
            Elements columns = row.children();

            JSONObject obj = new JSONObject();

            String nameInterface = columns.get(0).text();
            String macAddress = columns.get(1).text();
            String status = columns.get(2).text();
            String mode = columns.get(3).text();
            Object rxPackets = parseDouble(columns.get(4).text());
            Object rxErrors = parseDouble(columns.get(5).text());
            Object txPackets = parseDouble(columns.get(6).text());
            Object txErrors = parseDouble(columns.get(7).text());

            obj.put("name_interface", nameInterface);
            obj.put("mac_address", macAddress);
            obj.put("status", status);
            obj.put("mode", mode);
            obj.put("rx_packets", rxPackets);
            obj.put("rx_errors", rxErrors);
            obj.put("tx_packets", txPackets);
            obj.put("tx_errors", txErrors);
            obj.put("type", "InterfaceStatistics");

            list.put(obj);

            JSONObject objLast = new JSONObject(obj.toString());
            objLast.put("type", "LatestInterfaceStatistics");
            objLast.put("id_doc", "interface_statistics_" + macAddress);
            
            list.put(objLast);
            
        }

        return list;

    }   

    public JSONArray buildWirelessLinksStatistics() throws JSONException {

        JSONArray list = new JSONArray();

        Element tableInterfaces = doc.getElementById("tblActiveNeighbors_rf5.0");
        Elements rowsInterfaces = tableInterfaces.child(0).children();

        for (int i = 1; i < rowsInterfaces.size(); i++) {

            Element row = rowsInterfaces.get(i);
            Elements columns = row.children();

            JSONObject obj = new JSONObject();
            
            int qualityLink = getQualityLink(columns.get(0));
            String neighbor = columns.get(1).text();
            String macAddress = columns.get(2).text();
            Object distanceKm = parseDouble(columns.get(3).text());
            String transmitPowerRxTx = columns.get(4).text();
            String controlLevelRxTx = columns.get(5).text();
            String currentLevelRxTx = columns.get(6).text();
            String bitrateRxTx = columns.get(7).text();
            String retriesPctRxTx = columns.get(8).text();
            String errorsPctRxTx = columns.get(9).text();
            String loadKbpsRxTx = columns.get(10).text();
            String loadPpsRxTx = columns.get(11).text();

            obj.put("qualityLink", qualityLink);
            obj.put("neighbor", neighbor);
            obj.put("mac_address", macAddress);
            obj.put("distance_km", distanceKm);
            obj.put("transmit_power_rx_tx", transmitPowerRxTx);
            obj.put("control_level_rx_tx", controlLevelRxTx);
            obj.put("current_level_rx_tx", currentLevelRxTx);
            obj.put("bitrate_rx_tx", bitrateRxTx);
            obj.put("retries_pct_rx_tx", retriesPctRxTx);
            obj.put("errors_pct_rx_tx", errorsPctRxTx);
            obj.put("load_kbps_rx_tx", loadKbpsRxTx);
            obj.put("load_pps_rx_tx", loadPpsRxTx);
            obj.put("type", "WirelessLink");

            list.put(obj);
            
            JSONObject objLast = new JSONObject(obj.toString());
            objLast.put("type", "LatestWirelessLink");
            objLast.put("id_doc", "wireless_link_statistics_" + macAddress);            
            
            list.put(objLast);
            

        }

        return list;

    }    

    private Object parseDouble(String value){
        if (value == null){
            return JSONObject.NULL;
        }
        if (value.trim().isEmpty()){
            return JSONObject.NULL;
        }
        return new BigDecimal(value);
    }    
    
    private int getQualityLink(Element element) {
        String qualityLinkstyle = element.html();
        String regex = ".*qualityMark.*background-color:(.*)\".*";
        String qualityLink = qualityLinkstyle.replaceAll(regex, "$1");
        if (qualityLink.equalsIgnoreCase("green")){
            return 1;
        }
        if (qualityLink.equalsIgnoreCase("yellow")){
            return 2;
        }
        return 3;
    }

}
