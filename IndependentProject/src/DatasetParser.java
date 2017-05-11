import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.json.*;
import javax.json.stream.JsonParser;

public class DatasetParser {
	
	private static final String TWEETS_PATH = "docs/rts2016-qrels-tweets2016.jsonl";
	private static final String PROFILES_PATH = "docs/TREC2016-RTS-topics.json";
	
	/**
	 * Parse the string to json, now that the strange characters are removed
	 * @return
	 * @throws FileNotFoundException 
	 * @throws ParseException 
	 */
	public static ArrayList<Tweet> readTweets() throws FileNotFoundException, ParseException{
		ArrayList<Tweet> tweetsParsed = new ArrayList<Tweet>();
		
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
				Tweet t = new Tweet(convertStringToDate(creationDate), id, text, userId, userFollowers);
				tweetsParsed.add(t);
				
				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tweetsParsed;
	}
	
	/**
	 * Method to parse the string date to a Date Object.
	 * tweets date comes with this format: "Thu Aug 11 20:48:58 +0000 2016"
	 * @param dateTime
	 * @return
	 * @throws ParseException
	 */
	private static Date convertStringToDate(String dateTime) throws ParseException{
		
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
        formatter.setLenient(true);
        Date newDate = formatter.parse(dateTime);
        return newDate;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Tweet> readProfiles(){
		return null;
	}
	
	public static void main(String[] args) throws FileNotFoundException, ParseException{
		ArrayList<Tweet> tweets = readTweets();
		for(Tweet t : tweets){
			if(t.getUserFollowers() > 10000000) {
				System.out.println(t.toString());
			}
		}
		System.out.println("Finished Parsing Datasets");
	}
}
