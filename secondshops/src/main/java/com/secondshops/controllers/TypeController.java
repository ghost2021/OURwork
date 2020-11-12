package com.secondshops.controllers;

import com.secondshops.models.FirstType;
import com.secondshops.models.SecondType;
import com.secondshops.services.GoodService;
import com.secondshops.services.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@Controller
@RequestMapping("type")
public class TypeController {
	
	private final TypeService typeService;
	private final GoodService goodService;

	@Autowired
	public TypeController(TypeService typeService, GoodService goodService) {
		this.typeService = typeService;
		this.goodService = goodService;
	}
	//显示分类
	@RequestMapping(value = "/firstType/{firstTypeId}", method =
			  RequestMethod.GET) public ResponseEntity getFirstType(@PathVariable Integer
			  firstTypeId) { List<FirstType> firstTypes = typeService
			  .getFirstTypeById(firstTypeId); if (firstTypes == null) { return
			  ResponseEntity.ok("isNull"); } return ResponseEntity.ok(firstTypes);
			  
			  }

	//删除分类
	@RequestMapping(value = "/firstType/delete/{firstTypeId}", method = RequestMethod.GET)
	public ResponseEntity deleteFirstType(@PathVariable Integer firstTypeId) {
		Boolean success = typeService.getSecondTypeByFirstTypeId(firstTypeId)
				.isEmpty();
		if (success) {
			success = typeService.deleteFirstType(firstTypeId);
			if (success) {
				List<FirstType> firstTypeList = typeService.getAllFirstType();
				if (firstTypeList == null) {
					return ResponseEntity.ok("isNull");
				}
				return ResponseEntity.ok(firstTypeList);
			}
		}
		return ResponseEntity.ok(success);
	}
	

	
	
	 
	
	  
}
