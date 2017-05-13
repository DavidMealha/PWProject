import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileHandler {
	
	public void writeFile(List<Result> results){
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("docs/results.txt"))) {
			bw.write(String.format("%-15s %-10s %-10s %-20s %-10s %-10s %-10s \n", "CreationDate", "topic_id", "Q0", "tweet_id", "Rank", "Score", "RunID"));

			for (Result result : results) { 
				bw.write(result.toString());
				
			}
			bw.write("\n");
			
			// no need to close it.
			//bw.close();

			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

}
