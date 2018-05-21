package pl.sebastianczarnecki.newsreporter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<NewsItem> {

    public NewsAdapter(@NonNull Context context, ArrayList<NewsItem> newsItems) {
        super(context, 0, newsItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_details, parent, false);
        }

        NewsItem currentNewsItem = getItem(position);
        String date = currentNewsItem.getDateNews();
        String author = currentNewsItem.getAuthorNews();

        LinearLayout listMainLinearLayout = listItemView.findViewById(R.id.main_listview);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title_tv);
        titleTextView.setText(currentNewsItem.getTitleNews());

        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section_tv);
        sectionTextView.setText(currentNewsItem.getSectionNews());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_tv);
        checkDateTextView(dateTextView, date);

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author_tv);
        checkAuthorTextView(authorTextView, author);

        return listItemView;
    }

    private void checkAuthorTextView(TextView authorTV, String author) {
        if (author == null) {
            authorTV.setVisibility(View.GONE);
        } else {
            authorTV.setVisibility(View.VISIBLE);
            authorTV.setText(author);
        }
    }

    private void checkDateTextView(TextView dateTV, String date) {
        if (date == null) {
            dateTV.setVisibility(View.GONE);
        } else {
            dateTV.setVisibility(View.VISIBLE);
            SpannableString finalDate = new SpannableString(date);
            finalDate.setSpan(new UnderlineSpan(), 0, finalDate.length(), 0);
            dateTV.setText(finalDate);
        }
    }
}