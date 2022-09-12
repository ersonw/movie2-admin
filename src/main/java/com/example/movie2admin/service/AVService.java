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
    @Autowired
    private PublicizeReportDao publicizeReportDao;
    @Autowired
    private PublicizeDao publicizeDao;
    @Autowired
    private DiamondButtonDao diamondButtonDao;
    @Autowired
    private DiamondConfigDao diamondConfigDao;
    @Autowired
    private DiamondOrderDao diamondOrderDao;
    @Autowired
    private CashInOptionDao cashInOptionDao;
    @Autowired
    private CashInConfigDao cashInConfigDao;
    @Autowired
    private CashInOrderDao cashInOrderDao;
    @Autowired
    private DiamondService diamondService;

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
        for (long id : ids) {
            videoDao.removeAllById(id);
        }
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
        object.put("count", videoConcentrationListDao.countAllByConcentrationId(concentration.getId()));
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

    public ResponseData getVideoSource(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"addTime"));
        Page<VideoProduced> producedPage;
        if (StringUtils.isNotEmpty(title)){
            producedPage = videoProducedDao.findAllByNameLike("%"+title+"%",pageable);
        }else{
            producedPage = videoProducedDao.findAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (VideoProduced produced : producedPage.getContent()) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(produced));
            json.put("count",videoProducedRecordDao.countAllByProducedId(produced.getId()));
            array.add(json);
        }
        JSONObject object = ResponseData.object("total", producedPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }

    public ResponseData deleteVideoSource(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        for (Long id : ids) {
            videoProducedDao.removeAllById(id);
        }
        return ResponseData.success();
    }

    public ResponseData addVideoSource(String name, int status, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (StringUtils.isEmpty(name)) return ResponseData.error("类名必传!");
        name = name.replaceAll(" ", "");
        name = name.toUpperCase();
        name = name.trim();
        List<VideoProduced> producedPage = videoProducedDao.findAllByName(name);
        if (producedPage.size() > 0) return ResponseData.error("类名已存在");
        VideoProduced produced = new VideoProduced(name,status);
        videoProducedDao.saveAndFlush(produced);
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(produced));
        object.put("count", 0);
        return ResponseData.success(ResponseData.object("result", object));
    }

    public ResponseData updateVideoSource(long id, String name, int status, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (StringUtils.isEmpty(name)) return ResponseData.error("类名不允许为空!");
        if (id < 1) return ResponseData.error("记录不存在");
        VideoProduced produced = videoProducedDao.findAllById(id);
        if (produced == null) return ResponseData.error("记录不存在");
        name = name.replaceAll(" ", "");
        name = name.toUpperCase();
        name = name.trim();
        VideoConcentration c = videoConcentrationDao.findByName(name);
        if(c != null && c.getId() != id) return ResponseData.error("类名已重复");
        produced.setName(name);
        produced.setStatus(status);
        produced.setUpdateTime(System.currentTimeMillis());
        videoProducedDao.saveAndFlush(produced);
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(produced));
        object.put("count", videoProducedRecordDao.countAllByProducedId(produced.getId()));
        return ResponseData.success(ResponseData.object("result", object));
    }

    public ResponseData getVideoSourceList(long id, String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (id < 1) return ResponseData.error("记录不存在");
        VideoProduced produced = videoProducedDao.findAllById(id);
        if (produced == null) return ResponseData.error("记录不存在");
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"id"));
        Page<VideoProducedRecord> recordPage;
        if (StringUtils.isNotEmpty(title)){
            recordPage = videoProducedRecordDao.getAllByTitle("%"+title+"%",id,pageable);
        }else{
            recordPage = videoProducedRecordDao.findAllByProducedId(id,pageable);
        }
        JSONArray array = new JSONArray();
        for (VideoProducedRecord record: recordPage.getContent()) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(record));
            Video video = videoDao.findAllById(record.getVideoId());
            json.put("className",produced.getName());
            json.put("title","已删除");
            if (video != null){
                json.put("title",video.getTitle());
            }
            array.add(json);
        }
        JSONObject object = ResponseData.object("list", array);
        object.put("total", recordPage.getTotalElements());
        return ResponseData.success(object);
    }

    public ResponseData getVideoSourceActiveList(long id, String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (id < 1) return ResponseData.error("记录不存在");
        VideoProduced produced = videoProducedDao.findAllById(id);
        if (produced == null) return ResponseData.error("记录不存在");
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"add_time"));
        Page<Video> videoPage;
        if (StringUtils.isNotEmpty(title)){
            videoPage = videoDao.getVideoSourceActiveList("%"+title+"%",pageable);
        }else{
            videoPage = videoDao.getVideoSourceActiveList(pageable);
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

    public ResponseData deleteVideoSourceList(List<Long> ids, long id, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if(id < 1) return ResponseData.error("分类不存在");
        VideoProduced produced = videoProducedDao.findAllById(id);
        if (produced == null) return ResponseData.error("记录不存在");
        videoProducedRecordDao.deleteAllById(ids);
        return ResponseData.success();
    }

    public ResponseData addVideoSourceList(List<Long> ids, long id, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if(id < 1) return ResponseData.error("分类不存在");
        VideoProduced produced = videoProducedDao.findAllById(id);
        if (produced == null) return ResponseData.error("记录不存在");
        List<VideoProducedRecord> lists = new ArrayList<>();
        for (long i: ids) {
            Video video = videoDao.findAllById(i);
            List<VideoConcentrationList> list = videoConcentrationListDao.findAllByConcentrationIdAndVideoId(id,i);
            if (video != null && list.size() == 0){
                lists.add(new VideoProducedRecord(id,i));
            }
        }
        videoProducedRecordDao.saveAllAndFlush(lists);
        JSONArray array = new JSONArray();
        for (VideoProducedRecord record: lists) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(record));
            Video video = videoDao.findAllById(record.getVideoId());
            json.put("className",produced.getName());
            json.put("title","已删除");
            if (video != null){
                json.put("title",video.getTitle());
            }
            array.add(json);
        }
        return ResponseData.success(ResponseData.object("list", array));
    }

    public ResponseData getIndexPublicity(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"addTime"));
        Page<VideoPublicity> publicityPage;
        if (StringUtils.isNotEmpty(title)){
            publicityPage = videoPublicityDao.findAllByNameLike("%"+title+"%",pageable);
        }else{
            publicityPage = videoPublicityDao.findAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (VideoPublicity publicity : publicityPage.getContent()) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(publicity));
            json.put("count",videoPublicityReportDao.countAllByPublicityId(publicity.getId()));
            array.add(json);
        }
        JSONObject object = ResponseData.object("total", publicityPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }

    public ResponseData deleteIndexPublicity(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        for (Long id : ids) {
            videoPublicityDao.removeAllById(id);
        }
        return ResponseData.success();
    }

    public ResponseData addIndexPublicity(String name, String pic, String url, int type, int status, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if(StringUtils.isEmpty(pic)) return ResponseData.error("图片地址不可空");
        if(StringUtils.isEmpty(url)) return ResponseData.error("跳转地址不可空");
        VideoPublicity videoPublicity = new VideoPublicity();
        name = name.replaceAll(" ","");
        name = name.trim().toUpperCase();
        pic = pic.trim();
        url = url.trim();
        videoPublicity.setAddTime(System.currentTimeMillis());
        videoPublicity.setUrl(url);
        videoPublicity.setType(type);
        videoPublicity.setStatus(status);
        videoPublicity.setPic(pic);
        videoPublicity.setName(name);
        videoPublicityDao.saveAndFlush(videoPublicity);
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(videoPublicity));
        object.put("count", 0);
        return ResponseData.success(ResponseData.object("result", object));
    }

    public ResponseData updateIndexPublicity(long id, String name, String pic, String url, int type, int status, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (id < 1) return ResponseData.error("广告不存在");
        if(StringUtils.isEmpty(pic)) return ResponseData.error("图片地址不可空");
        if(StringUtils.isEmpty(url)) return ResponseData.error("跳转地址不可空");
        VideoPublicity videoPublicity = videoPublicityDao.findAllById(id);
        if (videoPublicity == null) return ResponseData.error("广告不存在");
        name = name.replaceAll(" ","");
        name = name.trim().toUpperCase();
        pic = pic.trim();
        url = url.trim();
        videoPublicity.setUrl(url);
        videoPublicity.setType(type);
        videoPublicity.setStatus(status);
        videoPublicity.setPic(pic);
        videoPublicity.setName(name);
        List<VideoPublicity> publicitys = videoPublicityDao.findAllByName(name);
        if (publicitys.size() > 0 && publicitys.get(0).getId() != id) return ResponseData.error("名称已存在");
        videoPublicityDao.saveAndFlush(videoPublicity);
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(videoPublicity));
        object.put("count", videoPublicityReportDao.countAllByPublicityId(id));
        return ResponseData.success(ResponseData.object("result", object));
    }

    public ResponseData getPlayPublicity(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"addTime"));
        Page<Publicize> publicizePage;
        if (StringUtils.isNotEmpty(title)){
            publicizePage = publicizeDao.findAllByNameLike("%"+title+"%",pageable);
        }else{
            publicizePage = publicizeDao.findAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (Publicize publicize : publicizePage.getContent()) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(publicize));
            json.put("count",publicizeReportDao.countAllByPublicityId(publicize.getId()));
            array.add(json);
        }
        JSONObject object = ResponseData.object("total", publicizePage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }

    public ResponseData deletePlayPublicity(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        for (Long id : ids) {
            publicizeDao.removeAllById(id);
        }
        return ResponseData.success();
    }

    public ResponseData addPlayPublicity(String name, String image, String url, int type, int page, int status, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if(StringUtils.isEmpty(image)) return ResponseData.error("图片地址不可空");
        if(StringUtils.isEmpty(url)) return ResponseData.error("跳转地址不可空");
        Publicize publicize = new Publicize();
        name = name.replaceAll(" ","");
        name = name.trim().toUpperCase();
        image = image.trim();
        url = url.trim();
        publicize.setAddTime(System.currentTimeMillis());
        publicize.setUpdateTime(System.currentTimeMillis());
        publicize.setUrl(url);
        publicize.setType(type);
        publicize.setStatus(status);
        publicize.setPage(page);
        publicize.setImage(image);
        publicize.setName(name);
        publicizeDao.saveAndFlush(publicize);
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(publicize));
        object.put("count", 0);
        return ResponseData.success(ResponseData.object("result", object));
    }

    public ResponseData updatePlayPublicity(long id, String name, String image, String url, int type, int page, int status, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if(StringUtils.isEmpty(image)) return ResponseData.error("图片地址不可空");
        if(StringUtils.isEmpty(url)) return ResponseData.error("跳转地址不可空");
        if (id < 1) return ResponseData.error("广告不存在");
        Publicize publicize = publicizeDao.findAllById(id);
        if (publicize == null) return ResponseData.error("广告不存在");
        name = name.replaceAll(" ","");
        name = name.trim().toUpperCase();
        image = image.trim();
        url = url.trim();
        publicize.setAddTime(System.currentTimeMillis());
        publicize.setUpdateTime(System.currentTimeMillis());
        publicize.setUrl(url);
        publicize.setType(type);
        publicize.setStatus(status);
        publicize.setPage(page);
        publicize.setImage(image);
        publicize.setName(name);
        List<Publicize> publicizes = publicizeDao.findAllByName(name);
        if (publicizes.size() > 0 && publicizes.get(0).getId() != id) return ResponseData.error("名称已存在");
        publicizeDao.saveAndFlush(publicize);
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(publicize));
        object.put("count", videoPublicityReportDao.countAllByPublicityId(id));
        return ResponseData.success(ResponseData.object("result", object));
    }

    public ResponseData getVideoConfig(SysUser user, String ip) {
        if (user == null) return ResponseData.error("");
        List<VideoPpvod> configs = videoPpvodDao.findAll();
        JSONObject object = new JSONObject();
        for (VideoPpvod config: configs) {
            object.put(config.getName(), config.getVal());
        }
        return ResponseData.success(object);
    }

    public ResponseData deleteVideoConfig(String name, SysUser user, String ip) {
        if (user == null) return ResponseData.error("");
        if (StringUtils.isEmpty(name)) return ResponseData.error("必须填写需要删除的值");
        List<VideoPpvod> configs = videoPpvodDao.findAllByName(name);
        for (VideoPpvod config: configs) {
            videoPpvodDao.deleteById(config.getId());
        }
        return ResponseData.success();
    }

    public ResponseData addVideoConfig(String name, String value, SysUser user, String ip) {
        if (user == null) return ResponseData.error("");
        if (StringUtils.isEmpty(name)) return ResponseData.error("");
        if (StringUtils.isEmpty(value)) return ResponseData.error("必须填写值");
        name = name.replaceAll(" ","").trim();
        value = value.replaceAll(" ","").trim();
        List<VideoPpvod> configs = videoPpvodDao.findAllByName(name);
        if (configs.size() > 0) return ResponseData.error("配置已存在");
        videoPpvodDao.saveAndFlush(new VideoPpvod(name,value));
        return ResponseData.success();
    }
    public List<VideoPpvod> getUpdateConfig(JSONObject data){
        List<VideoPpvod> configs = new ArrayList<>();
        JSONObject object = new JSONObject();
        for (String key : data.keySet()) {
            if (
                    data.get(key) != null && !key.equals("ip") &&
                    data.get(key) != null && !key.equals("isWeb") &&
                    data.get(key) != null && !key.equals("serverName") &&
                    data.get(key) != null && !key.equals("serverPort") &&
                    data.get(key) != null && !key.equals("uri") &&
                    data.get(key) != null && !key.equals("url") &&
                    data.get(key) != null && !key.equals("schema") &&
                    data.get(key) != null && !key.equals("user")
            ){
                object.put(key, data.get(key));
            }
        }
        for (String key : object.keySet()) {
            VideoPpvod config = videoPpvodDao.findByName(key);
            if (config == null){
                config = new VideoPpvod(key, object.getString(key));
            }else{
                config.setVal(object.getString(key));
                config.setUpdateTime(System.currentTimeMillis());
            }
            configs.add(config);
        }
        return configs;
    }
    public List<DiamondConfig> getDiamondUpdateConfig(JSONObject data){
        List<DiamondConfig> configs = new ArrayList<>();
        JSONObject object = new JSONObject();
        for (String key : data.keySet()) {
            if (
                    data.get(key) != null && !key.equals("ip") &&
                    data.get(key) != null && !key.equals("isWeb") &&
                    data.get(key) != null && !key.equals("serverName") &&
                    data.get(key) != null && !key.equals("serverPort") &&
                    data.get(key) != null && !key.equals("uri") &&
                    data.get(key) != null && !key.equals("url") &&
                    data.get(key) != null && !key.equals("schema") &&
                    data.get(key) != null && !key.equals("user")
            ){
                object.put(key, data.get(key));
            }
        }
        for (String key : object.keySet()) {
            DiamondConfig config = diamondConfigDao.findByName(key);
            if (config == null){
                config = new DiamondConfig(key, object.getString(key));
            }else{
                config.setVal(object.getString(key));
                config.setUpdateTime(System.currentTimeMillis());
            }
            configs.add(config);
        }
        return configs;
    }
    public ResponseData updateVideoConfig(JSONObject data) {
        String u = data.getString("user");
        if (StringUtils.isEmpty(u)) return ResponseData.error("");
        SysUser user = SysUser.getUser(u);
        if (user == null) return ResponseData.error("");
        videoPpvodDao.saveAllAndFlush(getUpdateConfig(data));
        return ResponseData.success();
    }

    public ResponseData getDiamondConfig(SysUser user, String ip) {
        if (user == null) return ResponseData.error("");
        List<DiamondConfig> configs = diamondConfigDao.findAll();
        JSONObject object = new JSONObject();
        for (DiamondConfig config: configs) {
            object.put(config.getName(), config.getVal());
        }
        return ResponseData.success(object);
    }

    public ResponseData updateDiamondConfig(JSONObject data) {
        String u = data.getString("user");
        if (StringUtils.isEmpty(u)) return ResponseData.error("");
        SysUser user = SysUser.getUser(u);
        if (user == null) return ResponseData.error("");
        diamondConfigDao.saveAllAndFlush(getDiamondUpdateConfig(data));
        return ResponseData.success();
    }
    public List<CashInOption> getAllowed(CashInConfig config){
        List<CashInOption> options = cashInOptionDao.findAllByStatus(1);
        if (config != null && StringUtils.isNotEmpty(config.getAllowed())){
            String[] allowed = config.getAllowed().split(",");
            options = new ArrayList<>();
            for (String s : allowed) {
                List<CashInOption> o = cashInOptionDao.findAllByStatusAndName(1, s);
//                    List<CashInOption> o = cashInOptionDao.findAllByStatusAndCode(1, s);
                if (o.size() > 0) {
                    options.add(o.get(0));
                }
            }
        }
        return options;
    }
    public JSONObject getButton(DiamondButton button){
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(button));
        json.put("cashIn","不指定充值通道");
        json.put("typeStr","无支付方式");
        List<CashInOption> options = new ArrayList<>();
        if (button.getCashInId() > 0){
            json.put("cashIn","指定充值通道已被删除");
            CashInConfig config = cashInConfigDao.findAllById(button.getCashInId());
            if (config != null){
                json.put("cashIn", config.getTitle());
                options = getAllowed(config);
            }
        }else{
            options = cashInOptionDao.findAll();
        }
        if (options.size() > 0){
            StringBuilder sb = new StringBuilder();
            for (CashInOption option: options) {
                sb.append(option.getName()).append(",");
            }
            json.put("typeStr",sb.toString());
        }
        return json;
    }
    public ResponseData getButtonList(long title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.ASC,"amount"));
        Page<DiamondButton> buttonPage;
        if (title > 0) {
            buttonPage = diamondButtonDao.findAllByAmountGreaterThanEqual(title,pageable);
        }else {
            buttonPage = diamondButtonDao.findAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (DiamondButton button : buttonPage.getContent()) {
            array.add(getButton(button));
        }
        JSONObject object = ResponseData.object("total", buttonPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }

    public ResponseData deleteButton(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        List<DiamondButton> buttons = diamondButtonDao.findAllById(ids);
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setStatus(-1);
        }
        diamondButtonDao.saveAllAndFlush(buttons);
        return ResponseData.success();
    }

    public ResponseData updateButton(long id, long amount, Double price, int less, long cashInId, int status, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        DiamondButton button = diamondButtonDao.findAllById(id);
        if (button == null) return ResponseData.error("按钮不存在");
        if (cashInId > 0){
            CashInConfig config = cashInConfigDao.findAllById(cashInId);
            if (config == null) return ResponseData.error("通道ID不存在");
        }else{
            cashInId = 0;
        }
        button.setAmount(amount);
        button.setPrice(price.longValue());
        button.setCashInId(cashInId);
        button.setLess(less);
        button.setStatus(status);
        button.setUpdateTime(System.currentTimeMillis());
        if (button.getAddTime() == 0) button.setAddTime(System.currentTimeMillis());
        diamondButtonDao.saveAndFlush(button);
        return ResponseData.success(getButton(button));
    }
    public ResponseData addButton(long amount, Double price, int less, long cashInId, int status, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (cashInId > 0){
            CashInConfig config = cashInConfigDao.findAllById(cashInId);
            if (config == null) return ResponseData.error("通道ID不存在");
        }else{
            cashInId = 0;
        }
        DiamondButton button = new DiamondButton();
        button.setAmount(amount);
        button.setPrice(price.longValue());
        button.setCashInId(cashInId);
        button.setLess(less);
        button.setStatus(status);
        button.setUpdateTime(System.currentTimeMillis());
        button.setAddTime(System.currentTimeMillis());
        diamondButtonDao.saveAndFlush(button);
        return ResponseData.success(getButton(button));
    }
    public ResponseData getDiamondOrderList(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"addTime"));
        Page<DiamondOrder> orderPage;
        if (StringUtils.isNotEmpty(title)) {
            orderPage = diamondOrderDao.findAllByOrderNoLike("%"+title+"%",pageable);
        }else {
            orderPage = diamondOrderDao.findAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (DiamondOrder order : orderPage.getContent()) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(order));
            User u = userDao.findAllById(order.getUserId());
            CashInOrder inOrder = cashInOrderDao.findAllByOrderNo(order.getOrderNo());
            json.put("price", String.format("%.2f", order.getPrice() / 100D));
            json.put("user", "用户不存在");
            json.put("status",0);
            json.put("type","支付方式不存在");
            json.put("ip","没有IP");
            if (u!= null) {
                json.put("user", u.getUsername());
            }
            if (inOrder!= null && inOrder.getStatus() >= 0) {
                json.put("status",inOrder.getStatus());
                json.put("totalFee",inOrder.getTotalFee());
                json.put("tradeNo",inOrder.getTradeNo());
                CashInOption option = cashInOptionDao.findAllById(inOrder.getType());
                if (option != null)json.put("type", option.getName());
                json.put("ip",inOrder.getIp());
                array.add(json);
            }
        }
        JSONObject object = ResponseData.object("total", orderPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }

    public ResponseData deleteDiamondOrder(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        List<DiamondOrder> orders = diamondOrderDao.findAllById(ids);
        List<CashInOrder> inOrders = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            CashInOrder order = cashInOrderDao.findAllByOrderNo(orders.get(i).getOrderNo());
            if (order != null){
                order.setStatus(-1);
                inOrders.add(order);
            }
        }
        diamondOrderDao.saveAllAndFlush(orders);
        cashInOrderDao.saveAllAndFlush(inOrders);
        return ResponseData.success();
    }
    public ResponseData makeupDiamondOrder(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        List<DiamondOrder> orders = diamondOrderDao.findAllById(ids);
        List<CashInOrder> inOrders = new ArrayList<>();
        JSONArray array = new JSONArray();
        for (DiamondOrder order: orders) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(order));
            User u = userDao.findAllById(order.getUserId());
            CashInOrder inOrder = cashInOrderDao.findAllByOrderNo(order.getOrderNo());
            json.put("price", String.format("%.2f", order.getPrice() / 100D));
            json.put("user", "用户不存在");
            json.put("status",0);
            json.put("type","支付方式不存在");
            json.put("ip","没有IP");
            if (u!= null) {
                json.put("user", u.getUsername());
            }
            if (inOrder != null && inOrder.getStatus() != 1) {
                inOrder.setStatus(1);
                inOrders.add(inOrder);
                json.put("status",inOrder.getStatus());
                json.put("totalFee",inOrder.getTotalFee());
                json.put("tradeNo",inOrder.getTradeNo());
                CashInOption option = cashInOptionDao.findAllById(inOrder.getType());
                if (option != null)json.put("type", option.getName());
                json.put("ip",inOrder.getIp());
                array.add(json);
            }
        }
        cashInOrderDao.saveAllAndFlush(inOrders);
        for (CashInOrder order: inOrders) {
            diamondService.handlerOrder(order.getOrderNo());
        }
        return ResponseData.success(array);
    }
}
