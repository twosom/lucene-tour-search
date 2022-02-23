package com.lucene.toursearch.example;

import com.lucene.toursearch.helper.CsvLoader;
import com.lucene.toursearch.model.TourInfo;
import com.lucene.toursearch.service.IndexService;
import com.lucene.toursearch.service.SearchService;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortedNumericSortField;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.List;

public class MultiFieldSortExample {
    public static void main(String[] args) throws IOException, ParseException {
        CsvLoader csvLoader = new CsvLoader();
        List<TourInfo> tourInfoList = csvLoader.readTourInfo();

        Directory directory = new ByteBuffersDirectory();

        IndexService indexService = new IndexService();
        indexService.indexTourInfo(directory, tourInfoList);

        getMultiFieldSortQuery(directory);
    }

    private static void getMultiFieldSortQuery(Directory directory) throws ParseException {
        SearchService searchService = new SearchService();
        Query query = new QueryParser("description", new KoreanAnalyzer()).parse("벚꽃");
        Sort sort = new Sort(
                new SortedNumericSortField("hour", SortField.Type.DOUBLE),
                new SortField("courseName", SortField.Type.STRING)
        );

        searchService.getSortedQueryResult(directory, query, sort);
    }
}
