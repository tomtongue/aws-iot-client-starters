# Getting temperature and humidity with HIH6130 sensor
## What is HIH6130?
Ref: https://www.sparkfun.com/products/11295

## How to use it
Using HIH-6130 sensor, you can get the temperature and humidity.

### Requirement

```
$ sudo apt-get install i2c-tools python-smbus
```

### Test it
confirm connecting to sensor

```
$ sudo i2cdetect -y 1
     0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f
00:          -- -- -- -- -- -- -- -- -- -- -- -- --
10: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
20: -- -- -- -- -- -- -- 27 -- -- -- -- -- -- -- --
30: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
40: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
50: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
60: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
70: -- -- -- -- -- -- -- --
```

