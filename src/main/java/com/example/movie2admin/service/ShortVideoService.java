package com.example.movie2admin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.movie2admin.dao.*;
import com.example.movie2admin.data.OssConfig;
import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.data.ShortVideoFile;
import com.example.movie2admin.entity.*;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
    @Autowired
    private UsersService usersService;

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

    public ResponseData getShortVideoUser(long id, String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (id < 1) return ResponseData.error("用户不存在");
        User u = userDao.findAllById(id);
        if (u == null) return ResponseData.error("用户不存在");
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"add_time"));
        Page<ShortVideo> videoPage;
        if (StringUtils.isNotEmpty(title)) {
            videoPage = shortVideoDao.getAllByUserVideos(id,"%"+title+"%",pageable);
        }else {
            videoPage = shortVideoDao.getAllByUserVideos(id,pageable);
        }
        JSONArray array = new JSONArray();
        for (ShortVideo video : videoPage.getContent()) {
            array.add(getTableVideo(video));
        }
        JSONObject object = ResponseData.object("total", videoPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }
    public String getOssUrl(String path,OssConfig config){
        String endPoint = config.getEndPoint();
        if(!endPoint.startsWith("http")){
            if(config.getUseSSL()){
                endPoint = "https://"+endPoint;
            }else {
                endPoint = "http://"+endPoint;
            }
        }
        if(config.getPort() != null){
            endPoint = endPoint+":"+config.getPort();
        }
//        log.error("endPoint:{} AccessKey:{} SecretKey:{}",endPoint,config.getAccessKey(),config.getSecretKey());
        switch (config.getType()){
            case OssConfig.TYPE_UPLOAD_OSS_MINIO:
                try {
                    MinioClient minioClient = new MinioClient(endPoint, config.getAccessKey(), config.getSecretKey());
//                    ObjectStat objectStat = minioClient.statObject(config.getBucket(), path);
//                    System.out.println(objectStat);
//                    System.out.printf(minioClient.getObjectUrl(config.getBucket(),path));
                    ObjectStat stat = minioClient.statObject(config.getBucket(),path);
//                    System.out.printf("length:%d\n",stat.length());
//                    if ()
                    return minioClient.getObjectUrl(config.getBucket(),path);
                } catch (InvalidPortException | InvalidEndpointException | InvalidBucketNameException |
                         InsufficientDataException | XmlPullParserException | ErrorResponseException |
                         NoSuchAlgorithmException | IOException | NoResponseException | InvalidKeyException |
                         InternalException e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }
    public JSONObject getShortVideo(ShortVideo video){
        if (video == null) return null;
        JSONObject object = new JSONObject();
        object.put("id",video.getId());
        object.put("title",video.getTitle());
        ShortVideoFile file = new ShortVideoFile(video.getFile());
        if(StringUtils.isNotEmpty(video.getPlayUrl())){
            object.put("playUrl",video.getPlayUrl());
        }else {
            String url = getOssUrl(file.getFilePath(), OssConfig.getOssConfig(file.getOssConfig()));
            if (url == null) return null;
            object.put("playUrl",url);
        }
        if (StringUtils.isNotEmpty(video.getPic())){
            object.put("pic",video.getPic());
        }else {
            String url = getOssUrl(file.getImagePath(),OssConfig.getOssConfig(file.getOssConfig()));
//            if (url != null) return null;
            object.put("pic",url);
        }
        object.put("addTime",video.getAddTime());

        object.put("plays",shortVideoPlayDao.countAllByVideoId(video.getId()));
        object.put("likes",shortVideoLikeDao.countAllByVideoId(video.getId()));
        object.put("pin",video.getPin() == 1);

//        object.put("comments",getComments(video.getId(),userId));
        object.put("comments",shortVideoCommentDao.countAllByVideoIdAndStatus(video.getId(),1));
        object.put("collects",shortVideoCollectDao.countAllByVideoId(video.getId()));
        object.put("forwards", shortVideoShareDao.countAllByVideoId(video.getId()));
        object.put("forward", video.getForward() == 1);

        User user = userDao.findAllById(video.getUserId());
        if (user != null){
            object.put("avatar",user.getAvatar());
            object.put("nickname",user.getNickname());
        }
        object.put("userId",video.getUserId());
        return  object;
    }
    public ResponseData getUser(long id, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (id < 1) return ResponseData.error("用户不存在");
        User u = userDao.findAllById(id);
        if (u == null) return ResponseData.error("用户不存在");
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(u));
        object.put("level", usersService.getMemberLevel(u.getId()));
        object.put("member", usersService.getMember(u.getId())?"VIP会员":"已过期");
        object.put("expired", usersService.getExpired(u.getId()));
        object.put("profile", usersService.getProgressProfile(u));
        object.put("videos", shortVideoDao.countAllByUserId(u.getId()));
        object.put("likes", shortVideoLikeDao.getAllByUserId(u.getId()));
        object.put("plays",shortVideoDao.countAllByUserId(u.getId()));
        object.put("comments",shortVideoCommentDao.countAllByUserId(u.getId()));
        return ResponseData.success(object);
    }

    public ResponseData getAuditVideoList(int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"addTime"));
        Page<ShortVideo> videoPage = shortVideoDao.findAllByStatus(0,pageable);
        JSONArray array = new JSONArray();
        for (ShortVideo video : videoPage.getContent()) {
            array.add(getShortVideo(video));
        }
        JSONObject object = ResponseData.object("total", videoPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }

    public ResponseData getAuditVideo(SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        ShortVideo video = shortVideoDao.getAuditVideo();
        return ResponseData.success(getShortVideo(video));
    }

    public ResponseData passAuditVideo(long id, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (id < 1) return ResponseData.error("");
        ShortVideo video = shortVideoDao.findAllById(id);
        if (video == null) return ResponseData.error("记录不存在!");
        video.setStatus(1);
        shortVideoDao.saveAndFlush(video);
        return ResponseData.success();
    }

    public ResponseData denyAuditVideo(long id, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (id < 1) return ResponseData.error("");
        ShortVideo video = shortVideoDao.findAllById(id);
        if (video == null) return ResponseData.error("记录不存在!");
        video.setStatus(-1);
        shortVideoDao.saveAndFlush(video);
        return ResponseData.success();
    }
}
