package com.ssafy.cookblog.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.cookblog.dto.ReportRecipeDto;
import com.ssafy.cookblog.dto.request.RecipeUpdateRequestDto;
import com.ssafy.cookblog.dto.response.ReportRecipeResponseDto;
import com.ssafy.cookblog.service.ReportService;
import com.ssafy.cookblog.service.UserService;
import com.ssafy.cookblog.util.JwtService;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin("*")
@RequestMapping("/report")
public class ReportController {
	
	@Autowired
	ReportService reportService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	JwtService jwtService;
	
	@ApiOperation("레시피 신고")
	@PostMapping("")
	public Object reportRecipe(@RequestBody ReportRecipeDto reportDto, HttpServletRequest request) {
	
		String email = jwtService.getEmailFromToken(request.getHeader("Authorization").substring(7));
		reportDto.setUserId(userService.findUserByEmail(email).getUserId());
		
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();
		
		int count = reportService.reportRecipe(reportDto);
		if(count!=0) {
			map.put("msg", "레시피 신고에 성공했습니다.");
			map.put("status", "success");
			response = new ResponseEntity(map, HttpStatus.OK);
		}else {
			map.put("msg", "레시피 신고에 실패했습니다.");
			map.put("status", "fail");
			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		return response;
	}
	
	@ApiOperation("레시피 신고 취소")
	@DeleteMapping("/{recipeId}")
	public Object cancelReportRecipe(@PathVariable long recipeId, HttpServletRequest request) {
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();
		
		String email = jwtService.getEmailFromToken(request.getHeader("Authorization").substring(7));
		long userId = userService.findUserByEmail(email).getUserId();
		
		int count = reportService.cancelReportRecipe(recipeId, userId);
		if(count!=0) {
			map.put("msg", "레시피 신고 취소에 성공했습니다.");
			map.put("status", "success");
			response = new ResponseEntity(map, HttpStatus.OK);
		}else {
			map.put("msg", "레시피 신고 취소에 실패했습니다.");
			map.put("status", "fail");
			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		return response;
	}
	
	@ApiOperation("(관리자)레시피 신고 취소")
	@DeleteMapping("/admin/{reportRecipeId}")
	public Object cancelReportRecipeByAdmin(@PathVariable("reportRecipeId") long reportRecipeId,HttpServletRequest request) {
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();
		
		String email = jwtService.getEmailFromToken(request.getHeader("Authorization").substring(7));
		long userId = userService.findUserByEmail(email).getUserId();
		
		if(userId!=0) {
			map.put("msg", "관리자가 아닙니다.");
			map.put("status", "fail");
			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
			return response;
		}
		
		int count = reportService.cancelByAdmin(reportRecipeId);
		if(count!=0) {
			map.put("msg", "레시피 신고 취소에 성공했습니다.");
			map.put("status", "success");
			response = new ResponseEntity(map, HttpStatus.OK);
		}else {
			map.put("msg", "레시피 신고 취소에 실패했습니다.");
			map.put("status", "fail");
			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		return response;
	}
	
	
	@ApiOperation("신고 레시피 전체 조회")
	@GetMapping("/admin")
	public Object getAllReport(HttpServletRequest request) {
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();
		
		String email = jwtService.getEmailFromToken(request.getHeader("Authorization").substring(7));
		if(email.equals("admin@co-ok.com")) {	//관리자인 경우
			List<ReportRecipeResponseDto> list = reportService.getAllReport();
			map.put("msg", "신고 레시피 조회에 성공했습니다.");
			map.put("status", "success");
			map.put("list",list);
			response = new ResponseEntity(map, HttpStatus.OK);
		}else {
			map.put("msg", "관리자가 아닙니다.");
			map.put("status", "fail");
			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		return response;
	}
	
	@ApiOperation("해당 레시피 신고했는지 조회")
	@GetMapping("/{recipeId}")
	public Object getReportRecipe(@PathVariable long recipeId, HttpServletRequest request) {
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();
		
		String email = jwtService.getEmailFromToken(request.getHeader("Authorization").substring(7));
		long userId = userService.findUserByEmail(email).getUserId();
		
		boolean isReport = reportService.getReportRecipe(recipeId, userId);
		map.put("msg", "레시피 신고 조회에 성공했습니다.");
		map.put("status", "success");
		map.put("isReport", isReport);
		response = new ResponseEntity(map, HttpStatus.OK);
		
		return response;
	}
	
}
