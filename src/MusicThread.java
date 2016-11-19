import java.util.ArrayList;
import java.util.HashMap;

public class MusicThread extends Thread{
	private ArrayList<String> songs;
	private MusicPlayer player;
	 HashMap<String, Integer> soundConfig;
	
	public MusicThread(ArrayList<String> songs){
		this.songs = songs;
		this.player = new MusicPlayer(songs, true);
	}
	public MusicThread(ArrayList<String> songs, HashMap<String, Integer> soundConfig) {
		this.songs = songs;
		this.player = new MusicPlayer(songs, true);
		this.soundConfig = soundConfig;
	}
	public void run(){
		player.play();
		player.changeVolume(soundConfig.get("Volume") / 10.0);
	}
	public ArrayList<String> getSongs() {
		return songs;
	}
	public void setSongs(ArrayList<String> songs) {
		this.songs = songs;
	}
	public MusicPlayer getPlayer() {
		return player;
	}
	public HashMap<String, Integer> getSoundConfig() {
		return soundConfig;
	}
	public void setSoundConfig(HashMap<String, Integer> soundConfig) {
		this.soundConfig = soundConfig;
	}
	
	
}