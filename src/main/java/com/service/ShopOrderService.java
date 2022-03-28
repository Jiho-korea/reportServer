package com.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

@Service
public class ShopOrderService {
	private static final Logger log = LogManager.getLogger(ShopOrderService.class);

	String reportPath = "jasper/";

	public byte[] generateOperationSheet(Map<String, Object> param) {
		// Compile the Jasper report from .jrxml to .japser
		byte[] data = null;
		try {
			List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
			
			JasperReport jasperReport = JasperCompileManager
					.compileReport(new ClassPathResource(reportPath + "operationSheet.jrxml").getInputStream());

			ObjectMapper objectMapper = new ObjectMapper();
			List list = objectMapper.readValue(param.get("orderList") + "", ArrayList.class);

			for(int i=0; i<list.size(); i++){
				Map<String, Object> order = (HashMap)list.get(i);
				
				// 바코드 generator 생성
				Code128Bean code128Bean = new Code128Bean();
				code128Bean.setModuleWidth(1f);	
				code128Bean.setFontSize(0);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				BitmapCanvasProvider provider= new BitmapCanvasProvider(out, "image/x-png", 110, BufferedImage.TYPE_BYTE_GRAY, false, 0);
				code128Bean.generateBarcode(provider, (String)order.get("orderNumber"));
				    
				
				BufferedImage orderNumberBarcodeImage = provider.getBufferedImage();
				order.put("orderNumberBarcode", orderNumberBarcodeImage);
				
				List orderRoutingList = (ArrayList) order.get("orderRoutingList");
				// 테스트용 쓰레기 값 //
//				for(int k=0;k<10;k++) {
//					orderRoutingList.add(orderRoutingList.get(0));
//				}
				
				// routing 리스트 DataSource 등록
				for(int j=0; j<orderRoutingList.size(); j++) {
					Map<String, Object> orderRoutingInfo = (HashMap)orderRoutingList.get(j);
					// routing 바코드 생성
					code128Bean.generateBarcode(provider, orderRoutingInfo.get("order_number") + "/" +orderRoutingInfo.get("gop"));
					BufferedImage orderRoutingInfoBarcodeImage = provider.getBufferedImage();
					orderRoutingInfo.put("orderRoutingInfoBarcode", orderRoutingInfoBarcodeImage);
				}
				provider.finish();
				order.put("orderRoutingList", new JRMapCollectionDataSource(orderRoutingList));		
				log.info(order);
				// Report에 파라미터 전달
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, order, new JREmptyDataSource());
				jasperPrintList.add(jasperPrint);
			}			
			
			JRPdfExporter exporter = new JRPdfExporter();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList)); //Set as export input my list with JasperPrint s
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out)); //or any other out streaam
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			configuration.setCreatingBatchModeBookmarks(true); //add this so your bookmarks work, you may set other parameters
			exporter.setConfiguration(configuration);
			exporter.exportReport();
			data = out.toByteArray();
		} catch (Exception e) {
			log.error(e);
		}
		return data;
	}
	
	public byte[] generateSimpleOperationSheet(Map<String, Object> param) {
		// Compile the Jasper report from .jrxml to .japser
		byte[] data = null;
		try {
			JasperReport jasperReport = JasperCompileManager
					.compileReport(new ClassPathResource(reportPath + "simpleOperationSheet.jrxml").getInputStream());

			ObjectMapper objectMapper = new ObjectMapper();
			List list = objectMapper.readValue(param.get("orderList") + "", ArrayList.class);

			for(int i=0; i<list.size(); i++){
				Map<String, Object> order = (HashMap)list.get(i);
				
				// 바코드 generator 생성
				Code128Bean code128Bean = new Code128Bean();
				code128Bean.setModuleWidth(1f);	
				code128Bean.setFontSize(0);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				BitmapCanvasProvider provider= new BitmapCanvasProvider(out, "image/x-png", 110, BufferedImage.TYPE_BYTE_GRAY, false, 0);
				code128Bean.generateBarcode(provider, (String)order.get("orderNumber"));
				    
				
				BufferedImage orderNumberBarcodeImage = provider.getBufferedImage();
				order.put("orderNumberBarcode", orderNumberBarcodeImage);
				
				List orderRoutingList = (ArrayList) order.get("orderRoutingList");
				// 테스트용 쓰레기 값 //
//				for(int k=0;k<10;k++) {
//					orderRoutingList.add(orderRoutingList.get(0));
//				}
				
				// routing 리스트 DataSource 등록
				for(int j=0; j<orderRoutingList.size(); j++) {
					Map<String, Object> orderRoutingInfo = (HashMap)orderRoutingList.get(j);
					// routing 바코드 생성
					code128Bean.generateBarcode(provider, orderRoutingInfo.get("order_number") + "/" +orderRoutingInfo.get("gop"));
					BufferedImage orderRoutingInfoBarcodeImage = provider.getBufferedImage();
					orderRoutingInfo.put("orderRoutingInfoBarcode", orderRoutingInfoBarcodeImage);
				}
				provider.finish();
				order.put("orderRoutingList", new JRMapCollectionDataSource(orderRoutingList));				
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
