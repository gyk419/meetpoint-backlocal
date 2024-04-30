package MeetPoint.meetpoint.Map.service;

import MeetPoint.meetpoint.Map.algorithm.MidPoint;
import MeetPoint.meetpoint.Map.dao.MapDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public HashMap<String, Object> findCenterPoint(List<Double> lat, List<Double> lon, Integer option) {
        System.out.println("center - 서비스 실행");
        System.out.println("service option : " + option);
        HashMap<String, Object> result = new HashMap<>();
        MidPoint midPoint = new MidPoint();

        // (1) 거리순 (2) 무게중심 (3) 교통점수순
        if(option == 1){

        }
        if(option == 2){
            result = midPoint.centerOfGravity(lat, lon);
        }
        if (option == 3) {
            double lowScoreLat = 0, lowScoreLon = 0; // 점수가 낮은 좌표의 위도, 경도
            HashMap<String, Double> comp = new HashMap<>(); // 점수가 작은 좌표를 찾기 위한 변수
            comp.put("latitude",lat.get(0));
            comp.put("longitude", lon.get(0));
            int score = mapdao.busStopCount(comp);
            int comp_score = 0;
            for(int i=1; i<lat.size();i++){
                comp.replace("latitude",lat.get(i));
                comp.replace("longitude",lon.get(i));
                comp_score = mapdao.busStopCount(comp);
                if(score > comp_score) {
                    score = comp_score;
                    lowScoreLat=comp.get("latitude");
                    lowScoreLon = comp.get("longitude");
                }
            }
            System.out.println("점수가 낮은 위도 : " + lowScoreLat);
            System.out.println("점수가 낮은 경도 : " + lowScoreLon);
            result = midPoint.vehiclesScore(lat, lon, lowScoreLat, lowScoreLon);
            System.out.println("result : " + result);
        }

        return result;
    }
}
