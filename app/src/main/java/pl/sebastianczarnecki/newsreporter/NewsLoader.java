package pl.sebastianczarnecki.newsreporter;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<NewsItem>> {

    String newsUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        newsUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsItem> loadInBackground() {
        if (newsUrl == null) {
            return null;
        }
        List<NewsItem> result = QueryUtilis.fetchNewsData(newsUrl);
        return result;
    }
}
