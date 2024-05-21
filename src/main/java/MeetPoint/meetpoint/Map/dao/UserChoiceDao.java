package MeetPoint.meetpoint.Map.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 작성일 : 2024.05.17
 * 작성자 : 김준식
 * 내용 : UserChoice DAO
 **/
@Mapper
public interface UserChoiceDao {

    /**
     * 작성일 : 2024.05.17
     * 작성자 : 김준식
     * 내용 : 중간지점명, 중간지점 위도와 경도를 저장하고 해당 인덱스 값을 반환 DAO
     **/
    int storeMP(HashMap<String, Object> params);

    /**
     * 작성일 : 2024.05.18
     * 작성자 : 김준식
     * 내용 : 사용자가 선택한 장소들을 저장
     **/
    void storePlace(Map<String, Object> params);

    /**
     * 작성일 : 2024.05.20
     * 작성자 : 김준식
     * 내용 : 방문할 장소를 저장
     **/
    void storePlaceToVisit(Map<String, Object> params);

    /**
     * 작성일 : 2024.05.21
     * 작성자 : 김준식
     * 내용 : 중간 주소, 위도, 경도, 건물명 데이터 조회
     **/
    public HashMap<String, Object> selectMeetPoint(HashMap<String, Integer> params);

    /**
     * 작성일 : 2024.05.21
     * 작성자 : 김준식
     * 내용 : 사용자가 선택한 장소 데이터 조회
     **/
    public List<HashMap<String, Object>> selectPlace(HashMap<String, Integer> params);

    /**
     * 작성일 : 2024.05.21
     * 작성자 : 김준식
     * 내용 : 사용자가 선택한 장소에 머물 시간 데이터 조회
     **/
    public List<HashMap<String, Object>> selectStayTime(HashMap<String, Integer> params);
}
