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
	
	//进入首页显示商品
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getHomeGoods(
			ModelMap model,
			@RequestParam(required = false, defaultValue = "") String searchText,
			@RequestParam(required = false) Integer secondTypeId,
			@RequestParam(required = false) Integer firstTypeId,
			@RequestParam(required = false, defaultValue = "0") int offset,
			@RequestParam(required = false, defaultValue = "8") int limit) {

		List<Good> goods = goodService.getGoodsBySearchAndType(searchText,
				 firstTypeId, offset, limit);
		
		List<FirstType> firstTypes = typeService.getAllFirstType();
		
//		  for (FirstType firstType : firstTypes) {
//			  firstType.setSecondType(typeService
//		  .getSecondTypeByFirstTypeId(firstType.getId())); }
		 
		model.addAttribute("firstTypes", firstTypes);
		model.addAttribute("goods", goods);
	
		model.addAttribute("offset", offset);
		model.addAttribute("limit", limit);
		return "home/homeGoods";
	}
	

	//映射到发布界面
	@RequestMapping(value = "/goods/publishGood", method = RequestMethod.GET)
	public String getPublishGood(ModelMap model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "redirect:/";
		}
		Good good = new Good();
		List<FirstType> firstTypes = typeService.getAllFirstType();
		//List<Good> goods = goodService.getAllGoods(0, 5);
		//model.addAttribute("goods", goods);
		model.addAttribute("good", good);
		model.addAttribute("firstTypes", firstTypes);
		return "goods/publishGood";
	}
	//获取商品id
	@RequestMapping(value = "/goods/publishGood", method = RequestMethod.POST)
	public String getGoodId(ModelMap model, HttpSession session,
			@Valid Good good) {
		
		User user = (User) session.getAttribute("user");
		
		good.setUserId(user.getId());
		good.setPhotoUrl("/statics/image/goods/default/nophoto.png");
		if (goodService.insertGood(good) != 1) {
			System.out.println("插入物品失败！");
		}
		//model.addAttribute("goods", goods);
		model.addAttribute("good", good);
		//model.addAttribute("firstTypes", firstTypes);
		return "goods/publishGood";
	}
	//上传商品图片
	@RequestMapping(value = "/goods/publishGood/uploadImage", method = RequestMethod.POST)
	public String uploadImage(
			HttpSession session,
			@RequestParam(value = "goodId", required = false) Integer goodId,
			@RequestParam(value = "mainFile", required = false) MultipartFile mainFile,
			@RequestParam(value = "file", required = false) MultipartFile[] file)
			throws IOException {
		User user = (User) session.getAttribute("user");
		FileCheck fileCheck = new FileCheck();
		RandomString randomString = new RandomString();
		String filePath = "/statics/image/goods/" + user.getId() + "/" + goodId;
		String pathRoot = fileCheck.checkGoodFolderExist(filePath);
		String name;
		if (!mainFile.isEmpty()) {
			String fileName = goodId + randomString.getRandomString(10);
			String contentType = mainFile.getContentType();
			String imageName = contentType.substring(contentType.indexOf("/") + 1);
			name = fileName + "." + imageName;
			mainFile.transferTo(new File(pathRoot + name));
			String photoUrl = filePath + "/" + name;
			goodService.updateGoodPhotoUrl(photoUrl, goodId);
		}
		for (MultipartFile mf : file) {
			if (!mf.isEmpty()) {
				// 生成uuid作为文件名称
				String fileName = goodId + randomString.getRandomString(10);
				// 获得文件类型（可以判断如果不是图片，禁止上传）
				String contentType = mf.getContentType();
				// 获得文件后缀名称
				String imageName = contentType.substring(contentType
						.indexOf("/") + 1);
				name = fileName + "." + imageName;
				System.out.println("name:" + name);
				mf.transferTo(new File(pathRoot + name));
				Image image = new Image();
				image.setGoodId(goodId);
				image.setName(name);
				image.setUrl(filePath + "/" + name);
				imageService.insertImage(image);
			} else {
				System.out.println("文件为空！");
			}
		}
		return "redirect:/";
	}
	//进入商品详情页
	@RequestMapping(value = "/goods/goodInfo", method = RequestMethod.GET)
	public String getGoodInfo(ModelMap model, HttpSession httpSession,
			@RequestParam(required = false) Integer goodId) {
		Good goodInfo = goodService.getGoodById(goodId);
		if (goodInfo == null) {
			return "goods/error";
		}
		Integer collect = 1;
		User user = (User) httpSession.getAttribute("user");
		if (user == null) {
			collect = 0;
		} else {
			if (collectService.getCollect(goodId, user.getId())) {
				collect = 2;
			}
		}
		List<Image> images = imageService.getImageByGoodId(goodId);
		User goodUser = userService.getUserById(goodInfo.getUserId());
		goodInfo.setGoodUser(userService.getUserById(goodInfo.getUserId()));
		//goodInfo-type
		goodInfo.setGoodFirstType(typeService.getFirstTypeById1(goodInfo.getFirstTypeId()));
		goodInfo.setGoodSecondType(typeService.getSecondTypeById(goodInfo.getSecondTypeId()));
		List<Review> reviews = reviewService.gerReviewByGoodId(goodId);
		for (Review review : reviews) {
			review.setReplys(reviewService.gerReplyByReviewId(review.getId()));
		}
		/*
		 * List<Good> goods = goodService.getRECGoods(goodInfo.getSecondTypeId(),
		 * goodInfo.getId());
		 */
		List<Good> goods = goodService.getRECGoods(goodInfo.getFirstTypeId(),
				goodInfo.getId());
		model.addAttribute("message", message);
		model.addAttribute("reviews", reviews);
		model.addAttribute("goodInfo", goodInfo);
		model.addAttribute("images", images);
		model.addAttribute("goodUser", goodUser);
		model.addAttribute("goods", goods);
		model.addAttribute("collect", collect);
		message = "";
		return "goods/goodInfo";
	}
	//后台删除商品
	@RequestMapping(value = "/goods/userGoodEdit/delete/{goodId}", method = RequestMethod.GET)
	public ResponseEntity<Boolean> deleteGood(@PathVariable Integer goodId) {
		Boolean success;
		success = goodService.deleteGood(goodId) > 0;
		 return ResponseEntity.ok(success); 
	
	}
	//后台下架商品
	@RequestMapping(value = "/goods/userGoodEdit/updateGoodStatus/{goodId}", method = RequestMethod.GET)
	public ResponseEntity updateGoodStatus(@PathVariable Integer goodId) {
		Boolean success;
		success = goodService.updateGoodStatusId(0, goodId) > 0;
		return ResponseEntity.ok(success);
	}
	
	@RequestMapping(value = "/admin/goods/allGoods", method = RequestMethod.GET)
	public ResponseEntity adminGetAllGoods() {
		List<Good> goodList = goodService.getAllGoodList();
		for (Good good : goodList) {
			good.setGoodUser(userService.getUserById(good.getUserId()));
			good.setGoodSecondType(typeService.getSecondTypeById(good
					.getSecondTypeId()));
		}
		return ResponseEntity.ok(goodList);
	}
	
	

	

}
