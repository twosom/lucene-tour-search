package com.lucene.toursearch.example;

import com.lucene.toursearch.helper.CsvLoader;
import com.lucene.toursearch.model.TourInfo;
import com.lucene.toursearch.service.IndexService;
import com.lucene.toursearch.service.SearchService;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.List;

public class MultiReaderExample {
    public static void main(String[] args) throws IOException, ParseException {
        CsvLoader csvLoader = new CsvLoader();
        List<TourInfo> tourInfoList = csvLoader.readTourInfo();
        List<TourInfo> tour1 = tourInfoList.subList(0, 9);
        List<TourInfo> tour2 = tourInfoList.subList(10, 19);

        Directory directory1 = new ByteBuffersDirectory();
        Directory directory2 = new ByteBuffersDirectory();

        IndexService indexService = new IndexService();
        indexService.indexTourInfo(directory1, tour1);
        indexService.indexTourInfo(directory2, tour2);

        SearchService searchService = new SearchService();
        Query query = new QueryParser("description", new KoreanAnalyzer()).parse("도보");
        searchService.multiSearch(directory1, directory2, query);
    }
}
