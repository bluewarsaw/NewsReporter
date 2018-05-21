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

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(@NonNull Context context, ArrayList<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_details, parent, false);
        }

        News currentNews = getItem(position);
        String date = currentNews.getDateNews();
        String author = currentNews.getAuthorNews();

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author_tv);
        checkAuthorTV(authorTextView, author);

        LinearLayout listMainLinearLayout = listItemView.findViewById(R.id.main_listview);

        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section_tv);
        sectionTextView.setText(currentNews.getSectionNews());

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title_tv);
        titleTextView.setText(currentNews.getTitleNews());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_tv);
        checkDateTV(dateTextView, date);

        return listItemView;
    }

    private void checkAuthorTV(TextView authorTV, String author) {
        if (author == null) {
            authorTV.setVisibility(View.GONE);
        } else {
            authorTV.setVisibility(View.VISIBLE);
            authorTV.setText(author);
        }
    }

    private void checkDateTV(TextView dateTV, String date) {
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
