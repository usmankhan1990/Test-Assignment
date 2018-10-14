package helper;

/**
 * Created by UsmanKhan on 14/Oct/2018.
 * This class is providing application and client key for parse to initialize server configurations.
 */

public class AppConfig {


    private static AppConfig appConfigInstance;

    public static AppConfig getInstance() {
        if (appConfigInstance == null) {
            appConfigInstance = new AppConfig();
        }
        return appConfigInstance;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public String getClientKey() {
        return clientKey;
    }

    private String applicationID = "1TcAvt0wD6YDxEffIJ7qJtRQvMXzu7";
    private String clientKey = "XUNfi612hLNdS6V8TTb582YTou8qSX";

}
