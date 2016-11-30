package android_2016.ifmo.ru.imageloader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;

/**
 * Created by maria on 30.11.16.
 */
public class OrientationReceiver extends BroadcastReceiver {

    private int configOrientation;
    public final String IMAGE_URL = "http://cs5.pikabu.ru/post_img/2015/07/26/11/1437933708_1220052471.jpg";
    public static final String myfile = "myfile.jpg";

    @Override
    public void onReceive(Context context, Intent intent) {
        configOrientation = context.getResources().getConfiguration().orientation;
        switch(configOrientation) {
            case Configuration.ORIENTATION_LANDSCAPE :
            {
                Log.d("ORIENTATION", "BECAME LANDSCAPE");
                Intent i = new Intent(context, ImageLoadService.class);
                i.putExtra(MainActivity.KEY_URL, IMAGE_URL);
                i.putExtra(MainActivity.KEY_NAME, myfile);
                context.startService(i);
            }
        }
    }
}
