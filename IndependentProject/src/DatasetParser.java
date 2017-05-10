import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;

import javax.json.*;
import javax.json.stream.JsonParser;

public class DatasetParser {
	
	private static final String TWEETS_PATH = "docs/rts2016-qrels-tweets2016.jsonl";
	private static final String PROFILES_PATH = "docs/TREC2016-RTS-topics.json";
	
	/**
	 * Parse the string to json, now that the strange characters are removed
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static ArrayList<Tweet> readTweets() throws FileNotFoundException{
		
		try (BufferedReader br = new BufferedReader(new FileReader(TWEETS_PATH))) {
			String line = br.readLine();
			
			while (line != null) {
				JsonReader reader = Json.createReader(new StringReader(line));
				
				JsonObject tweetObject = reader.readObject();
				
				//gets tweet info
				String creationDate = tweetObject.getString("created_at");
				int id = tweetObject.getInt("id");
				String text = tweetObject.getString("text");
				
				//gets user info
				JsonObject userInfo = tweetObject.getJsonObject("user");
				int userId = userInfo.getInt("id");
				int userFollowers = userInfo.getInt("followers_count");
				
				//creationDate, id, text, userId, userFollowers
				Tweet t = new Tweet(new Date(), id, text, userId, userFollowers);
				
				System.out.println(t.toString());
				//tweetsParsed.add();
				
				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public ArrayList<Tweet> readProfiles(){
		return null;
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		readTweets();
	}
}
