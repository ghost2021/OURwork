package com.secondshops.controllers;

import com.secondshops.models.*;
import com.secondshops.services.*;
import com.secondshops.models.*;
import com.secondshops.services.*;
import com.secondshops.utils.FileCheck;
import com.secondshops.utils.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
@Controller
public class GoodController {

	private final GoodService goodService;
	private final TypeService typeService;
	private final ReviewService reviewService;
	private final UserService userService;
	private final ImageService imageService;
	private final CollectService collectService;

	private static String message = "";
	
	@Autowired
	public GoodController(GoodService goodService, TypeService typeService,
			ReviewService reviewService, UserService userService,
			ImageService imageService, CollectService collectService) {
		this.goodService = goodService;
		this.typeService = typeService;
		this.reviewService = reviewService;
		this.userService = userService;
		this.imageService = imageService;
		this.collectService = collectService;
	}
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getHomeGoods(
			ModelMap model,
			@RequestParam(required = false, defaultValue = "") String searchText,
			@RequestParam(required = false) Integer secondTypeId,
			@RequestParam(required = false, defaultValue = "0") int offset,
			@RequestParam(required = false, defaultValue = "40") int limit) {
		List<Good> goods = goodService.getGoodsBySearchAndType(searchText,
				secondTypeId, offset, limit);
		double goodsNum = goodService.getGoodsBySearchAndTypeCount(searchText,
				secondTypeId);
		List<FirstType> firstTypes = typeService.getAllFirstType();
		for (FirstType firstType : firstTypes) {
			firstType.setSecondType(typeService
					.getSecondTypeByFirstTypeId(firstType.getId()));
		}
		model.addAttribute("firstTypes", firstTypes);
		model.addAttribute("goods", goods);
		model.addAttribute("pages", Math.ceil(goodsNum / limit));
		model.addAttribute("goodsNum", goodsNum);
		model.addAttribute("offset", offset);
		model.addAttribute("limit", limit);
		return "home/homeGoods";
	}
	

}