package com.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/pdf")
public class PdfController {
	@PostMapping("/shopOrderSimple")
	public String index(@RequestParam Map<String, Object> param, Model model) {
//		model.addAttribute("title", title);
		return "index";		
	}
}
