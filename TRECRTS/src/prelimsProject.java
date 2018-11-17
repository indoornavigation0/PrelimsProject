import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import sun.jvm.hotspot.ui.tree.FloatTreeNodeAdapter;

import javax.swing.text.Utilities;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class prelimsProject {

    public static void main(String[] args) throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException {

        /**String corpusSource = "/data/data/collections/microblog2017/RTS17CrawlData-All";
        String indexLocation = "/usa/srikumar/Lucene/BM25/index";
        String queryProfilesLocation = "/usa/srikumar/Lucene/BM25/profiles/profiles.json";**/
        queryTweetCollection qt = new queryTweetCollection();
        indexTweetCollection it = new indexTweetCollection();
        getScoreTweetDocument gst = new getScoreTweetDocument();
        String indexLocation;// = "/Users/sri/Downloads/RTS2017/BM25index";
        Similarity indexSimilarity;// = new BM25Similarity();
        String queryProfilesLocation = "/Users/sri/Downloads/RTS2017/profiles/profiles.json";

        //************************************** Start of Indexing **************************************

        String corpusSource = "/Users/sri/Downloads/RTS2017Data";

        indexLocation = "/Users/sri/Downloads/RTS2016/index/DFRindex";
        indexSimilarity = new DFRSimilarity(new BasicModelBE(), new AfterEffectB(), new NormalizationH3());
        /**it.Indexer(corpusSource, indexLocation, indexSimilarity);

        indexLocation = "/Users/sri/Downloads/RTS2017/index/BM25index";
        indexSimilarity = new BM25Similarity();
        it = new indexTweetCollection();
        it.Indexer(corpusSource, indexLocation, indexSimilarity);

        indexLocation = "/Users/sri/Downloads/RTS2017/index/LMDindex";
        indexSimilarity = new LMDirichletSimilarity();
        it = new indexTweetCollection();
        it.Indexer(corpusSource, indexLocation, indexSimilarity);

        indexLocation = "/Users/sri/Downloads/RTS2017/index/LMJindex";
        indexSimilarity = new LMJelinekMercerSimilarity(0.2F);
        it = new indexTweetCollection();
        it.Indexer(corpusSource, indexLocation, indexSimilarity);

        indexLocation = "/Users/sri/Downloads/RTS2017/index/DFIindex";
        indexSimilarity = new DFISimilarity(new IndependenceChiSquared());
        it = new indexTweetCollection();
        it.Indexer(corpusSource, indexLocation, indexSimilarity);

        indexLocation = "/Users/sri/Downloads/RTS2016/DFRindex";
        indexSimilarity = new DFRSimilarity(new BasicModelBE(), new AfterEffectB(), new NormalizationH3());
        it = new indexTweetCollection();
        it.Indexer(corpusSource, indexLocation, indexSimilarity);

        indexLocation = "/Users/sri/Downloads/RTS2016/IBindex";
        indexSimilarity = new IBSimilarity(new DistributionLL(), new LambdaDF(), new NormalizationH3());
        it = new indexTweetCollection();
        it.Indexer(corpusSource, indexLocation, indexSimilarity);**/

        // ======================================= End of Indexing=============================
        // *************************** Start of Querying with Profiles *****************************

        /**utilities ut = new utilities();
        Iterator profileIterator = ut.getTweetProfiles(queryProfilesLocation);
        FileWriter csvWriter = ut.createcsv(indexLocation);
        int i =0;
        while(profileIterator.hasNext()) {
            JSONObject profile = (JSONObject) profileIterator.next();
            String profileNarrative = profile.get("narrative").toString();
            String profileDesc = profile.get("description").toString();
            String profileTitle = profile.get("title").toString();
            String searchQuery =profileTitle;
         //searchQuery = ut.boostQuery(searchQuery, profileTitle, similarity);

                ScoreDoc[] tweetSearchResults = qt.QueryBuilder(indexLocation,searchQuery);

        ut.writeScoreDoctocsv(csvWriter, indexLocation,tweetSearchResults,topicid);

        }
        csvWriter.flush();
        csvWriter.close();
        System.out.println("Search Results are saved to CSV at : "
                            +indexLocation.replace("index", "searchresults")+"/SearchResults_DescriptionTitle.csv");**/

        // ======================================= End of Querying with Profiles ============================
        gst.getScoresofRelJudgements(indexLocation, indexSimilarity);
    }

}
