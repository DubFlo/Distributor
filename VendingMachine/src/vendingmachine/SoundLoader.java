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
 * Supply Clip sounds useful to a vending machine.
 * Uses the singleton design pattern.
 */
public final class SoundLoader {

  private static SoundLoader INSTANCE;

  private static final Logger log = LogManager.getLogger("PictureLoader");

  /**
   * Sound that indicates that something is ready (five beeps).
   */
  public final Clip BEEP = getSound("beep.wav");

  /**
   * Sound of coins falling on the ground.
   */
  public final Clip CLING = getSound("cling.wav");

  /**
   * Sound of a coin falling inside a machine.
   */
  public final Clip FOP = getSound("fop.wav");

  /**
   * Sound of a small button being pressed.
   */
  public final Clip CLICK = getSound("click.wav");

  /**
   * Sound of a cup that is filling with water.
   */
  public final Clip FILLING = getSound("filling.wav");

  // http://www.freesound.org/people/AlaskaRobotics/sounds/221087/

  // http://www.freesfx.co.uk/sfx/coins?p=3
  // money_multiple_coins_drop_on_hard_surface_001

  // http://www.freesfx.co.uk/sfx/coins?p=1
  // several_coins_placed_lightly_down_on_table

  //https://www.freesound.org/people/kwahmah_02/sounds/256116/

  //https://www.freesound.org/people/sam_tura/sounds/211504/

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
      stream = AudioSystem.getAudioInputStream(SoundLoader.class.getResource("/resources/" + file));
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
    if (INSTANCE == null) {
      INSTANCE = new SoundLoader();
    }
    return INSTANCE;
  }

}
