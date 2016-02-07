package vendingmachine;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class SoundLoader {
	
	private static final Logger log = LogManager.getLogger("Context");
	private static final String PATH = "src" + File.separator + "resources" + File.separator;
	
	public static final Clip beep;
	
	static {
		//http://www.freesound.org/people/AlaskaRobotics/sounds/221087/
		Clip bip;
		AudioInputStream audioInputStream;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File(PATH + "beep.wav"));
			bip = AudioSystem.getClip();
			bip.open(audioInputStream);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			bip = null;
			log.error("beep.wav not properly loaded.");
		}
		beep = bip;
	}
	
	public static void beep() {
		if (beep.isRunning())
			beep.stop();
		beep.setFramePosition(0); //Rewind the beep
		beep.start();
	}

}
