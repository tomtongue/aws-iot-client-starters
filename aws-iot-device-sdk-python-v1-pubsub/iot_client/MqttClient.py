from AWSIoTPythonSDK.MQTTLib import AWSIoTMQTTClient
from iot_client import MQTTv3_1_1, DROP_NEWEST
from iot_client import logger


class MqttClient(AWSIoTMQTTClient):
    """Overried MQTT Client for AWS IoT"""
    def __init__(self, id: str, host: str, port: int, rootca: str, key: str, cert: str):
        super().__init__(id, protocolType=MQTTv3_1_1, useWebsocket=False, cleanSession=True)
        self.id = id
        self.host = host
        self.port = port
        self.rootca = rootca
        self.key = key
        self.cert = cert

    def set_mqtt_configuration(self, base_reconnect_quiet_time=1,
                               max_reconnect_quiet_time=32,
                               stable_connection_time=20,
                               queueSize=-1, dropBehavior=DROP_NEWEST,
                               frequencyInHz=2,
                               connection_timeout=10,
                               operation_timeout=5,
                               on_message=None):
        """Set mqtt client configuration"""

        self.configureEndpoint(self.host, self.port)
        self.configureCredentials(self.rootca, self.key, self.cert)
        self.configureAutoReconnectBackoffTime(base_reconnect_quiet_time, max_reconnect_quiet_time, stable_connection_time)
        self.configureOfflinePublishQueueing(queueSize, dropBehavior)
        self.configureDrainingFrequency(frequencyInHz)
        self.configureConnectDisconnectTimeout(connection_timeout)
        self.configureMQTTOperationTimeout(operation_timeout)
        self.onMessage(on_message)

    @staticmethod
    def suback_cb(mid, data):
        logger.info(f'Packet id: {str(mid)}, message: {data}')

    @staticmethod
    def puback_cb(mid):
        logger.info(f'Received PUBACK packet id: {str(mid)}')


