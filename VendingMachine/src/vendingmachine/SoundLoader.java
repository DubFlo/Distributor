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
	
	private static final Logger log = LogManager.getLogger("SoundLoader");
	private static final String PATH = "src" + File.separator + "resources" + File.separator;
	
	public static final Clip beep;
	public static final Clip cling;
	public static final Clip fop;
	
	static {
		//http://www.freesound.org/people/AlaskaRobotics/sounds/221087/
		Clip bip;
		AudioInputStream beepStream;
		try {
			beepStream = AudioSystem.getAudioInputStream(new File(PATH + "beep.wav"));
			bip = AudioSystem.getClip();
			bip.open(beepStream);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			bip = null;
			log.error("beep.wav not properly loaded.");
		}
		beep = bip;
		
		//http://www.freesfx.co.uk/sfx/coins?p=3
		//money_multiple_coins_drop_on_hard_surface_001
		Clip cli;
		AudioInputStream clingStream; //un seul AIS ? à tester
		try {
			clingStream = AudioSystem.getAudioInputStream(new File(PATH + "cling.wav"));
			cli = AudioSystem.getClip();
			cli.open(clingStream);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			cli = null;
			log.error("cling.wav not properly loaded.");
		}
		cling = cli;
		
		//http://www.freesfx.co.uk/sfx/coins?p=1
		//several_coins_placed_lightly_down_on_table
		Clip fo;
		AudioInputStream fopStream;
		try {
			fopStream = AudioSystem.getAudioInputStream(new File(PATH + "fop.wav"));
			fo = AudioSystem.getClip();
			fo.open(fopStream);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			fo = null;
			log.error("fop.wav not properly loaded.");
		}
		fop = fo;
	}
	
	public static void play(Clip clip) {
		if (clip != null) { //to work if something hasn't loaded properly
			if (clip.isRunning())
				clip.stop();
			clip.setFramePosition(0); //Rewind the beep
			clip.start();
		}
	}

}
