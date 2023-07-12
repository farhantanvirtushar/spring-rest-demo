package com.example.springrestdemo.controllers;

import com.example.springrestdemo.model.HelloRes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.Date;

@Controller
@RequestMapping("/rest/home")
public class HomeController {
    @ResponseBody
    @RequestMapping(value = "",method = RequestMethod.GET)
    public ResponseEntity<HelloRes> hello(){

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        HelloRes helloRes = new HelloRes();
        helloRes.setMessage("Hello World");
        helloRes.setTime(timestamp.toString());

        return ResponseEntity.ok(helloRes);
    }


}
