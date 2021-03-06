package com.ssafy.cookblog.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ssafy.cookblog.dto.MeetDto;
import com.ssafy.cookblog.dto.MeetJoinDto;
import com.ssafy.cookblog.dto.response.MeetViewResponseDto;

@Repository
public class MeetDaoImpl implements MeetDao {

	@Autowired
	SqlSession session;

	@Override
	public List<MeetDto> selectAllMeet(int startIndex) {
		return session.selectList("meet.selectAllMeet", startIndex);
	}

	@Override
	public MeetViewResponseDto selectMeet(long meetId) {
		return session.selectOne("meet.selectMeet", meetId);
	}

	@Override
	public List<MeetJoinDto> selectMeetJoinList(long meetId) {
		return session.selectList("meet.selectMeetJoinList", meetId);
	}
	
	@Override
	public int delete(long meetId) {
		return session.delete("meet.delete", meetId);
	}
	
	@Override
	public int insert(MeetDto meetDto) {
		return session.insert("meet.insert",meetDto);
	}
	
	@Override
	public int update(MeetDto meetDto) {
		return session.update("meet.update",meetDto);
	}
	
	@Override
	public int insertMeetJoin(MeetJoinDto meetJoinDto) {
		return session.insert("meet.insertMeetJoin",meetJoinDto);
	}
	
	@Override
	public int deleteMeetJoin(MeetJoinDto meetJoinDto) {
		return session.delete("meet.deleteMeetJoin",meetJoinDto);
	}
	
	@Override
	public List<MeetDto> selectMeetByType(MeetDto meetDto) {
		return session.selectList("meet.selectMeetByType",meetDto);
	}
	
	@Override
	public long selectMeetTotalByType(String type) {
		return session.selectOne("meet.selectMeetTotalByType",type);
	}
	
	@Override
	public List<MeetDto> selectMeetByUserid(long userId) {
		return session.selectList("meet.selectMeetByUserid",userId);
	}
	@Override
	public List<MeetDto> selectMeetByMeetJoinUserid(long userId) {
		return session.selectList("meet.selectMeetByMeetJoinUserid",userId);
	}
}
