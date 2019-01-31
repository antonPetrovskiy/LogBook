package social.tosch.com.social;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.widget.Toast;

import java.io.IOException;

public class MusicWorker {
    private static MusicWorker instance;

    private SoundPool mSoundPool;
    private AssetManager mAssetManager;
    public static int click, back, top, bot, timeout, win;
    private int mStreamID;
    static Context cont;

    private MusicWorker(){

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // Для устройств до Android 5
            createOldSoundPool();
        } else {
            // Для новых устройств
            createNewSoundPool();
        }
        mAssetManager = cont.getAssets();

        // получим идентификаторы
        click = loadSound("click.mp3");
        back = loadSound("back.mp3");
        top = loadSound("top.mp3");
        bot = loadSound("bot.mp3");
        timeout = loadSound("timeout.mp3");
        win = loadSound("win.mp3");
    }

    public static MusicWorker getInstance(Context c){
        cont = c;
        if(instance == null){
            instance = new MusicWorker();
            return instance;
        }else{
            return instance;
        }
    }



    private int loadSound(String fileName) {
        AssetFileDescriptor afd;
        try {
            afd = mAssetManager.openFd(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(cont, "Не могу загрузить файл " + fileName,
                    Toast.LENGTH_SHORT).show();
            return -1;
        }
        return mSoundPool.load(afd, 1);
    }

    public int playSound(int sound) {
        if (sound > 0) {
            mStreamID = mSoundPool.play(sound, 3, 3, 1, 0, 1);
        }
        return mStreamID;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createNewSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    @SuppressWarnings("deprecation")
    private void createOldSoundPool() {
        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
    }



}
