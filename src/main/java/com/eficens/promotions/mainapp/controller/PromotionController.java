package com.eficens.promotions.mainapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eficens.promotions.mainapp.modal.Product;
import com.eficens.promotions.mainapp.modal.PromoResponse;
import com.eficens.promotions.mainapp.service.PromoService;

@RestController
@RequestMapping("/promo")
public class PromotionController {

	@Autowired PromoService promoService;
	
	@GetMapping("/createProductsFile")
	public PromoResponse createProductsFile() {
		Product[] productArray = promoService.getAllProductsFromProductAPI();
		PromoResponse promoResponse = new PromoResponse();
		
		try {
			String fileName = promoService.createProductsFile(productArray);
			promoResponse.setFileName(fileName);
			promoResponse.setStatusCode(200);
			promoResponse.setStatusMessage("Product API call is Successful and File Created Successflly.");
			
		}
		catch(Exception ex){
			promoResponse.setFileName(null);
			promoResponse.setStatusCode(500);
			promoResponse.setStatusMessage("createProductsFile API call is failed");
		}
		
		return promoResponse;
	}
	
	@PostMapping("/sendProductDataInEmail")
	public PromoResponse sendProductDataInEmail() {
		PromoResponse promoResponse = new PromoResponse();
		Product[] productArray = promoService.getAllProductsFromProductAPI();
		String fileName = promoService.createProductCSVFile(productArray);
		String emailAPIResponse = promoService.sendEmailWithProductDataInCSV(fileName);
		promoResponse.setFileName(fileName);
		promoResponse.setStatusCode(200);
		promoResponse.setStatusMessage(emailAPIResponse);
		
		return promoResponse;
	}
}
