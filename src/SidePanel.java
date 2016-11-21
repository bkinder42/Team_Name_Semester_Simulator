import java.util.HashMap;

//500 x 768
public class SidePanel{
	final int width = 500;
	private HashMap<String, String> conMap;
	private String jIP;
	private int jPort;
	
	public SidePanel(HashMap<String, String> conMap, int jPort, String jIP){
		this.conMap = conMap;
		this.jPort = jPort;
		this.jIP = jIP;
	}
}