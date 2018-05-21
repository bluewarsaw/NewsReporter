package pl.sebastianczarnecki.newsreporter;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

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
    public List<News> loadInBackground() {
        if (newsUrl == null) {
            return null;
        }
        List<News> result = QueryUtilis.fetchNewsData(newsUrl);
        return result;
    }
}
