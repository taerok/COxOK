package com.ssafy.cookblog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VersusPointDto {
	private long versusPointId;
	private long versusId;
	private long recipeId;
	private int count;
	private String recipeName;
	private String recipeThumbnailSrc;
	private int cookTime;
	private int level;
	
	private double avgRating;
}

	


	
