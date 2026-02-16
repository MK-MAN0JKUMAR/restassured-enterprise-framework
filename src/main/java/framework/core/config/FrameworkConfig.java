package framework.core.config;

import framework.core.exception.ConfigException;
import framework.core.mock.WireMockManager;
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

    /**
     * Eager immutable load → enterprise-safe for parallel execution.
     */
    private static final FrameworkConfig INSTANCE = load();

    private final String env;
    private final boolean mockMode;
    private final String baseUrl;
    private final int connectTimeout;
    private final int readTimeout;
    private final String authType;
    private final String token;

    private FrameworkConfig(
            String env,
            boolean mockMode,
            String baseUrl,
            int connectTimeout,
            int readTimeout,
            String authType,
            String token
    ) {
        this.env = env;
        this.mockMode = mockMode;
        this.baseUrl = baseUrl;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.authType = authType;
        this.token = token;
    }

    private static FrameworkConfig load() {

        // -------- ENV RESOLUTION --------
        String env = System.getProperty("env", DEFAULT_ENV).toLowerCase();

        if (!ALLOWED_ENVS.contains(env)) {
            throw new ConfigException("Invalid environment: " + env + ". Allowed: " + ALLOWED_ENVS);
        }

        // -------- LOAD PROPERTY FILE --------
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

        // -------- MOCK MODE --------
        boolean mockMode = Boolean.parseBoolean(System.getProperty("mock", "false"));

        // -------- BASE URL RESOLUTION --------
        String baseUrl;

        if (mockMode) {
            // WireMock MUST already be started in BaseTest @BeforeSuite
            int port = WireMockManager.port();
            baseUrl = "http://localhost:" + port;
        } else {
            baseUrl = System.getProperty("base.url", require(props, "base.url"));
        }

        // -------- OTHER CONFIG --------
        int connectTimeout = Integer.parseInt(require(props, "connect.timeout"));
        int readTimeout = Integer.parseInt(require(props, "read.timeout"));
        String authType = require(props, "auth.type");

        // CLI override for secrets (CI-safe)
        String token = System.getProperty("token", props.getProperty("token", ""));

        FrameworkConfig config = new FrameworkConfig(
                env, mockMode, baseUrl, connectTimeout, readTimeout, authType, token
        );

        // -------- STARTUP LOGGING --------
        log.info("========== Framework Configuration ==========");
        log.info("Environment      : {}", config.env);
        log.info("Mock Mode        : {}", config.mockMode);
        log.info("Base URL         : {}", config.baseUrl);
        log.info("Connect Timeout  : {} ms", config.connectTimeout);
        log.info("Read Timeout     : {} ms", config.readTimeout);
        log.info("Auth Type        : {}", config.authType);
        log.info("Token Provided   : {}", !config.token.isBlank());
        log.info("=============================================");

        return config;
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

    // -------- GETTERS --------

    public String getEnv() { return env; }

    public boolean isMockMode() { return mockMode; }

    public String getBaseUrl() { return baseUrl; }

    public int getConnectTimeout() { return connectTimeout; }

    public int getReadTimeout() { return readTimeout; }

    public String getAuthType() { return authType; }

    public String getToken() { return token; }
}
