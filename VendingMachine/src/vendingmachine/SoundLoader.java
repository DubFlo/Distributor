package vendingmachine;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Supply Clip sounds useful to a vending machine.
 */
public final class SoundLoader {

  private static final String PATH = "src" + File.separator + "resources" + File.separator;

  public static final Clip BEEP = getSound("beep.wav");
  public static final Clip CLING = getSound("cling.wav");
  public static final Clip FOP = getSound("fop.wav");
  public static final Clip CLICK = getSound("click.wav");
  public static final Clip FILLING = getSound("filling.wav");

    // http://www.freesound.org/people/AlaskaRobotics/sounds/221087/

    // http://www.freesfx.co.uk/sfx/coins?p=3
    // money_multiple_coins_drop_on_hard_surface_001

    // http://www.freesfx.co.uk/sfx/coins?p=1
    // several_coins_placed_lightly_down_on_table
    
    //https://www.freesound.org/people/kwahmah_02/sounds/256116/
    
    //https://www.freesound.org/people/sam_tura/sounds/211504/

  public static void play(Clip clip) {
    if (clip != null) { // works if something hasn't loaded properly
      if (clip.isRunning()) {
        clip.stop();
      }
      clip.setFramePosition(0); // Rewind the beep
      clip.start();
    }
  }
  
  private static Clip getSound(String file) {
    Clip clip;
    AudioInputStream stream;
    try {
      stream = AudioSystem.getAudioInputStream(new File(PATH + file));
      clip = AudioSystem.getClip();
      clip.open(stream);
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
      clip = null;
      e.printStackTrace();
    }
    return clip;
  }
  
  private SoundLoader() {}

}
