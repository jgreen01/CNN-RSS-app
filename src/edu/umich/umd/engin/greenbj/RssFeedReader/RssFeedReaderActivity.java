package edu.umich.umd.engin.greenbj.RssFeedReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

public class RssFeedReaderActivity extends ExpandableListActivity {
	
	private String CNNTopStories = "http://rss.cnn.com/rss/cnn_topstories.rss";
	private FeedData feed;
	
	private TextView headerTitle;
	private TextView headerDescription;
	private TextView headerCopyright;
	
	private static String TAG = "RssFeedReaderActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        headerTitle = (TextView) findViewById(R.id.titleView);
        headerDescription = (TextView) findViewById(R.id.descrView);
        headerCopyright = (TextView) findViewById(R.id.copyrightView);
        
        RSSManager rssFeed = new RSSManager(CNNTopStories);
        feed = rssFeed.getRssFeed();
        
        headerTitle.setText(feed.getTitle());
        headerDescription.setText(feed.getDescription());
        headerCopyright.setText(feed.getCopyright());
        
		SimpleExpandableListAdapter rssItemList = new SimpleExpandableListAdapter(
        		this, 						// context (this class)
        		createTitleList(),			// Creating group List
        		R.layout.parent_row,		// Group item layout XML
        		new String[]{"titles"},		// the key of group item
        		new int[]{R.id.itemTitle},	// ID of each group item.-Data under the key goes into this TextView
        		createChildList(),			// childData describes second-level entries
        		R.layout.child_row,			// Layout for sub-level entries(second level)
        		new String[]{"desc"},		// Key/s in childData maps to display
        		new int[]{R.id.itemDesc}	// Data under the key/s above go into these TextView/s
        );
        setListAdapter(rssItemList);
    }

	private List<HashMap<String, String>> createTitleList(){
		
    	ArrayList<HashMap<String, String>> itemTitles = 
    			new ArrayList<HashMap<String, String>>();
    	
    	ArrayList<ItemData> entriesList = 
    			(ArrayList<ItemData>) feed.getMessages();
    	
    	for(int i = 0; i < entriesList.size(); i++){
    		
    		HashMap<String, String> m = 
    				new HashMap<String, String>();
    		
    		m.put("titles", entriesList.get(i).getTitle());
    		
    		itemTitles.add(m);
    	}
    	
    	return itemTitles;
    }
	
	private List<List<HashMap<String, String>>> createChildList(){
		
		List<List<HashMap<String, String>>> groupList = 
				new ArrayList<List<HashMap<String,String>>>();
    	
    	ArrayList<ItemData> entriesList = 
    			(ArrayList<ItemData>) feed.getMessages();
    	
    	for(int i = 0; i < entriesList.size(); i++){
    		
    		ArrayList<HashMap<String, String>> childList = 
    				new ArrayList<HashMap<String, String>>();
    		
    		HashMap<String, String> m = 
    				new HashMap<String, String>();
    		
    		m.put("desc", entriesList.get(i).getDescription()
    				.split("<")[0]);	// to remove the links at
    									// the end of the description
    		
    		childList.add(m);
	    		
    		groupList.add(childList);
    	}
    	
    	return groupList;
    }
}