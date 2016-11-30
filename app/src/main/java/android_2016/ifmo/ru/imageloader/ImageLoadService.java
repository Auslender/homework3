package android_2016.ifmo.ru.imageloader;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by maria on 30.11.16.
 */
public class ImageLoadService extends IntentService {

    public ImageLoadService() {
        super("MyService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("INTENTSERVICE", "StateReciever Created");
    }

    class ILBinder extends Binder {
        ImageLoadService get() {
            return ImageLoadService.this;
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String img_url = intent.getStringExtra(MainActivity.KEY_URL);
        String fileName = intent.getStringExtra(MainActivity.KEY_NAME);

        try {
            URL url = new URL(img_url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();

            File file = new File(Environment.getExternalStorageDirectory(), fileName);
            Log.d("NEW FILE", file.getPath());
            InputStream in = urlConnection.getInputStream();
            FileOutputStream out = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ((bufferLength = in.read(buffer)) > 0) {
                out.write(buffer, 0, bufferLength);
            }
            out.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

