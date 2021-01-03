# AWSIoTSystemMetricsPublisher

```text
[Publisher] -> [IoT Core] -(Rule)-> [KDS] -> [Spark Streaming] -> ...
```


## Requirement

```
$ java -version
java version "1.8.0_231"
Java(TM) SE Runtime Environment (build 1.8.0_231-b11)
Java HotSpot(TM) 64-Bit Server VM (build 25.231-b11, mixed mode)
$ mvn -version
Apache Maven 3.6.3 (cecedd343002696d0abb50b32b541b8a6ba2883f)
Maven home: /opt/apache-maven-3.6.3
Java version: 1.8.0_231, vendor: Oracle Corporation, runtime: /Library/Java/JavaVirtualMachines/jdk1.8.0_231.jdk/Contents/Home/jre
Default locale: en_IE, platform encoding: UTF-8
OS name: "mac os x", version: "10.14.6", arch: "x86_64", family: "mac"
```


## How to run the command

Run the following command on your instance:

```text
nohup java -cp ~/AWSIoTSystemMetricsPublisher.jar com.tomtongue.iot.Run \
--id <INSTANCE_ID>
--rootca ./certs/AmazonRootCA1.pem \
--cert ./certs/<CACERT> \
--key ./certs/<PRIVKEY> \
--endpoint <ATS Endpoint> \
--port 8883 >> /dev/null &
```

Currently, the topic is automatically specified based on your specifying clientId. For example, if you specify "sample-pub" as a clientId, the topic is set to "sys/metrics/sample-pub".

## (Optional) Kinesis Firehose Condiguration
If you want to output data to S3 directly through Kinesis Data Streams, you should configure as follows:

* S3 prefix

```
raw-datasets/kafka-components-metrics/year=!{timestamp:yyyy}/month=!{timestamp:MM}/day=!{timestamp:dd}/
```

* S3 error prefix

```
raw-datasets/kafka-components-metrics-error/year=!{timestamp:yyyy}/month=!{timestamp:MM}/reason=!{firehose:error-output-type}/
```

* Buffersize: 20MB
* BufferSeconds: 300sec
* Compression: gzip

Sometimes, gzip has some problem because it's unsplittable, therefore you have to change Buffersize and BufferSeconds based on the amount of input data.


## Future Plan
* Adding test code
* Change log format to Log4J
* Inherit Publisher class from abstract class 
* Change resource paths for certificates

## Reference
* https://github.com/aws/aws-iot-device-sdk-java-v2


