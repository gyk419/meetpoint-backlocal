package MeetPoint.meetpoint.Map.controller;

import MeetPoint.meetpoint.Map.service.mapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/map")
public class MapController {

    @Autowired
    mapService mapService;

    @GetMapping
    public String webView(){
        System.out.println("webView 실행");
        return mapService.webView();
    }

    /**
     * 작성일 : 2024.04.01
     * 작성자 : 김준식
     * 내용 : 중간지점 좌표 계산 Controller
     **/
    @PostMapping("/mainPage")
    public HashMap<String, Object> findCenterPoint(@RequestBody HashMap<String, List<Double>> params) {
        System.out.println("findCenterPoint - 컨트롤러 실행");
        System.out.println("parmas: " + params);
        double re = params.get("calculation").get(0);
        int option = (int)re;
        return mapService.findCenterPoint(params.get("lat"), params.get("lon"), option);
    }

}
