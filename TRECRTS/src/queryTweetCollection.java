import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class queryTweetCollection extends indexTweetCollection {

    public ScoreDoc[] QueryBuilder(String indexdirpath, String tweettitle) throws IOException {
        IndexReader reader = null;
        try {
            reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexdirpath)));
        } catch (IOException e) { e.printStackTrace(); }

        IndexSearcher searcher = new IndexSearcher(reader);
        StandardAnalyzer analyzer = new StandardAnalyzer();
        TopScoreDocCollector collector = TopScoreDocCollector.create(10);

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

