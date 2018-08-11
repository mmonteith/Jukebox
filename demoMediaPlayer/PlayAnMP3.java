package demoMediaPlayer;

/**
 * This code will play "LopingSting.mp3" assuming that file is in folder songfiles. 
 */
import java.io.File;
import java.net.URI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class PlayAnMP3 extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  private int songsPlayed = 0;

  @Override
  public void start(Stage stage) throws Exception {
    String path = "songfiles/Capture.mp3";

    // Need a File and URI object so the path works on all OSs
    File file = new File(path);
    URI uri = file.toURI();
    // Play one mp3 and and have code run when the song ends
    Media media = new Media(uri.toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);
    mediaPlayer.setOnEndOfMedia(new EndOfSongHandler());
    mediaPlayer.play();
    System.out.println(mediaPlayer.getOnEndOfMedia());
    System.out.println("You may need to shut this App down");
  }

  private class EndOfSongHandler implements Runnable {
    @Override
    public void run() {
      // This Runnable apparently does not get called all the time.
      // However, I have the same code in my Jukebox and it works.
      // This question "setOnEndOfMedia does not work" is unanswered on the web.
      songsPlayed++;
      System.out.println("Song ended, play song #" + songsPlayed);
      Platform.exit();
    }
  }
}