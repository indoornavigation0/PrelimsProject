import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class queryTweetCollection extends indexTweetCollection {

    public void QueryBuilder(String indexdirpath) throws IOException, ParseException {
        float score[];
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexdirpath)));
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(10);

        JSONParser jsonParser = new JSONParser();
        try (FileReader jsonfr = new FileReader("/Users/sri/Downloads/RTS2017/profiles/profiles.json")) {

            Object jsonobj = jsonParser.parse(jsonfr);
            JSONArray profiles = (JSONArray) jsonobj;

            profiles.forEach(profile -> parseProfileObject((JSONObject) profile));
                //Query tweetQuery = new QueryParser()

        }
    }


    private static void parseProfileObject(JSONObject pr) {
        JSONObject profileObject = (JSONObject) pr.get("pr");
        String narrative = (String) profileObject.get("narrative");
        String title = (String) profileObject.get("title");
        String topid = (String) profileObject.get("topid");
        String description = (String) profileObject.get("description");
        }

}