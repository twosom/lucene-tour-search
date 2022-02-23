package com.lucene.toursearch.example;

import com.lucene.toursearch.helper.CsvLoader;
import com.lucene.toursearch.model.TourInfo;
import com.lucene.toursearch.service.IndexService;
import com.lucene.toursearch.service.SearchService;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.List;

public class TermQueryExample {

    public static void main(String[] args) throws IOException {
        // csv 파일 읽기
        CsvLoader csvLoader = new CsvLoader();
        List<TourInfo> tourInfoList = csvLoader.readTourInfo();

        // directory 사용
        Directory directory = new ByteBuffersDirectory();

        // 색인한다.
        IndexService indexService = new IndexService();
        indexService.indexTourInfo(directory, tourInfoList);
        getTermQuery(directory);
    }

    private static void getTermQuery(Directory directory) {
        // 분석 결과를 확인한다.
        SearchService searchService = new SearchService();
        TermQuery termQuery = new TermQuery(new Term("description", "해변"));
        searchService.getQueryResult(directory, termQuery);
    }
}
