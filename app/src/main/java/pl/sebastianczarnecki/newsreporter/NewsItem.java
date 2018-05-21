package pl.sebastianczarnecki.newsreporter;

public class NewsItem {

    String titleNews;
    String sectionNews;
    String authorNews;
    String dateNews;
    String urlNews;

    public NewsItem(String title, String section, String author, String date, String url) {
        this.titleNews = title;
        this.sectionNews = section;
        this.authorNews = author;
        this.dateNews = date;
        this.urlNews = url;
    }

    public String getTitleNews() {
        return titleNews;
    }

    public String getSectionNews() {
        return sectionNews;
    }

    public String getAuthorNews() {
        return authorNews;
    }

    public String getDateNews() {
        return dateNews;
    }

    public String getUrlNews() {
        return urlNews;
    }
}
