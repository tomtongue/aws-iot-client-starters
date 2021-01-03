let awsIot = require('aws-iot-device-sdk');
let awsIoTconfig = require('config');

let device = awsIot.device({
    region: conf.use1.region,
    keyPath: conf.use1.key,
    certPath: conf.use1.cert,
    caPath: conf.use1.rootca,
    host: conf.use1.host,
    clientId: conf.client.id,
    debug: true
});

let pubTopic = conf.topics.pub
let subTopic = conf.topics.sub


device.on('connect', function() {
    console.log('connected');

    device.subscribe(subTopic, {qos: 1}, function(error, result) { // Set QoS = 1
        console.log(result)
    });

    setInterval( function() {   
        var date = new Date();   
        device.publish(
            pubTopic, 
            JSON.stringify({"id":1201, "ts":date.getTime(), "data":{"temperature":50, "humidity":50}}),
            {qos: 1, dup: false}); // Set QoS = 1
    }, 1000); // per 1sec
    
});


// Show subscribed message
device.on('message', function(topic, payload) {
    console.log('message: ', topic, payload.toString());
});


// Show subscribed message
device.on('message', function(topic, payload) {
    console.log('message', topic, payload.toString());
});