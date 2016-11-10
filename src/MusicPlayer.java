import java.io.File;
import java.util.ArrayList;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MusicPlayer{
	private boolean loop;
	private ArrayList<String> songs;
	MediaPlayer player;
	private int curSong;
	private boolean playing;
	public final double maxVol;
	public final double minVol;
	
	/** Constructor-
	 * songs: List of song titles, not resource paths,
	 * loop: Whether or not to start at beginning at end of music
	 * @param songs
	 * @param loop
	 */
	public MusicPlayer(ArrayList<String> songs, boolean loop){
		maxVol = 1;
		minVol = 0;
		loadToolkit();
		this.loop = loop;
		this.songs = songs;
		curSong = 0;
		playing = false;
	}
	
	/**Constructor- Song: specific song to add, loop: Whether or not to loop the song*/
	public MusicPlayer(String song, boolean loop){
		maxVol = 100;
		minVol = 0;
		loadToolkit();
		songs = new ArrayList<String>();
		this.songs.add(song);
		this.loop = loop;
		this.curSong = 0;
		playing = false;
	}
	/** Constructor- songs: List of song titles, not resource paths*/
	public MusicPlayer(ArrayList<String> songs){
		maxVol = 100;
		minVol = 0;
		loadToolkit();
		this.loop = false;
		this.songs = songs;
		curSong = 0;
		playing = false;
	}
	/**Constructor- Song: specific song to add*/
	public MusicPlayer(String song){
		maxVol = 100;
		minVol = 0;
		loadToolkit();
		songs = new ArrayList<String>();
		this.songs.add(song);
		this.loop = false;
		this.curSong = 0;
		playing = false;
	}
	/**Default constructor*/
	public MusicPlayer(){
		maxVol = 100;
		minVol = 0;
		loadToolkit();
		songs = new ArrayList<String>();
		loadSongsFromResources();
		loop = false;
		curSong = 0;
		playing = false;
	}
	/**Constructor- loop: whether or not to loop music, when songs added*/
	public MusicPlayer(boolean loop){
		maxVol = 100;
		minVol = 0;
		loadToolkit();
		songs = new ArrayList<String>();
		loadSongsFromResources();
		this.loop = loop;
		this.curSong = 0;
		playing = false;
	}
	private void loadToolkit() {
		JFXPanel toolkitLoader= new JFXPanel();
	}
	private void loadSongsFromResources() {
		//To Implement, adds any and all resources with the .mp3 format
		
	}
	/**Plays the songs contained within the songs array*/
	public void play(){
		if(songs.size() == 0)
			throw new NoSongsException();
		Media media = new Media(this.getClass().getResource(songs.get(curSong)).toString());
		player = new MediaPlayer(media);
		player.play();
		playing = true;
		curSong ++;
		if(loop && curSong >= songs.size()){
			curSong = 0;
			player.setOnEndOfMedia(new Runnable(){
				public void run(){
					play();
				}
			});
		}else if(curSong < songs.size()){
			player.setOnEndOfMedia(new Runnable(){
				public void run(){
					play();
				}
			});
		}else{
			player.setOnEndOfMedia(new Runnable(){
				public void run(){
					player = null;
					playing = false;
					curSong = 0;
				}
			});
		}
	}
	/**Plays the a single song, at ends nullifies the player, takes a song name
	 * @param resource*/
	public void playOne(String resource){
		MediaPlayer player= new MediaPlayer(new Media(this.getClass().getResource(resource).toString()));
		player.play();
		player.setOnEndOfMedia(new Runnable(){
			public void run(){
				playerReset();
			}
		});
	}
	
	public void overrideReturn(String resource){
		if(playing && player != null){
			Duration curTime = player.getCurrentTime();
			player = new MediaPlayer(new Media(this.getClass().getResource(resource).toString()));
			player.setOnEndOfMedia(new Runnable(){
				public void run(){
					playAtPoint(curTime);
				}
			});
		}
	}
	
	private void playAtPoint(Duration time) {
		player = new MediaPlayer(new Media(this.getClass().getResource(songs.get(curSong)).toString()));
		player.setStartTime(time);
		curSong++;
		player.setOnEndOfMedia(new Runnable(){
			public void run(){
				play();
			}
		});
	}
	
	public void playAtPointResource(String resource, Duration time){
		player = new MediaPlayer(new Media(this.getClass().getResource(resource).toString()));
		player.setStartTime(time);
		player.play();
		player.setOnEndOfMedia(new Runnable(){
			public void run(){
				play();
			}
		});
	}
	
	public void pause(){
		if(playing && player != null){
			player.pause();
			playing = false;
		}else if(!playing && player != null){
			System.out.println("Player is not playing");
		}else{
			System.out.println("Player is not created, play a song first");
		}
	}
	
	public void resume(){
		if(!playing){
			player.play();
			playing = true;
		}else
			System.out.println("Player is playing");
	}
	
	public void stop(){
		if(player != null){
			player.stop();
			playing = false;
		}else
			System.out.println("Player is not initialized, play a song first");
	}
	
	/** Changes the volume, the volume must reside between 0 and 1
	 * @param double
	 */
	public void changeVolume(Double volume){
		player.setVolume(volume);
	}
	
	public void playerReset(){
		player = null;
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}
	
	public void loop(){
		this.loop = true;
	}
	public void noLoop(){
		this.loop = false;
	}

	public ArrayList<String> getSongs() {
		return songs;
	}

	public void setSongs(ArrayList<String> songs) {
		this.songs = songs;
	}

	public MediaPlayer getPlayer() {
		return player;
	}

	public void setPlayer(MediaPlayer player) {
		this.player = player;
	}

	public int getCurSong() {
		return curSong;
	}

	public void setCurSong(int curSong) {
		this.curSong = curSong;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}
	public void addSong(String name){
		songs.add(name);
	}

	/**
	 * Method to delete a song from the list of song names, returns a boolean for success or not
	 * @param songName
	 * @return deleted
	 */
	public boolean removeSong(String songName){
		if(songs.contains(songName)){
			songs.remove(songName);
			return true;
		}else
			return false;
	}
	public void tempFirstSong(){
		player = new MediaPlayer(new Media(this.getClass().getResource(songs.get(0)).toString()));
	}
	public void nextSong(){
		System.out.println("Here");
		player.stop();
		player = null;
		curSong ++;
		if(curSong >= songs .size())
			curSong = 0;
		play();
	}
	public void prevSong(){
		System.out.println("Here 2");
		player.stop();
		player = null;
		curSong --;
		if(curSong < 0)
			curSong = songs.size() - 1;
		play();
	}

	public void setTime(int progressBarVal) {
		Duration time = new Duration(progressBarVal * 1000);
	       player.seek(time);
	}
}

class NoSongsException extends RuntimeException{
	
}