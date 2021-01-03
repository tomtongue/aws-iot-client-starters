package com.tomtongue.awsiot;

// Ref: https://github.com/aws/aws-iot-device-sdk-java-v2/blob/master/samples/BasicPubSub/src/main/java/pubsub/PubSub.java
public class ArgParser extends AwsIotMqttClient {
    public void argParse(String[] args) {
        for(int i = 0; i < args.length; ++i) {
            System.out.println(args[i]);
            switch(args[i]) {
                case "--id":
                    if(i + 1 < args.length) {
                        this.clientId = args[++i];
                    }
                    break;
                case "--rootca":
                    if(i + 1 < args.length) {
                        this.rootCaPath = args[++i];
                    }
                    break;
                case "--cert":
                    if(i + 1 < args.length) {
                        this.certPath = args[++i];
                    }
                    break;
                case "--key":
                    if(i + 1 < args.length) {
                        this.privKeyPath = args[++i];
                    }
                    break;
                case "--endpoint":
                    if(i + 1 < args.length) {
                        this.endpoint = args[++i];
                    }
                    break;
                case "--port":
                    if(i + 1 < args.length) {
                        this.port = Integer.parseInt(args[++i]);
                    }
                    break;
                default:
                    System.out.println("Unknown argument. Please specify" + printUsage());
            }
        }
    }

    public static String printUsage() {
        return "Usage:\n"+
                "  --id    Client ID to use when connecting\n"+
                "  --rootca   Path to the root certificate\n"+
                "  --cert     Path to the IoT thing certificate\n"+
                "  --key      Path to the IoT thing private key\n"+
                "  --endpoint AWS IoT service endpoint hostname\n"+
                "  --port     Port to connect to on the endpoint\n";
    }
}
