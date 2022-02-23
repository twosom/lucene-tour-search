package com.lucene.toursearch.model;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourInfo {

    /**
     * 걷는 길 명칭
     */
    @CsvBindByPosition(position = 0)
    private String pathName;

    /**
     * 코스명
     */
    @CsvBindByPosition(position = 1)
    private String courseName;

    /**
     * 지역
     */
    @CsvBindByPosition(position = 2)
    private String area;

    /**
     * 난이도
     */
    @CsvBindByPosition(position = 3)
    private String level;

    /**
     * 상세거리
     */
    @CsvBindByPosition(position = 4)
    private String distance;

    /**
     * 소요 시간
     */
    @CsvBindByPosition(position = 5)
    private String hour;

    /**
     * 소개
     */
    @CsvBindByPosition(position = 6)
    private String description;
}
