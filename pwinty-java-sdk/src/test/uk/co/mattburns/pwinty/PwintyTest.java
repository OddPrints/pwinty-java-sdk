package uk.co.mattburns.pwinty;

import java.util.Properties;

import org.junit.Test;

import uk.co.mattburns.pwinty.Pwinty.Environment;

public class PwintyTest {

    @Test
    public void test_create_order_doesnt_time_out() {

        Properties props = new Properties();

        try {
            // load a properties file
            props.load(getClass().getResourceAsStream(
                    "test-settings.properties"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Pwinty pwinty = new Pwinty(Environment.SANDBOX,
                props.getProperty("PWINTY_MERCHANT_ID"),
                props.getProperty("PWINTY_MERCHANT_KEY"));

    }

}
