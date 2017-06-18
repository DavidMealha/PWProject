/**
 * 
 */


import java.util.List;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.commongrams.*;
import org.apache.lucene.analysis.miscellaneous.FingerprintFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.shingle.ShingleFilter;

public class CustomAnalyzer extends StopwordAnalyzerBase {

	static List<String> stopWords = Arrays.asList("to", "the", "in", "and", "a", "of", "for", "is", "on",
			"the", "at", "it", "from", "the", "i", "be", "from", "be", "are", "my", "this", "by", "that");
	
	static CharArraySet stopSet = new CharArraySet(stopWords, true);

	/** Default maximum allowed token length */
	private int maxTokenLength = 25;
	
	private boolean useStandardFilter;
	private boolean useLowerCaseFilter;
	private boolean useStopWordsFilter;
	private boolean useShingleFilter;
	private boolean useCommonGramsFilter;
	private boolean useNGramTokenFilter;
	private boolean useEdgeNGramFilter;
	private boolean useSnowballFilter;

	/**
	 * Builds an analyzer with the default stop words ({@link #STOP_WORDS_SET}).
	 */
	public CustomAnalyzer(String config) {
		super(stopSet);
		convertConfig(config);
	}

	@Override
	protected TokenStreamComponents createComponents(final String fieldName) {
		
		final StandardTokenizer src = new StandardTokenizer();
		
		TokenStream tok = null;
		
		if (useStandardFilter) {
			// text into non punctuated text
			tok = new StandardFilter(src);					
		}
		
		if(useLowerCaseFilter) {
			// changes all texto into lowercase
			tok = new LowerCaseFilter(tok);					
		}
		
		if(useStopWordsFilter) {
			// removes stop words
			tok = new StopFilter(tok, stopwords);			
		}

		if(useShingleFilter) {
			// creates word-grams with neighboring works
			tok = new ShingleFilter(tok, 2, 3);				
		}
		
		if(useCommonGramsFilter) {
			// creates word-grams with stopwords
			tok = new CommonGramsFilter(tok, stopwords);	
		}
		
		if(useNGramTokenFilter) {
			// creates unbounded n-grams
			tok = new NGramTokenFilter(tok,2,5);			
		}
		
		if(useEdgeNGramFilter) {
			// creates word-bounded n-grams
			tok = new EdgeNGramTokenFilter(tok,2,3);		
		}
		
		if(useSnowballFilter) {
			// stems workds according to the specified language
			tok = new SnowballFilter(tok, "English");	
		}
		
		return new TokenStreamComponents(src, tok) {
			@Override
			protected void setReader(final Reader reader) {	
				src.setMaxTokenLength(CustomAnalyzer.this.maxTokenLength);
				//super.setReader(new HTMLStripCharFilter(reader));
				super.setReader(reader);
			}
		};
	}

	@Override
	protected TokenStream normalize(String fieldName, TokenStream in) {
		TokenStream result = new StandardFilter(in);
		result = new LowerCaseFilter(result);
		return result;
	}
	
	private void convertConfig(String config){
		String[] configSplitted = config.split("_");
		
		for(String conf: configSplitted){
			
			switch (conf) {
			case "STANDARD":
				useStandardFilter = true;
				break;
			case "LOWER":
				useLowerCaseFilter = true;
				break;
			case "STOP":
				useStopWordsFilter = true;
				break;
			case "SHINGLE":
				useShingleFilter = true;
				break;
			case "COMMON":
				useCommonGramsFilter = true;
				break;
			case "NGRAM":
				useNGramTokenFilter = true;
				break;
			case "EDGE":
				useEdgeNGramFilter = true;
				break;
			case "SNOWBALL":
				useSnowballFilter = true;
				break;
			default:
				break;
			}
		}
	}
}
