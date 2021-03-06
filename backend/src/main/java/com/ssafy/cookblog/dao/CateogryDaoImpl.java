package com.ssafy.cookblog.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ssafy.cookblog.dto.FavoriteDto;
import com.ssafy.cookblog.dto.FoodCategoryDto;
import com.ssafy.cookblog.dto.RecipeFoodCategoryDto;


@Repository
public class CateogryDaoImpl implements CategoryDao {

	@Autowired
	SqlSession session;
	
	public int insertRecipeCategory(RecipeFoodCategoryDto recipeFoodCategoryDto) {
		return session.insert("category.insertRecipeCategory", recipeFoodCategoryDto);
	}
	
	@Override
	public List<String> selectRecipeCategoryName(long recipeId) {
		return session.selectList("category.selectRecipeCategoryName", recipeId);
	}

	@Override
	public List<Integer> selectRecipeCategoryId(long recipeId) {
		return session.selectList("category.selectRecipeCategoryId", recipeId);
	}

	@Override
	public List<Long> selectUserFavoriteCategoryId(long userId) {
		return session.selectList("category.selectUserFavoriteCategory", userId);
	}
	
	@Override
	public int deleteRecipeCategory(long recipeId) {
		return session.delete("category.deleteRecipeCategory", recipeId);
	}

	@Override
	public int insertUserFavoriteCategory(FavoriteDto favoriteDto) {
		return session.insert("category.insertUserFavoriteCategory", favoriteDto);
	}

	@Override
	public int deleteUserFavoriteCategory(long userId) {
		return session.delete("category.deleteUserFavoriteCategory", userId);
	}
	
	@Override
	public FoodCategoryDto selectRandomCategoryByUserId(long userId) {
		return session.selectOne("category.selectRandomCategoryByUserId",userId);
	}
}
