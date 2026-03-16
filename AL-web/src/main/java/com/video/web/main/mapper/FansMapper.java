package com.video.web.main.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.model.entity.Follow;
import com.video.model.entity.UserInfo;
import com.video.web.main.vo.FollowQueryVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface FansMapper {
    @Insert("insert into follow (follower_id, followee_id, created_at) VALUES (#{followerId}, #{followeeId}, #{createdAt})")
    void insertOne(Follow follow);

    @Delete("delete from follow where followee_id=#{followeeId} and follower_id=#{followerId}")
    void deleteById(Follow follow);

    @Select("select * from follow where followee_id=#{followeeId} and follower_id=#{followerId}")
    Follow getFollow(Follow follow);

    IPage<UserInfo> getFollowList(IPage<UserInfo> page, FollowQueryVo query);

    IPage<UserInfo> getFansList(IPage<UserInfo> page, FollowQueryVo query);
}
