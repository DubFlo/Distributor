package vendingmachine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PictureLoader {
	
	private static final String PATH = "src" + File.separator + "resources" + File.separator;
	private static final Logger log = LogManager.getLogger("Context");
	
	public static final BufferedImage cupImage;
	public static final BufferedImage changeImage;
	public static final BufferedImage displayPanel;
	public static final BufferedImage coffee;
	public static final BufferedImage leftPanel;
	public static final BufferedImage slot;
	public static final BufferedImage sugarDisplay;
	public static final BufferedImage southPanel;
	public static final BufferedImage drinkButton;
	public static final BufferedImage euro2, euro1, cent50, cent20, cent10, cent5, cent2, cent1;
	
	static {
		BufferedImage cup;
		try {
			cup = ImageIO.read(new File(PATH + "cup.jpg"));
			}
		catch (IOException e) {
			cup = null;
			log.error("cup.jpg not properly loaded.");
		}
		
		BufferedImage change;
		try {
			change = ImageIO.read(new File(PATH + "change.png"));
		} catch (IOException e) {
			change = null;
			log.error("change.png not properly loaded.");
		}
		
		BufferedImage display;
		try {
			display = ImageIO.read(new File(PATH + "displayPanel.jpg"));
		} catch (IOException e) {
			display = null;
			log.error("displayPanel.jpg not properly loaded.");
		}
		
		BufferedImage cof;
		try {
			cof = ImageIO.read(new File(PATH + "coffee.png"));
		} catch (IOException e) {
			cof = null;
			log.error("coffee.png not properly loaded.");
		}
		
		BufferedImage left;
		try {
			left = ImageIO.read(new File(PATH + "leftPanel.png"));
		} catch (IOException e) {
			left = null;
			log.error("leftPanel.png not properly loaded.");
		}
		
		BufferedImage sl;
		try {
			sl = ImageIO.read(new File(PATH + "slot.jpg"));
		} catch (IOException e) {
			sl = null;
			log.error("slot.jpg not properly loaded.");
		}
		
		BufferedImage sugar;
		try {
			sugar = ImageIO.read(new File(PATH + "sugarDisplay.jpg"));
		} catch (IOException e) {
			sugar = null;
			log.error("sugarDisplay.jpg not properly loaded.");
		}
		
		BufferedImage south;
		try {
			south = ImageIO.read(new File(PATH + "southPanel.png"));
		} catch (IOException e) {
			south = null;
			log.error("southPanel.png not properly loaded.");
		}
		
		BufferedImage drink;
		try {
			drink = ImageIO.read(new File(PATH + "drinkButton.png"));
		} catch (IOException e) {
			drink = null;
			log.error("drinkButton.png not properly loaded.");
		}
		
		BufferedImage eur2, eur1, cen50, cen20, cen10, cen5, cen2, cen1;
		try {
			eur2 = ImageIO.read(new File(PATH + "2euro.png"));
			eur1 = ImageIO.read(new File(PATH + "1euro.png"));
			cen50 = ImageIO.read(new File(PATH + "50cent.png"));
			cen20 = ImageIO.read(new File(PATH + "20cent.png"));
			cen10 = ImageIO.read(new File(PATH + "10cent.png"));
			cen5 = ImageIO.read(new File(PATH + "5cent.png"));
			cen2 = ImageIO.read(new File(PATH + "2cent.png"));
			cen1 = ImageIO.read(new File(PATH + "1cent.png"));
		} catch (IOException e) {
			eur2 = null;	eur1 = null;	cen50 = null;
			cen20 = null;	cen10 = null;	cen5 = null;
			cen2 = null;	cen1 = null;
			log.error("Some coin image not properly loaded.");
		}
		cupImage = cup;
		changeImage = change;
		displayPanel = display;
		coffee = cof;
		leftPanel = left;
		slot = sl;
		sugarDisplay = sugar;
		southPanel = south;
		drinkButton = drink;
		euro2 = eur2;	euro1 = eur1;	cent50 = cen50;
		cent20 = cen20;	cent10 = cen10;	cent5 = cen5;
		cent2 = cen2;	cent1 = cen1;
	}

}
