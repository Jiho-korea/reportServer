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
					.compileReport(ResourceUtils.getFile(reportPath + "\\shopOrderSimple.jrxml").getAbsolutePath());

			ObjectMapper objectMapper = new ObjectMapper();
			List list = objectMapper.readValue(param.get("orderList") + "", ArrayList.class);
			log.info(list);
			for(int i=0; i<list.size(); i++){
				Map<String, Object> order = (HashMap)list.get(i);
				List orderRoutingList = (ArrayList) order.get("orderRoutingList");
				// 테스트용 쓰레기 값
				for(int j=0;j<20;j++)
					orderRoutingList.add(orderRoutingList.get(0));
				//
				order.put("orderRoutingList", new JRMapCollectionDataSource(orderRoutingList));				
				log.info(orderRoutingList);
			}
			param.put("OrderListDataSource", new JRMapCollectionDataSource(list));
			
			// Report에 파라미터 전달
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, new JREmptyDataSource());

			data = JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (Exception e) {
			log.error(e);
		}
		return data;
	}
}
