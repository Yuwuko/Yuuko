package com.yuuko.core.commands.audio.handlers;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.metrics.MetricsManager;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class YouTubeSearchHandler {
    private static final Logger log = LoggerFactory.getLogger(YouTubeSearchHandler.class);
    private static final HashMap<String, SearchListResponse> searchCache = new HashMap<>();

    /**
     * Searches youtube using e.getParameters() and returns the first result.
     * @return youtube video url.
     */
    public static List<SearchResult> search(MessageEvent e) {
        try {
            SearchListResponse searchResponse = searchCache.getOrDefault(e.getParameters(), null);

            if(searchResponse == null) {
                YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), request -> {
                }).setApplicationName("yuuko-204012").build();

                YouTube.Search.List search = youtube.search().list("id,snippet")
                        .setKey(Utilities.getApiKey("google"))
                        .setQ(e.getParameters())
                        .setType("video")
                        .setFields("items(id/videoId,snippet/title)")
                        .setMaxResults(10L);

                searchResponse = search.execute();
                searchCache.put(e.getParameters(), searchResponse);
            } else {
                MetricsManager.getCacheMetrics().TRACK_ID_CACHE_HITS.getAndIncrement();
            }

            return searchResponse.getItems();

        } catch (GoogleJsonResponseException ex) {
            log.error("There was a service error: " + ex.getDetails().getCode() + " : " + ex.getDetails().getMessage());

            EmbedBuilder embed = new EmbedBuilder().setTitle("Service Error: " + ex.getDetails().getCode())
                    .setDescription(ex.getDetails().getMessage());
            MessageHandler.sendMessage(e, embed.build());
            return null;
        } catch (IOException cx) {
            log.error("There was an IO error: " + cx.getCause());
            return null;
        }
    }

    public static HashMap<String, SearchListResponse> getSearchCache() {
        return searchCache;
    }

    public static void clearSearchCache() {
        searchCache.clear();
    }
}
