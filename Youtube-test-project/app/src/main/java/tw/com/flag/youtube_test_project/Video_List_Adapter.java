package tw.com.flag.youtube_test_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by AllenLin on 2016/8/30.
 */

public class Video_List_Adapter extends ArrayAdapter<VideoItem> {

    protected List<VideoItem> items;

    public Video_List_Adapter(Context context) {
        super(context,0);
    }

    public Video_List_Adapter(Context context, List<VideoItem> searchResult) {
        super(context, 0);
        this.items = searchResult;
    }

    public void Remove_Duplicate(List<VideoItem> list)
    {
        Set set = new HashSet();
        List<VideoItem> newList = new ArrayList<VideoItem>();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            VideoItem element = (VideoItem) it.next();
            if (set.add(element.getId())) newList.add(element);
        }
        list.clear();
        list.addAll(newList);
    }

    public void addMoreItems(List<VideoItem> newItems) {
        this.items.addAll(newItems);
        Remove_Duplicate(items);
        notifyDataSetChanged();
    }

    public void addMoreItems(int location, List<VideoItem> newItems) {
        this.items.addAll(location, newItems);
        Remove_Duplicate(items);
        notifyDataSetChanged();
    }

    public void removeAllItems() {
        this.items.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public VideoItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        }
        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.imageView);
        TextView title = (TextView) convertView.findViewById(R.id.textView_title);
        TextView datetime = (TextView) convertView.findViewById(R.id.textView_datetime);
        NumberProgressBar progressBar = (NumberProgressBar) convertView.findViewById(R.id.numberbar);
        NumberProgressBar progressBar2 = (NumberProgressBar) convertView.findViewById(R.id.numberbar2);
        NumberProgressBar progressBar3 = (NumberProgressBar) convertView.findViewById(R.id.numberbar3);

        VideoItem searchResult = items.get(position);

        Picasso.with(parent.getContext()).load(searchResult.getThumbnailURL()).into(thumbnail);
        title.setText(searchResult.getTitle());
        datetime.setText(searchResult.getDatetime().toString());
        progressBar.setProgress((int) (Double.parseDouble(searchResult.getTotalWeight())*100));
        progressBar2.setProgress((int) (Double.parseDouble(searchResult.getPersonWeight())*100));
        progressBar3.setProgress((int) (Double.parseDouble(searchResult.getCommunityWeight())*100));
        return convertView;
    }
}


