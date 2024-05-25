package MeetPoint.meetpoint.Map.service;

import MeetPoint.meetpoint.Map.algorithm.MidPoint;
import MeetPoint.meetpoint.Map.dao.MapDao;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class MapService {

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
    public HashMap<String, Object> findCenterPoint(List<Object> params, HttpServletResponse response, HttpServletRequest request) {

        // 중간지점 계산 방식 옵션 저장 (1)거리순 (2)무게중심 (3)교통점수
        Integer option = (Integer) params.get(0);

        // 입력받은 여러 좌표의 위도, 경도 값을 각각 리스트로 저장
        List<HashMap<String, Object>> re = (List<HashMap<String, Object>>) params.get(1);
        List<Double> lat = new ArrayList<>(); // 위도
        List<Double> lon = new ArrayList<>(); // 경도

        // 쿠키에 저장할 여러 사용자의 이름, 주소를 리스트로 저장
        List<String> users = new ArrayList<>();

        for(HashMap<String, Object> res : re) {
            HashMap<String, Object> position = (HashMap<String, Object>) res.get("position");
            lat.add(Double.parseDouble(position.get("y").toString()));
            lon.add(Double.parseDouble(position.get("x").toString()));
            users.add(res.get("name").toString() + "=" + res.get("address") + "=" + position.get("address_name").toString() + "=" + position.get("y").toString() + "=" + position.get("x").toString() );
        }

        HashMap<String, Object> result = new HashMap<>();
        MidPoint midPoint = new MidPoint();

        // (1) 직선거리순 (2) 무게중심 (3) 교통점수순
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

        // 쿠키를 저장하기 전 기존에 쿠키가 존재하면 삭제
        deleteCookie(response, request);

        // 이름 및 위치 데이터 저장 및 쿠키에 저장
        int usersLength = users.size();
        for(int i = 1; i <= usersLength; i++){
            String cookieName = "USER" + i; // 쿠키 이름을 사용자1, 사용자2, 사용자3, 사용자4 식으로 저장.
            createCookie(response, cookieName, users.get(i-1)); // 쿠키 생성
        }

        return result;
    }

    /**
     * 작성일 : 2024.05.01
     * 작성자 : 김준식
     * 내용 : 쿠키 생성
     **/
    public void createCookie(HttpServletResponse response, String cookieName, String cookieValue) { // 크키를 생성할 때 쿠키값은 인코딩하여 생성 (만약 인코딩이 지원되지 않는 경우 예외 발생)
        try{
            String urlEncodedCookieValue = URLEncoder.encode(cookieValue, StandardCharsets.UTF_8); // 쿠키값을 url 인코딩하여 저장
            Cookie cookie = new Cookie(cookieName, urlEncodedCookieValue);
            cookie.setMaxAge(24*60*60); // 쿠키 수명 24시간 (시간*분*초)
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 작성일 : 2024.05.01
     * 작성자 : 김준식
     * 내용 : 쿠키 삭제
     **/
    public void deleteCookie(HttpServletResponse response, HttpServletRequest request){
        try{
            Cookie[] cookies = request.getCookies();
            if(cookies != null){
                for(Cookie cookie : cookies){
                    if(cookie.getName().contains("USER")){
                        // 쿠키에 사용자정보가 있을 경우
                        cookie.setMaxAge(0);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 작성일 : 2024.05.05
     * 작성자 : 김준식
     * 내용 : 재탐색 Service
     **/
    public HashMap<String, Object> reSearchPoint( HashMap<String,Object> params){
        HashMap<String ,Object> result = new HashMap<>();
        try{
            result = mapdao.reSearchPoint(params);
        } catch (Exception e) {
            result.put("response", 0); // 검색 결과가 없을 경우 null 반환
            e.printStackTrace();
        }

        return result;
    }
}
