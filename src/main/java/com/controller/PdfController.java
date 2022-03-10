package com.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;

@RequiredArgsConstructor
@Controller
@RequestMapping("/pdf")
public class PdfController {
	private static final Logger log = LogManager.getLogger(PdfController.class);
	
	@RequestMapping(method=RequestMethod.POST, value="/test")
	public String test(@RequestParam Map<String, Object> param, Model model) {
	    try
	    {
	        JRDataSource datasource = new JREmptyDataSource(1);	// 빈 데이터 선언
	            
	        model.addAttribute("format","pdf");	// format PDF로 설정
	        model.addAttribute("test","Hello JasperReports");	// jasper 보고서 파일에 넘겨줄 파라미터1 추가
	        model.addAttribute("name","강지호");	// jasper 보고서 파일에 넘겨줄 파라미터1 추가
	        
	        // jasper 보고서 파일에 넘겨줄 datasource 지정 (datasource 안 넘겨줘도 실행되지만, 보고서가 아무것도 보이지 않을 수 있다.)
	        model.addAttribute("dataSource", datasource);                
	    } 
	    catch (Exception e)
	    {
	    	log.error(e.getCause()); 
	    }
		return "jasper/test/test1";	// jasper 파일 위치 지정		
	}
}
