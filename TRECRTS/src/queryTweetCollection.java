import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class queryTweetCollection extends indexTweetCollection {

    public ScoreDoc[] QueryBuilder(String indexdirpath, String tweettitle, Similarity similarity) throws IOException {
        IndexReader reader = null;
        try {
            reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexdirpath)));
        } catch (IOException e) { e.printStackTrace(); }
        float mu = 5000;
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(similarity);
        EnglishAnalyzer analyzer = new EnglishAnalyzer();
        TopScoreDocCollector collector = TopScoreDocCollector.create(20000);

        Query tweetQuery = null;
        try {
            tweetQuery = new QueryParser("txt", analyzer).parse(tweettitle);
        } catch (org.apache.lucene.queryparser.classic.ParseException e) { e.printStackTrace(); }
        try {
            searcher.search(tweetQuery,collector);
        } catch (IOException e) { e.printStackTrace(); }
        ScoreDoc[] tweetsearch = collector.topDocs().scoreDocs;

        reader.close();
        return tweetsearch;
            }


}


