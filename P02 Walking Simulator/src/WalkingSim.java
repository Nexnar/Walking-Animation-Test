//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: WalkerSim.java
// Course: CS 300 Fall 2024
//
// Author: Tristin Yun
// Email: tyun7@wisc.edu
// Lecturer: Hobbes LeGault
//
//////////////////////// ASSISTANCE/HELP CITATIONS ////////////////////////////
//
// Persons: NOBODY
// Online Sources: NONE
//
///////////////////////////////////////////////////////////////////////////////

import java.util.Random;
import java.io.File;
import processing.core.PImage;

/**
 * This class displays various Walkers and allows the user to interact with them based on their
 * inputs
 */
public class WalkingSim {
  //static data fields
  private static Random randGen;
  private static int bgColor;
  private static PImage[] frames;
  private static Walker[] walkers;

  /**
   * The main method starts the application
   * 
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    Utility.runApplication(); // starts the application
  }

  /**
   * a callback method that sets up the application. This is where data field initialization should
   * occur. Only called once
   */
  public static void setup() {
    randGen = new Random();

    bgColor = randGen.nextInt();
    // initializes frames array to a length of Walker.Num_Frames
    frames = new PImage[Walker.NUM_FRAMES];
    // stores each image inside of the frames array
    for (int i = 0; i < frames.length; i++) {
      frames[i] = Utility.loadImage("images" + File.separator + "walk-" + i + ".png");
    }

    walkers = new Walker[8]; // sets walkers array length to 8
    // adds a random amount of Walker objects
    // (at random locations) into the walkers array
    for (int j = 0; j < randGen.nextInt(1, walkers.length); j++) {
      walkers[j] = new Walker(randGen.nextInt(Utility.width()), randGen.nextInt(Utility.height()));
    }
  }

  /**
   * A callback method that runs continuously and creates the window where the Walkers will be
   * displayed
   */
  public static void draw() {
    Utility.background(bgColor);

    // iterates and displays each non-null Walker in walkers array

    for (int i = 0; i < walkers.length; i++) {
      if (walkers[i] != null) {
        // if the walker is walking, translate him 3 units to the right
        // before drawing (mod 800 in order to wrap)
        if (walkers[i].isWalking() == true) {
          walkers[i].setPositionX((walkers[i].getPositionX() + 3) % 800);
        }
        Utility.image(frames[walkers[i].getCurrentFrame()], walkers[i].getPositionX(),
            walkers[i].getPositionY());
        if (isMouseOver(walkers[i])) {
          System.out.println("Mouse is over a walker!");
        }
        // starts advancing the current frame index through frames array
        if (walkers[i].isWalking() == true) {
          walkers[i].update();
        }
      }
    }

  }

  /**
   * Determines whether the user's mouse is hovering over a walker
   * 
   * @param walker a walker object to see if the mouse hovers over it
   * @return true if the user's mouse is within the bounds of the Walker's PImage
   */
  public static boolean isMouseOver(Walker walker) {
    // if the user's mouse is within the boundaries of a walker, which can
    // be calculated by taking the x and y coordinates and adding/subtracting
    // half of the width and height of the image respectively, then return true.
    if ((Utility.mouseX() < walker.getPositionX() + frames[walker.getCurrentFrame()].width / 2
        && Utility.mouseX() > walker.getPositionX() - frames[walker.getCurrentFrame()].width / 2)
        && (Utility.mouseY() < walker.getPositionY() + frames[walker.getCurrentFrame()].height / 2
            && Utility.mouseY() > walker.getPositionY()
                - frames[walker.getCurrentFrame()].height / 2)) {
      return true;
    }
    return false;
  }

  /**
   * a callback method that is called when the mouse button is pressed
   */
  public static void mousePressed() {
    int walkerIndex = 8; // set at a high index just so that
                         // any given walker will have a 'lower'
                         // index and start moving
    // iterates to check which walker, if any, the mouse is
    // pressed on, and will make the lowest index walker move
    for (int i = 0; i < walkers.length; i++) {
      if (walkers[i] != null) {
        if (isMouseOver(walkers[i]) && i < walkerIndex) {
          walkerIndex = i;
          walkers[i].setWalking(true);
        }
      }
    }
  }

  /**
   * Performs an action based on what key the user presses
   * 
   * @param key the character that the user presses
   */
  public static void keyPressed(char key) {
    int walkersCount = 0; // used to see if there is room left to add a Walker
    int openIndex = 1; // the first open index to add a new Walker

    for (int i = 0; i < walkers.length; i++) {
      if (walkers[i] != null) {
        walkersCount++;
        openIndex = i + 1;
      }
    }

    if (key == 'a' && walkersCount < walkers.length) {
      walkers[openIndex] =
          new Walker(randGen.nextInt(Utility.width()), randGen.nextInt(Utility.height()));
    }
    if (key == 's') {
      for (int j = 0; j < walkers.length; j++) {
        if (walkers[j] != null) {
          walkers[j].setWalking(false);
        }
      }
    }
  }
}
