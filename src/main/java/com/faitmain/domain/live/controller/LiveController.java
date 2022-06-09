package com.faitmain.domain.live.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.faitmain.domain.product.service.ProductService;
import com.faitmain.domain.live.domain.Live;
import com.faitmain.domain.live.domain.LiveProduct;

import com.faitmain.domain.live.domain.LiveReservation;

import com.faitmain.domain.live.domain.LiveUserStatus;
import com.faitmain.domain.live.service.LiveService;
import com.faitmain.domain.user.domain.User;
import com.faitmain.domain.user.service.UserSerivce;

@Slf4j
@Controller
@RequestMapping( "/live/" )
public class LiveController {
   
   // Field
   @Autowired
   @Qualifier("liveServiceImpl")
   private LiveService liveService;
   
   @Autowired
   @Qualifier("productServiceImpl")
   private ProductService productService;
   
   @Autowired
   @Qualifier("userServiceImpl")
   private UserSerivce userSerivce;
   
   public LiveController() {
	   log.info( "Controller = {} ", this.getClass() );
   }
   
   @GetMapping( "liveRoom" )
   public String getLiveRoomList( Model model ) throws Exception {
      
      System.out.println("/live/getLiveRoomList : GET start...");
      log.info( "Controller = {} ", "/live/liveRoomList : GET start..." );
      
		log.info( "getRoomsList = {} ", this.getClass() );
		
		JSONObject result = null;
		StringBuilder sb = new StringBuilder();
		
		 TrustManager[] trustCerts = new TrustManager[]{
	                new X509TrustManager() {
	                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                        return null;
	                    }
	                    public void checkClientTrusted(
	                        java.security.cert.X509Certificate[] certs, String authType) {
	                    }
	                    public void checkServerTrusted(
	                        java.security.cert.X509Certificate[] certs, String authType) {
	                    }
	                }
	            };
	 
	         SSLContext sc = SSLContext.getInstance("TLSv1.2");
	         sc.init(null, trustCerts, new java.security.SecureRandom());
	         
	         URL url = new URL("https://vchatcloud.com/openapi/v1/rooms");
	 
	         HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
	         conn.setSSLSocketFactory(sc.getSocketFactory());
	         conn.setRequestMethod("GET");
	         
	         conn.setRequestProperty("Content-type", "application/json");
	         conn.setRequestProperty("accept", "*/*");
	         conn.setRequestProperty("api_key", "kmLueZ-chdq38-O7LGgP-Ggd14x-20220604144349");
	         conn.setRequestProperty("X-AUTH-TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJ2Y3h6dmN4ejE1OUBnbWFpbC5jb20iLCJleHAiOjE2NTQ2NzAxMDksImlhdCI6MTY1NDY1MjEwOSwiYXV0aG9yaXRpZXMiOiJbUk9MRV9VU0VSXSJ9.YHpIHG-4HyD-Iib4SRGjwxGkxsyaS5p69E5vLT46Elg");
	         conn.setDoOutput(true);
	         
	         // 데이터 입력 스트림에 답기
	         BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	         while(br.ready()) {
	        	 sb.append(br.readLine());
	         }
	         conn.disconnect();
	         
	         result = (JSONObject) new JSONParser().parse(sb.toString());
	         
	         // REST API 호출 상태 출력하기
	         StringBuilder out = new StringBuilder();
	         out.append(result.get("status") + " : " + result.get("status_message")+"\n");
	         
	         // JSON데이터에서 "data"라는 JSONObject를 가져온다.
	         JSONArray data = (JSONArray)result.get("list");
	         JSONObject tmp;
	         for(int i=0; i<data.size(); i++) {
	        	 tmp = (JSONObject)data.get(i);
	        	 System.out.println("data["+i+"] : "+tmp);
	         }  
	         System.out.println("data : " + data);
	         model.addAttribute("json", data);
      
	         	log.info( "Controller = {} ", "/live/liveRoomList : GET end..." );
      
      			return "view/live/liveList";
   }
   
   @GetMapping( "live" )
   public String getLive( ) throws Exception {
      
      System.out.println("/live/getLive : GET start...");
      log.info( "Controller = {} ", "/live/getLive : GET start..." );
     
      
      log.info( "Controller = {} ", "/live/getLive : GET end..." );
      
      return "view/live/live";
   }
   
   
   
   
	@PostMapping("create")
	public String createRoom(  HttpServletRequest req,
							   @RequestParam("roomName") String liveTitle,
								HttpSession session ) throws Exception {
		log.info( "createRoom = {} ", this.getClass() );
		
		String[] liveProducts = req.getParameterValues("liveProduct");
		
		System.out.println(liveTitle);
		
		for(String product : liveProducts) {
		System.out.println(product);
		}
		
		JSONObject result = null;
		StringBuilder sb = new StringBuilder();
		
		 TrustManager[] trustCerts = new TrustManager[]{
	                new X509TrustManager() {
	                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                        return null;
	                    }
	                    public void checkClientTrusted(
	                        java.security.cert.X509Certificate[] certs, String authType) {
	                    }
	                    public void checkServerTrusted(
	                        java.security.cert.X509Certificate[] certs, String authType) {
	                    }
	                }
	            };
	 
	         SSLContext sc = SSLContext.getInstance("TLSv1.2");
	         sc.init(null, trustCerts, new java.security.SecureRandom());
	         
	         URL url = new URL("https://vchatcloud.com/openapi/v1/rooms");
	 
	         HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
	         conn.setSSLSocketFactory(sc.getSocketFactory());
	         
	         conn.setRequestMethod("POST");
	         
	         conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
	         conn.setRequestProperty("accept", "*/*");
	         conn.setRequestProperty("api_key", "kmLueZ-chdq38-O7LGgP-Ggd14x-20220604144349");
	         conn.setRequestProperty("X-AUTH-TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJ2Y3h6dmN4ejE1OUBnbWFpbC5jb20iLCJleHAiOjE2NTQ3NzQxOTYsImlhdCI6MTY1NDc1NjE5NiwiYXV0aG9yaXRpZXMiOiJbUk9MRV9VU0VSXSJ9.AXFLJ3ohZbkiUVBtUa9DIMzpd8txXSwb8ttebAuxInk");
	         conn.setDoOutput(true);
	        
	         String Data = "roomName="+ liveTitle + "&maxUser=5&webrtc=91";
	         
//	         JSONObject Data = new JSONObject();
//	         Data.put("maxUser", "5");
//	         Data.put("roomName", "CreateRoomTest");
//	         System.out.println("JSONData : " + Data.toString());
	        
	         OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	         wr.write(Data);
	         wr.flush();
	         
	         // 데이터 입력 스트림에 담기
	         BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	         while(br.ready()) {
	        	 sb.append(br.readLine());
	         }
	         conn.disconnect();
	         System.out.println("br" + br);
	         System.out.println("wr" + wr);
	         result = (JSONObject) new JSONParser().parse(sb.toString());
	         
	         // REST API 호출 상태 출력하기
	         StringBuilder out = new StringBuilder();
	         out.append(result.get("status") + " : " + result.get("status_message")+"\n");
	         
	         // JSON데이터에서 "data"라는 JSONObject를 가져온다.
	         JSONObject data = (JSONObject) result.get("data");
	         String roomId = (String) data.get("roomId"); 
	         long Code = (long)result.get("result_cd");
	         System.out.println("Code : " + Code);
	         System.out.println("data : " + data);
	         System.out.println("roomId : " + roomId);
	         
	         //라이브 방송 등록 후 DB에 데이터 입력
	         //라이브
	         User user = (User)session.getAttribute("user");
	         
	         Live live = new Live();
	         
	         live.setRoomId(roomId);
	         live.setStoreId(user.getId());
	         live.setLiveTitle(liveTitle);
	         live.setLiveIntro(liveTitle);
	         live.setLiveImage("라이브 대표사진.png");
	     
	         
	         liveService.addLive(live);
	         System.out.println("이잉" + liveService.getLive(10011));
	         live = null;
	         
//	 		for(String product : liveProducts) {
//	 			System.out.println(product);
//	 			}
	         
	         //라이브 판매 상품
	         live = liveService.getLiveByStoreId(user.getId());
	        
	         LiveProduct liveProduct = new LiveProduct();
	         
	         for(String product : liveProducts) {
	        	 liveProduct.setLiveNumber(live.getLiveNumber());
	        	 liveProduct.setLiveReservationNumber(0);
	        	 liveProduct.setProductNumber(Integer.parseInt(product));
	        	 liveProduct.setProductMainImage(productService.getProduct(Integer.parseInt(product)).getProductMainImage());
	        	 liveProduct.setProductName(productService.getProduct(Integer.parseInt(product)).getProductName());
	        	 liveProduct.setProductDetail(productService.getProduct(Integer.parseInt(product)).getProductDetail());
	        	 liveService.addLiveProduct(liveProduct);
	         	}
	         
	         
	         
	         return "view/live/live";
	         
	}
   
   @GetMapping( "addLiveView" )
   public String addLiveView( HttpSession session,
		   					  Model model ) throws Exception {
      
      System.out.println("/live/addLiveView : GET start...");
      log.info( "Controller = {} ", "/live/addLiveView : GET start..." );
      
	   User user = (User)session.getAttribute("user");
	   
	   Map<String, Object> map = productService.getProductListByStoreId(user.getId());
	      
	   model.addAttribute("map", map);
	   
	   System.out.println(map);
     
      
      log.info( "Controller = {} ", "/live/addLiveView : GET end..." );
      
      return "view/live/addLiveView";
   }
   
   @GetMapping( "addLive" )
   public String addLive( ) throws Exception {
      
      System.out.println("/live/addLive : GET start...");
      log.info( "Controller = {} ", "/live/addLive : GET start..." );
      
     
      
      log.info( "Controller = {} ", "/live/addLiveView : GET end..." );
      
      return "view/live/addLive";
   }
   
   @GetMapping( "getLiveList" )
   public String getLiveList( Model model ) throws Exception {
      
      System.out.println("/live/getLiveList : GET start...");
      
      Map<String, Object> map = liveService.getLiveList();
      
      model.addAttribute(map);
      
      System.out.println("/live/getLiveList : GET end...");
      
      return "forward:/live/main.jsp";
   }
   
   @GetMapping( "getLiveProductList" )
   public String getLiveProductLivst( HttpSession session,
		   							  Model model ) throws Exception {
	   
	   System.out.println("/live/getLiveList : GET start...");
	      
	   User user = (User)session.getAttribute("user");
	   
//	   Map<String, Object> map = productService.getProductListByStoreId(user.getId());
//	      
//	   model.addAttribute(map);
	      
	   System.out.println("/live/getLiveList : GET end...");
	   
	   return "forward://live/getLiveProductList.jsp";
   }
   
   
   @GetMapping("watchLive")
   public String watchLive( @RequestParam( "liveNumber" ) int liveNumber,
		   												  Model model) throws Exception {
	   
	      System.out.println("/live/watchLive : GET start...");
	      
	      Live live = liveService.getLive(liveNumber);
	     
	      System.out.println("Controller watchLive liveInfo : " + live);
	     
	      model.addAttribute("live", live);
	      
	      System.out.println("/live/watchLive : GET end...");
	      
	      return "forward:/live/watchLive.jsp";
	   
   }
   
   @GetMapping("updateLive")
   public String updateLive( ) throws Exception {
	   
	      System.out.println("/live/updateLive : Get start...");
	     
	      System.out.println("/live/updateLive : Get end...");
	      
	      return "forward:/live/updateLiveView.jsp";
	   
   }
   
   @PostMapping("updateLive")
   public String updateLive( @ModelAttribute( "live" ) Live live,
		   											   Model model ) throws Exception {
	   
	      System.out.println("/live/updateLive : POST start...");
	      
	      int result = liveService.updateLive(live);
	      
	      System.out.println("Controller updateLive result : " + result);
	      
	      System.out.println("/live/updateLive : POST end...");
	      
	      //�젙蹂대�寃� �썑 諛⑹넚�솕硫댁쑝濡� �떎�떆 �씠�룞
	      live = liveService.getLive(live.getLiveNumber());
	      
	      model.addAttribute("live", live);
	      
	      return "forward:/live/transLive.jsp";
	   
   }
   
   @GetMapping("updateLiveStatusCode")
   public String updateLiveStatusCode( @RequestParam( "liveNumber" ) int liveNumber,
		   															 Model model ) throws Exception {
	   	  //諛⑹넚 醫낅즺 updateLiveStatusCode
	      System.out.println("/live/updateLiveStatusCode : GET start...");
	      
	      liveService.updateLiveStatusCode(liveNumber);
	      
	      System.out.println("/live/updateLiveStatusCode : GET end...");
	      
	      
	      //諛⑹넚 醫낅즺 �썑 liveList濡� �씠�룞
	      System.out.println("/live/getLiveList : GET start...");
	      
	      Map<String, Object> map = liveService.getLiveList();
	      
	      model.addAttribute(map);
	      
	      System.out.println("/live/getLiveList : GET end...");
	      
	      return "forward:/live/updateLive.jsp";
	   
   }
   
   @GetMapping("addLiveUserStatus")
   public void addLiveUserStatus( @ModelAttribute( "liveUsrStatus") LiveUserStatus liveUserStatus ) throws Exception {
	      
	      System.out.println("/live/addSanctionUser : GET start...");
	      
	      liveService.addLiveUserStatus(liveUserStatus);
	      
	      System.out.println("/live/addSanctionUser : GET end...");
	   	
   }
   
   @GetMapping("getAlarmList")
   public String getAlarmList( @RequestParam ("liveNumber") int liveNumber,
		   													Model model ) throws Exception {
	   
	   System.out.println("/live/getAlarmList : GET start...");
	 
	   Map<String, Object> map = liveService.getLiveUserStatusList(liveNumber);
	   
	   model.addAttribute(map);   
	   
	   System.out.println("/live/getAlarmList : GET end...");
	   
	   return "forward:/live/getAlarmList.jsp";
   }
   
   @GetMapping("getLiveReservationCal")
   public String getLiveReservationCal() {
	   log.info("getLiveReservationCal() : GET start... ");
	   
	   // 단순 네비게이션
	   
	   log.info("getLiveReservationCal() : GET end... ");
	   return "/view/live/liveReservationCal";
   }
   
   @GetMapping("getLiveReservationList")
   public String getLiveReservationList(@RequestParam String date) throws Exception {
	   log.info("getLiveReservationList() : GET start... ");
	   
	   log.info("date : " + date);
	   
	   List<LiveReservation> list = liveService.getLiveReservationList(date);
	   log.info("list : {}", list);
	   List<LiveReservation> resultList = new ArrayList<LiveReservation>();
	   
	   for(LiveReservation obj : list) {
		   obj.setLiveProduct(liveService.getLiveProductList(obj.getLiveReservationNumber()));
		   obj.setStore(userSerivce.getUser(obj.getStore().getId()));
		   
		   resultList.add(obj);
	   }
	   
	   // 결과 확인용 for문
	   for(LiveReservation obj : resultList) {
		   log.info("resultList : {}", obj);
	   }
	   
	   
	   log.info("getLiveReservationList() : GET end... ");
	   return "/view/live/liveReservationList";
   }

}
