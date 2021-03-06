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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.cookblog.dto.FoodCategoryDto;
import com.ssafy.cookblog.dto.IngredientDto;
import com.ssafy.cookblog.dto.LikeDto;
import com.ssafy.cookblog.dto.RecipeDto;
import com.ssafy.cookblog.dto.request.RecipeRegisterRequestDto;
import com.ssafy.cookblog.dto.request.RecipeSearchRequestDto;
import com.ssafy.cookblog.dto.request.RecipeUpdateRequestDto;
import com.ssafy.cookblog.dto.response.RecipeResponseDto;
import com.ssafy.cookblog.service.RecipeService;
import com.ssafy.cookblog.service.UserService;
import com.ssafy.cookblog.util.JwtService;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin("*")
@RequestMapping("/recipe")
public class RecipeController {
	
	@Autowired
	RecipeService recipeService;
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	UserService userService;
	
	@ApiOperation("레시피 상세 조회")
	@GetMapping("/view/{id}")
	public Object getOneRecipe(@PathVariable("id")int id, HttpServletRequest request ) throws Exception {
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();

		RecipeResponseDto recipe = recipeService.getOneRecipe(id);
		int likeCnt = recipeService.recipeLikeCount(id);
		
		String token = request.getHeader("Authorization");
		String email = null;
		long userId = -1;
		
		if(recipe!=null) {
			recipe.setRecipeId(id);
			map.put("msg", "레시피 조회를 성공했습니다.");
			map.put("status", "success");
			map.put("recipe", recipe);
			map.put("likeCnt", likeCnt);
			boolean userLike = false;
			if(token != null && jwtService.isValid(token)) {
				email = jwtService.getEmailFromToken(token.substring(7));
				userId = userService.userIdByEmail(email);
				LikeDto like = new LikeDto();
				like.setRecipeId(id);
				like.setUserId(userId);
				userLike = recipeService.reipceUserLike(like);
			}
			map.put("userLike", userLike);
			map.put("loginUserId", userId);
			response = new ResponseEntity(map, HttpStatus.OK);
		}else {
			map.put("msg", "레시피를 찾지 못했습니다.");
			map.put("status", "fail");
			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		return response;
	}
	
	@ApiOperation("선호하는 카테고리중 18개 뽑기")
	@GetMapping("/my")
	public Object getMyRecommand(HttpServletRequest request ) {
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();
		
		String token = request.getHeader("Authorization");
		String email = jwtService.getEmailFromToken(token.substring(7));
		long userId = userService.userIdByEmail(email);
		
		FoodCategoryDto category = recipeService.getRandomCategoryByUserId(userId) ;
		if(category==null) {
			map.put("msg","선호 카테고리가 없습니다.");
			map.put("status", "success");
			response = new ResponseEntity(map, HttpStatus.OK);
			return response;
		}
		
		List<RecipeDto> recipe= recipeService.getRecipeListByCategoryId(category.getFoodCategoryId());
		
		if(recipe!=null) {
			map.put("msg", "레시피 조회를 성공했습니다.");
			map.put("status", "success");
			map.put("category",category);
			map.put("recipe", recipe);
			response = new ResponseEntity(map, HttpStatus.OK);
		}else {
			map.put("msg", "레시피를 찾지 못했습니다.");
			map.put("status", "fail");
			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		return response;
	}
	
	// 레시피 전체 조회
	@ApiOperation("레시피 전체 조회")
	@GetMapping("/all/{startIndex}")
	public Object getAllRecipe(@PathVariable("startIndex") int startIndex) {
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();

		List<RecipeDto> list = recipeService.selectAll(6*startIndex);
		if(list!=null) {
			map.put("msg", "레시피 조회를 성공했습니다.");
			map.put("status", "success");
			map.put("list",list);
			map.put("total",recipeService.getTotalRecipeNum());
			response = new ResponseEntity(map, HttpStatus.OK);
		}else {
			map.put("msg", "레시피를 찾지 못했습니다.");
			map.put("status", "fail");
			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		return response;
	}
	
	@ApiOperation("모든 재료 받기 (레시피 등록 과정)")
	@GetMapping("/goRegister")		
	public Object getAllIngredient() {
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();
		
		List<IngredientDto> ingredients = recipeService.selectAllIngredient();
		List<FoodCategoryDto> catogories = recipeService.selectAllFoodCategory();
		
		map.put("msg", "성공");
		map.put("status", "success");
		map.put("ingredients",ingredients);
		map.put("catogories",catogories);
		
		response = new ResponseEntity(map, HttpStatus.OK);
		return response;
	}
	
	@ApiOperation("모든 재료 받기 (레시피 검색 과정)")
	@GetMapping("/get")
	public Object getAllIngredient2() {
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();
		
		List<IngredientDto> ingredients = recipeService.selectAllIngredient();
		List<FoodCategoryDto> catogories = recipeService.selectAllFoodCategory();
		
		map.put("msg", "성공");
		map.put("status", "success");
		map.put("ingredients",ingredients);
		map.put("catogories",catogories);
		
		response = new ResponseEntity(map, HttpStatus.OK);
		return response;
	}
	
	@ApiOperation("레시피 등록")
	@PostMapping("/register")
	public Object registerRecipe(@ModelAttribute RecipeRegisterRequestDto recipe, HttpServletRequest request) {
	
		String email = jwtService.getEmailFromToken(request.getHeader("Authorization").substring(7));
		recipe.setUserId(userService.findUserByEmail(email).getUserId());
		
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();

		int count = recipeService.registerRecipe(recipe);
		if(count!=0) {
			map.put("msg", "레시피 등록에 성공했습니다.");
			map.put("status", "success");
			map.put("recipeId",recipeService.getRecipeId());
			response = new ResponseEntity(map, HttpStatus.OK);
		}else {
			map.put("msg", "레시피 등록에 실패했습니다.");
			map.put("status", "fail");
			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	@ApiOperation("레시피 수정 버튼 클릭 시 기존 정보 불러오기")
	@GetMapping("/modifyInfo/{id}")
	public Object modifyInfoRecipe(@PathVariable int id, HttpServletRequest request) {
		
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();
		
		RecipeResponseDto recipe = recipeService.getOneRecipe(id);
		
		String token = request.getHeader("Authorization");
		String email = null;
		long userId = -1;
		
		if(recipe != null) {
			recipe.setRecipeId(id);
			map.put("msg", "레시피 정보 조회 성공");
			map.put("status", "success");
			map.put("recipe", recipe);
			email = jwtService.getEmailFromToken(token.substring(7));
			userId = userService.userIdByEmail(email);
			map.put("loginUserId", userId);
			response = new ResponseEntity(map, HttpStatus.OK);
		} else {
			map.put("msg", "레시피 정보 조회 실패");
			map.put("status", "fail");
			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		
		return response;
		
	}
	
	@ApiOperation("레시피 수정")
	@PostMapping("/modify")
	public Object modifyRecipe(@RequestBody RecipeUpdateRequestDto recipe, HttpServletRequest request) {
		
		String email = jwtService.getEmailFromToken(request.getHeader("Authorization").substring(7));
		recipe.setUserId(userService.findUserByEmail(email).getUserId());
		
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();

		int count = recipeService.updateRecipe(recipe);
		
		if(count != 0) {
			map.put("msg", "레시피 수정에 성공했습니다.");
			map.put("status", "success");
			response = new ResponseEntity(map, HttpStatus.OK);
		} else {
			map.put("msg", "레시피 수정에 실패했습니다.");
			map.put("status", "fail");
			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	@ApiOperation("레시피 삭제")
	@DeleteMapping("/delete/{id}")
	public Object deleteRecipe(@PathVariable long id) {
		
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();
		
		int count = recipeService.deleteRecipe(id);
		if(count != 0) {
			map.put("msg", "레시피가 삭제되었습니다.");
			map.put("status", "success");
			response = new ResponseEntity(map, HttpStatus.OK);
		}else {
			map.put("msg", "레시피를 삭제하지 못했습니다.");
			map.put("status", "fail");
			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		return response;
	}
	
	@ApiOperation("레시피 검색")
	@PostMapping("/search/{startIndex}")
	public Object search(@ModelAttribute RecipeSearchRequestDto recipeSearchRequestDto,
			@PathVariable("startIndex")int startIndex) {
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();		
		
		recipeSearchRequestDto.setStartIndex(startIndex*6);
		List<RecipeDto> list = recipeService.search(recipeSearchRequestDto);
		
		if(list!=null) {
			map.put("msg", "레시피 검색을 성공했습니다.");
			map.put("status", "success");
			map.put("list",list);
			map.put("total",recipeService.searchTotal(recipeSearchRequestDto));
			response = new ResponseEntity(map, HttpStatus.OK);
		}else {
			map.put("msg", "레시피를 검색을 실패했습니다.");
			map.put("status", "fail");
			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		return response;
	}
	
	@ApiOperation("좋아요 반영")
	@PostMapping("/like")
	public Object like(@RequestBody LikeDto like, HttpServletRequest request) {
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();
		
		String email = jwtService.getEmailFromToken(request.getHeader("Authorization").substring(7));
		like.setUserId(userService.findUserByEmail(email).getUserId());
		
		int count = recipeService.insertRecipeLike(like);
		int likeCnt = recipeService.recipeLikeCount(like.getRecipeId());
		
		if(count != 0) {
			map.put("msg", "좋아요가 반영되었습니다.");
			map.put("status", "success");
			map.put("likeCnt", likeCnt);
			response = new ResponseEntity(map, HttpStatus.OK);
		}else {
			map.put("msg", "좋아요를 반영하지 못했습니다.");
			map.put("status", "fail");
			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	@ApiOperation("좋아요 취소")
	@DeleteMapping("/likeCancel")
	public Object likeCancel(@RequestBody LikeDto like, HttpServletRequest request) {
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();
		
		String email = jwtService.getEmailFromToken(request.getHeader("Authorization").substring(7));
		like.setUserId(userService.findUserByEmail(email).getUserId());
		
		int count = recipeService.cancelRecipeLike(like);
		int likeCnt = recipeService.recipeLikeCount(like.getRecipeId());
		
		if(count != 0) {
			map.put("msg", "좋아요가 취소되었습니다.");
			map.put("status", "success");
			map.put("likeCnt", likeCnt);
			response = new ResponseEntity(map, HttpStatus.OK);
		}else {
			map.put("msg", "좋아요 취소에 실패했습니다.");
			map.put("status", "fail");
			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	@ApiOperation("(관리자) 재료 전체 목록")
	@GetMapping("/admin/ingredient/{startIndex}")
	public Object getIngredient(HttpServletRequest request, @PathVariable("startIndex")int startIndex) {
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();
		
		String email = jwtService.getEmailFromToken(request.getHeader("Authorization").substring(7));
		if(!email.equals("admin@co-ok.com")) {
			map.put("msg", "관리자가 아닙니다.");
			map.put("status", "fail");
			return new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		
		List<IngredientDto> dto = recipeService.selectAllIngredientAdmin(startIndex*20);
		
		if(dto != null) {
			map.put("msg", "재료 불러오기에 성공했습니다.");
			map.put("status", "success");
			map.put("ingredient", dto);
			map.put("total",recipeService.selectAllIngredient().size());
			response = new ResponseEntity(map, HttpStatus.OK);
		}else {
			map.put("msg", "재료 불러오기에 실패했습니다.");
			map.put("status", "fail");
			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	@ApiOperation("(관리자) 재료 상세 보기")
	@GetMapping("/admin/ingredient/detail/{id}")
	public Object readIngredient(@PathVariable long ingredientId, HttpServletRequest request) {
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();
		
		String email = jwtService.getEmailFromToken(request.getHeader("Authorization").substring(7));
		if(!email.equals("admin@co-ok.com")) {
			map.put("msg", "관리자가 아닙니다.");
			map.put("status", "fail");
			return new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		
		IngredientDto ingredient = recipeService.readIngredientAdmin(ingredientId);
		
		if(ingredient != null) {
			map.put("msg", "재료 불러오기에 성공했습니다.");
			map.put("status", "success");
			map.put("ingredient", ingredient);
			response = new ResponseEntity(map, HttpStatus.OK);
		}else {
			map.put("msg", "재료 불러오기에 실패했습니다.");
			map.put("status", "fail");
			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	@ApiOperation("(관리자) 재료 추가")
	@PostMapping("/admin/ingredient")
	public Object addIngredient(@RequestBody IngredientDto dto, HttpServletRequest request) {
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();
		
		String email = jwtService.getEmailFromToken(request.getHeader("Authorization").substring(7));
		if(!email.equals("admin@co-ok.com")) {
			map.put("msg", "관리자가 아닙니다.");
			map.put("status", "fail");
			return new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		
		int count = recipeService.registerIngredientAdmin(dto);
		
		if(count != 0) {
			map.put("msg", "재료 등록에 성공했습니다.");
			map.put("status", "success");
			response = new ResponseEntity(map, HttpStatus.OK);
		}else {
			map.put("msg", "재료 등록에 실패했습니다.");
			map.put("status", "fail");
			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	@ApiOperation("(관리자) 재료 수정")
	@PutMapping("/admin/ingredient")
	public Object modifyIngredient(@RequestBody IngredientDto dto, HttpServletRequest request) {
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();
		
		String email = jwtService.getEmailFromToken(request.getHeader("Authorization").substring(7));
		if(!email.equals("admin@co-ok.com")) {
			map.put("msg", "관리자가 아닙니다.");
			map.put("status", "fail");
			return new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		
		int count = recipeService.modifyIngredientAdmin(dto);
		
		if(count != 0) {
			map.put("msg", "재료 수정에 성공했습니다.");
			map.put("status", "success");
			response = new ResponseEntity(map, HttpStatus.OK);
		}else {
			map.put("msg", "재료 수정에 실패했습니다.");
			map.put("status", "fail");
			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	@ApiOperation("재료 삭제")
	@DeleteMapping("/admin/ingredient/{id}")
	public Object deleteIngredient(HttpServletRequest request, @PathVariable long ingredientId) {
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();
		
		String email = jwtService.getEmailFromToken(request.getHeader("Authorization").substring(7));
		if(!email.equals("admin@co-ok.com")) {
			map.put("msg", "관리자가 아닙니다.");
			map.put("status", "fail");
			return new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		
		int count = recipeService.removeIngredientAdmin(ingredientId);
		
		if(count != 0) {
			map.put("msg", "재료 삭제에 성공했습니다.");
			map.put("status", "success");
			response = new ResponseEntity(map, HttpStatus.OK);
		}else {
			map.put("msg", "재료 삭제에 실패했습니다.");
			map.put("status", "fail");
			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	@ApiOperation("업데이트가 필요한 재료 목록")
	@GetMapping("/admin/modify-ingredient/{startIndex}")
	public Object getIngredientToBeUpdated(@PathVariable int startIndex, HttpServletRequest request) {
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();
		
		String email = jwtService.getEmailFromToken(request.getHeader("Authorization").substring(7));
		if(!email.equals("admin@co-ok.com")) {
			map.put("msg", "관리자가 아닙니다.");
			map.put("status", "fail");
			return new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		
		List<IngredientDto> ingredient = recipeService.readAllIngredientToBeUpdated(startIndex*20);
		
		if(ingredient != null) {
			map.put("msg", "업데이트가 필요한 재료 목록 불러오기에 성공했습니다.");
			map.put("status", "success");
			map.put("ingredient", ingredient);
			response = new ResponseEntity(map, HttpStatus.OK);
		}else {
			map.put("msg", "업데이트가 필요한 재료 목록 불러오기에 실패했습니다.");
			map.put("status", "fail");
			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	@ApiOperation("관리자가 레시피 삭제")
	@DeleteMapping("/admin/{id}")
	public Object adminDeleteRecipe(HttpServletRequest request, @PathVariable long id) {
		ResponseEntity response = null;
		Map<String,Object> map = new HashMap<String, Object>();
		
		String email = jwtService.getEmailFromToken(request.getHeader("Authorization").substring(7));
		if(!email.equals("admin@co-ok.com")) {
			map.put("msg", "관리자가 아닙니다.");
			map.put("status", "fail");
			return new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		int count = recipeService.deleteRecipe(id);
		if(count != 0) {
			map.put("msg", "레시피가 삭제되었습니다.");
			map.put("status", "success");
			response = new ResponseEntity(map, HttpStatus.OK);
		}else {
			map.put("msg", "레시피를 삭제하지 못했습니다.");
			map.put("status", "fail");
			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
		}
		return response;
	}
	
	
//	@ApiOperation("레시피 검색(요리시간)")
//	@GetMapping("/cooktime/{cookTime}/{startIndex}")
//	public Object searchCookTime(@PathVariable("cookTime")int cookTime,
//			@PathVariable("startIndex")int startIndex) {
//		ResponseEntity response = null;
//		Map<String,Object> map = new HashMap<String, Object>();		
//		
//		RecipeDto recipeDto = new RecipeDto();
//		recipeDto.setCookTime(cookTime);
//		recipeDto.setStartIndex(startIndex*6);
//		
//		List<RecipeDto> list = recipeService.getRecipeListByCookTime(recipeDto);
//		
//		if(list!=null) {
//			map.put("msg", "레시피 검색을 성공했습니다.");
//			map.put("status", "success");
//			map.put("list",list);
//			map.put("total",recipeService.getRecipeTotalByCookTime(cookTime));
//			response = new ResponseEntity(map, HttpStatus.OK);
//		}else {
//			map.put("msg", "레시피를 검색을 실패했습니다.");
//			map.put("status", "fail");
//			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
//		}
//		return response;
//	}
//	
//	@ApiOperation("레시피 검색(요리시간)")
//	@GetMapping("/level/{level}/{startIndex}")
//	public Object searchLevel(@PathVariable("level")int level,
//			@PathVariable("startIndex")int startIndex) {
//		ResponseEntity response = null;
//		Map<String,Object> map = new HashMap<String, Object>();		
//		
//		RecipeDto recipeDto = new RecipeDto();
//		recipeDto.setLevel(level);
//		recipeDto.setStartIndex(startIndex*6);
//		
//		List<RecipeDto> list = recipeService.getRecipeListByLevel(recipeDto);
//		
//		if(list!=null) {
//			map.put("msg", "레시피 검색을 성공했습니다.");
//			map.put("status", "success");
//			map.put("list",list);
//			map.put("total",recipeService.getRecipeTotalByLevel(level));
//			response = new ResponseEntity(map, HttpStatus.OK);
//		}else {
//			map.put("msg", "레시피를 검색을 실패했습니다.");
//			map.put("status", "fail");
//			response = new ResponseEntity(map, HttpStatus.BAD_REQUEST);
//		}
//		return response;
//	}
	
}
