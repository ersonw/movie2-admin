package com.example.movie2admin.util;

import com.alibaba.fastjson.JSONObject;
import com.example.movie2admin.data.wData;
import com.example.movie2admin.data.wRecord;
import com.example.movie2admin.entity.*;
import com.example.movie2admin.dao.*;
import com.example.movie2admin.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneOffset;
import java.util.*;

@Configurable
@Component
@Slf4j
public class WaLiUtil {
    private static WaLiUtil self;
    private static final int TIME_OUT = 30;
    public static final String TRANSFER_V2 = "transferV2";
    public static final String TRANSFER_V3 = "transferV3";
    public static final String QUERY_ORDER_V3 = "queryOrderV3";
    public static final String GET_AGENT_BALANCE = "getAgentBalance";
    public static final String REGISTER = "register";
    public static final String ENTER_GAME = "enterGame";
    public static final String KICK = "kick";
    public static final String GET_BALANCE = "getBalance";
    public static final String GET_RECORD_V2 = "getRecordV2";
    private static final String prefix = "CC";
    private static final Timer timer = new Timer();

    @Autowired
    private GameService service;
    @Autowired
    private GameWaterDao gameWaterDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GameDao gameDao;
    @Autowired
    private GameScrollDao gameScrollDao;

    static String apiUrl;
    static String agentId;
    static String apiUser;
    static String encryptKey;
    static String signKey;

    private static void _timersRecords() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getRecords();
                log.info("_timersRecords:{}", TimeUtil.getNowDate());
            }
        }, 1000,1000 * 60 * 10);
    }
    private static Map<String, String> _getMaps(String p){
        int t = (int) (System.currentTimeMillis() / 1000);
        Map<String, String> map = new HashMap<>();
        map.put("a",apiUser);
        map.put("t", String.valueOf(t));
        p = encrypt(encryptKey,p);
        String sign = getSign(signKey,p,t);
        map.put("p",p);
        map.put("k",sign);
        return map;
    }
    public static void handlerRecords(wRecord record){
        List<String> uids = record.getUid();
        List<Integer> games = record.getGame();
        List<String> profits = record.getProfit();
        List<String> balances = record.getBalance();
        List<String> validBets = record.getValidBet();
        List<String> taxs = record.getTax();
        List<String> recordTimes = record.getRecordTime();
        List<String> recordIds = record.getRecordId();
        List<String> detailUrls = record.getDetailUrl();
        List<GameWater> recordsList = new ArrayList<>();
        List<GameScroll> scrolls = new ArrayList<>();
        for (int i=0; i< uids.size(); i++) {
            if (i < games.size() && i < profits.size() &&
                    i < balances.size() && i < validBets.size() &&
                    i < recordTimes.size() && i < taxs.size() &&
                    i < recordIds.size()){
                GameWater records = self.gameWaterDao.findAllByRecordId(recordIds.get(i));
                Game game = self.gameDao.findAllByGameId(games.get(i));
                User user = self.userDao.findAllById(Long.parseLong(uids.get(i).replaceAll(prefix,"")));
                if (user != null && records == null && game != null){
                    records = new GameWater();
                    records.setUserId(user.getId());
                    records.setGameId(game.getId());
                    records.setProfit(new Double(Double.parseDouble(profits.get(i)) * 100).longValue());
                    records.setBalance(new Double(Double.parseDouble(balances.get(i)) * 100).longValue());
                    records.setValidBet(new Double(Double.parseDouble(validBets.get(i)) * 100).longValue());
                    records.setTax(new Double(Double.parseDouble(taxs.get(i)) * 100).longValue());
                    records.setRecordTime(TimeUtil.strToTime(recordTimes.get(i)));
                    records.setRecordId(recordIds.get(i));
                    records.setAddTime(System.currentTimeMillis());
                    if (detailUrls != null && i < detailUrls.size()){
                        records.setDetailUrl(detailUrls.get(i));
                    }
                    recordsList.add(records);
                    if (records.getProfit() < 0){
//                        GameScroll scroll = new GameScroll();
//                        scroll.setAddTime(System.currentTimeMillis());
//                        scroll.setName(user.getNickname());
//                        scroll.setGame(game.getName());
////                        scroll.setAmount((new Double(Double.parseDouble(profits.get(i)) * 100).longValue()));
//                        scroll.setAmount(-(new Double(Double.parseDouble(profits.get(i)) * 100).longValue()));
//                        scrolls.add(scroll);
                        scrolls.add(new GameScroll(user.getNickname(),-(new Double(Double.parseDouble(profits.get(i)) * 100).longValue()), game.getName()));
                    }
                }
            }
        }
        self.gameScrollDao.saveAllAndFlush(scrolls);
        self.gameWaterDao.saveAllAndFlush(recordsList);
    }
    private static void getRecords(String from, String until){
        String p = "from="+from+"&until="+until+"&detail=2";
        Map<String, String> map = _getMaps(p);
        String result = sendGet(apiUrl+"/"+GET_RECORD_V2,map);
        wData data = JSONObject.toJavaObject(JSONObject.parseObject(result),wData.class);
        if (data != null) {
//            System.out.println(result);
            if (data.getRecords() != null && data.getRecords().getCount() > 0){
                handlerRecords(data.getRecords().getList());
                if (data.getRecords().isHasMore()){
                    getRecords(from,until);
                }
            }
        }
    }
    public static void getRecords(){
        getRecords(TimeUtil._getTime(-35),TimeUtil._getTime(-2));
    }
    public static boolean tranfer(long id, long balance) {
        return tranferV3(id,balance);
    }
    public static boolean tranferV3(long id, long balance) {
        String p = "uid="+prefix+id+"&credit="+(balance / 100d)+"&orderId="+agentId+"_"+TimeUtil._getOrderNo()+"_"+prefix+ id;
        Map<String, String> map = _getMaps(p);
        String result = sendGet(apiUrl+"/"+TRANSFER_V3,map);
        System.out.println(result);
        if (result != null && result.startsWith("{")){
            wData data = JSONObject.toJavaObject(JSONObject.parseObject(result),wData.class);
            if (data != null) {
                if (data.getCode() == 0){
                    if (data.getObject().getInteger("status") == 1){
                        return true;
                    }else{
                        System.out.println("UID:"+id+" ????????????! ??????"+ (balance / 100d));
                    }
                }else{
                    handlerError(data.getMsg());
                }
            }
        }
        return false;
    }
    public static boolean tranferV2(long id, long balance) {
        String p = "uid="+prefix+id+"&credit="+(balance / 100d)+"&orderId="+agentId+"_"+TimeUtil._getOrderNo()+"_"+prefix+ id;
        Map<String, String> map = _getMaps(p);
        String result = sendGet(apiUrl+"/"+TRANSFER_V2,map);
        System.out.println(result);
        if (result != null && result.startsWith("{")){
            wData data = JSONObject.toJavaObject(JSONObject.parseObject(result),wData.class);
            if (data != null) {
                if (data.getCode() == 0){
                    if (data.getObject().getInteger("status") == 1){
                        return true;
                    }else{
                        System.out.println("UID:"+id+" ????????????! ??????"+ (balance / 100d));
                    }
                }else{
                    handlerError(data.getMsg());
                }
            }
        }
        return false;
    }
    @PostConstruct
    public void init(){
        self = this;
        rest();
    }
    public static void rest(){
        apiUrl = (self.service.getConfig("apiUrl"));
        agentId = (self.service.getConfig("agentId"));
        apiUser = (self.service.getConfig("apiUser"));
        encryptKey = (self.service.getConfig("encryptKey"));
        signKey = (self.service.getConfig("signKey"));
        if (StringUtils.isNotEmpty(apiUrl) &&
                StringUtils.isNotEmpty(agentId) &&
                StringUtils.isNotEmpty(apiUser) &&
                StringUtils.isNotEmpty(encryptKey) &&
                StringUtils.isNotEmpty(signKey)
        ) {
            System.out.println("?????????????????????????????????");
//            _timersRecords();
        }

    }
    private static String sendGet(String httpUrl, Map<String, String> parameter) {
        if (parameter == null || httpUrl == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = parameter.entrySet().iterator();
        while (iterator.hasNext()) {
            if (sb.length() > 0) {
                sb.append('&');
            }
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            String value;
            try {
                value = URLEncoder.encode(entry.getValue(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                value = "";
            }
            sb.append(key).append('=').append(value);
        }
        String urlStr = null;
        if (httpUrl.lastIndexOf('?') != -1) {
            urlStr = httpUrl + '&' + sb.toString();
        } else {
            urlStr = httpUrl + '?' + sb.toString();
        }

        HttpURLConnection httpCon = null;
        String responseBody = null;
        try {
            URL url = new URL(urlStr);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("GET");
            httpCon.setConnectTimeout(TIME_OUT * 1000);
            httpCon.setReadTimeout(TIME_OUT * 1000);
            // ???????????????????????????
            InputStream in = httpCon.getInputStream();
            byte[] readByte = new byte[1024];
            // ?????????????????????
            int readCount = in.read(readByte, 0, 1024);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (readCount != -1) {
                baos.write(readByte, 0, readCount);
                readCount = in.read(readByte, 0, 1024);
            }
            responseBody = new String(baos.toByteArray(), "UTF-8");
            baos.close();
        } catch (Exception ignored) {
        } finally {
            if (httpCon != null)
                httpCon.disconnect();
        }
        return responseBody;
    }
    private static String encrypt(String key, String src){
        String p = null;
//        String src = "name=Alice&text=Hello";
        byte[] aesKey = key.getBytes(StandardCharsets.UTF_8);
        try{
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(aesKey, "AES"));
            byte[] encrypted = cipher.doFinal(src.getBytes(StandardCharsets.UTF_8));
            p = Base64.getEncoder().encodeToString(encrypted);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return p;
    }
    private static String getSign(String key, String p, int t){
        return DigestUtils.md5DigestAsHex((p + t + key).getBytes());
    }
    private static void handlerError(String msg){
        switch(msg){
            case "illegal_a":
                System.out.println("a ???????????????????????????API ????????????????????????");
                break;
            case "illegal_t":
                System.out.println("t ???????????????????????????\n" +
                        "1. ??????????????????????????????????????????????????????????????????\n" +
                        "2. ???????????????????????????????????????????????????????????????????????? 1 ???\n" +
                        "??????\n" +
                        "3. ?????????????????????????????????????????????????????????");
                break;
            case "illegal_p__base64":
                System.out.println("p ????????????????????? Base64???????????????\n" +
                        "?????? url ?????? p ??????????????? url ???????????????url encode??????\n" +
                        "Base64 ???????????? / + = ???????????????????????????????????????????????????\n");
                break;
            case "illegal_p__aes":
                System.out.println("p ?????? AES ???????????????");
                break;
            case "illegal_k":
                System.out.println("k ??????????????????????????????");
                break;
            case "illegal_src_ip":
                System.out.println("IP ??????????????????\n" +
                        "???????????????????????? IP ????????????????????????\n" +
                        "?????????????????????????????????2???????????????");
                break;
            case "illegal_act":
                System.out.println("?????????????????????");
                break;
            case "too_many_requests":
                System.out.println("???????????????????????????????????????????????????");
                break;
            case "internal_error":
                System.out.println("????????????????????????");
                break;
            case "uid_required":
                System.out.println("?????????????????? uid");
                break;
            case "credit_illegal":
                System.out.println("???????????? credit ????????????\n");
                break;
        }
    }
    public static Double getBalance(long uid){
        int t = (int) (System.currentTimeMillis() / 1000);
        Map<String, String> map = new HashMap<>();
        map.put("a",apiUser);
        map.put("t", String.valueOf(t));
        String p = "uid="+prefix+uid;
        p = encrypt(encryptKey,p);
        String k = getSign(signKey,p,t);
        map.put("p",p);
        map.put("k",k);
        String result = sendGet(apiUrl+"/"+GET_BALANCE,map);
//        System.out.println(result);
        if (result != null && result.startsWith("{")){
            wData data = JSONObject.toJavaObject(JSONObject.parseObject(result),wData.class);
            if (data != null) {
                if (data.getCode() == 0){
                    if (data.getBalance().getStatus() == -1){
                        register(uid);
                    }
                    return data.getBalance().getBalance();
                }else{
                    handlerError(data.getMsg());
                }
            }
        }
        return 0.0;
    }
    public static boolean register(long uid){
        int t = (int) (System.currentTimeMillis() / 1000);
        Map<String, String> map = new HashMap<>();
        map.put("a",apiUser);
        map.put("t", String.valueOf(t));
        String p = "uid="+prefix+uid;
        p = encrypt(encryptKey,p);
        String k = getSign(signKey,p,t);
        map.put("p",p);
        map.put("k",k);
        String result = sendGet(apiUrl+"/"+REGISTER,map);
        if (result != null && result.startsWith("{")){
            wData data = JSONObject.toJavaObject(JSONObject.parseObject(result),wData.class);
            if (data != null) {
                if (data.getCode() == 0){
                    if (data.getObject().getInteger("status") == 1){
                        return true;
                    }else{
                        System.out.println("UID:"+uid+" ??????????????????!");
                    }
                }else{
                    handlerError(data.getMsg());
                }
            }
        }
        return false;
    }
    public static JSONObject enterGame(long uid, int gid){
        int t = (int) (System.currentTimeMillis() / 1000);
        Map<String, String> map = new HashMap<>();
        map.put("a",apiUser);
        map.put("t", String.valueOf(t));
        String p = "uid="+prefix+uid+"&game="+gid;
        p = encrypt(encryptKey,p);
        String k = getSign(signKey,p,t);
        map.put("p",p);
        map.put("k",k);
        String result = sendGet(apiUrl+"/"+ENTER_GAME,map);
//        System.out.println(result);
        if (result != null && result.startsWith("{")){
            wData data = JSONObject.toJavaObject(JSONObject.parseObject(result),wData.class);
            if (data != null) {
                if (data.getCode() == 0){
                    return data.getObject();
                }else{
                    handlerEnterGameError(data.getMsg());
                }
            }
        }
        return null;
    }
    private static void handlerEnterGameError(String msg) {
        switch (msg){
            case "game_requests":
                System.out.println("game ????????????????????????????????????????????????game???key???value???????????????");
                break;
            case "orderId_requests":
                System.out.println("orderId ??????????????????????????????????????????orderId???key???value????????????");
                break;
        }
    }
}

