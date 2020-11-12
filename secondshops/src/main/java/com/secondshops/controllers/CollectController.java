package com.secondshops.controllers;

import com.secondshops.models.Collect;
import com.secondshops.services.CollectService;
import com.secondshops.services.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@Controller
@RequestMapping(value = "/collect")
public class CollectController {
	
    private final CollectService collectService;
    private final GoodService goodService;

    @Autowired
    public CollectController(CollectService collectService, GoodService goodService) {
        this.collectService = collectService;
        this.goodService = goodService;
    }
     //实现收藏
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ResponseEntity insertCollect(@RequestBody Collect collect){
        return ResponseEntity.ok(collectService.insertCollect(collect));
    }
    
    //删除收藏
    @RequestMapping(value = "/delete/{collectId}&{userId}", method = RequestMethod.GET)
    public ResponseEntity deleteCollect(@PathVariable Integer collectId,
                                        @PathVariable Integer userId){
        Boolean success = collectService.deleteCollect(collectId);
        if (success){
            List<Collect> collects = collectService.getCollectByUserId(userId);
            for (Collect collect : collects){
                collect.setGood(goodService.getGoodById(collect.getGoodId()));
            }
            return ResponseEntity.ok(collects);
        }
        return ResponseEntity.ok(success);
    }

}
