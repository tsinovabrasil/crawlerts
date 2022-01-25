package br.com.leodevelb.crawlerts.models;

public class Host {
    
    private String host;
    private int port;
    private String version;
    private String community;
    private String user;
    private String password;

    public Host(String host, int port, String version, String community, String user, String password) {
        this.host = host;
        this.port = port;
        this.version = version;
        this.community = community;
        this.user = user;
        this.password = password;
    }

    public Host() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Host{" + "host=" + host + ", port=" + port + ", version=" + version + ", community=" + community + ", user=" + user + ", password=" + password + '}';
    }
    
        
}