package com.lucene.toursearch.example;

import com.lucene.toursearch.helper.CsvLoader;
import com.lucene.toursearch.model.TourInfo;
import com.lucene.toursearch.service.IndexService;
import com.lucene.toursearch.service.SearchService;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.List;

public class ScoreExplainExample {
    public static void main(String[] args) throws IOException {
        CsvLoader csvLoader = new CsvLoader();
        List<TourInfo> tourInfoList = csvLoader.readTourInfo();
        Directory directory = new ByteBuffersDirectory();
        IndexService indexService = new IndexService();
        indexService.indexTourInfo(directory, tourInfoList);
        SearchService searchService = new SearchService();
        Query query = new TermQuery(new Term("area", "충북"));
        searchService.getQueryResultWithScoreExplain(directory, query);
    }
}
