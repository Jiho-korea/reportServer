package com.controller;

import java.io.FileNotFoundException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.service.ShopOrderService;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/pdf")
public class PdfController {
	private static final Logger log = LogManager.getLogger(PdfController.class);

	@Autowired
	private ShopOrderService shopOrderService;

	@PostMapping("/shopOrderSimple")
	public ResponseEntity<byte[]> shopOrderSimple(@RequestParam Map<String, Object> param) throws JsonMappingException, JsonProcessingException, FileNotFoundException, JRException {
		byte[] data = shopOrderService.generateShopOrderSimpleReport(param);
		return new ResponseEntity<byte[]>(data, HttpStatus.OK);

	}
}
