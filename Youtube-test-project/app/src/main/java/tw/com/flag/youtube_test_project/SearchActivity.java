package tw.com.flag.youtube_test_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AllenLin on 2016/6/21.
 */
public class SearchActivity extends Activity implements TextView.OnEditorActionListener
        , SeekBar.OnSeekBarChangeListener {
    private EditText searchInput;
    private SeekBar seekBar;
    private TextView textView;
    private ListView videosFound;
    private List<VideoItem> searchResults;
    private Video_List_Adapter adapter;
    private View footView;

    private Filters filters;
    private Double filterValue;

    private Handler handler;
    private ArrayList<String[]> keyword_combination;

    private String Community_keyword = "hello";

    private int page = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchInput = (EditText) findViewById(R.id.editText);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        textView = (TextView) findViewById(R.id.textView3);
        videosFound = (ListView) findViewById(R.id.listView);

        filterValue = seekBar.getProgress() * 0.01;

        handler = new Handler() ;

        searchInput.setOnEditorActionListener(this);
        seekBar.setOnSeekBarChangeListener(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        footView = inflater.inflate(R.layout.foot_view, null);

        videosFound.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        loadData();
                        Log.d("debug", "loading");
                    }
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });
    }

    protected void loadData() {
        if (page < keyword_combination.size() - 1) {
            page++;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    searchOnYoutube(page);
                }
            }, 2000);
            Log.d("debug", String.valueOf(page));
        } else {
            videosFound.removeFooterView(footView);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            filters = new Filters(v.getText().toString(), Community_keyword, filterValue);
            keyword_combination = filters.Keyword_Combinations();

            videosFound.addFooterView(footView);
            searchResults = new ArrayList<VideoItem>();
            page = 0;
            searchOnYoutube(page);
            addClickListener();

            String s = "自主關鍵字:" + v.getText().toString().trim() +
                    "\n社群關鍵字:" + Community_keyword +
                    "\n" + textView.getText().toString();
            Toast.makeText(this, s, Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private void searchOnYoutube(final int page) {
        new Thread() {
            @Override
            public void run() {
                YoutubeConnector yc = new YoutubeConnector(SearchActivity.this);
                searchResults.addAll(yc.search(keyword_combination.get(page)));
                handler.post(new Runnable() {
                    public void run() {
                        if (page == 0) {
                            updateVideosFound();
                        } else {
                            loadVideoFound();
                        }
                    }
                });
            }
        }.start();
    }

    private void updateVideosFound() {
        adapter = new Video_List_Adapter(this, searchResults);
        videosFound.setAdapter(adapter);
    }

    private void loadVideoFound(){
        adapter.addMoreItems(searchResults);
    }

    private void addClickListener() {
        videosFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos,
                                    long id) {
                Intent intent = new Intent(getApplicationContext(), Video_Player_Activity.class);
                intent.putExtra("VIDEO_ID", searchResults.get(pos).getId());
                startActivity(intent);
            }

        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        DecimalFormat df = new DecimalFormat("#.##");
        filterValue = progress*0.01;
        String s = df.format(filterValue);
        textView.setText("當前α值為:" + s);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
//        filters = new Filters(searchInput.getText().toString(), Community_keyword, filterValue);
//        keyword_combination = filters.Keyword_Combinations(3);
//        threads = new Thread[keyword_combination.size()];
//        searchResults = new ArrayList<VideoItem>();
//        for (int i = 0; i < keyword_combination.size(); i++) {
//            searchOnYoutube(i);
//        }
//        try {
//            threads[0].start();
//            threads[0].join();
//            for(int i = 1; i < keyword_combination.size(); i++){
//                threads[i].start();
//                threads[i - 1].join();
//            }
//            while(threads[keyword_combination.size() - 1].getState() == Thread.State.RUNNABLE){
//                Thread.sleep(100);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        handler.post(new Runnable() {
//            public void run() {
//                filters.Remove_Duplicate(searchResults);
//                updateVideosFound();
//            }
//        });
//        addClickListener();
//        String s = "自主關鍵字:" + searchInput.getText().toString() +
//                "\n社群關鍵字:" + Community_keyword +
//                "\n" + textView.getText().toString();
//        Toast.makeText(this, s, Toast.LENGTH_LONG).show();

    }
}
