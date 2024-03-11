package MeetPoint.meetpoint.Map.controller;

import MeetPoint.meetpoint.Map.service.mapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
