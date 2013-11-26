package scheduler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class RandomFileReader {
	private final FileInputStream inputStream;
	private final BufferedReader reader;
		
	public RandomFileReader() throws Error, Exception {		
		inputStream = new FileInputStream("random-numbers.txt");
		reader = new BufferedReader(
				new InputStreamReader(inputStream, Charset.forName("UTF-8")));
	}
	
	public String getNextLine() throws IOException{
		return reader.readLine();
	}

}