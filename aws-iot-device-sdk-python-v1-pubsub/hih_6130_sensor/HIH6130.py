import time
import smbus

from iot_client import PayloadBuilder


class HIH6130(PayloadBuilder):
    def __init__(self, temperature_key: str, humidity_key: str):
        self.buf = smbus.SMBus(1).read_i2c_block_data(0x27, 0, 4)
        self.temperature_key = temperature_key
        self.humidity_key = humidity_key
        pass

    def get_sensor_status(self):
        """Getting sensor status"""
        return self.buf[0] >> 6 & 0x03

    def get_temp_and_humid(self) -> dict:
        """Getting temperature and humidity from HIH6130 sensor and converting them"""
        # Digital output
        humidity_digit = (self.buf[0] & 0x3F) * 256 + self.buf[1]
        temperature_digit = self.buf[2] * 64 + (self.buf[3] >> 2)

        # Convert output
        humidity = round(humidity_digit / 16383.0 * 100, 8)
        temperature = round(temperature_digit / 16383.0 * 165 - 40, 8)

        return {self.temperature_key: temperature, self.humidity_key: humidity}
