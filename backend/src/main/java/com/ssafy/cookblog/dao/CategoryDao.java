package com.ssafy.cookblog.dao;

import java.util.List;

import com.ssafy.cookblog.dto.FavoriteDto;
import com.ssafy.cookblog.dto.FoodCategoryDto;
import com.ssafy.cookblog.dto.RecipeFoodCategoryDto;

public interface CategoryDao {
	
	public int insertRecipeCategory(RecipeFoodCategoryDto recipeFoodCategoryDto);
	public List<String> selectRecipeCategoryName(long recipeId);
	public List<Integer> selectRecipeCategoryId(long recipeId);
	public List<Long> selectUserFavoriteCategoryId(long userId);
	public int deleteRecipeCategory(long recipeId);
	public int insertUserFavoriteCategory(FavoriteDto favoriteDto);
	public int deleteUserFavoriteCategory(long userId);

	public FoodCategoryDto selectRandomCategoryByUserId(long userId);
}
