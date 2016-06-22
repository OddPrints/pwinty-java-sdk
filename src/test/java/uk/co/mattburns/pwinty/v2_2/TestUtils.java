package uk.co.mattburns.pwinty.v2_2;

import java.util.Properties;

public class TestUtils {

    public static Properties loadProps() {
        Properties prop = new Properties();

        try {
            // load a properties file
            prop.load(TestUtils.class.getResourceAsStream("test-settings.properties"));

        } catch (Exception e) {
            throw new RuntimeException("Couldn't find test-settings.properties", e);
        }

        for (Object key : prop.keySet()) {
            String keyString = (String) key;
            String keyValue = prop.getProperty(keyString);
            if (keyValue.matches("\\$\\{.*?\\}")) {
                keyValue = resolveKeyValue(keyValue);
            }
            prop.setProperty(keyString, keyValue);
        }
        return prop;
    }

    private static String resolveKeyValue(String keyValue) {
        keyValue = keyValue.replaceAll("\\$\\{(.*?)\\}", "$1");
        String envVar = keyValue;
        String defaultVal = null;
        if (keyValue.indexOf(':') != -1) {
            String[] parts = keyValue.split(":");
            envVar = parts[0];
            defaultVal = parts[1];
        }

        String envVal = System.getenv(envVar);
        if (envVal == null) {
            envVal = System.getProperty(envVar);
        }
        return envVal == null ? defaultVal : envVal;
    }
}
