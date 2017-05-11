import java.io.BufferedReader;
import java.io.File;
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
	
	private ArrayList<Tweet> tweetsParsed;
	private ArrayList<InterestProfile> profilesParsed;
	
	
	public DatasetParser() {
		tweetsParsed = new ArrayList<Tweet>();
		profilesParsed = new ArrayList<InterestProfile>();
	}
	
	/**
	 * Parse the string to json, now that the strange characters are removed
	 * @return
	 * @throws FileNotFoundException 
	 * @throws ParseException 
	 */
	public ArrayList<Tweet> readTweets() throws FileNotFoundException, ParseException{		
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
	private Date convertStringToDate(String dateTime) throws ParseException{
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
        formatter.setLenient(true);
        Date newDate = formatter.parse(dateTime);
        return newDate;
	}
	
	
	/**
	 * 
	 * @return
	 * @throws FileNotFoundException 
	 */
	public ArrayList<InterestProfile> readProfiles() throws FileNotFoundException{
		JsonReader reader = Json.createReader(new FileReader(PROFILES_PATH));
		JsonArray profilesArray = reader.readArray();
		
		for(int i = 0; i < profilesArray.size(); i++){
			JsonObject profileObj = profilesArray.getJsonObject(i);
			String topid = profileObj.getString("topid");
			String title = profileObj.getString("title");
			String description = profileObj.getString("description");
			String narrative = profileObj.getString("narrative");
			
			InterestProfile ip = new InterestProfile(topid, title, description, narrative);
			profilesParsed.add(ip);
		}
				
		return profilesParsed;
	}
	
}
