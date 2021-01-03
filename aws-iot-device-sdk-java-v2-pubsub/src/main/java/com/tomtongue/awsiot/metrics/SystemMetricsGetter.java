package com.tomtongue.awsiot.metrics;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static java.lang.Double.NaN;

public class SystemMetricsGetter {
    public static Map<String, Object> getUsage() {
        Map<String, Object> usage = new LinkedHashMap<String, Object>(); // Use LinkedHashMap because of order
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.getName().startsWith("get") && Modifier.isPublic(method.getModifiers())) {
                Object value;
                try {
                    value = method.invoke(operatingSystemMXBean);
                } catch (Exception e) {
                    value = null;
                    e.printStackTrace();
                }

                if (value.equals(NaN)) {
                    value = null;
                }
                usage.put(method.getName(), value);
                // System.out.println(method.getName() + " = " + value); // For debug
            }
        }
        return usage;
    }
}
