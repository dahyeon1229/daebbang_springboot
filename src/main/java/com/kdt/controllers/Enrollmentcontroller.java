package com.kdt.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdt.dto.NewEstateDTO;
import com.kdt.dto.RealEstateAgentDTO;
import com.kdt.services.AgentService;
import com.kdt.services.NewEstateService;

@RestController
@RequestMapping("/api/enrollment")
public class Enrollmentcontroller {
	private static final Logger logger = LoggerFactory.getLogger(Enrollmentcontroller.class);
	@Autowired
	private AgentService aServ;
	
	@Autowired
	private NewEstateService neServ;
	
	@GetMapping("/openApi/{bsnmCmpnm}")
	public ResponseEntity<String> callExternalApi(@PathVariable String bsnmCmpnm) {
		String apiUrl = "https://api.vworld.kr/ned/data/getEBOfficeInfo";
		String authKey = "32313C80-CF6D-3E59-953F-930749A348A4";

		try {
			// 한글 부분을 URLEncoder로 인코딩하여 URL에 추가
			String encodedBsnmCmpnm = URLEncoder.encode(bsnmCmpnm, StandardCharsets.UTF_8.toString());

			// URL 및 파라미터 설정
			String urlString = apiUrl + "?key=" + authKey +
					"&domain=http://localhost" +
					"&sttusSeCode=1" +
					"&ldCode=44" +
					"&format=json" +
					"&bsnmCmpnm=" + encodedBsnmCmpnm +
					"&pageSize=10" +
					"&pageNo=1";

			// URL 객체 생성
			URL url = new URL(urlString);

			// HttpURLConnection을 이용한 요청 설정
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");

			// 요청 전송
			int responseCode = connection.getResponseCode();

			// 응답 처리
			BufferedReader in;
			
			StringBuilder response = new StringBuilder();
			if (responseCode >= 200 && responseCode < 300) {
				in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
				
			} else {
				in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
			}

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			System.out.println("서버 요청"+response.toString());
			return ResponseEntity.ok().body(response.toString());
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}
	}
	@GetMapping("/agent/isEstateNumber/{number}")
	public ResponseEntity<Boolean> isEstateNumber(@PathVariable String number) {
		boolean isDuplicate = aServ.isEstateNumber(number);
		return ResponseEntity.ok(isDuplicate);
	}
	@PostMapping("/agent/signup")
	public ResponseEntity<Void> signup(RealEstateAgentDTO RealEstateAgentDTO) {
		aServ.signup(RealEstateAgentDTO);
		return ResponseEntity.ok().build();
	}
	 //부동산 신규회원 등록 수
    @GetMapping("/agent/todayNewEstate")
    public ResponseEntity<NewEstateDTO> getTodayNewEstate() {
    	try {
    		NewEstateDTO dto = neServ.getTodayNewEstate();
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            // 예외가 발생한 경우 처리
            e.printStackTrace(); // 또는 로깅하여 예외 정보 기록
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/agent/createNewEstate")
    public ResponseEntity<Void> createNewEstate() {
    	neServ.createNewEstate();
        return ResponseEntity.ok().build();
    }
    @PutMapping("/agent/incrementNewEstate/{seq}")
    public ResponseEntity<Void> incrementNewEstate(@PathVariable Long seq) {
        try {
        	neServ.incrementNewEstate(seq);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @ExceptionHandler
	public ResponseEntity<String> excetion(Exception e){
		e.printStackTrace();
		logger.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("삭제할 대상이 존재하지 않습니다.");
	}
	
	
	
}
