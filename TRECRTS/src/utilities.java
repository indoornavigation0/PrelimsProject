import cc.twittertools.index.IndexStatuses;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.*;

public class utilities {

    public Iterator getTweetProfiles(String queryProfilesLocation) throws IOException, ParseException {
        //String tweettitle = null;
        JSONParser jsonParser = new JSONParser();
        FileReader jsonfr = new FileReader(queryProfilesLocation);
        Object jsonobj = jsonParser.parse(jsonfr);
        JSONArray profiles = (JSONArray) jsonobj;
        Iterator profileitr = profiles.iterator();
        return profileitr;
    }

    public FileWriter createcsv(String filePath) throws IOException {
        String FILE_HEADER = "Topic ID,Query,TweetID,Rank,Doc Score,Doc ID,Follower Count";
        String outputPath = filePath.replace("index", "searchresults");
        FileWriter fileWriter = new FileWriter(outputPath + "/SearchResults_DescriptionTitle.csv");
        String NEW_LINE_SEPARATOR = "\n";

        try {
            fileWriter.append(FILE_HEADER.toString());
            fileWriter.append(NEW_LINE_SEPARATOR);
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        }
        return fileWriter;
    }

    public void writeScoreDoctocsv(FileWriter fileWriter, String filePath, ScoreDoc[] searchresults, String searchQuery) throws IOException
    {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(filePath)));
        String COMMA_DELIMITER = ",";
        String NEW_LINE_SEPARATOR = "\n";
        try {
            fileWriter.append(NEW_LINE_SEPARATOR);

            for (int i = 0; i < searchresults.length; i++) {
                Set<String> docVal = new TreeSet<>();
                docVal.add("id");
                docVal.add("followercount");
                fileWriter.append(searchQuery.toString());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append("Q0");
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append("'"+reader.document(searchresults[i].doc, docVal).getValues("id")[0].toString());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(Integer.toString(i+1));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(searchresults[i].score));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(searchresults[i].doc));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(reader.document(searchresults[i].doc, docVal).getValues("followercount")[0].toString());
                fileWriter.append(NEW_LINE_SEPARATOR);
                //System.out.println(searchQuery+"-----"+searchresults[i].doc+"----"+searchresults[i].score+"------"+reader.document(searchresults[i].doc, docVal));
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        }
    }

    public List<String> countWordsUsingStringTokenizer(String prTitle) {
        List<String> titleTokens = new ArrayList<>();
        if (prTitle == null || prTitle.isEmpty()) {
            return null;
        }
        StringTokenizer tokens = new StringTokenizer(prTitle);
        int i =0;
        while(tokens.hasMoreTokens()) {
            titleTokens.add(tokens.nextToken().toString());
            //titleTokens.remove(i);
            //i++;
        }
        return titleTokens;

    }

    public String boostQuery(String source, String key) {

        List<String> profileTitleTokens = countWordsUsingStringTokenizer(key);
        for (String titleToken : profileTitleTokens) {
            source = source.replace(titleToken, titleToken+"^1.5"); }

        source = source.replace('?',' ');
        source = source.replace(':',' ');
        source = source.replace("^1.5^1.5","^1.5");

        return source;
    }

    public Document getDocwithTweetID(String indexDirPath, String tweetid) throws IOException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexDirPath)));
        System.out.println("Number of Tweets in Corpus are: "+reader.numDocs());

        StringBuilder sb = new StringBuilder(tweetid);
        tweetid = sb.deleteCharAt(0).toString();

        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(new TermQuery(new Term("id", tweetid)), 1);

        if (docs.totalHits > 0) {
            Document d = searcher.doc(docs.scoreDocs[0].doc);
            return d;}
            else return null;
    }

    public int getDocIdwithTweetID(String indexDirPath, String tweetid) throws IOException {
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexDirPath)));
        System.out.println("Number of Tweets in Corpus are: "+reader.numDocs());
        int docID =0;
        StringBuilder sb = new StringBuilder(tweetid);
        tweetid = sb.deleteCharAt(0).toString();

        Set<String> docVal = new TreeSet<>();
        docVal.add("id");

        IndexSearcher searcher = new IndexSearcher(reader);
        PrintWriter writer = new PrintWriter("/Users/sri/Downloads/RTS2016/judgements/AllTweetsinIndex.txt", "UTF-8");
        for (int i =0; i<reader.maxDoc();i++) {
            //System.out.println("Next Tweet: "+searcher.doc(i,docVal).getValues("id")[0].toString());
            //if (searcher.doc(i,docVal).getValues("id")[0].toString().equals(tweetid))
            Document doc = reader.document(i);
            System.out.println("Next Tweet: "+doc.getField(IndexStatuses.StatusField.ID.name).stringValue());
            if (doc.getField(IndexStatuses.StatusField.ID.name).stringValue().equals(tweetid))
            {   docID = i; break;   }
        }
        if (docID !=0) {
                System.out.println("Tweet: " + tweetid + " found at Document ID:" + docID);
                writer.println("Tweet: " + tweetid + " found at Document ID:" + docID);
            }

        else {
                System.out.println("Tweet: " + tweetid + " not found");
                writer.println("Tweet: " + tweetid + " not found");
            }

        /**for (int i=0; i<reader.maxDoc(); i++) {
            Document doc = reader.document(i);
            System.out.println("TweetID: "+doc.getField(IndexStatuses.StatusField.ID.name).stringValue());
            writer.println(doc.getField(IndexStatuses.StatusField.ID.name).stringValue());
        }**/
        writer.close();
        return docID;
    }
 }
