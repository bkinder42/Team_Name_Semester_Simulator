import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Logger{
	private int lineNum;
	private final  File LOG = new File("Log.txt");
	public Logger(){
		lineNum = 0;
	}
	public void log(String txt){
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(LOG));
			Scanner inFile = new Scanner(LOG);
			ArrayList<String> readIn = new ArrayList<String>();
			while(inFile.hasNext()){
				readIn.add(inFile.nextLine());
			}
			for(String s : readIn){
				lineNum ++;
				writer.write(lineNum + ": " + s + "\n");
				writer.flush();
			}
			writer.write(lineNum + ": " + txt);
			writer.close();
			inFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}