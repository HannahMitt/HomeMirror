package com.morristaedt.mirror.modules;

import android.os.AsyncTask;
import android.util.Log;

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSReader;
import org.mcsoxford.rss.RSSReaderException;

/**
 * Created by alex on 21/09/15.
 */
public class NewsModule {
    public interface NewsListener {
        void onNewNews(String headline);
    }

    public static void getNewsHeadline(final NewsListener newsListener) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                newsListener.onNewNews(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RSSReader rssReader = new RSSReader();
                String url = "http://feeds.bbci.co.uk/news/world/rss.xml?edition=uk";
                try {
                    RSSFeed feed = rssReader.load(url);
                    return feed.getItems().get(0).getTitle();
                } catch (RSSReaderException e) {
                    Log.e("NewsModule", "Error parsing RSS");
                    return null;
                } catch (Exception e) {
                    // not great to catch general exceptions, but this lib is being sketchy
                    Log.e("NewsModule", "Error parsing RSS");
                    return null;
                }
            }
        }.execute();
    }
}
