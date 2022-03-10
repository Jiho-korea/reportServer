package com.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsViewResolver;

@Configuration
public class JasperReportsConfig{
	@Bean
	public JasperReportsViewResolver jasperReportsViewResolver() {
	    JasperReportsViewResolver resolver = new JasperReportsViewResolver();
	    resolver.setPrefix("/WEB-INF/");
	    resolver.setSuffix(".jasper");
	    resolver.setReportDataKey("datasource");
	    resolver.setViewNames(new String[] {"jasper/*"});
	    resolver.setViewClass(JasperReportsMultiFormatView.class);
	    Properties headers = new Properties();
	    headers.put("filename", "PDF_ExportFile.pdf");
	    resolver.setHeaders(headers);
	    resolver.setOrder(0);
	    return resolver;
	}
}