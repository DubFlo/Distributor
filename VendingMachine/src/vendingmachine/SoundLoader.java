package vendingmachine;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class supplies Clip sounds useful to a vending machine.
 * Uses the singleton design pattern to be created only once when needed (thread-safe).
 * This solution has been found on http://stackoverflow.com/a/11165975 . As explained on this link,
 * "The class [SoundLoader].Loader is first accessed inside the getInstance() method, so the Loader
 * class loads when getInstance() is called for the first time. Further, the class loader
 * guarantees that all static initialization is complete before you get access to the class -
 * that's what gives you thread-safety."
 */
public final class SoundLoader {
  
  private static final Logger log = LogManager.getLogger("PictureLoader");

  /**
   * Sound that indicates that something is ready (five beeps).
   * Source: http://goo.gl/DH8JSJ
   */
  public final Clip BEEP = getSound("beep.wav");

  /**
   * Sound of a small button being pressed.
   * Source: https://goo.gl/ElAFZv
   */
  public final Clip CLICK = getSound("click.wav");

  /**
   * Sound of coins falling on the ground.
   * Source: http://goo.gl/7mYUOv under the name
   * "Money, Multiple Coins Drop On Hard Surface 001"
   * Credit: http://www.freesfx.co.uk/
   */
  public final Clip CLING = getSound("cling.wav");

  /**
   * Sound of a cup that is filling with water.
   * Source: https://goo.gl/EDangj
   */
  public final Clip FILLING = getSound("filling.wav");

  /**
   * Sound of a coin falling inside a machine.
   * Source: http://goo.gl/bV1Tao under the name
   * "Several Coins Placed Lightly Down On Table"
   * Credit: http://www.freesfx.co.uk/
   */
  public final Clip FOP = getSound("fop.wav");
  
  /**
   * Sound of a machine being repaired.
   * Source:
   */
  //public final Clip REPAIR = getSount("repair.wav");
  
  /**
   * Plays the specified clip from the beginning (if it exists).
   * If the clip is already running, stops it and plays it again.
   * 
   * @param clip the Clip to play
   */
  public static void play(Clip clip) {
    if (clip != null) {
      if (clip.isRunning()) {
        clip.stop();
      }
      clip.setFramePosition(0); // Rewinds the clip
      clip.start();
    }
  }

  /**
   * Stops the specified clip (if it exists).
   * 
   * @param clip the Clip to stop.
   */
  public static void stop(Clip clip) {
    if (clip != null) {
      clip.stop();
    }
  }

  /**
   * Returns a Clip loaded from the file name specified.
   * If the file does not exists or is not a valid sound file, returns null.
   * 
   * @param file the name of the sound file, placed in /resources/
   * @return the Clip loaded from the file if it exists, null otherwise
   * @see Clip
   */
  private static Clip getSound(String file) {
    Clip clip;
    AudioInputStream stream;
    try {
      stream = AudioSystem.getAudioInputStream(SoundLoader.class.getResource("/resources/sounds/" + file));
      clip = AudioSystem.getClip();
      clip.open(stream);
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | NullPointerException e) {
      clip = null;
      log.error(file + " not properly loaded. Sounds will be missing.");
    }
    return clip;
  }

  private SoundLoader() {}

  /**
   * @return the only SoundLoader instance (creates it if it doesn't exist)
   */
  public static SoundLoader getInstance() {
    return Loader.INSTANCE;
  }
  
  private static class Loader {
    public static final SoundLoader INSTANCE = new SoundLoader();
  }

}
