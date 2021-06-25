package com.yuuko.modules.audio.handlers;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.api.entity.Api;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.metrics.MetricsManager;
import net.dv8tion.jda.api.EmbedBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class YouTubeSearchHandler {
    private static final Logger log = LoggerFactory.getLogger(YouTubeSearchHandler.class);
    private static final Api api = Yuuko.API_MANAGER.getApi("google");
    private static final HashMap<String, SearchListResponse> searchCache = new HashMap<>();

    /**
     * Searches youtube using e.getParameters() and returns the first result.
     * @return youtube video url.
     */
    public static List<SearchResult> search(MessageEvent context) {
        if(!api.isAvailable()) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Google API key missing, unable to use YouTube search features.");
            MessageDispatcher.reply(context, embed.build());
            return null;
        }

        try {
            SearchListResponse searchResponse = searchCache.getOrDefault(context.getParameters(), null);

            if(searchResponse == null) {
                YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), request -> {})
                        .setApplicationName("yuuko-discord")
                        .build();

                YouTube.Search.List search = youtube.search().list("id,snippet")
                        .setKey(api.getKey())
                        .setQ(context.getParameters())
                        .setType("video")
                        .setFields("items(id/videoId,snippet/title)")
                        .setMaxResults(10L);

                searchResponse = search.execute();
                searchCache.put(context.getParameters(), searchResponse);
            } else {
                MetricsManager.getAudioMetrics().TRACK_ID_CACHE_HITS.getAndIncrement();
            }

            return searchResponse.getItems();

        } catch (GoogleJsonResponseException e) {
            log.error("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());

            EmbedBuilder embed = new EmbedBuilder().setTitle("Service Error: " + e.getDetails().getCode())
                    .setDescription(e.getDetails().getMessage());
            MessageDispatcher.reply(context, embed.build());
        } catch (IOException cx) {
            log.error("There was an IO error: " + cx.getCause());
        }

        return null;
    }

    public static HashMap<String, SearchListResponse> getSearchCache() {
        return searchCache;
    }

    public static void clearSearchCache() {
        searchCache.clear();
    }
}
