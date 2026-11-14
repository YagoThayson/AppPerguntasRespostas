
package com.example.appperguntasrespostas;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicaService extends Service {

    private MediaPlayer musica;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (musica == null) {
            musica = MediaPlayer.create(this, R.raw.musica);

            if (musica != null) {
                musica.setLooping(true);
                musica.start();
            }
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (musica != null) {
            musica.stop();
            musica.release();
            musica = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
