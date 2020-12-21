import logging
import yaml

MQTTv3_1 = 3
MQTTv3_1_1 = 4
DROP_OLDEST = 0
DROP_NEWEST = 1


def init_logger(logger):
    logger.setLevel(logging.DEBUG)
    stream_handler = logging.StreamHandler()
    formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
    stream_handler.setFormatter(formatter)
    logger.addHandler(stream_handler)
    return logger


def config_loader(filename: str):
    try:
        with open(filename) as f:
            config = yaml.load(f, Loader=yaml.FullLoader)
            return config
    except FileNotFoundError as ffe:
        raise ffe


logger = init_logger(logging.getLogger(__name__))
