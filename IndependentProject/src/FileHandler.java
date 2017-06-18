import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

public class FileHandler {
	
	public void writeFile(List<Result> results, String path){
		writeTrecFormat(results, path);
		writeJSONFormat(results, path);	
	}
	
	private void writeTrecFormat(List<Result> results, String path){
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(path + ".txt"))) {
			bw.write(String.format("%-15s %-10s %-10s %-20s %-10s %-15s %-10s \n", "CreationDate", "topic_id", "Q0", "tweet_id", "Rank", "Score", "RunID"));

			for (Result result : results) { 
				bw.write(result.toString());
				
			}
			//bw.write("\n");
			
			// no need to close it.
			//bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeJSONFormat(List<Result> results, String path){
		JsonArrayBuilder array = Json.createArrayBuilder();
		
		for(Result r : results){
			JsonObjectBuilder obj = Json.createObjectBuilder();
			obj.add("tweetId", r.getAnswerId());
			obj.add("profileId", r.getQueryId());
//			obj.add("tweetText", r.getTweetText());
			obj.add("score", r.getScore());
			obj.add("date", r.getDate());
//			obj.add("userId", r.getUserId());
//			obj.add("userName", r.getUserName());
//			obj.add("userAvatar", r.getUserAvatar());
//			obj.add("userFollowers", r.getUserFollowers());
			array.add(obj);
		}
		JsonArray arrayJson = array.build();
		
//		OutputStream os = null;
//		try {
//			os = new FileOutputStream("docs/results.json");
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		}
//		JsonWriter jsonWriter = Json.createWriter(os);
//		
//		jsonWriter.writeArray(arrayJson);
//		jsonWriter.close();
//		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(path + ".json"))) {
			bw.write(arrayJson.toString());

			System.out.println("Done JSON");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
