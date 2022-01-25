package br.com.leodevelb.crawlerts;

import br.com.leodevelb.crawlerts.config.Output;
import br.com.leodevelb.crawlerts.config.Beat;
import br.com.leodevelb.crawlerts.config.Device;
import br.com.leodevelb.crawlerts.services.WirelessAlvarionService;
import br.com.leodevelb.crawlerts.utils.Ping;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadSend extends Thread {

    private static final Logger LOGGER = Logger.getLogger(ReadSend.class.getName());

    public final Device device;
    private final Output output;
    private final Beat beat;

    private boolean run;

    public ReadSend(Device device, Output output, Beat beat) {
        this.device = device;
        this.output = output;
        this.run = true;
        this.beat = beat;
    }

    private boolean ping() {

        int retry = 5;
        int delay = 1000;
        int x = 0;
        boolean ping = false;

        while (x < retry) {

            x += 1;

            String ip = device.getHost();

            LOGGER.log(Level.INFO, "Ping {0}", ip);

            if (!Ping.ping(ip)) {
                LOGGER.log(Level.WARNING, "Device {0} não encontrado", ip);
                ping = false;
                try {
                    Thread.sleep(delay);
                } catch (Exception ex) {
                }
                continue;
            }

            LOGGER.log(Level.INFO, "Device {0} encontrado", ip);
            ping = true;

            break;

        }

        return ping;

    }

    @Override
    public void run() {

        while (run) {

            try {

                if (!ping()) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                    }
                    continue;
                }

                if (device.isWirelessAlvarion()) {
                    WirelessAlvarionService.execute(device, output, beat);
                }

            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Falha ao enviar dados do device " + device.getHost(), ex);
            }

            // Intervalo de espera
            try {
                Thread.sleep(device.getInterval() * 1000);
            } catch (InterruptedException ex) {
            }

        }

    }

    public void close() {
        this.run = false;
    }

    //File file = Paths.get("D:\\teste.html").toFile();
    //Document document = Jsoup.parse(file, "UTF-8");
}
