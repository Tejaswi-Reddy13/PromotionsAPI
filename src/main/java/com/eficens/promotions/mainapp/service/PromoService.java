package com.eficens.promotions.mainapp.service;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eficens.email.app.modal.EmailTemplate;
import com.eficens.promotions.mainapp.modal.Product;

@Service
public class PromoService {

	@Value("${application.getProducts.url}")
	private String getAllProductsURL;
	
	@Value("${application.file.server.path}")
	private String serverPath;
	
	@Value("${application.send.email.with.attachment.url}")
	private String sendEmailWithAttachmentEndpoint;
	
	@Value("${mail.to.address}")
	private String toAddress;
	
	public Product[] getAllProductsFromProductAPI() {
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(getAllProductsURL, Product[].class);
		System.err.println(responseEntity.getBody().length);//total no of Products
		return responseEntity.getBody();
	}
	
	public String createProductsFile(Product[] productArray) {
		String fileName = serverPath+"products.txt";
		try {
		      FileWriter myWriter = new FileWriter(fileName);
		      for(Product product : productArray) {
		    	  myWriter.write(product.getProductName()+"\t"+product.getPrice()+"\n");
		      }
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		return fileName;
	}
	public String createProductCSVFile(Product[] productArray) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddhhmmss");
		String dateAsString = simpleDateFormat.format(new Date());
		String fileName=serverPath + "products_" + dateAsString + ".csv";
		for(int i= 0; i < productArray.length; i++) {
			System.out.println(productArray[i].getProductId()+","+productArray[i].getProductName()+","+productArray[i].getPrice());
		}
		try {
		      FileWriter myWriter = new FileWriter(fileName);
		      myWriter.write("Product Id" + "," + "Product Name" + "," + "Price" + "," + "Discount"+ "\n");
		      for(Product product : productArray) {
		    	  myWriter.write(product.getProductId()+ "," + product.getProductName() + "," + product.getPrice() + "," + product.getDiscount()+"\n");
		      }
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		return fileName;
		
	}
	public String sendEmailWithProductDataInCSV(String fileName) {
		RestTemplate restTemplate = new RestTemplate();
		EmailTemplate emailTemplate = new EmailTemplate();
		String emailBody = "Hi, \n"
		                   +"\n"
		                   + "Please find the attached file with Product Data in a CSV."
		                   + "\n"
		                   + "Thanks and Regards,"
		                   + "E-Comm Promo Team";
		String subject = "Product Data : " + new Date();
		emailTemplate.setToAddress(toAddress);
		emailTemplate.setSubject(subject);
		emailTemplate.setEmailBody(emailBody);
		emailTemplate.setAttachmentRequired(true);
		emailTemplate.setFilePath(fileName);
		System.out.println("Email");
		ResponseEntity<String> response = restTemplate.postForEntity(sendEmailWithAttachmentEndpoint, emailTemplate, String.class);
		System.out.println("Email");
	 return response.getBody();
	}
	
	
}
