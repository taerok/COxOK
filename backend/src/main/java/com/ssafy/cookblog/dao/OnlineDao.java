package com.ssafy.cookblog.dao;

import java.util.List;

import com.ssafy.cookblog.dto.MeetJoinDto;
import com.ssafy.cookblog.dto.OnlineDto;

public interface OnlineDao {
	public int update(OnlineDto onlineDto);
	public int insert(OnlineDto onlineDto);
	public List<OnlineDto> selectAll(int startIndex);
	public OnlineDto selectOnline(long onlineId);
	public int delete(long onlineId);
	
	public int insertOnlineJoin(MeetJoinDto meetJoinDto);
	public int deleteOnlineJoin(MeetJoinDto meetJoinDto);
	
	public List<OnlineDto> selectOnlineByType(OnlineDto onlineDto);
	public long selectOnlineTotalByType(String type);
	
	public List<MeetJoinDto> selectOnlineJoinList(long onlineId);
	
	public List<OnlineDto> selectOnlineByUserid(long userId);
	public List<OnlineDto> selectOnlineByMeetJoinUserid(long userId);
}
