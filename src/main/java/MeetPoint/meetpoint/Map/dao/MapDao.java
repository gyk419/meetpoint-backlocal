package MeetPoint.meetpoint.Map.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

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
    int busStopCount(HashMap<String, Double> params);

    /**
     * 작성일 : 2024.05.06
     * 작성자 : 김준식
     * 내용 : 재탐색(사용자가 선택한 옵션에 맞는 관광지 검색 후 위도, 경도 반환)
     **/
//    List<HashMap<String, Object>> reSearchPoint(HashMap<String, Object> params);
    HashMap<String, Object> reSearchPoint(HashMap<String, Object> params);
}
