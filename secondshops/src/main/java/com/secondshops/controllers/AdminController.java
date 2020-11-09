package com.secondshops.controllers;

import com.secondshops.models.FirstType;
import com.secondshops.models.Good;
import com.secondshops.models.Order;
import com.secondshops.models.User;
import com.secondshops.services.GoodService;
import com.secondshops.services.OrderService;
import com.secondshops.services.TypeService;
import com.secondshops.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(value = "admin")
public class AdminController {

    private final UserService userService;
    private final GoodService goodService;
    private final TypeService typeService;
    private final OrderService orderService;

    @Autowired
    public AdminController(UserService userService, GoodService goodService, TypeService typeService, OrderService orderService) {
        this.userService = userService;
        this.goodService = goodService;
        this.typeService = typeService;
        this.orderService = orderService;
    }
    @RequestMapping(value = "/adminLogin", method = RequestMethod.GET)
    public String getAdminLogin(){
        return "admin/adminLogin";
    }


}
