package MeetPoint.meetpoint.Map.service;

import MeetPoint.meetpoint.Map.dao.UserChoiceDao;
import MeetPoint.meetpoint.Map.model.selectPlaceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 작성일 : 2024.05.17
 * 작성자 : 김준식
 * 내용 : UserChoice Service
 **/
@Service
public class UserChoiceService {
    @Autowired
    UserChoiceDao userChoiceDao;

    /**
     * 작성일 : 2024.05.17
     * 작성자 : 김준식
     * 내용 : 중간지점명, 중간지점 위도와 경도, 사용자가 선택한 장소들을 저장하는 Service
     **/
    public HashMap<String, Object> storePlace(HashMap<String, Object> params){
        HashMap<String , Object> result = new HashMap<>();

        // 중간지점명, 중간지점 위도와 경도를 저장하고 해당 인덱스 값을 반환( 이유 : 사용자가 선택한 장소들을 저장하기위해 사용 )
        try{
            // 위도가 문자열로 저장되어 실수로 변환
            params.put("mpLat", Double.parseDouble((String) params.get("mpLat")));
            // 경도가 문자열로 저장되어 실수로 변환
            params.put("mpLon", Double.parseDouble((String) params.get("mpLon")));
            userChoiceDao.storeMP(params);
            // 저장한 인덱스 반환
            Long idx = (Long) params.get("id");
            // 이전 페이지에서 선택한 장소 리스트를 저장
            List<HashMap<String, Object>> places = (List<HashMap<String, Object>>) params.get("selectInfo");
            for(HashMap<String, Object> place : places){
                place.put("MP_ID", idx);
                userChoiceDao.storePlace(place);
            }
            // 이전 페이지에서 선택한 장소들 중에서 방문할 장소 선택한 곳을 저장
            places = (List<HashMap<String, Object>>) params.get("addCheckInfoList");
            for(HashMap<String, Object> place : places) {
                place.put("MP_ID", idx);
                userChoiceDao.storePlaceToVisit(place);
            }
            // 결과값은 중간지점을 저장한 인덱스(id)를 반환
            result.put("index", idx);
        } catch (Exception e){
            result.put("index", -1);
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 작성일 : 2024.05.21
     * 작성자 : 김준식
     * 내용 : 사용자가 선택한 장소 및 중간 지점명, 위도, 경도값 반환
     **/
    public HashMap<String, Object> selectPlace(HashMap<String, Integer> params){
        HashMap<String, Object> result = new HashMap<>();

        // 중간지점명, 중간지점 위도, 경도, 건물명 조회
        result = userChoiceDao.selectMeetPoint(params);

        // 선택한 장소 데이터 조회
        List<HashMap<String, Object>> select = userChoiceDao.selectPlace(params);
        result.put("selectInfo", select);

        List<HashMap<String, Object>> selectTime = userChoiceDao.selectStayTime(params);
        result.put("addCheckInfoList", selectTime);
        System.out.println("result : " + result);

        return result;

    }

}
