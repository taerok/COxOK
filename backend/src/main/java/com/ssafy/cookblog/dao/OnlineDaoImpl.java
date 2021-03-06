package com.ssafy.cookblog.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ssafy.cookblog.dto.MeetJoinDto;
import com.ssafy.cookblog.dto.OnlineDto;

@Repository
public class OnlineDaoImpl implements OnlineDao {
	
	@Autowired
	SqlSession session;
	
	@Override
	public int insert(OnlineDto onlineDto) {
		return session.insert("online.insert",onlineDto);
	}
	@Override
	public int update(OnlineDto onlineDto) {
		return session.update("online.update",onlineDto);
	}
	
	@Override
	public List<OnlineDto> selectAll(int startIndex) {
		return session.selectList("online.selectAll",startIndex);
	}
	
	@Override
	public OnlineDto selectOnline(long onlineId) {
		return session.selectOne("online.selectOnline",onlineId);
	}
	
	@Override
	public int delete(long onlineId) {
		return session.delete("online.delete",onlineId);
	}
	
	
	@Override
	public int insertOnlineJoin(MeetJoinDto meetJoinDto) {
		return session.insert("online.insertOnlineJoin",meetJoinDto);
	}
	@Override
	public int deleteOnlineJoin(MeetJoinDto meetJoinDto) {
		return session.delete("online.deleteOnlineJoin",meetJoinDto);
	}
	
	@Override
	public List<OnlineDto> selectOnlineByType(OnlineDto onlineDto) {
		return session.selectList("online.selectOnlineByType",onlineDto);
	}
	
	@Override
	public long selectOnlineTotalByType(String type) {
		return session.selectOne("online.selectOnlineTotalByType",type);
	}
	
	@Override
	public List<MeetJoinDto> selectOnlineJoinList(long onlineId) {
		return session.selectList("online.selectOnlineJoinList",onlineId);
	}
	
	@Override
	public List<OnlineDto> selectOnlineByUserid(long userId) {
		return session.selectList("online.selectOnlineByUserid",userId);
	}
	
	@Override
	public List<OnlineDto> selectOnlineByMeetJoinUserid(long userId) {
		return session.selectList("online.selectOnlineByMeetJoinUserid",userId);
	}
}
