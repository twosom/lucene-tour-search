package com.lucene.toursearch.example;

import com.lucene.toursearch.helper.CsvLoader;
import com.lucene.toursearch.model.TourInfo;
import com.lucene.toursearch.service.IndexService;
import com.lucene.toursearch.service.SearchService;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.List;

public class TermRangeQueryExample {
    public static void main(String[] args) throws IOException {
        CsvLoader csvLoader = new CsvLoader();
        List<TourInfo> tourInfoList = csvLoader.readTourInfo();

        Directory directory = new ByteBuffersDirectory();
        IndexService indexService = new IndexService();
        indexService.indexTourInfo(directory, tourInfoList);
        System.out.println("====================================>> '느티오무' 단어가 포함된 여행정보");

        getTermRangeQuery(directory);
    }

    private static void getTermRangeQuery(Directory directory) {
        TermRangeQuery query = TermRangeQuery.newStringRange(
                "courseName",
                "01코스",
                "04코스",
                true,
                true
        );

        SearchService searchService = new SearchService();
        searchService.getQueryResult(directory, query);
    }
}
