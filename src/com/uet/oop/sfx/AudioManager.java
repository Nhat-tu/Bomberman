package com.uet.oop.sfx;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class AudioManager {
    private Map<String, Clip> audioClips;
    private FloatControl masterVolumeControl;
    private boolean muted;

    public AudioManager() {
        audioClips = new HashMap<>();
        muted = false;
        initializeMasterVolume();
    }

    // initialize master volume
    private void initializeMasterVolume() {
        try {
            AudioSystem.getMixerInfo(); // Ensure mixers are loaded
            for (Mixer.Info info : AudioSystem.getMixerInfo()) {
                Mixer mixer = AudioSystem.getMixer(info);
                Line.Info[] targetLineInfo = mixer.getTargetLineInfo(); // Get output lines

                if (mixer.isLineSupported(Port.Info.SPEAKER)) {
                    Line line = mixer.getLine(Port.Info.SPEAKER);
                    line.open();
                    if (line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                        masterVolumeControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
                        System.out.println("Master volume control initialized. Range: " +
                                masterVolumeControl.getMinimum() + " to " + masterVolumeControl.getMaximum() + " dB");
                    }
                }
            }
        } catch (LineUnavailableException e) {
            System.err.println("Could not get master volume control. " + e.getMessage());
            masterVolumeControl = null;
        }
    }

    // load audio
    public boolean loadAudio(String audioKey, String filePath) {
        try {
            File audioFile = new File(filePath);
            if (!audioFile.exists()) {
                System.err.println("Audio file not found: " + filePath);
                return false;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            audioClips.put(audioKey, clip);
            System.out.println("Loaded audio: " + audioKey + " from " + filePath);
            return true;
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio file format for " + filePath + ": " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading audio file " + filePath + ": " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("Audio line unavailable for " + filePath + ": " + e.getMessage());
        }
        return false;
    }

    // play the audio
    public void playSound(String audioKey) {
        if (muted) return;

        Clip clip = audioClips.get(audioKey);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();   // Stop if already playing to allow retriggering
            }
            clip.setFramePosition(0); // Rewind to the beginning
            clip.start();
        } else {
            System.err.println("Audio clip not found for key: " + audioKey);
        }
    }

    // loop the audio
    public void loopSound(String audioKey, int loopCount) {
        if (muted) return;

        Clip clip = audioClips.get(audioKey);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.loop(loopCount);
        } else {
            System.err.println("Audio clip not found for key: " + audioKey);
        }
    }

    //stop the audio
    public void stopSound(String audioKey) {
        Clip clip = audioClips.get(audioKey);
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.setFramePosition(0); // Rewind for next playback
        }
    }

    // stop all audio
    public void stopAllSounds() {
        for (Clip clip : audioClips.values()) {
            if (clip.isRunning()) {
                clip.stop();
                clip.setFramePosition(0);
            }
        }
    }

    //set master volume to all sounds
    public void setMasterVolume(float volume) {
        if (masterVolumeControl != null) {
            if (volume < 0.0f) volume = 0.0f;
            if (volume > 1.0f) volume = 1.0f;

            // Convert linear volume (0.0-1.0) to decibels (logarithmic)
            // A common conversion, though can be adjusted for desired sound profile.
            float minDb = masterVolumeControl.getMinimum();
            float maxDb = masterVolumeControl.getMaximum();
            float db = (float) (Math.log10(volume) * 20.0); // Simple dB conversion

            // Ensure the calculated dB is within the control's range
            if (db < minDb) db = minDb;
            if (db > maxDb) db = maxDb;

            masterVolumeControl.setValue(db);
            System.out.println("Master volume set to: " + volume + " (approx " + db + " dB)");
        } else {
            System.err.println("Master volume control not available.");
        }
    }

    // get current master volume
    public float getMasterVolume() {
        if (masterVolumeControl != null) {
            float db = masterVolumeControl.getValue();
            // Convert decibels back to linear volume (0.0-1.0)
            return (float) Math.pow(10, db / 20.0);
        }
        return -1.0f; // Indicate not available
    }

    // mute audio
    public void mute() {
        if (!muted) {
            setMasterVolume(0.0f); // Set volume to minimum
            muted = true;
            System.out.println("Audio muted.");
        }
    }

    // unmute all audio and set it to mid.
    public void unmute() {
        if (muted) {
            setMasterVolume(0.5f);
            muted = false;
            System.out.println("Audio unmuted.");
        }
    }

    // release all audio source
    public void shutdown() {
        for (Clip clip : audioClips.values()) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.close();
        }
        audioClips.clear();
        System.out.println("AudioManager shut down. All audio resources released.");
    }
}
