package com.lucene.toursearch.example;

import com.lucene.toursearch.helper.CsvLoader;
import com.lucene.toursearch.model.TourInfo;
import com.lucene.toursearch.service.IndexService;
import com.lucene.toursearch.service.SearchService;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.List;

public class FilterExample {
    public static void main(String[] args) throws IOException, ParseException {
        CsvLoader csvLoader = new CsvLoader();
        List<TourInfo> tourInfoList = csvLoader.readTourInfo();

        Directory directory = new ByteBuffersDirectory();
        IndexService indexService = new IndexService();
        indexService.indexTourInfo(directory, tourInfoList);


        System.out.println("\n==========QUERY");
        getSingleQuery(directory);

        System.out.println("\n==========MUST QUERY");
        getMustQuery(directory);

        System.out.println("\n==========FILTER QUERY");
        getFilterQuery(directory);
    }

    private static void getFilterQuery(Directory directory) throws ParseException {
        SearchService searchService = new SearchService();
        Query baseQuery = new QueryParser("description", new KoreanAnalyzer()).parse("바다");
        Query filterQuery = DoublePoint.newRangeQuery("hour", 4, 5);
        Query booleanQuery = new BooleanQuery.Builder()
                .add(baseQuery, BooleanClause.Occur.MUST)
                .add(filterQuery, BooleanClause.Occur.FILTER)
                .build();

        searchService.getQueryResult(directory, booleanQuery);
    }

    private static void getMustQuery(Directory directory) throws ParseException {
        SearchService searchService = new SearchService();
        Query baseQuery = new QueryParser("description", new KoreanAnalyzer()).parse("바다");
        Query filterQuery = DoublePoint.newRangeQuery("hour", 4, 5);

        Query booleanQuery = new BooleanQuery.Builder()
                .add(baseQuery, BooleanClause.Occur.MUST)
                .add(filterQuery, BooleanClause.Occur.MUST)
                .build();

        searchService.getQueryResult(directory, booleanQuery);
    }

    private static void getSingleQuery(Directory directory) throws ParseException {
        SearchService searchService = new SearchService();
        Query query = new QueryParser("description", new KoreanAnalyzer()).parse("바다");
        searchService.getQueryResult(directory, query);
    }
}
