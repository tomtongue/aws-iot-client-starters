package com.tomtongue.awsiot;

import com.tomtongue.awsiot.metrics.SystemMetricsGetter;
import software.amazon.awssdk.crt.mqtt.MqttClientConnection;

public class Run {
    public static void main(String[] args) {
        ArgParser argParser = new ArgParser();
        argParser.argParse(args);

        Publisher publisher = new Publisher(
                argParser.getRootCaPath(), // rootca,
                argParser.getCertPath(), // ca
                argParser.getPrivKeyPath(), // privkey
                argParser.getEndpoint(), // ATS endpoint
                argParser.getPort());
        publisher.setClientId(argParser.getClientId());
        publisher.setTopic("sys/metrics/" + argParser.getClientId()); // Topic is specified based on clientId
        String info = String.format(
                "[INFO] RootCAPATH: %s, CertPATH: %s,PrivKeyPATH: %s, Endpoint: %s, Port: %s, ClientId: %s, Topic: %s",
                publisher.rootCaPath,
                publisher.privKeyPath,
                publisher.certPath,
                publisher.endpoint,
                publisher.port,
                publisher.getClientId(),
                publisher.getTopic());
        System.out.println(info); // TODO: Change log format

        // Initial setup for client connection
        publisher.setBuilder();
        publisher.setPublisher(publisher.getBuilder());

        MqttClientConnection clientConnection = publisher.getPublisher();
        publisher.connect(clientConnection);
        try {
            while(true) {
                if(publisher.getSessionPresent()) {
                    publisher.connect(clientConnection);
                }
                try {
                    // System.out.println(publisher.getSessionPresent()); // TODO: change it to logger
                    publisher.setMessage(SystemMetricsGetter.getUsage());
                    publisher.publish(clientConnection, publisher.getMessage());
                    Thread.sleep(1000);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            publisher.disconnect(clientConnection);
        }
    }
}
