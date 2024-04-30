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
//    @PostMapping("/mainPage")
//    public HashMap<String, Object> findCenterPoint(@RequestBody HashMap<String, List<Double>> params) {
//        System.out.println("findCenterPoint - 컨트롤러 실행");
//        System.out.println("parmas: " + params);
//        double re = params.get("calculation").get(0);
//        int option = (int)re;
//        return mapService.findCenterPoint(params.get("lat"), params.get("lon"), option);
//    }

//    @PostMapping("/mainPage")
//    public void findPoint(@RequestBody List<Object> params){
//        System.out.println("params : " + params);
//        List<HashMap<String, Object>> re = (List<HashMap<String, Object>>) params.get(1);
//        int i = 1;
//        for(HashMap<String, Object> result : re) {
//            System.out.println("result" + i + " : " + result.toString());
//            System.out.println(i + " position : " + result.get("position"));
//            HashMap<String, Object> position = (HashMap<String, Object>) result.get("position");
//            System.out.println(i + " latitude : " + Double.parseDouble(position.get("y").toString()));
//            System.out.println(i + " longitude : " + Double.parseDouble(position.get("x").toString()) );
//            i++;
//        }
//        System.out.println("num : " + params.get(0));
//        System.out.println("num type : " + params.get(0).getClass());
//    }

    @PostMapping("/mainPage")
    public HashMap<String, Object> calCenterPoint(@RequestBody List<Object> params){
        System.out.println("findCenterPoint - Controller 실행");
        return mapService.findCenterPoint(params);
    }

}
