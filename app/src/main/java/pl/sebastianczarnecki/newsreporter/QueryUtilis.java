package pl.sebastianczarnecki.newsreporter;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QueryUtilis {

    private static final String LOG_TAG = QueryUtilis.class.getName();

    private QueryUtilis() { }

    public static List<News> fetchNewsData(String url) {

        URL newsUrl = createUrl(url);

        String jsonResponse;

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        jsonResponse = initHttpRequest(newsUrl);
        List<News> news = extractResultsFromJSON(jsonResponse);
        return news;
    }

    private static List<News> extractResultsFromJSON(String newsJSON) {

        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> newsItemsArrayList = new ArrayList<>();

        String titleNews;
        String sectionNews;
        String urlNews;
        String firstName;
        String lastName;
        String authorNews;
        String rawDateNews;
        String dateNews;

        try {
            JSONObject rootJsonObject = new JSONObject(newsJSON);
            JSONObject responsesJasonObj = rootJsonObject.getJSONObject("response");
            JSONArray jsonResultsArray = responsesJasonObj.getJSONArray("results");

            for (int i = 0; i < jsonResultsArray.length(); i++) {
                JSONObject currentJson = jsonResultsArray.getJSONObject(i);
                titleNews = currentJson.getString("webTitle");
                sectionNews = currentJson.getString("sectionName");
                urlNews = currentJson.getString("webUrl");
                JSONArray tagsArray = currentJson.getJSONArray("tags");
                if (!tagsArray.isNull(0)) {
                    JSONObject currentTagObj = tagsArray.getJSONObject(0);
                    if (!currentTagObj.isNull("firstName")) {
                        firstName = currentTagObj.getString("firstName");
                    } else {
                        firstName = null;
                    }
                    if (!currentTagObj.isNull("lastName")) {
                        lastName = currentTagObj.getString("lastName");
                    } else {
                        lastName = null;
                    }
                    authorNews = getAuthorName(firstName, lastName);
                } else {
                    authorNews = null;
                }
                if (!currentJson.isNull("webPublicationDate")) {
                    rawDateNews = currentJson.getString("webPublicationDate");
                    dateNews = getFormattedDate(rawDateNews);
                } else {
                    dateNews = null;
                }
                newsItemsArrayList.add(new News(titleNews, sectionNews, authorNews, dateNews, urlNews));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsItemsArrayList;
    }

    private static String getFormattedDate(String rawDate) {
        if (rawDate == null) {
            return null;
        }
        Date date = null;
        SimpleDateFormat formattedDate = new SimpleDateFormat("MMM dd, yyyy - HH:mm");
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(rawDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate.format(date);
    }

    private static String getAuthorName(String firstName, String lastName) {
        if (firstName == null && lastName == null) {
            return null;
        } else if (firstName == null || firstName.isEmpty()) {
            return lastName;
        } else if (lastName == null || lastName.isEmpty()) {
            return firstName;
        } else {
            return (firstName + " " + lastName);
        }
    }

    private static String initHttpRequest(URL ncUrl) {
        String jsonResponse = null;

        if (ncUrl == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) ncUrl.openConnection();
            urlConnection.setConnectTimeout(1500);
            urlConnection.setReadTimeout(5000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }

        StringBuilder output = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = bufferedReader.readLine();
        while (line != null) {
            output.append(line);
            line = bufferedReader.readLine();
        }

        return output.toString();
    }

    private static URL createUrl(String url) {
        URL nUrl = null;
        if (url == null) {
            return nUrl;
        }
        try {
            nUrl = new URL(url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "url error");
            e.printStackTrace();
        }
        return nUrl;
    }
}
