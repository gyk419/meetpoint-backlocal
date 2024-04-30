package MeetPoint.meetpoint.Map.service;

import MeetPoint.meetpoint.Map.algorithm.MidPoint;
import MeetPoint.meetpoint.Map.dao.MapDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class mapService {

    @Autowired
    MapDao mapdao;

    public String webView(){
        return "YSY - MEET POINT";
    }

    /**
     * 작성일 : 2024.04.01
     * 작성자 : 김준식
     * 내용 : 중간지점 좌표 계산 Service
     **/
    public HashMap<String, Object> findCenterPoint(List<Object> params) {
        System.out.println("center - 서비스 실행");
        // 중간지점 계산 방식 옵션 저장 (1)거리순 (2)무게중심 (3)교통점수
        Integer option = (Integer) params.get(0);
        // 입력받은 여러 좌표의 위도, 경도 값을 각각 리스트로 저장
        List<HashMap<String, Object>> re = (List<HashMap<String, Object>>) params.get(1);
        List<Double> lat = new ArrayList<>(); // 위도
        List<Double> lon = new ArrayList<>(); // 경도
        System.out.println("re : " + re);
        for(HashMap<String, Object> res : re) {
            HashMap<String, Object> position = (HashMap<String, Object>) res.get("position");
            lat.add(Double.parseDouble(position.get("y").toString()));
            lon.add(Double.parseDouble(position.get("x").toString()));
        }
        System.out.println("lat : " + lat);
        System.out.println("lon : " + lon);
        HashMap<String, Object> result = new HashMap<>();
        MidPoint midPoint = new MidPoint();

        // (1) 거리순 (2) 무게중심 (3) 교통점수순
        if(option == 1){
            result = midPoint.distance(lat, lon);
        }
        if(option == 2){
            result = midPoint.centerOfGravity(lat, lon);
        }
        if (option == 3) { // 1km 이내에 있는 버스정류장 수
            int optionValue = 1; // 똑같은 점수를 받은 좌표들이 여러개일 경우 좌표의 갯수 만큼 저장, 똑같은 점수를 받은 좌표들이 없을 경우 1(즉, 점수가 다 다를 경우)
            double lowScoreLat = 0, lowScoreLon = 0; // 점수가 낮은 좌표의 위도, 경도
            double sumLat = 0, sumLon = 0; // 낮은 점수 값이 같은 좌표들의 합

            HashMap<String, Double> comp = new HashMap<>(); // 점수가 작은 좌표를 찾기 위한 변수
            comp.put("latitude",lat.get(0));
            comp.put("longitude", lon.get(0));

            lowScoreLat = comp.get("latitude");
            lowScoreLon = comp.get("longitude");
            sumLat += lowScoreLat;
            sumLon += lowScoreLon;

            int score = mapdao.busStopCount(comp); // 현재 젤 낮은 점수 변수
            int comp_score = 0; // 비교할 점수

            for(int i=1; i<lat.size();i++){
                comp.replace("latitude",lat.get(i));
                comp.replace("longitude",lon.get(i));
                comp_score = mapdao.busStopCount(comp);

                if(score > comp_score) {
                    optionValue = 1;
                    sumLat = 0; sumLon = 0;
                    score = comp_score;
                    lowScoreLat = comp.get("latitude");
                    lowScoreLon = comp.get("longitude");
                    sumLat += lowScoreLat;
                    sumLon += lowScoreLon;
                    continue;
                }

                if(score == comp_score) {
                    optionValue += 1;
                    sumLat += comp.get("latitude");
                    sumLon += comp.get("longitude");
                }
            }
            if(optionValue == 1){
                result = midPoint.vehiclesScore(lat, lon, lowScoreLat, lowScoreLon, optionValue);
            } else {
                result = midPoint.vehiclesScore(lat, lon, sumLat, sumLon, optionValue);
            }
        }

        return result;
    }
}
