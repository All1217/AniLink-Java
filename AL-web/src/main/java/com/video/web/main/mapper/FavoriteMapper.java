package com.video.web.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.model.entity.Favorite;
import com.video.model.entity.FavoriteVideo;
import com.video.model.entity.UserVideo;
import com.video.web.main.vo.BatchUpdateVo;
import com.video.web.main.vo.UserVideoQueryVo;
import com.video.web.main.vo.VideoFavoriteVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface FavoriteMapper extends BaseMapper<Favorite> {
    @Select("select * from user_video where uid=#{uid} and vid=#{vid}")
    UserVideo getInterActionFavorite(UserVideoQueryVo userVideoQueryVo);

    IPage<Favorite> getFavoriteListPage(IPage<Favorite> page, Long uid);

    @Select("select vid from favorite_video where fid = #{fid} and is_remove = 0")
    List<Long> getVideoIds(long fid);

    IPage<VideoFavoriteVo> getVideoFavoritePage(IPage<Favorite> page, long fid, String sortField);

    @Update("update favorite set count = count - #{size} where fid = #{fid}")
    void subCount(Integer size, Long fid);

    @Update("update favorite set count = count + #{size} where fid = #{fid}")
    void addCount(Integer size, Long fid);

    @Insert("INSERT INTO favorite_video (vid, fid, time, is_remove) values (#{vid}, #{fid}, #{time}, #{isRemove})")
    void batchInsertSv(FavoriteVideo favoriteVideo);

    @Delete("delete from favorite_video where fid = #{fid} and vid = #{vid}")
    void deleteFVById(Long fid, Long vid);

    List<Long> getFVByUidAndVid(long uid, long vid);
}
