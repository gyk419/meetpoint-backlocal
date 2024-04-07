package MeetPoint.meetpoint.Map.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

/**
 * 작성일 : 2024.04.06
 * 작성자 : 김준식
 * 내용 : map DAO
 **/
@Mapper
public interface MapDao {
    /**
     * 작성일 : 2024.04.06
     * 작성자 : 김준식
     * 내용 : 버스 정류장 개수 조회
     **/
    public int busStopCount(HashMap<String, Double> params);


}
