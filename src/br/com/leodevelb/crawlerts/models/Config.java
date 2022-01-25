package br.com.leodevelb.crawlerts.models;

import java.util.List;

public class Config {
    
    private Output output;
    private Beat beat;
    private List<Device> devices;

    public Output getOutput() {
        return output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    public Beat getBeat() {
        return beat;
    }

    public void setBeat(Beat beat) {
        this.beat = beat;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }   
    
}
