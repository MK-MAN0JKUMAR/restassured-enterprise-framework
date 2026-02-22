package framework.core.config;

import framework.core.exception.ConfigException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public final class FrameworkConfig {

    private static final Logger log = LogManager.getLogger(FrameworkConfig.class);

    private static final List<String> ALLOWED_ENVS = List.of("qa", "stage", "prod");
    private static final String DEFAULT_ENV = "qa";
    private static final String CONFIG_PATH = "config/";

    private static final FrameworkConfig INSTANCE = load();

    private final String env;
    private final Properties properties;

    private FrameworkConfig(String env, Properties properties) {
        this.env = env;
        this.properties = properties;
    }

    private static FrameworkConfig load() {

        String env = System.getProperty("env", DEFAULT_ENV).toLowerCase();

        if (!ALLOWED_ENVS.contains(env)) {
            throw new ConfigException("Invalid environment: " + env + ". Allowed: " + ALLOWED_ENVS);
        }

        String fileName = CONFIG_PATH + env + ".properties";
        Properties props = new Properties();

        try (InputStream input =
                     FrameworkConfig.class.getClassLoader().getResourceAsStream(fileName)) {

            if (input == null) {
                throw new ConfigException("Config file not found: " + fileName);
            }

            props.load(input);

        } catch (Exception e) {
            throw new ConfigException("Failed to load config file: " + fileName, e);
        }

        log.info("========== Framework Configuration ==========");
        log.info("Environment      : {}", env);
        log.info("Connect Timeout  : {} ms", require(props, "connect.timeout"));
        log.info("Read Timeout     : {} ms", require(props, "read.timeout"));
        log.info("=============================================");

        return new FrameworkConfig(env, props);
    }

    private static String require(Properties props, String key) {
        String value = props.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new ConfigException("Missing required config key: " + key);
        }
        return value.trim();
    }

    public static FrameworkConfig get() {
        return INSTANCE;
    }

    public String getEnv() {
        return env;
    }

    public String getRequired(String key) {
        return require(properties, key);
    }

    public String getOptional(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getInt(String key) {
        return Integer.parseInt(getRequired(key));
    }

    public String getTokenOverride(String key) {
        return System.getProperty(key, properties.getProperty(key, ""));
    }
}