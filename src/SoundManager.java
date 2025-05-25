import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class SoundManager {

    private static Clip backgroundClip;

    // 播放一次性音效
    public static void playSoundEffect(String path, float volumeDb) {
        new Thread(() -> {
            try {
                InputStream audioSrc = SoundManager.class.getResourceAsStream(path);
                if (audioSrc == null) {
                    throw new IllegalArgumentException("Resource not found: /sound/" + path);
                }
                InputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    volumeControl.setValue(volumeDb);
                }
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // 播放背景音樂（會自動 loop）
    public static void playBackgroundMusic(String path, float volumeDb) {
        stopBackgroundMusic(); // 先停止舊的背景音樂
        new Thread(() -> {
            try {
                InputStream audioSrc = SoundManager.class.getResourceAsStream(path);
                if (audioSrc == null) {
                    throw new IllegalArgumentException("Resource not found: /sound/" + path);
                }
                InputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn);
                backgroundClip = AudioSystem.getClip();
                backgroundClip.open(ais);
                if (backgroundClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl volumeControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
                    volumeControl.setValue(volumeDb);
                }
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY); // 循環播放
                backgroundClip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // 停止背景音樂
    public static void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
        }
    }

    // 播放一次性音效（預設音量）
    public static void playSoundEffect(String path) {
        playSoundEffect(path, -10f); // 預設音量
    }
}
