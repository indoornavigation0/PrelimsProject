import org.apache.lucene.search.ScoreDoc;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class prelimsProject {

    public static void main(String[] args) throws IOException, ParseException {

        String corpusSource = "/Users/sri/Downloads/RTS2017Data";
        String indexLocation = "/Users/sri/Downloads/RTS2017/index";
        String queryProfilesLocation = "/Users/sri/Downloads/RTS2017/profiles/profiles.json";

        //indexTweetCollection it = new indexTweetCollection();
        //it.Indexer(corpusSource, indexLocation);

        utilities ut = new utilities();
        Iterator profileIterator = ut.getTweetProfiles(queryProfilesLocation);
        FileWriter csvWriter = ut.createcsv(indexLocation);
        while(profileIterator.hasNext()) {
            JSONObject profile = (JSONObject) profileIterator.next();
            String searchQuery = profile.get("title").toString();

                queryTweetCollection qt = new queryTweetCollection();
                ScoreDoc[] tweetSearchResults = qt.QueryBuilder(indexLocation,searchQuery);

        ut.writetocsv(csvWriter, indexLocation,tweetSearchResults,searchQuery); }
        csvWriter.flush();
        csvWriter.close();
        System.out.println("Search Results are saved to CSV at : "+indexLocation.replace("index", "searchresults")+"/SearchResults.csv");
    }

}
