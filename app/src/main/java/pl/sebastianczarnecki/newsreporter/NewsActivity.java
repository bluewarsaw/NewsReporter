package pl.sebastianczarnecki.newsreporter;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<List<NewsItem>> {

    private static final int NEWS_LOADER_ID = 1;

    private final String NEWS_REQUEST_URL = "http://content.guardianapis.com/search?order-by=newest&show-tags=contributor&page" +
                    "=1&page-size=50&q=Android&api-key=d6cd3112-b2d1-44c1-9862-1032a95ed5b9";

    private ProgressBar newsProgressBar;
    private TextView newsStatusTV;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list);

        newsProgressBar = (ProgressBar) findViewById(R.id.loading_spinner);

        ListView newsListView = (ListView) findViewById(R.id.list);

        newsAdapter = new NewsAdapter(this, new ArrayList<NewsItem>());

        newsListView.setAdapter(newsAdapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri newsUri = Uri.parse(newsAdapter.getItem(position).getUrlNews());
                Intent newsIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(newsIntent);
            }
        });

        newsStatusTV = (TextView) findViewById(R.id.status_tv);

        newsListView.setEmptyView(newsStatusTV);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager newsLoaderManager = getLoaderManager();
            newsLoaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            newsProgressBar.setVisibility(View.GONE);
            newsStatusTV.setText(R.string.no_connection);
        }
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, NEWS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> data) {
        newsAdapter.clear();
        newsProgressBar.setVisibility(View.GONE);
        newsStatusTV.setText(R.string.empty_list);

        if (data != null && !data.isEmpty()) {
            newsAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        newsAdapter.clear();
    }
}
