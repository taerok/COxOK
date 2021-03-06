package com.ssafy.cookblog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.cookblog.dao.VersusDao;
import com.ssafy.cookblog.dto.VersusDto;
import com.ssafy.cookblog.dto.VersusPointDto;
import com.ssafy.cookblog.dto.response.VersusResponseDto;
import com.ssafy.cookblog.dto.response.VersusViewResponseDto;

@Service
public class VersusServiceImpl implements VersusService {
	
	@Autowired
	VersusDao versusDao;

	@Override
	@Transactional 
	public int registerVersus(VersusDto versusDto) {
		return versusDao.registerVersus(versusDto);
	}

	@Override
	@Transactional 
	public int winVersus(VersusPointDto versusPoint) {
		return versusDao.winVersus(versusPoint);
	}

	@Override
	public List<VersusResponseDto> getAllVersus(int startIndex) {
		return versusDao.selectAllVersus(startIndex);
	}

	@Override
	public VersusViewResponseDto getOneVersus(long versusId) {
		VersusViewResponseDto versusView = versusDao.selectVersus(versusId);
		versusView.changeList();
		versusView.setRecipeList(versusDao.selectVersusRecipe(versusView));
		return versusView;
	}

	@Override
	@Transactional 
	public int removeVersus(long versusId) {
		return versusDao.delete(versusId);
	}

	@Override
	public List<VersusPointDto> getVersusResult(long versusId) {
		return versusDao.selectVersusResult(versusId);
	}

	@Override
	public List<VersusPointDto> getTop3VersusResult() {
		return versusDao.selectTop3VersusResult();
	}


}
