package MeetPoint.meetpoint.Map.controller;

import MeetPoint.meetpoint.Map.service.UserChoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * 작성일 : 2024.05.17
 * 작성자 : 김준식
 * 내용 : UserChoice Controller
 **/
@RestController
@RequestMapping("/choice")
public class UserChoiceController {

    @Autowired
    UserChoiceService userChoiceService;

    /**
     * 작성일 : 2024.05.18
     * 작성자 : 김준식
     * 내용 : 선택한 장소 저장 Controller
     **/
    @PostMapping("/storePlace")
    public HashMap<String, Object> storePlace(@RequestBody HashMap<String, Object> params) {
        return userChoiceService.storePlace(params);
    }

    /**
     * 작성일 : 2024.05.21
     * 작성자 : 김준식
     * 내용 : 선택한 장소 및 중간 지점 조회 Controller
     **/
    @PostMapping("selectPlace")
    public HashMap<String, Object> selectPlace(@RequestBody HashMap<String, Object> params){
        return userChoiceService.selectPlace(params);
    }
}
