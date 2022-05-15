package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.UserDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

private UserDao userDao;

public AccountController (UserDao userDao){
    this.userDao = userDao;
}

    @RequestMapping (path = "/account/", method = RequestMethod.GET)
    public BigDecimal getBalance (Principal principal) {
        int id = userDao.findIdByUsername(principal.getName());
        return userDao.getAccountBalance(id);
    }

    @RequestMapping (path = "/user/", method = RequestMethod.GET)
    public UserDTO getUser(Principal principal){
        return userDao.findByUsername(principal.getName());
    }

    @RequestMapping (path = "/user/account/", method = RequestMethod.GET)
    public UserDTO getUserAccount(Principal principal){
        return userDao.findAccountByUsername(principal.getName());
    }

    @RequestMapping (path = "/user/allusers/", method = RequestMethod.GET)
    public List<UserDTO> getUsers (Principal principal) {
        return userDao.findAllUsersWithAccount(principal.getName());
    }
}
