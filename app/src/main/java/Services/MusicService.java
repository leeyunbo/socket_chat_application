package Services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import com.example.family_share_community.R;

public class MusicService extends Service {
    MediaPlayer player;
    private static final String TAG = "MusicService";
    public MusicService() {
        super();
    }

    @Override
    public void onCreate() {
        player = MediaPlayer.create(this, R.raw.havana);
        player.setLooping(false);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Music Service 시작", Toast.LENGTH_LONG).show();
        player.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Music Service 중지", Toast.LENGTH_LONG).show();
        player.stop();
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
