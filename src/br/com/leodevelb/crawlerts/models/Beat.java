package br.com.leodevelb.crawlerts.models;

import java.util.List;

public class Beat {
    
    private String name;
    private String version;
    private List<String> tags;  
    private String passwordSudo;
    private String clientName;
    private Integer clientId;
    private String elasticsearchHost;

    public Beat() {
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getPasswordSudo() {
        return passwordSudo;
    }

    public void setPasswordSudo(String passwordSudo) {
        this.passwordSudo = passwordSudo;
    }    

    public String getElasticsearchHost() {
        return elasticsearchHost;
    }

    public void setElasticsearchHost(String elasticsearchHost) {
        this.elasticsearchHost = elasticsearchHost;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }
    
}
