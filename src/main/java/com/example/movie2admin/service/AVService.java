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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AVService {
    @Autowired
    private VideoDao videoDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private VideoPpvodDao videoPpvodDao;
    @Autowired
    private VideoClassDao videoClassDao;
    @Autowired
    private VideoPayDao videoPayDao;
    @Autowired
    private VideoPayRecordDao videoPayRecordDao;
    @Autowired
    private VideoLikeDao videoLikeDao;
    @Autowired
    private VideoPlayDao videoPlayDao;
    @Autowired
    private VideoCommentDao videoCommentDao;
    @Autowired
    private VideoCommentLikeDao videoCommentLikeDao;
    @Autowired
    private VideoConcentrationDao videoConcentrationDao;
    @Autowired
    private VideoConcentrationListDao videoConcentrationListDao;
    @Autowired
    private VideoProducedDao videoProducedDao;
    @Autowired
    private VideoProducedRecordDao videoProducedRecordDao;
    @Autowired
    private VideoPublicityDao videoPublicityDao;
    @Autowired
    private VideoPublicityReportDao videoPublicityReportDao;
    @Autowired
    private VideoScaleDao videoScaleDao;

    public boolean getConfigBool(String name){
        return getConfigLong(name) > 0;
    }
    public long getConfigLong(String name){
        String value = getConfig(name);
        if(value == null) return 0;
        return Long.parseLong(value);
    }
    public String getConfig(String name){
        List<VideoPpvod> configs = videoPpvodDao.findAllByName(name);
        return configs.isEmpty() ? null : configs.get(0).getVal();
    }
    public ResponseData getAVList(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"id"));
        Page<Video> videoPage;
        if (StringUtils.isNotEmpty(title)) {
            videoPage = videoDao.getAllByTitle("%"+title+"%",pageable);
        }else {
            videoPage = videoDao.getAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (Video video : videoPage.getContent()) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(video));
            VideoClass videoClass = videoClassDao.findAllById(video.getVodClass());
            if (videoClass != null) {
                json.put("class", videoClass.getName());
            }
            VideoPay pay = videoPayDao.findAllByVideoId(video.getId());
            if (pay != null) {
                json.put("price", pay.getAmount());
            }else {
                json.put("price",0);
            }
            json.put("like", videoLikeDao.countAllByVideoId(video.getId()));
            json.put("play",videoPlayDao.countAllByVideoId(video.getId()));
            json.put("scale",new Double(videoPlayDao.getScale(video.getId()) / videoScaleDao.countAllByVideoId(video.getId()) * 100).longValue());
            array.add(json);
        }
        JSONObject object = ResponseData.object("total", videoPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }

    public ResponseData delete(List<Long> ids, SysUser user, String ip) {
//        System.out.println(ids);
        if (user == null) return ResponseData.error(201);
        List<Video> videoList = videoDao.findAllById(ids);
        for (Video video : videoList) {
            video.setStatus(-1);
        }
        videoDao.saveAll(videoList);
        return ResponseData.success();
    }

    public ResponseData update(long trial,long id, long plays, long likes, String title, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (id < 1) return ResponseData.error("该记录不存在!");
        Video video = videoDao.findAllById(id);
        if (video == null) return ResponseData.error("该记录不存在!");
        video.setPlays(plays);
        video.setLikes(likes);
        video.setTitle(title);
        video.setTrial(trial);
        videoDao.saveAndFlush(video);
        return ResponseData.success();
    }

    public ResponseData getTrashList(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"id"));
        Page<Video> videoPage;
        if (StringUtils.isNotEmpty(title)) {
            videoPage = videoDao.findAllByTitleLikeAndStatus("%"+title+"%",-1,pageable);
        }else {
            videoPage = videoDao.findAllByStatus(-1,pageable);
        }
        JSONArray array = new JSONArray();
        for (Video video : videoPage.getContent()) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(video));
            array.add(json);
        }
        JSONObject object = ResponseData.object("total", videoPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }

    public ResponseData deleteTrash(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
//        videoDao.deleteAllById(ids);
        return ResponseData.success();
    }

    public ResponseData updateTrash(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        List<Video> videoList = videoDao.findAllById(ids);
        for (Video video : videoList) {
            video.setStatus(1);
        }
        videoDao.saveAll(videoList);
        return ResponseData.success();
    }

    public ResponseData addPrice(List<Long> ids, long amount, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        List<Video> videoList = videoDao.findAllById(ids);
        List<VideoPay> videoPays = new ArrayList<>();
        for (Video video : videoList) {
            VideoPay pay = videoPayDao.findAllByVideoId(video.getId());
            if (pay != null){
                pay.setAmount(amount);
            }else {
                pay = new VideoPay();
                pay.setVideoId(video.getId());
                pay.setAmount(amount);
                pay.setAddTime(System.currentTimeMillis());
            }
            pay.setUpdateTime(System.currentTimeMillis());
            videoPays.add(pay);
        }
        videoPayDao.saveAll(videoPays);
        return ResponseData.success();
    }

    public ResponseData getPriceList(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"id"));
        Page<Video> videoPage;
        if (StringUtils.isNotEmpty(title)) {
            videoPage = videoDao.getAllPriceByTitle("%"+title+"%",pageable);
        }else {
            videoPage = videoDao.getAllPrice(pageable);
        }
        JSONArray array = new JSONArray();
        for (Video video : videoPage.getContent()) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(video));
            VideoPay pay = videoPayDao.findAllByVideoId(video.getId());
            if (pay != null) {
                json.put("price", pay.getAmount());
                json.put("pid", pay.getId());
                array.add(json);
            }
        }
        JSONObject object = ResponseData.object("total", videoPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }

    public ResponseData deletePrice(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        for (long id: ids) {
            videoPayRecordDao.deleteAllByPay(id);
        }
        videoPayDao.deleteAllById(ids);
        return ResponseData.success();
    }

    public ResponseData updatePrice(List<Long> ids, long amount, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        List<VideoPay> payList = videoPayDao.findAllById(ids);
        for (VideoPay pay : payList) {
            pay.setAmount(amount);
        }
        videoPayDao.saveAll(payList);
        return ResponseData.success();
    }

    public ResponseData getOrderList(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"id"));
        Page<VideoPayRecord> recordPage;
        if(StringUtils.isNotEmpty(title)){
            recordPage = videoPayRecordDao.getAllByTitle("%"+title+"%",pageable);
        }else {
            recordPage = videoPayRecordDao.findAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (VideoPayRecord record: recordPage.getContent()) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(record));
            User u = userDao.findAllById(record.getUserId());
            json.put("nickname","用户不存在");
            json.put("price",0);
            json.put("title","视频不存在");
            if (u != null) {
                json.put("nickname",u.getNickname());
            }
            VideoPay pay = videoPayDao.findAllById(record.getPayId());
            if (pay != null) {
                json.put("price",pay.getAmount());
                Video video = videoDao.findAllById(pay.getVideoId());
                if (video != null) {
                    json.put("title",video.getTitle());
                }
            }
            array.add(json);
        }
        JSONObject object = ResponseData.object("total",recordPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }

    public ResponseData deleteOrder(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        videoPayRecordDao.deleteAllById(ids);
        return ResponseData.success();
    }

    public ResponseData getCommentList(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit,Sort.by(Sort.Direction.DESC,"add_time"));
        Page<VideoComment> commentPage;
        if(StringUtils.isNotEmpty(title)){
            commentPage = videoCommentDao.getAllByTitle("%"+title+"%",pageable);
        }else {
            commentPage = videoCommentDao.getAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (VideoComment comment: commentPage.getContent()) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(comment));
            json.put("likes",videoCommentLikeDao.countAllByCommentId(comment.getId()));
            User u = userDao.findAllById(comment.getUserId());
            if (u == null) {
                json.put("nickname","用户不存在");
            }else {
                json.put("nickname",u.getNickname());
            }
            Video video = videoDao.findAllById(comment.getVideoId());
            if (video == null) {
                json.put("title","视频不存在");
            }else{
                json.put("title",video.getTitle());
            }
            json.put("reply","一级评论");
            if (comment.getReplyId() > 0){
                VideoComment c = videoCommentDao.findAllById(comment.getReplyId());
                if (c != null) {
                    User p = userDao.findAllById(c.getUserId());
                    if(p != null){
                        json.put("reply","回复["+p.getNickname()+"]:"+c.getText());
                    }
                }
            }
            array.add(json);
        }
        JSONObject object = ResponseData.object("total",commentPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }

    public ResponseData deleteComment(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        for (long id: ids) {
            videoCommentDao.removeAllById(id);
        }
        return ResponseData.success();
    }

    public ResponseData passComment(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        List<VideoComment> comments = videoCommentDao.findAllById(ids);
        for (VideoComment comment : comments) {
            comment.setStatus(1);
        }
        videoCommentDao.saveAllAndFlush(comments);
        return ResponseData.success();
    }

    public ResponseData getConcentration(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"add_time"));
        Page<VideoConcentration> concentrationPage;
        if(StringUtils.isNotEmpty(title)){
            concentrationPage = videoConcentrationDao.getAllByName("%"+title+"%",pageable);
        }else {
            concentrationPage = videoConcentrationDao.getAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (VideoConcentration concentration: concentrationPage.getContent()) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(concentration));
            json.put("count", videoConcentrationListDao.countAllByConcentrationId(concentration.getId()));
            array.add(json);
        }
        JSONObject object = ResponseData.object("total",concentrationPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }

    public ResponseData deleteConcentration(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        for (Long id : ids) {
            videoConcentrationDao.removeAllById(id);
        }
        return ResponseData.success();
    }

    public ResponseData addConcentration(String name, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (StringUtils.isEmpty(name)) return ResponseData.error("类名必传!");
        name = name.replaceAll(" ", "");
        name = name.toUpperCase();
        name = name.trim();
        List<VideoConcentration> concentrations = videoConcentrationDao.findAllByName(name);
        if (concentrations.size() > 0) return ResponseData.error("类名已存在");
        VideoConcentration concentration = new VideoConcentration(name);
        videoConcentrationDao.saveAndFlush(concentration);
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(concentration));
        object.put("count", 0);
        return ResponseData.success(ResponseData.object("result", object));
    }

    public ResponseData updateConcentration(long id, String name, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (StringUtils.isEmpty(name)) return ResponseData.error("类名不允许为空!");
        if (id < 1) return ResponseData.error("记录不存在");
        VideoConcentration concentration = videoConcentrationDao.findAllById(id);
        if (concentration == null) return ResponseData.error("记录不存在");
        name = name.replaceAll(" ", "");
        name = name.toUpperCase();
        name = name.trim();
        VideoConcentration c = videoConcentrationDao.findByName(name);
        if(c != null && c.getId() != id) return ResponseData.error("类名已重复");
        concentration.setName(name);
        concentration.setUpdateTime(System.currentTimeMillis());
        videoConcentrationDao.saveAndFlush(concentration);
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(concentration));
        object.put("count", 0);
        return ResponseData.success(ResponseData.object("result", object));
    }

    public ResponseData deleteConcentrationList(List<Long> ids, long id, SysUser user, String ip) {
//        System.out.println(ids);
        if (user == null) return ResponseData.error(201);
        if(id < 1) return ResponseData.error("分类不存在");
        VideoConcentration concentration = videoConcentrationDao.findAllById(id);
        if(concentration == null) return ResponseData.error("分类不存在");
        videoConcentrationListDao.deleteAllById(ids);
        return ResponseData.success();
    }

    public ResponseData addConcentrationList(List<Long> ids, long id, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if(id < 1) return ResponseData.error("分类不存在");
        VideoConcentration concentration = videoConcentrationDao.findAllById(id);
        if(concentration == null) return ResponseData.error("分类不存在");
        List<VideoConcentrationList> lists = new ArrayList<>();
        for (long i: ids) {
            Video video = videoDao.findAllById(i);
            List<VideoConcentrationList> list = videoConcentrationListDao.findAllByConcentrationIdAndVideoId(id,i);
            if (video != null && list.size() == 0){
                lists.add(new VideoConcentrationList(id,i));
            }
        }
        videoConcentrationListDao.saveAllAndFlush(lists);
        JSONArray array = new JSONArray();
        for (VideoConcentrationList list: lists) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(list));
            Video video = videoDao.findAllById(list.getVideoId());
            json.put("className",concentration.getName());
            json.put("title","已删除");
            if (video != null){
                json.put("title",video.getTitle());
            }
            array.add(json);
        }
        return ResponseData.success(ResponseData.object("list", array));
    }

    public ResponseData getConcentrationList(long id, String title, int page, int limit, SysUser user, String ip) {
//        System.out.println(id);
        if (user == null) return ResponseData.error(201);
        if (id < 1) return ResponseData.error("记录不存在");
        VideoConcentration concentration = videoConcentrationDao.findAllById(id);
        if (concentration == null) return ResponseData.error("记录不存在");
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"id"));
        Page<VideoConcentrationList> listPage;
        if (StringUtils.isNotEmpty(title)){
            listPage = videoConcentrationListDao.getAllByTitle("%"+title+"%",id,pageable);
        }else{
            listPage = videoConcentrationListDao.findAllByConcentrationId(id,pageable);
        }
        JSONArray array = new JSONArray();
        for (VideoConcentrationList list: listPage.getContent()) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(list));
            Video video = videoDao.findAllById(list.getVideoId());
            json.put("className",concentration.getName());
            json.put("title","已删除");
            if (video != null){
                json.put("title",video.getTitle());
            }
            array.add(json);
        }
        JSONObject object = ResponseData.object("list", array);
        object.put("total", listPage.getTotalElements());
        return ResponseData.success(object);
    }

    public ResponseData getActiveList(long id, String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (id < 1) return ResponseData.error("记录不存在");
        VideoConcentration concentration = videoConcentrationDao.findAllById(id);
        if (concentration == null) return ResponseData.error("记录不存在");
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"add_time"));
        Page<Video> videoPage;
        if (StringUtils.isNotEmpty(title)){
            videoPage = videoDao.getActiveList(id,"%"+title+"%",pageable);
        }else{
            videoPage = videoDao.getActiveList(id,pageable);
        }
        JSONArray array = new JSONArray();
        for (Video video : videoPage.getContent()) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(video));
            VideoClass videoClass = videoClassDao.findAllById(video.getVodClass());
            if (videoClass != null) {
                json.put("class", videoClass.getName());
            }
            VideoPay pay = videoPayDao.findAllByVideoId(video.getId());
            if (pay != null) {
                json.put("price", pay.getAmount());
            }else {
                json.put("price",0);
            }
            json.put("like", videoLikeDao.countAllByVideoId(video.getId()));
            json.put("play",videoPlayDao.countAllByVideoId(video.getId()));
            array.add(json);
        }
        JSONObject object = ResponseData.object("total", videoPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }
}
