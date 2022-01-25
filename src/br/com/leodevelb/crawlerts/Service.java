package br.com.leodevelb.crawlerts;

import br.com.leodevelb.crawlerts.models.Device;
import br.com.leodevelb.crawlerts.models.Config;
import br.com.leodevelb.crawlerts.utils.Util;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Service extends Thread {

    private static final Logger LOGGER = Logger.getLogger(Service.class.getName());

    private final List<ReadSend> listReadSends;
    private Config config;
    private final String[] args;

    public Service(String[] args) {
        this.args = args;
        listReadSends = new ArrayList<>();
    }

    private boolean readConfigFile() {

        String pathFile = null;

        for (String arg : args) {
            String parts[] = arg.split(":");
            if (parts[0].equalsIgnoreCase("-file")) {
                pathFile = parts[1];
            }
        }

        if (pathFile == null) {
            pathFile = "crawlerts.json";
        }

        LOGGER.log(Level.INFO, "Efetuando leitura do arquivo de configuração {0}", pathFile);

        try (InputStream inputStream = new FileInputStream(Paths.get(pathFile).toFile())) {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            config = objectMapper.readValue(inputStream, Config.class);

            LOGGER.log(Level.INFO, Util.prettyPrinter(config));
            LOGGER.log(Level.INFO, "Leitura efetuada com sucesso");

            return true;

        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Falha ao efetuar leitura do arquivo de configuração", ex);
            return false;

        }

    }

    @Override
    public void run() {

        if (!readConfigFile()) {
            return;
        }

        for (Device device : config.getDevices()) {

            ReadSend readSend = new ReadSend(device, config.getOutput(), config.getBeat());
            readSend.start();
            listReadSends.add(readSend);

            LOGGER.log(Level.INFO, "Processo de leitura do device {0} iniciado", device.getHost());

        }

    }

    public static void main(String[] args) {
        Service service = new Service(args);
        service.start();
    }

}
