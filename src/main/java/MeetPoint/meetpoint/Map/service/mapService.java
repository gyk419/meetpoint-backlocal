package MeetPoint.meetpoint.Map.service;

import MeetPoint.meetpoint.Map.algorithm.MidPoint;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class mapService {

    public String webView(){
        return "YSY - MEET POINT";
    }

    /*********************************
     * 날짜 : 2024.04.01
     * 이름 : 김준식
     * 내용 : 중간지점 좌표 계산 Service
     * *******************************/
    public HashMap<String, Object> findCenterPoint(List<Double> lat, List<Double> lon) {
        System.out.println("center - 서비스 실행");
        MidPoint midPoint = new MidPoint();
        return midPoint.centerOfGravity(lat, lon);
    }
}
