package com.lucene.toursearch.example;

import com.lucene.toursearch.helper.CsvLoader;
import com.lucene.toursearch.model.TourInfo;
import com.lucene.toursearch.service.IndexService;
import com.lucene.toursearch.service.SearchService;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.List;

public class ScoreBoostExample {
    public static void main(String[] args) throws IOException {
        CsvLoader csvLoader = new CsvLoader();
        List<TourInfo> tourInfoList = csvLoader.readTourInfo();
        Directory directory = new ByteBuffersDirectory();
        IndexService indexService = new IndexService();
        indexService.indexTourInfo(directory, tourInfoList);

        SearchService searchService = new SearchService();
        System.out.println("Boosting을 적용하지 않은 예제=====================");
        Query query = getTermQuery();
        searchService.getQueryResultWithScoreExplain(directory, query);

        System.out.println("Boosting을 적용한 예제=====================");
        Query query2 = getBoostedQuery();
        searchService.getQueryResultWithScoreExplain(directory, query2);

    }

    private static Query getBoostedQuery() {
        Query query1 = new TermQuery(new Term("title", "남해"));
        Query query2 = new TermQuery(new Term("description", "바다"));

        Query boostQuery = new BoostQuery(query2, 2);
        return new BooleanQuery.Builder()
                .add(query1, BooleanClause.Occur.SHOULD)
                .add(boostQuery, BooleanClause.Occur.SHOULD)
                .build();
    }

    private static Query getTermQuery() {
        Query termQuery1 = new TermQuery(new Term("title", "남해"));
        Query termQuery2 = new TermQuery(new Term("description", "바다"));

        return new BooleanQuery.Builder()
                .add(termQuery1, BooleanClause.Occur.SHOULD)
                .add(termQuery2, BooleanClause.Occur.SHOULD)
                .build();
    }
}
