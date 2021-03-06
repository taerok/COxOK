package com.ssafy.cookblog.dao;

import java.util.List;

import com.ssafy.cookblog.dto.FoodCategoryDto;
import com.ssafy.cookblog.dto.IngredientDto;
import com.ssafy.cookblog.dto.LikeDto;
import com.ssafy.cookblog.dto.RecipeDto;
import com.ssafy.cookblog.dto.RecipeIngredientDto;
import com.ssafy.cookblog.dto.request.RecipeSearchRequestDto;
import com.ssafy.cookblog.dto.response.RecipeIngredientResponseDto;
import com.ssafy.cookblog.dto.response.RecipeResponseDto;

public interface RecipeDao {
	public RecipeResponseDto select(long recipeId);
	public List<RecipeDto> selectAll(int startIndex);
	public int write(RecipeDto recipeDto);
	public int updateRecipe(RecipeDto recipeDto);
	public int delete(long recipeId);
	public long getRecipeId();
	public List<FoodCategoryDto> selectAllFoodCategory();
	public List<IngredientDto> selectAllIngredient();
	public List<IngredientDto> selectAllIngredientAdmin(int startIndex);
	public List<IngredientDto> selectAllIngredientToBeUpdated(int startIndex);
	public int insertIngredientUser(IngredientDto ingredient);
	public int insertIngredientAdmin(IngredientDto ingredient);
	public int updateIngredientAdmin(IngredientDto ingredient);
	public int deleteIngredientAdmin(long ingredientId);
	public List<RecipeIngredientResponseDto> selectRecipeIngredient(long recipeId);
	public IngredientDto selectIngredientById(long ingredientId);
	public int insertRecipeIngredient(RecipeIngredientDto recipeIngredient);
	public int deleteRecipeIngredient(long recipeId);
	public Long selectTotalRecipeNum();
	public List<RecipeDto> search(RecipeSearchRequestDto recipeSearchRequestDto);
	public Long searchTotal(RecipeSearchRequestDto recipeSearchRequestDto);
	
	public int insertRecipeLike(LikeDto like);
	public int cancelRecipeLike(LikeDto like);
	public int recipeLikeCount(long recipeId);
	public boolean reipceUserLike(LikeDto likeDto);
	public List<LikeDto> allUserLike(long recipeId);
	
	public List<RecipeDto> selectRecipeListByCategoryId(long foodCategoryId);
	
	
	public List<Long> selectRecipeIdByIngredientId(long ingredientId);
	
	public int updateNutrient(RecipeDto recipeDto);
	
	public List<RecipeDto> selectRecipeListByCookTime(RecipeDto recipeDto);
	public long selectRecipeTotalByCookTime(int cookTime);
	
	
	public List<RecipeDto> selectRecipeListByLevel(RecipeDto recipeDto);

	public long selectRecipeTotalByLevel(int level);

}
