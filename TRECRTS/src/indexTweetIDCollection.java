import cc.twittertools.corpus.data.JsonStatusCorpusReader;
import cc.twittertools.corpus.data.Status;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.DFISimilarity;
import org.apache.lucene.search.similarities.IndependenceChiSquared;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class indexTweetIDCollection
 {

    public void IndexerwithTweeID(String corpusPath, String indexdirpath) throws IOException
    {
        Directory indexDirectory = FSDirectory.open(Paths.get(indexdirpath));
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setSimilarity(new DFISimilarity(new IndependenceChiSquared()));
        IndexWriter indexWriter = new IndexWriter(indexDirectory,config);

        File tweetsrddir = new File(corpusPath);

        JsonStatusCorpusReader jsoncorpusreader = new JsonStatusCorpusReader(tweetsrddir);
        //JsonStatusBlockReader jsonblockreader = new JsonStatusBlockReader(tweetsrddir);
        while(jsoncorpusreader.next() != null) {
            Status status = jsoncorpusreader.next();
            //String tweetlang = status.getLang();
            //String tweettext = status.getText();
            //String tweeturl = ((Status) status).getJsonObject().get("entities").getAsJsonObject().get("urls").getAsJsonArray()
            //		.elements.get(0).getAsJsonObject().get("url");

//if (status.getLang()=="en") {
            Document document = new Document();
            document.add(new StringField("id",Long.toString(status.getId()), Field.Store.YES));
            document.add(new TextField("txt",status.getText(),Field.Store.NO));
            document.add(new StringField("followercount",Integer.toString(status.getFollowersCount()),Field.Store.YES));
            indexWriter.addDocument(document);}
//		}
        indexWriter.close();
        System.out.println("Indexing Complete");
    }

}

