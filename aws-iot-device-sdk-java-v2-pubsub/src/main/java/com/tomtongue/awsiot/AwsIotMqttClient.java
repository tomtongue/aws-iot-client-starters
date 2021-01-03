package com.tomtongue.awsiot;

public abstract class AwsIotMqttClient implements AwsIotClient {
    String clientId;
    String topic;
    String rootCaPath;
    String certPath;
    String privKeyPath;
    String endpoint;
    int port;

    // Setter
    public void setClientId(String clientId) { this.clientId = clientId; }
    public void setTopic(String topic) { this.topic = topic; }
    public void setRootCaPath(String rootCaPath) { this.rootCaPath = rootCaPath; }
    public void setCertPath(String certPath) { this.certPath = certPath; }
    public void setPrivKeyPath(String privKeyPath) { this.privKeyPath = privKeyPath; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public void setPort(int port){ this.port = port; }

    // Getter
    public String getClientId(){ return this.clientId; }
    public String getTopic(){ return this.topic; }
    public String getRootCaPath(){ return this.rootCaPath; }
    public String getCertPath(){ return this.certPath; }
    public String getPrivKeyPath(){ return this.privKeyPath; }
    public String getEndpoint(){ return this.endpoint; }
    public int getPort(){ return this.port; }
}
