package com.ssafy.cookblog.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRegisterRequestDto {
	private long recipeId;
	private String recipeName;
	private String recipeDetail;
	private MultipartFile recipeThumbnail;	//수정필요
	private int[] categories;
	private int[] ingredientPk;
	private int[] ingredientAmount;
	private String[] description;
	private MultipartFile[] photo;		
	
	private String level;
	private String cookTime;
	
	private long userId;
	
	private String uploadPath;
}
