package com.tomtongue.awsiot;

import software.amazon.awssdk.crt.CRT;
import software.amazon.awssdk.crt.CrtRuntimeException;
import software.amazon.awssdk.crt.io.ClientBootstrap;
import software.amazon.awssdk.crt.io.EventLoopGroup;
import software.amazon.awssdk.crt.io.HostResolver;
import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.MqttClientConnectionEvents;
import software.amazon.awssdk.crt.mqtt.MqttMessage;
import software.amazon.awssdk.crt.mqtt.QualityOfService;
import software.amazon.awssdk.iot.AwsIotMqttConnectionBuilder;

import com.google.gson.*;

import java.time.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class Publisher extends AwsIotMqttClient {
    private String message;

    private AwsIotMqttConnectionBuilder builder;
    private MqttClientConnection publisher;
    private boolean sessionPresent;


    public Publisher(String rootCaPath, String certPath, String privKeyPath, String endpoint, int port) {
        this.rootCaPath = rootCaPath;
        this.certPath = certPath;
        this.privKeyPath = privKeyPath;
        this.endpoint = endpoint;
        this.port = port;
    }


    public void setClientId(String clientId) {
        if(clientId.isEmpty()) {
            throw new IllegalArgumentException("You need to specify clientId, not null");
        }
        this.clientId = clientId;
    }
    public String getClientId() { return this.clientId; }
    public void setTopic(String topic) {
        if(topic.isEmpty()) {
            throw new IllegalArgumentException("You need to specify topic, not null");
        }
        this.topic = topic;
    }
    public String getTopic() { return this.topic; }
    public void setMessage(Map<String, Object> metrics) {
        if(metrics.isEmpty()) {
            throw new IllegalArgumentException("You need to specify message, not null");
        }
        Gson gson = new Gson();
        long ts = Instant.now().getEpochSecond();
        Map<String, Object> payload = new LinkedHashMap<>();

        try {
            payload.put("clientId", this.clientId);
            payload.put("timestamp", ts);
            payload.putAll(metrics);
            this.message = gson.toJson(payload);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String getMessage() { return this.message; }

    public void setBuilder() {
        MqttClientConnectionEvents callbacks = new MqttClientConnectionEvents() {
            @Override
            public void onConnectionInterrupted(int errorCode) {
                if (errorCode != 0) {
                    System.out.println("Connection interrupted: " + errorCode + ": " + CRT.awsErrorString(errorCode));
                }
            }

            @Override
            public void onConnectionResumed(boolean sessionPresent) {
                System.out.println("Connection resumed: " + (sessionPresent ? "existing session" : "clean session"));
            }
        };

        EventLoopGroup eventLoopGroup = new EventLoopGroup(1);
        HostResolver resolver = new HostResolver(eventLoopGroup);
        ClientBootstrap clientBootstrap = new ClientBootstrap(eventLoopGroup, resolver);
        AwsIotMqttConnectionBuilder builder = AwsIotMqttConnectionBuilder.newMtlsBuilderFromPath(this.certPath, this.privKeyPath);
        this.builder = builder.withBootstrap(clientBootstrap)
                .withConnectionEventCallbacks(callbacks)
                .withClientId(this.clientId)
                .withEndpoint(this.endpoint)
                .withCleanSession(true);
    }

    public AwsIotMqttConnectionBuilder getBuilder() {
        return this.builder;
    }

    public void setPublisher(AwsIotMqttConnectionBuilder builder) {
        this.publisher = builder.build();
    }

    public MqttClientConnection getPublisher() {
        return this.publisher;
    }

    // Connect operation
    public void connect(MqttClientConnection publisher) {
        try {
            CompletableFuture<Boolean> connected = publisher.connect();
            this.sessionPresent = connected.get();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    public boolean getSessionPresent() {
        // TODO: check about client's connection
        return this.sessionPresent;
    }


    // Publish operation
    public void publish(MqttClientConnection publisher, String message) {
        try {
            CompletableFuture<Integer> publish = publisher.publish(
                    new MqttMessage(this.topic, message.getBytes()),
                    QualityOfService.AT_MOST_ONCE, // QoS = 0
                    false);
            System.out.println(message); // TODO: change it to logger
            publish.get();
        } catch(CrtRuntimeException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    public void disconnect(MqttClientConnection publisher) {
        try {
            CompletableFuture<Void> disconnected = publisher.disconnect();
            disconnected.get();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
