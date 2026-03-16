package com.video.web.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.model.entity.Favorite;
import com.video.model.entity.UserVideo;
import com.video.web.main.vo.*;

import java.util.List;

public interface FavoriteService {
    void insertOne(Favorite favorite);

    UserVideo getInterActionFavorite(UserVideoQueryVo userVideoQueryVo);

    IPage<Favorite> getFavoriteListPage(IPage<Favorite> page, Long uid);

    List<VideoVo> getVideoByFavor(long fid);

    IPage<VideoFavoriteVo> getVideoFavoritePage(IPage<Favorite> page, long fid, String sortField);

    void update(Favorite favorite);

    Integer batchMove(BatchUpdateVo batchUpdateVo);

    Integer batchCopy(BatchUpdateVo batchUpdateVo);

    List<Long> getFVByUidAndVid(long uid, long vid);

    void collect(BatchCollectVo batchCollectVo);

    void unCollect(BatchUpdateVo batchUpdateVo);
}
