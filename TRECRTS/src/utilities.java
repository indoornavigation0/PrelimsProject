import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

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
        String FILE_HEADER = "Query Text,Doc ID,Doc Score,TweetID";
        String outputPath = filePath.replace("index", "searchresults");
        FileWriter fileWriter = new FileWriter(outputPath + "/SearchResults.csv");
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

    public void writetocsv(FileWriter fileWriter, String filePath, ScoreDoc[] searchresults, String searchQuery) throws IOException {

        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(filePath)));
        String COMMA_DELIMITER = ",";
        String NEW_LINE_SEPARATOR = "\n";
        try {
            fileWriter.append(NEW_LINE_SEPARATOR);

            for (int i = 0; i < searchresults.length; i++) {
                Set<String> docVal = new TreeSet<>();
                docVal.add("id");
                fileWriter.append(searchQuery.toString());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(searchresults[i].doc));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(searchresults[i].score));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append("'"+reader.document(searchresults[i].doc, docVal).getValues("id")[0].toString());
                fileWriter.append(NEW_LINE_SEPARATOR);
                //System.out.println(searchQuery+"-----"+searchresults[i].doc+"----"+searchresults[i].score+"------"+reader.document(searchresults[i].doc, docVal));
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        }
    }
}
