package framework;

import android.app.Application;
import com.parse.Parse;
import helper.AppConfig;
import okhttp3.OkHttpClient;

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private AppConfig appConfigInstance = AppConfig.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(appConfigInstance.getApplicationID())
                .clientKey(appConfigInstance.getClientKey())
                .server("https://eyenight-dev.herokuapp.com/parse")
                .clientBuilder(new OkHttpClient.Builder())
                .build()
        );
    }

}
