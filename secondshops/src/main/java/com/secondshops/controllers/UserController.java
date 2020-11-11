package com.secondshops.controllers;

import com.secondshops.models.*;
import com.secondshops.services.*;
import com.secondshops.models.*;
import com.secondshops.services.*;
import com.secondshops.utils.FileCheck;
import com.secondshops.utils.InfoCheck;
import com.secondshops.utils.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;




@Controller
@RequestMapping(value = "user")
public class UserController {
	
	private final GoodService goodService;
	private final OrderService orderService;
	private final ReviewService reviewService;
	private final UserService userService;
	private final CollectService collectService;
	
	@Autowired
	public UserController(GoodService goodService, OrderService orderService,
			ReviewService reviewService, UserService userService,
			CollectService collectService) {
		this.goodService = goodService;
		this.orderService = orderService;
		this.reviewService = reviewService;
		this.userService = userService;
		this.collectService = collectService;
	}
	
	//映射到个人中心
	@RequestMapping(value = "userProfile", method = RequestMethod.GET)
	public String getMyProfile(ModelMap model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "redirect:/";
		}
		List<Collect> collects = collectService
				.getCollectByUserId(user.getId());
		for (Collect collect : collects) {
			collect.setGood(goodService.getGoodById(collect.getGoodId()));
		}
		List<Good> goods = goodService.getGoodByUserId(user.getId());
		List<Order> orders = orderService.getOrderByCustomerId(user.getId());
		List<Review> reviews = reviewService.gerReviewByToUserId(user.getId());
		List<Reply> replies = reviewService.gerReplyByToUserId(user.getId());
		List<Order> sellGoods = orderService.getOrderBySellerId(user.getId());
		model.addAttribute("collects", collects);
		model.addAttribute("goods", goods);
		model.addAttribute("orders", orders);
		model.addAttribute("reviews", reviews);
		model.addAttribute("replies", replies);
		model.addAttribute("sellGoods", sellGoods);
		return "user/userProfile";
	}

	
	

}
