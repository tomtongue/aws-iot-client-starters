import logging
import time
import json
import datetime
import calendar

from iot_client.MqttClient import MqttClient
from iot_client import init_logger, logger, config_loader


if __name__ == '__main__':
    init_logger(logging.getLogger("AWSIoTPythonSDK.core"))
    conf = config_loader('./resources/awsiot_conf.yml')
    mqtt_client = MqttClient(id=conf['client']['id'],
                             host=conf['use1']['host'],
                             port=conf['client']['port'],
                             rootca=conf['use1']['rootca'],
                             key=conf['use1']['key'],
                             cert=conf['use1']['cert'])
    mqtt_client.set_mqtt_configuration()
    mqtt_client.connect(30)

    while True:
        # timestamp
        now = datetime.datetime.utcnow()
        recordat = str(now.strftime("%Y-%m-%d"))
        ts = str(calendar.timegm(now.utctimetuple()))

        # publish
        payload = json.dumps(
            {"date": recordat, "ts": ts, "id": mqtt_client.id, "message": "Hello World!"})
        logger.info("Client will publish: " + payload)
        mqtt_client.publishAsync(conf['topics']['pub'], payload, 1, ackCallback=mqtt_client.puback_cb)
        time.sleep(1)

        # subscribe
        mqtt_client.subscribeAsync(conf['topics']['pub'], 1, ackCallback=mqtt_client.suback_cb)
        time.sleep(2)








