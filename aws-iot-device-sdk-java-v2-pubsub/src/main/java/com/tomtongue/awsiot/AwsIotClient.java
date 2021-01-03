package com.tomtongue.awsiot;

public interface AwsIotClient {
    void setClientId(String clientId);
    void setTopic(String topic);
    void setEndpoint(String endpoint);
    void setPort(int port);

    String getClientId();
    String getTopic();
    String getEndpoint();
    int getPort();
}
