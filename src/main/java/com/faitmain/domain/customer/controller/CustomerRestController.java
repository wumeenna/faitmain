

package com.faitmain.domain.customer.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.faitmain.domain.customer.domain.Customer;
import com.faitmain.domain.customer.service.CustomerService;
import com.faitmain.domain.user.domain.User;
import com.faitmain.global.common.Paging;
import com.faitmain.global.common.Search;
import com.faitmain.global.util.security.SecurityUserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/customer/*")
public class CustomerRestController {
	
	 @Autowired
	 @Qualifier("customerServiceImpl")
	private CustomerService customerService;
	
	public CustomerRestController() {
		log.info("Controller = {} ", CustomerRestController.class);
	}
	
	@GetMapping("json/FAQListConditionPayment")
	public JSONArray FAQListConditionPayment () throws Exception {
		
		
		JSONArray lol = new JSONArray();
		
		return lol;
	}
	
//	@RequestMapping(value = "/json/getFAQCategoryCode", method = RequestMethod.GET, produces="application/json;")
//	public @ResponseBody List<Customer> getFAQCategoryCode(String FAQCategoryCode) throws Exception{
//		String result = "";
//		List<Customer> boardList = customerService.getFAQList(FAQCategoryCode);
//		
//		return boardList;
//	}
	
	@GetMapping("json/getCustomerBoardList")
	public List<Customer> listFAQ(@ModelAttribute Customer customer, String FAQCategoryCode, Paging paging) throws Exception{
		
		System.out.println(FAQCategoryCode);
		if(FAQCategoryCode.equals("00")) {
			List<Customer> boardList = customerService.getFAQList("");
	
			return boardList;	
		
		}else {
		
			List<Customer> boardList = customerService.getFAQList(FAQCategoryCode);
			System.out.println(boardList);

			return boardList;
		}
		
		
	}
	
	@GetMapping("json/deleteFAQ/{boardNumber}")
	public String deleteFAQ(@PathVariable int boardNumber) throws Exception{
		
		JSONObject jsonObject = new JSONObject();
		int result = customerService.deleteCustomerBoard(boardNumber);
		
		System.out.println("FAQ ?????? : "+ result);
		
		if(result > 0) {
			jsonObject.put("responseCode", "success");
		}else {
			jsonObject.put("responseCode", "fail");
		}
		
		
		return jsonObject.toJSONString();
	}
	
	
	
	@PostMapping("json/updateCustomerBoard")
	public int  getCustomerBoard(@ModelAttribute Customer customer) throws Exception{
		
		SecurityUserService securityUserService = ( SecurityUserService ) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // principal ??? ????????? ?????? ?????? ??????
		User user = (User) securityUserService.getUser();
		
		System.out.println("/customer/json/updateCustomerBoard : POST start ...");
		System.out.println("????????????!! Cutstomer ." + customer);

		
//		customerService.getCustomerBoardList(customer.getBoardType());
		
		int result =  customerService.updateCustomerBoard(customer) ; 
		System.out.println("result = " +  result ); // ????????? ???????????? 1 , ??????  0  		
		System.out.println("/customer/json/updateCustomerBoard : POST end ...");
		
		
 		return result;
	}
	
//	/customer/json/uploadSummernoteImageFile	
	@PostMapping(value="json/uploadSummernoteImageFile", produces = "application/json")
	@ResponseBody
	public String uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile) {
		
		System.out.println("uploadSummernoteImageFile");
		
		JSONObject jsonObject = new JSONObject();
		
		String fileRoot = "C:\\summernote_image\\";											//????????? ?????? ?????? ??????
		String originalFileName = multipartFile.getOriginalFilename();						//???????????? ?????????
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));  //?????? ?????????
		
		String savedFileName = UUID.randomUUID() + extension;								//????????? ?????????
		
		File targetFile = new File(fileRoot + savedFileName);
		
		try {
			InputStream fileStream = multipartFile.getInputStream();
			FileUtils.copyInputStreamToFile(fileStream, targetFile);	//?????? ??????
			jsonObject.put("url", "/summernoteImage/"+savedFileName);
			jsonObject.put("responseCode", "success");
				
		} catch (IOException e) {
			FileUtils.deleteQuietly(targetFile);	//????????? ?????? ??????
//			jsonObject.addProperty("responseCode", "error");
			e.printStackTrace();
		}
		return jsonObject.toJSONString();
	}
	
	
	
}