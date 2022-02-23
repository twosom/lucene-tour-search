package com.lucene.toursearch.service;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;

import java.io.IOException;

public class SearchService {


    public static final int MAX_HIT_COUNT = 10;

    public void getQueryResult(Directory directory, Query query) {
        try (IndexReader reader = DirectoryReader.open(directory)) {
            IndexSearcher indexSearcher = new IndexSearcher(reader);
            TopDocs docs = indexSearcher.search(query, MAX_HIT_COUNT);
            ScoreDoc[] scoreDocs = docs.scoreDocs;
            printSearchResult(indexSearcher, scoreDocs);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getSortedQueryResult(Directory directory, Query query, Sort sort) {
        try (IndexReader reader = DirectoryReader.open(directory)) {
            IndexSearcher indexSearcher = new IndexSearcher(reader);
            TopFieldDocs docs = indexSearcher.search(query, MAX_HIT_COUNT, sort, true);
            printSearchResult(indexSearcher, docs.scoreDocs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 다중 색인에서 검색을 합니다.<br/>
     * 다중 색인에서 검색하려면 {@link IndexReader}가 여러개 필요합니다.<br/>
     * 해당 메소드에서는 두 개의 색인에서 검색을 할 거기 때문에 두 개의 {@link Directory} 객체를 받아 {@link IndexReader}를 생성한 후 하나의 {@link MultiReader}로 만들어줍니다.<br/>
     * 만들어진 {@link MultiReader}로 검색 객체를 생성한 후 쿼리 질의를 수행합니다.
     *
     * @param directory1 인덱스가 저장된 디렉토리1
     * @param directory2 인덱스가 저장된 디렉토리2
     * @param query      검색하고자 하는 쿼리 질의
     */
    public void multiSearch(Directory directory1, Directory directory2, Query query) {
        try (var reader1 = DirectoryReader.open(directory1);
             var reader2 = DirectoryReader.open(directory2)
        ) {
            IndexReader multiReader = new MultiReader(reader1, reader2);
            IndexSearcher indexSearcher = new IndexSearcher(multiReader);
            TopDocs docs = indexSearcher.search(query, MAX_HIT_COUNT);
            printSearchResult(indexSearcher, docs.scoreDocs);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getQueryResultWithScoreExplain(Directory directory, Query query) {
        try (IndexReader reader = DirectoryReader.open(directory)) {
            IndexSearcher indexSearcher = new IndexSearcher(reader);
            TopDocs docs = indexSearcher.search(query, MAX_HIT_COUNT);
            ScoreDoc[] hits = docs.scoreDocs;
            System.out.println(hits.length + " 개의 결과를 찾았습니다.");
            for(int i=0;i<hits.length;++i) {
                int docId = hits[i].doc;
                System.out.println("################");
                System.out.println(indexSearcher.explain(query, docId));
                System.out.println("################");
                Document d = indexSearcher.doc(docId);
                System.out.println((i + 1) + ". ");
                System.out.println("[길 이름] " + "\t" + d.get("pathName"));
                System.out.println("[코스 이름] " + "\t" + d.get("courseName"));
                System.out.println("[소요 시간] " + "\t" + d.get("hour"));
                System.out.println("[코스 설명] " + "\t" + d.get("description"));
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printSearchResult(IndexSearcher indexSearcher, ScoreDoc[] scoreDocs) throws IOException {
        System.out.println(scoreDocs.length + " 개의 결과를 찾았습니다.");
        for (int i = 0; i < scoreDocs.length; i++) {
            ScoreDoc scoreDoc = scoreDocs[i];
            int docId = scoreDoc.doc;
            Document doc = indexSearcher.doc(docId);
            System.out.println((i + 1) + ". ");
            System.out.println("- Score : " + scoreDocs[i].score);
            System.out.println("[길 이름] " + "\t" + doc.get("pathName"));
            System.out.println("[코스 이름] " + "\t" + doc.get("courseName"));
            System.out.println("[소요 시간] " + "\t" + doc.get("hour"));
            System.out.println("[코스 설명] " + "\t" + doc.get("description"));
            System.out.println();
        }
    }
}
