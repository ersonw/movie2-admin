package com.example.movie2admin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.movie2admin.dao.*;
import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ShortVideoService {
    @Autowired
    private ShortVideoDao shortVideoDao;
    @Autowired
    private ShortVideoCollectDao shortVideoCollectDao;
    @Autowired
    private ShortVideoCommentDao shortVideoCommentDao;
    @Autowired
    private ShortVideoCommentLikeDao shortVideoCommentLikeDao;
    @Autowired
    private ShortVideoCommentReportDao shortVideoCommentReportDao;
    @Autowired
    private ShortVideoDownloadDao shortVideoDownloadDao;
    @Autowired
    private ShortVideoForwardDao shortVideoForwardDao;
    @Autowired
    private ShortVideoLikeDao shortVideoLikeDao;
    @Autowired
    private ShortVideoPlayDao shortVideoPlayDao;
    @Autowired
    private ShortVideoPpvodDao shortVideoPpvodDao;
    @Autowired
    private ShortVideoScaleDao shortVideoScaleDao;
    @Autowired
    private ShortVideoShareDao shortVideoShareDao;
    @Autowired
    private UserDao userDao;

    public ResponseData getVideoList(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"addTime"));
        Page<ShortVideo> videoPage;
        if (StringUtils.isNotEmpty(title)) {
            videoPage = shortVideoDao.findAllByTitleLike("%"+title+"%",pageable);
        }else {
            videoPage = shortVideoDao.findAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (ShortVideo video : videoPage.getContent()) {
            array.add(getTableVideo(video));
        }
        JSONObject object = ResponseData.object("total", videoPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }

    public ResponseData deleteVideo(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        for (long id :ids) {
            shortVideoDao.removeAllById(id);
        }
        return ResponseData.success();
    }

    public ResponseData updateVideo(long id, String title, int pin, int forward,int status, SysUser user, String ip) {
//        System.out.printf("pin:%d forward:%d\n",pin,forward);
        if (user == null) return ResponseData.error(201);
        if (id < 1) return ResponseData.error("视频不存在");
        ShortVideo video = shortVideoDao.findAllById(id);
        if (video == null) return ResponseData.error("视频不存在");
        video.setTitle(title);
        video.setPin(pin);
        video.setForward(forward);
        video.setStatus(status);
        video.setUpdateTime(System.currentTimeMillis());
        shortVideoDao.saveAndFlush(video);
        JSONObject json = getTableVideo(video);
        return ResponseData.success(json);
    }

    private JSONObject getTableVideo(ShortVideo video) {
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(video));
        User u = userDao.findAllById(video.getUserId());
        json.put("nickname","用户不存在");
        if (u!= null) {
            json.put("nickname",u.getNickname());
        }
        json.put("like", shortVideoLikeDao.countAllByVideoId(video.getId()));
        json.put("play",shortVideoPlayDao.countAllByVideoId(video.getId()));
        json.put("comments",shortVideoCommentDao.countAllByVideoId(video.getId()));
        return json;
    }
}
