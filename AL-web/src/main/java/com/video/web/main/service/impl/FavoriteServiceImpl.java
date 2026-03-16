package com.video.web.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.video.common.constant.Constant;
import com.video.model.entity.Favorite;
import com.video.model.entity.FavoriteVideo;
import com.video.model.entity.UserVideo;
import com.video.web.main.mapper.FavoriteMapper;
import com.video.web.main.mapper.VideoMapper;
import com.video.web.main.service.FavoriteService;
import com.video.web.main.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    @Autowired
    private FavoriteMapper favoriteMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private VideoMapper videoMapper;

    @Override
    public void insertOne(Favorite favorite) {
        favorite.setFid(0L);
        favoriteMapper.insert(favorite);
    }

    @Override
    @Transactional
    public UserVideo getInterActionFavorite(UserVideoQueryVo userVideoQueryVo) {
        UserVideo res = new UserVideo();
        String videoKey = Constant.MAIN_VIDEO_FAVORITE_PREFIX + userVideoQueryVo.getVid();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(videoKey))) {
            Integer tCount = (Integer) redisTemplate.opsForHash().get(videoKey, userVideoQueryVo.getUid());
            if (tCount != null) res.setCollect(tCount);
            else {
                UserVideo temp = favoriteMapper.getInterActionFavorite(userVideoQueryVo);
                res.setCollect(temp == null ? 0 : temp.getCollect());
            }
        } else {
            UserVideo temp = favoriteMapper.getInterActionFavorite(userVideoQueryVo);
            res.setCollect(temp == null ? 0 : temp.getCollect());
        }
        return res;
    }

    @Override
    public IPage<Favorite> getFavoriteListPage(IPage<Favorite> page, Long uid) {
        return favoriteMapper.getFavoriteListPage(page, uid);
    }

    @Override
    public List<VideoVo> getVideoByFavor(long fid) {
        List<VideoVo> res = new ArrayList<>();
        List<Long> videoIds = favoriteMapper.getVideoIds(fid);
        if (!videoIds.isEmpty()) {
            res = videoMapper.batchSelect(videoIds);
        }
        return res;
    }

    @Override
    public IPage<VideoFavoriteVo> getVideoFavoritePage(IPage<Favorite> page, long fid, String sortField) {
        return favoriteMapper.getVideoFavoritePage(page, fid, sortField);
    }

    @Override
    public void update(Favorite favorite) {
        UpdateWrapper<Favorite> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("fid", favorite.getFid());
        Favorite newFavor = new Favorite();
        if (favorite.getIsDelete() != null) newFavor.setIsDelete(favorite.getIsDelete());
        if (favorite.getVisible() != null) newFavor.setVisible(favorite.getVisible());
        if (favorite.getDescription() != null) newFavor.setDescription(favorite.getDescription());
        if (favorite.getTitle() != null) newFavor.setTitle(favorite.getTitle());
        if (favorite.getCover() != null) newFavor.setCover(favorite.getCover());
        favoriteMapper.update(newFavor, updateWrapper);
    }

    @Override
    @Transactional
    public Integer batchMove(BatchUpdateVo q) {
        //减旧收藏夹数量
        List<Long> vids = q.getVids();
        favoriteMapper.subCount(vids.size(), q.getPreFid());
        //删除旧收藏夹收藏关系
        vids.forEach(e -> {
            favoriteMapper.deleteFVById(q.getPreFid(), e);
        });
        //获取新收藏夹内视频ID列表
        List<Long> targetFavorVids = favoriteMapper.getVideoIds(q.getNewFid());
        //从全集消去重复元素
        vids.removeAll(targetFavorVids);
        int res = 0;
        if (!vids.isEmpty()) {
            res = vids.size();
            //更新新收藏夹视频数量
            favoriteMapper.addCount(vids.size(), q.getNewFid());
            vids.forEach(e -> {//逐条插入收藏关系
                FavoriteVideo t = new FavoriteVideo();
                t.setFid(q.getNewFid());
                t.setVid(e);
                t.setTime(new Date());
                t.setIsRemove(0);
                favoriteMapper.batchInsertSv(t);
            });
        }
        return res;
    }

    @Override
    @Transactional
    public Integer batchCopy(BatchUpdateVo q) {
        List<Long> vids = q.getVids();
        List<Long> targetFavorVids = favoriteMapper.getVideoIds(q.getNewFid());
        vids.removeAll(targetFavorVids);
        int res = 0;
        if (!vids.isEmpty()) {
            res = vids.size();
            favoriteMapper.addCount(vids.size(), q.getNewFid());
            vids.forEach(e -> {
                FavoriteVideo t = new FavoriteVideo();
                t.setFid(q.getNewFid());
                t.setVid(e);
                t.setTime(new Date());
                t.setIsRemove(0);
                favoriteMapper.batchInsertSv(t);
            });
        }
        return res;
    }

    @Override
    public List<Long> getFVByUidAndVid(long uid, long vid) {
        return favoriteMapper.getFVByUidAndVid(uid, vid);
    }

    @Override
    @Transactional
    public void collect(BatchCollectVo batchCollectVo) {
        Long vid = batchCollectVo.getVid();
        List<Long> newList = batchCollectVo.getFids();
        List<Long> oldList = favoriteMapper.getFVByUidAndVid(batchCollectVo.getUid(), vid);
        List<Long> toRemove = new ArrayList<>(oldList);
        toRemove.removeAll(newList);
        List<Long> toAdd = new ArrayList<>(newList);
        toAdd.removeAll(oldList);
        toRemove.forEach(id -> {
            favoriteMapper.deleteFVById(id, vid);
            favoriteMapper.subCount(1, id);
        });
        toAdd.forEach(id -> {
            FavoriteVideo t = new FavoriteVideo();
            t.setFid(id);
            t.setVid(vid);
            t.setTime(new Date());
            t.setIsRemove(0);
            favoriteMapper.batchInsertSv(t);
            favoriteMapper.addCount(1, id);
        });
        //更新缓存
        UserVideoQueryVo tUVQ = new UserVideoQueryVo();
        tUVQ.setUid(batchCollectVo.getUid());
        tUVQ.setVid(batchCollectVo.getVid());
        if (newList.isEmpty()) tUVQ.setActionType(0);
        else tUVQ.setActionType(1);
        updateCollectStatus(tUVQ);
    }

    @Override
    public void unCollect(BatchUpdateVo q) {
        //减收藏夹数量
        List<Long> vids = q.getVids();
        favoriteMapper.subCount(vids.size(), q.getPreFid());
        //删除旧收藏夹收藏关系
        vids.forEach(e -> {
            favoriteMapper.deleteFVById(q.getPreFid(), e);
        });
    }

    public void updateCollectStatus(UserVideoQueryVo userVideoQueryVo) {
        UserVideo res = new UserVideo();
        String videoKey = Constant.MAIN_VIDEO_FAVORITE_PREFIX + userVideoQueryVo.getVid();
        redisTemplate.opsForHash().put(videoKey,
                userVideoQueryVo.getUid(),
                userVideoQueryVo.getActionType());
        res.setCollect(userVideoQueryVo.getActionType());
    }
}
