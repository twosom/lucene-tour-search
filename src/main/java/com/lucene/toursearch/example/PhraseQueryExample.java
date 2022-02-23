package com.lucene.toursearch.example;

import com.lucene.toursearch.helper.CsvLoader;
import com.lucene.toursearch.model.TourInfo;
import com.lucene.toursearch.service.IndexService;
import com.lucene.toursearch.service.SearchService;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.List;

public class PhraseQueryExample {
    public static void main(String[] args) throws IOException {
        CsvLoader csvLoader = new CsvLoader();
        List<TourInfo> tourInfoList = csvLoader.readTourInfo();
        Directory directory = new ByteBuffersDirectory();
        IndexService indexService = new IndexService();
        indexService.indexTourInfo(directory, tourInfoList);
        System.out.println("====================================>> '마애여래삼존상'와 '개심사' 사이가 하나의 단어가 포함된 여행정보");
        System.out.println("===== slop 방식 =====");
        getPhraseQueryUsingSlop(directory);
        System.out.println("===== position 방식 =====");
        getPhraseQueryUsingPosition(directory);

    }

    private static void getPhraseQueryUsingPosition(Directory directory) {
        Query phraseQuery = new PhraseQuery.Builder()
                .add(new Term("description", "마애여래삼존상"), 3)
                .add(new Term("description", "개심사"), 5)
                .build();

        SearchService searchService = new SearchService();
        searchService.getQueryResult(directory, phraseQuery);
    }

    private static void getPhraseQueryUsingSlop(Directory directory) {
        Query phraseQuery = new PhraseQuery.Builder()
                .add(new Term("description", "마애여래삼존상"))
                .add(new Term("description", "개심사"))
                .setSlop(2)
                .build();

        SearchService searchService = new SearchService();
        searchService.getQueryResult(directory, phraseQuery);
    }
}
