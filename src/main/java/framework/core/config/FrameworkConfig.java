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
    private final String reqresBaseUrl;
    private final String petstoreBaseUrl;
    private final String githubBaseUrl;
    private final int connectTimeout;
    private final int readTimeout;
    private final String authType;
    private final String token;

    private FrameworkConfig(
            String env,
            String reqresBaseUrl,
            String petstoreBaseUrl,
            String githubBaseUrl,
            int connectTimeout,
            int readTimeout,
            String authType,
            String token
    ) {
        this.env = env;
        this.reqresBaseUrl = reqresBaseUrl;
        this.petstoreBaseUrl = petstoreBaseUrl;
        this.githubBaseUrl = githubBaseUrl;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.authType = authType;
        this.token = token;
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

        String reqresBaseUrl = require(props, "reqres.base.url");
        String petstoreBaseUrl = require(props, "petstore.base.url");
        String githubBaseUrl = require(props, "github.base.url");

        int connectTimeout = Integer.parseInt(require(props, "connect.timeout"));
        int readTimeout = Integer.parseInt(require(props, "read.timeout"));
        String authType = require(props, "auth.type");
        String token = System.getProperty("token", props.getProperty("token", ""));

        FrameworkConfig config = new FrameworkConfig(
                env,
                reqresBaseUrl,
                petstoreBaseUrl,
                githubBaseUrl,
                connectTimeout,
                readTimeout,
                authType,
                token
        );

        log.info("========== Framework Configuration ==========");
        log.info("Environment      : {}", config.env);
        log.info("Reqres URL       : {}", config.reqresBaseUrl);
        log.info("Petstore URL     : {}", config.petstoreBaseUrl);
        log.info("GitHub URL       : {}", config.githubBaseUrl);
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

    public static FrameworkConfig get() { return INSTANCE; }

    public String getReqresBaseUrl() { return reqresBaseUrl; }

    public String getPetstoreBaseUrl() { return petstoreBaseUrl; }

    public String getGithubBaseUrl() { return githubBaseUrl; }

    public int getConnectTimeout() { return connectTimeout; }

    public int getReadTimeout() { return readTimeout; }

    public String getAuthType() { return authType; }

    public String getToken() { return token; }
}