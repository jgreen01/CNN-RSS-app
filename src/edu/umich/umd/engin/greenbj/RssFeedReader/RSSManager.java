package edu.umich.umd.engin.greenbj.RssFeedReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class RSSManager {
	
	private FeedData rssFeed;
	
	private static String TAG = "RSSManager";
	
	private URL feedURL;

	
	public RSSManager(String rssUrl){
		
		try {
			feedURL = new URL(rssUrl);
		} catch (MalformedURLException e) {
			//feedURL = null;
			throw new RuntimeException(e);
		}
		
		try {
			readData();
		} catch (XmlPullParserException e) {
			Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
	}
	
	private void readData() 
				throws XmlPullParserException, IOException {
		
		FeedData rssData = null;
		boolean isHeader = true;
		
		String description = "";
		String title = "";
		String link = "";
		String language = "";
		String copyright = "";
		String author = "";
		String pubdate = "";
		String guid = "";
		
		/*if(feedURL.equals(null)){
			rssData = new FeedData("No connnection", "", "Was not able to connect to site."
					,"", "", "");
			ItemData item = new ItemData();
			item.setTitle("No news");
			item.setAuthor("");
			item.setDescription(":(");
			item.setLink("");
			item.setGuid("");
			rssData.getMessages().add(item);
			rssFeed = rssData;
			return;
		}*/

		InputStream streamIn = feedURL.openStream();
		
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser XmlPP = factory.newPullParser();
        
        XmlPP.setInput(streamIn, "UTF-8");
        
        int eventType = XmlPP.getEventType();
        
        while (eventType != XmlPullParser.END_DOCUMENT) {
        	
        	eventType = XmlPP.next();
        	
        	if(eventType == XmlPullParser.START_TAG) {
        		
        		if (XmlPP.getName().equals("item")){
        			if(isHeader){
        				isHeader = false;
        				rssData = new FeedData(title, link, description, 
        							language, copyright, pubdate);
        			}
        			continue;
        		}
        		
        		if (XmlPP.getName().equals("title")){
        			title = XmlPP.nextText();
        			continue;
        		}
        			
        		if (XmlPP.getName().equals("description")){
        			description = XmlPP.nextText();
        			continue;
        		}
        			
        		if (XmlPP.getName().equals("link")){
        			link = XmlPP.nextText();
        			continue;
        		}
        			
        		if (XmlPP.getName().equals("pubDate")){
        			pubdate = XmlPP.nextText();
        			continue;
        		}
        			
        		if (XmlPP.getName().equals("guid")){
        			guid = XmlPP.nextText();
        			continue;
        		}
        		
        		if (XmlPP.getName().equals("language")){
        			language = XmlPP.nextText();
        			continue;
        		}
        		
        		if (XmlPP.getName().equals("copyright")){
        			copyright = XmlPP.nextText();
        			continue;
        		}
        		
        		if (XmlPP.getName().equals("author")){
        			author = XmlPP.nextText();
        			continue;
        		}

        	} else if (eventType == XmlPullParser.END_TAG) 
        		if(XmlPP.getName().equals("item")){
        			
        			ItemData item = new ItemData();
        			item.setTitle(title);
        			item.setAuthor(author);
        			item.setDescription(description
        					.replace("&apos;", "'") 	// to remove the
        					.replace("&quot;", "\""));	// xml formatting
        			item.setLink(link);
        			item.setGuid(guid);
        			
        			rssData.getMessages().add(item);
        		}
        }
        rssFeed = rssData;
	}
	
	public FeedData getRssFeed(){
		return rssFeed;
	}
}