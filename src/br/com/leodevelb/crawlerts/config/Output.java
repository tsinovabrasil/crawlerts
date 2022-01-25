package br.com.leodevelb.crawlerts.config;

public class Output {

    private OutputLogstash logstash;

    public Output() {
    }        

    public OutputLogstash getLogstash() {
        return logstash;
    }

    public void setLogstash(OutputLogstash logstash) {
        this.logstash = logstash;
    }    
    
}
