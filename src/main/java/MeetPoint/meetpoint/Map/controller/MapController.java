package MeetPoint.meetpoint.Map.controller;

import MeetPoint.meetpoint.Map.service.MapService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/map")
public class MapController {

    @Autowired
    MapService mapService;

    /**
     * 작성일 : 2024.04.01
     * 작성자 : 김준식
     * 내용 : 중간지점 좌표 계산 Controller
     **/
    @PostMapping("/mainPage")
    public HashMap<String, Object> calCenterPoint(@RequestBody List<Object> params, HttpServletResponse response, HttpServletRequest request) {
        return mapService.findCenterPoint(params, response, request);
    }

    /**
     * 작성일 : 2024.05.05
     * 작성자 : 김준식
     * 내용 : 재탐색 Controller
     **/
    @PostMapping("/reSearchPoint")
    public HashMap<String, Object> reSearchPoint(@RequestBody HashMap<String, Object> params){
        return mapService.reSearchPoint(params);
    }
}
