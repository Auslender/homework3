package android_2016.ifmo.ru.imageloader;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_URL = "image_url";
    public static final String KEY_NAME = "file_name";
    public static final String myfile = "myfile.jpg";
    public static final String UPDATE = "NEW_BROADCAST";

    ImageView image;
    TextView error;
    ImageLoadService myService;

    private BroadcastReceiver sr, u;
    boolean isBounded = false;

    private ServiceConnection isConnected = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            ImageLoadService.ILBinder binder = (ImageLoadService.ILBinder) service;
            myService = binder.get();
            isBounded = true;
        }
        public void onServiceDisconnected(ComponentName className) {
            isBounded = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ONCREATE", "Called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.image);
        error = (TextView)findViewById(R.id.error);

        Intent i = new Intent(getApplicationContext(), ImageLoadService.class);
        getApplicationContext().bindService(i, isConnected, BIND_AUTO_CREATE);

        openImage();

        u = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                openImage();
            }
        };
        sr = new StateReciever();
        registerReceiver(sr, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        registerReceiver(u, new IntentFilter(UPDATE));
    }

    private void openImage() {
        File f = new File(Environment.getExternalStorageDirectory(), myfile);
        if (f.exists()) {
            Log.d("File EXISTS", f.getPath());
            Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
            image.setImageBitmap(bitmap);
            image.setVisibility(View.VISIBLE);
            error.setVisibility(View.INVISIBLE);
        } else {
            image.setVisibility(View.INVISIBLE);
            error.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MAINACTIVITY", "DESTROYED");
        unregisterReceiver(sr);
        unregisterReceiver(u);
    }

}
