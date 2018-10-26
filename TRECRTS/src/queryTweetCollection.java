import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;

public class queryTweetCollection extends indexTweetCollection {

    public void QueryBuilder(String indexdirpath) throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException {
        float score[];
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexdirpath)));
        IndexSearcher searcher = new IndexSearcher(reader);
        StandardAnalyzer analyzer = new StandardAnalyzer();
        TopScoreDocCollector collector = TopScoreDocCollector.create(10);

        JSONParser jsonParser = new JSONParser();
        try (FileReader jsonfr = new FileReader("/Users/sri/Downloads/RTS2017/profiles/profiles.json")) {
            Object jsonobj = jsonParser.parse(jsonfr);
            JSONArray profiles = (JSONArray) jsonobj;
            Iterator it = profiles.iterator();
            int i = 0;
            while (it.hasNext()) {
                JSONObject profile = (JSONObject) it.next();
                String tweettitle = profile.get("title").toString();
                Query tweetQuery = new QueryParser(tweettitle, analyzer).parse(tweettitle);
                searcher.search(tweetQuery,collector);
                ScoreDoc[] tweetsearch = collector.topDocs().scoreDocs;
                System.out.println(tweetsearch[i].doc+"----"+tweetsearch[i].score);
                i++;
            }
        }
    }
    /**private static void parseProfileObject(JSONObject pr) {
        JSONObject profileObject = (JSONObject) pr.get("pr");
        String narrative = (String) profileObject.get("narrative");
        String title = (String) profileObject.get("title");
        String topid = (String) profileObject.get("topid");
        String description = (String) profileObject.get("description");
        }**/
}