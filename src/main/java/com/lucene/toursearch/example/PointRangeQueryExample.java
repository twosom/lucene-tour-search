package com.lucene.toursearch.example;

import com.lucene.toursearch.helper.CsvLoader;
import com.lucene.toursearch.model.TourInfo;
import com.lucene.toursearch.service.IndexService;
import com.lucene.toursearch.service.SearchService;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.List;

public class PointRangeQueryExample {
    public static void main(String[] args) throws IOException {
        CsvLoader csvLoader = new CsvLoader();
        List<TourInfo> tourInfoList = csvLoader.readTourInfo();

        Directory directory = new ByteBuffersDirectory();
        IndexService indexService = new IndexService();
        indexService.indexTourInfo(directory, tourInfoList);

        System.out.println("====================================>> 소요시간 1~3시간의 여행정보");

        getPointRangeQuery(directory);
    }

    private static void getPointRangeQuery(Directory directory) {
        Query query = DoublePoint.newRangeQuery("hour", 1, 3);
        SearchService searchService = new SearchService();
        searchService.getQueryResult(directory, query);
    }
}
