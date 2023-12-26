package com.kdt.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdt.dto.MyReportDTO;
import com.kdt.dto.ReportDTO;
import com.kdt.services.ReportService;

@RestController
@RequestMapping("/api/report/")
public class ReportController {
	@Autowired
	private ReportService rServ;
	
	@GetMapping("selectAll")
	public ResponseEntity<List<ReportDTO>> selectAll() {
		List<ReportDTO> list = rServ.selectAll();
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("myReport/{id}")
	public ResponseEntity<List<MyReportDTO>> myReport(@PathVariable String id) {
		List<MyReportDTO> list = rServ.myReport(id);
		return ResponseEntity.ok(list);
	}
}
