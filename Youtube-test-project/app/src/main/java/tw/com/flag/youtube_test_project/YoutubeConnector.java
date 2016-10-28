package tw.com.flag.youtube_test_project;

import android.content.Context;
import android.util.Log;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AllenLin on 2016/7/15.
 */
public class YoutubeConnector {
    private YouTube youtube;
    private YouTube.Search.List query;

    // Your developer key goes here
    public static final String KEY
            = "AIzaSyCxZvFrP6xsdXT6RsfQ1Jlv-Oz5tZR_glk";

    public YoutubeConnector(Context context) {
        youtube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(com.google.api.client.http.HttpRequest request) throws IOException {}
        }).setApplicationName(context.getString(R.string.app_name)).build();

        try{
            query = youtube.search().list("id,snippet");
            query.setKey(KEY);
            query.setType("video");
            query.setFields("items(id/videoId,snippet/title,snippet/publishedAt,snippet/thumbnails/default/url)");
            query.setMaxResults((long)10);
        }catch(IOException e){
            Log.d("YC", "Could not initialize: "+e);
        }
    }

    public List<VideoItem> search(String[] keywords){
        query.setQ(keywords[0]);
        try{
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();

            List<VideoItem> items = new ArrayList<VideoItem>();
            for(SearchResult result:results){
                VideoItem item = new VideoItem();
                item.setTitle(result.getSnippet().getTitle());
                item.setDatetime(result.getSnippet().getPublishedAt());
                item.setThumbnailURL(result.getSnippet().getThumbnails().getDefault().getUrl());
                item.setId(result.getId().getVideoId());
                item.setTotalWeight(keywords[1]);
                item.setPersonWeight(keywords[2]);
                item.setCommunityWeight(keywords[3]);
                items.add(item);
            }
            return items;
        }catch(IOException e){
            Log.d("YC", "Could not search: "+e);
            return null;
        }
    }
}
