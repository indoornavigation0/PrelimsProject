import cc.twittertools.corpus.data.JsonStatusCorpusReader;
import cc.twittertools.corpus.data.Status;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.apache.lucene.analysis.standard.StandardAnalyzer;

public class indexTweetCollection {

	public void Indexer(String corpusPath, String indexdirpath, Similarity indexSimilarity) throws IOException
	{
		Directory indexDirectory = FSDirectory.open(Paths.get(indexdirpath));
		EnglishAnalyzer analyzer = new EnglishAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setSimilarity(indexSimilarity);
		IndexWriter indexWriter = new IndexWriter(indexDirectory,config);

		File tweetsrddir = new File(corpusPath);

		JsonStatusCorpusReader jsoncorpusreader = new JsonStatusCorpusReader(tweetsrddir);
		Status status;
		//JsonStatusBlockReader jsonblockreader = new JsonStatusBlockReader(tweetsrddir);
		while((status = jsoncorpusreader.next()) != null) {
			//Status status = jsoncorpusreader.next();
			//String tweetlang = status.getLang();
			//String tweettext = status.getText();
			//String tweeturl = ((Status) status).getJsonObject().get("entities").getAsJsonObject().get("urls").getAsJsonArray()
			//		.elements.get(0).getAsJsonObject().get("url");

if ((status != null) && (status.getLang().equals("en"))) {
			Document document = new Document();
			document.add(new StringField("id",Long.toString(status.getId()),Field.Store.YES));
			document.add(new TextField("txt",status.getText(),Field.Store.NO));
			document.add(new StringField("followercount",Integer.toString(status.getFollowersCount()),Field.Store.YES));
			indexWriter.addDocument(document);}
		}
		int numDocs = indexWriter.numDocs();
		System.out.println("No of Indexes in "+indexdirpath+" are "+numDocs);
		indexWriter.close();
		System.out.println("Indexing Complete");
	}

}