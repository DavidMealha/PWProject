import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;

public class SocialMediaSummarization {

	public static void main(String[] args) throws FileNotFoundException, ParseException {
		//load data sets
		DatasetParser dp = new DatasetParser();
		ArrayList<Tweet> tweets = dp.readTweets();
		ArrayList<InterestProfile> profiles = dp.readProfiles();
		
		for(Tweet t : tweets){
			System.out.println(t.toString());
		}
		
		for(InterestProfile ip : profiles){
			System.out.println(ip.toString());
		}
		
		//index the documents(tweets)
		
		//search the documents with the queries(interest profiles)
	}

}
