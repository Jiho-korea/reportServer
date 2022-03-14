package com.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

@Service
public class ShopOrderService {
	private static final Logger log = LogManager.getLogger(ShopOrderService.class);

	String reportPath = "classpath:jasper\\";

	public byte[] generateShopOrderSimpleReport(Map<String, Object> param) {
		// Compile the Jasper report from .jrxml to .japser
		byte[] data = null;
		try {
			JasperReport jasperReport = JasperCompileManager
					.compileReport(ResourceUtils.getFile(reportPath + "\\test1.jrxml").getAbsolutePath());

			ObjectMapper objectMapper = new ObjectMapper();
			List list = objectMapper.readValue(param.get("orderList") + "", ArrayList.class);
			log.info(param);
			log.info(list);
			
			Map<String, Object> parameters = new HashMap<String,Object>();
			parameters.put("param1", "jhkang");
			List<Map<String, ?>> paramMaps = new ArrayList<Map<String, ?>>();
			Map<String, Object> addMap = null;
			Map<String, Object> addMap2 = null;
			for(int i = 0; i < 5; i++) {
				addMap = new HashMap<String, Object>();
				addMap.put("listName1", "value"+i);
				addMap.put("listName2", i);
				paramMaps.add(addMap);
			}
			for(int i = 0; i < 5; i++) {
				addMap2 = new HashMap<String, Object>();
				addMap2.put("listName1", "value"+i);
				addMap2.put("listName2", i);
				paramMaps.add(addMap2);
			}
			parameters.put("TestDataSource", new JRMapCollectionDataSource(paramMaps));
			log.info(parameters);
	            
			// 선택된 Shop Order 목록을 데이터 소스에 넣어줌 
			JRMapCollectionDataSource jrMapCollectionDataSource = new JRMapCollectionDataSource(paramMaps);
			// 그리고 Map 의 Value중 리스트들도(ex. 라우팅) 데이터 소스로 래핑해서 다시 Entry에 넣어줘야 한다.
			//param.put("test", "Hello JasperReport");
			//param.put("name", "jhkang");
			
			// Fill the report
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

			data = JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (Exception e) {
			log.error(e);
		}
		return data;
	}
}
