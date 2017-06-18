import java.util.Calendar;
import java.util.GregorianCalendar;

public class Utils {
	
	public static final String[] INDEX_PATHS = new String[]
			{
				"docs/index/2016/08/02", 
				"docs/index/2016/08/03", 
				"docs/index/2016/08/04", 
				"docs/index/2016/08/05", 
				"docs/index/2016/08/06", 
				"docs/index/2016/08/07", 
				"docs/index/2016/08/08", 
				"docs/index/2016/08/09", 
				"docs/index/2016/08/10",
				"docs/index/2016/08/11"
			};
	
	public static final Calendar[] TWEETS_DATES = new Calendar[]
			{
				new GregorianCalendar(2016,7,2,23,59,59),
				new GregorianCalendar(2016,7,3,23,59,59),
				new GregorianCalendar(2016,7,4,23,59,59),
				new GregorianCalendar(2016,7,5,23,59,59),
				new GregorianCalendar(2016,7,6,23,59,59),
				new GregorianCalendar(2016,7,7,23,59,59),
				new GregorianCalendar(2016,7,8,23,59,59),
				new GregorianCalendar(2016,7,9,23,59,59),
				new GregorianCalendar(2016,7,10,23,59,59),
				new GregorianCalendar(2016,7,11,23,59,59)
			};
	
	public static final String[] TEST_CASES = new String[] {
			"STANDARD",
			"STANDARD_LOWER",
			"STANDARD_STOP",
			"STANDARD_SHINGLE",
			"STANDARD_COMMON",
//			"STANDARD_NGRAM",
//			"STANDARD_EDGE",
			"STANDARD_SNOWBALL",
			"STANDARD_LOWER_STOP",
			"STANDARD_LOWER_STOP_SNOWBALL",
			"STANDARD_STOP_SNOWBALL",
			"STANDARD_STOP_SNOWBALL_NGRAM",
			"STANDARD_LOWER_STOP_SHINGLE",
			"STANDARD_LOWER_STOP_SHINGLE_COMMON",
//			"STANDARD_LOWER_STOP_SHINGLE_COMMON_NGRAM",
//			"STANDARD_LOWER_STOP_SHINGLE_COMMON_NGRAM_EDGE",
//			"STANDARD_LOWER_STOP_SHINGLE_COMMON_NGRAM_EDGE_SNOWBALL"
	 };
	
	public static boolean areDatesEqual(Calendar date1, Calendar date2){
		if(date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) &&
		   date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH) && 
		   date1.get(Calendar.DAY_OF_MONTH) == date2.get(Calendar.DAY_OF_MONTH)){
			return true;
		}
		return false;
	}
}
