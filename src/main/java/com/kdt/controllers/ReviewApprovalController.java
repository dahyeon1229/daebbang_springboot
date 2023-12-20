package com.kdt.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdt.dto.EstateDTO;
import com.kdt.dto.ReviewApprovalDTO;
import com.kdt.dto.SawEstateDTO;
import com.kdt.dto.UploadReviewApprovalDTO;
import com.kdt.services.EstateService;
import com.kdt.services.ReviewApprovalService;

@RestController
@RequestMapping("/api/reviewApproval/")
public class ReviewApprovalController {

	@Autowired
	private ReviewApprovalService rServ;

	@Autowired
	private EstateService eServ;

	@GetMapping("myReview/{id}")
	public ResponseEntity<List<ReviewApprovalDTO>> selectAll(@PathVariable String id) {
		List<ReviewApprovalDTO> list = rServ.selectAll(id);
		return ResponseEntity.ok(list);
	}

	@GetMapping("agentReview/{loginId}")
	public ResponseEntity<List<UploadReviewApprovalDTO>> selectById(@PathVariable String loginId) {
		List<UploadReviewApprovalDTO> list = rServ.selectByAgent(loginId);

		return ResponseEntity.ok(list);
	}

	@GetMapping("estate/{id}")
	public ResponseEntity<EstateDTO> selectEstate(@PathVariable Long id) {
		EstateDTO dto = eServ.selectEstate(id);
		return ResponseEntity.ok(dto);
	}

	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody UploadReviewApprovalDTO dto) {

		System.out.println(dto.getEstateCode());//확인해보셈
		System.out.println(dto.getUserId());

		rServ.insert(dto);

		return ResponseEntity.ok().build();
	}

	@PutMapping("updateStatus/{seq}")
	public ResponseEntity<Void> updateStatus(@PathVariable Long seq, @RequestBody ReviewApprovalDTO dto) {
		dto.setSeq(seq);

		rServ.updateStatus(dto);

		return ResponseEntity.ok().build();
	}
	
	@GetMapping("sawEstate/{id}")
	public ResponseEntity<List<SawEstateDTO>> selectSawEstate(@PathVariable String id) {
		List<SawEstateDTO> list = rServ.selectSawEstate(id);
		return ResponseEntity.ok(list);
	}
	//관리자 승인
	@GetMapping("admin/selectByAdmin")
	public ResponseEntity<List<ReviewApprovalDTO>> selectByAdmin() {
		List<ReviewApprovalDTO> list = rServ.selectByAdmin();
		return ResponseEntity.ok(list);
	}
	@PutMapping("admin/revoke-approval/{seq}")
	public ResponseEntity<Void> revoke(@PathVariable Long seq) {
		rServ.revoke_approval(seq);
		System.out.println("번호"+seq);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("admin/approval/{seq}")
	public ResponseEntity<Void> approval(@PathVariable Long seq) {
		rServ.approval(seq);
		
		return ResponseEntity.ok().build();
	}
	@PutMapping("admin/return/{seq}")
	public ResponseEntity<Void> back(@PathVariable Long seq) {
		rServ.back(seq);
		return ResponseEntity.ok().build();
	}
	@PutMapping("admin/finalBack/{seq}")
	public ResponseEntity<Void> finalBack(@PathVariable Long seq) {
		rServ.finalBack(seq);
		return ResponseEntity.ok().build();
	}
}
