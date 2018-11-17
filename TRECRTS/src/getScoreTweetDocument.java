import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONObject;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class getScoreTweetDocument extends indexTweetCollection {

    public static PrintWriter writer;

    {
        try {
            writer = new PrintWriter("/Users/sri/Downloads/RTS2016/judgements/AllTweetsinIndex.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void getScoresofRelJudgements(String indexLocation, Similarity similarity) throws IOException, org.apache.lucene.queryparser.classic.ParseException {

        String relJudgementPath = "/Users/sri/Downloads/RTS2016/judgements/RelevanceJudgements2016.csv";
        String relJudgementopPath = "/Users/sri/Downloads/RTS2016/judgements/RelevanceJudgements2016_op.csv";
        String queryProfilesLocation = "/Users/sri/Downloads/RTS2016/profiles/profiles.json";
        utilities ut = new utilities();

        CSVReader csvReader = new CSVReader(new FileReader(relJudgementPath));
        CSVWriter csvWriter = new CSVWriter(new FileWriter(relJudgementopPath), ',');
        String[] nextRelJudgement = null;
        int i =0;
        while ((nextRelJudgement= csvReader.readNext()) != null) {
            String topicID = nextRelJudgement[0];
            String tweetID = nextRelJudgement[2];
            List<String> relJudgementOP = new ArrayList(Arrays.asList(nextRelJudgement));

            try {
                Iterator profileIterator = ut.getTweetProfiles(queryProfilesLocation);
                while(profileIterator.hasNext()) {
                    JSONObject profile = (JSONObject) profileIterator.next();
                    if (profile.get("topid").equals(topicID)) {
                        String profileNarrative = profile.get("narrative").toString();
                        String profileDesc = profile.get("description").toString();
                        String profileTitle = profile.get("title").toString();
                        String searchQuery =profileTitle;
                        //searchQuery = ut.boostQuery(searchQuery, profileTitle);

                        // ScoreDoc[] indexDocID = gst.getDocIDforTweet(indexLocation,nextRelJudgement[2]);
                        float tweetScore = QueryBuilderonSingleIndex(indexLocation,searchQuery, tweetID, similarity);
                        relJudgementOP.add(Float.toString(tweetScore));
                        String[] relJudgementOPArr = relJudgementOP.toArray(new String[4]);
                        csvWriter.writeNext(relJudgementOPArr);
                        //System.out.println(relJudgementOPArr[0]+":"+relJudgementOPArr[2]+":"+relJudgementOPArr[3]+":"+relJudgementOPArr[4]);
                        //System.out.println("Written score for: "+i+" th Record");

                    }
                }} catch (org.json.simple.parser.ParseException e) { System.out.println(e.getMessage()); }
            //int docID = ut.getDocIdwithTweetID(indexLocation,tweetID);
            i++;
        }
        csvReader.close();
        csvWriter.close();
        writer.close();
    }

    public static float QueryBuilderonSingleIndex(String indexdirpath, String tweettitle, String tweetID, Similarity similarity)
            throws IOException, ParseException {
        IndexReader reader = null;
        utilities ut = new utilities();
        try {
            reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexdirpath)));
        } catch (IOException e) { e.printStackTrace(); }
        ScoreDoc[] tweetIDSearch = getDocIDforTweet(indexdirpath, tweetID); /**Get the Document ID for a given Tweet**/
        float score = 0;
        if (tweetIDSearch.length > 0) {
            writer.println("Tweet: " + tweetID + " found at Document ID:" + tweetID);
            System.out.println("Tweet: " + tweetID + " found at Document ID:" + tweetIDSearch[0].doc);
            int indexID = tweetIDSearch[0].doc;
            //Document document = ut.getDocwithTweetID(indexdirpath, tweetID);
            //int docID = ut.getDocIdwithTweetID(indexdirpath, tweetID);
            if (indexID != 0) {
            IndexSearcher searcher = new IndexSearcher(reader);
            searcher.setSimilarity(similarity);
            EnglishAnalyzer analyzer = new EnglishAnalyzer();

            Query tweetQuery = null;
            try {
                tweetQuery = new QueryParser("txt", analyzer).parse(tweettitle);
            } catch (org.apache.lucene.queryparser.classic.ParseException e) { e.printStackTrace(); }
            Explanation explain = searcher.explain(tweetQuery, indexID);
            score = explain.getValue();
            System.out.println(explain);
            Explanation explanation[] = explain.getDetails();
            //System.out.println(explanation[0].toString());
            }   }
            else {
            writer.println("Tweet: " + tweetID + " not found in Corpus");
            System.out.println("Tweet: " + tweetID + " not found in Corpus");}
            reader.close();
            return score;
  }

    public static ScoreDoc[] getDocIDforTweet(String indexDirPath, String tweetid) throws IOException, ParseException {

        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexDirPath)));
        //System.out.println("Number of Tweets in Corpus are: "+reader.numDocs());
        StringBuilder sb = new StringBuilder(tweetid);
        tweetid = sb.deleteCharAt(0).toString();
        org.apache.lucene.document.Document document;
        Set<String> docVal = new TreeSet<>();

        docVal.add("id");
        IndexSearcher searcher = new IndexSearcher(reader);
        StandardAnalyzer analyzer = new StandardAnalyzer();
        TopScoreDocCollector collector = TopScoreDocCollector.create(1);
        Query tweetQuery = new QueryParser("id", analyzer).parse(tweetid);
        searcher.search(tweetQuery,collector);
        ScoreDoc[] tweetIDSearch = collector.topDocs().scoreDocs;
        /**for (int i =0; i <reader.numDocs(); i++) {
         document = reader.document(i, docVal);
         if (document.getValues("id")[0].toString().equals(tweetid)) {
         indexID = i;
         }
         }**/
        if (tweetIDSearch.length == 0) {System.out.println("Tweet "+tweetid+" not found in Corpus");}
        reader.close();
        return tweetIDSearch;
    }
}

