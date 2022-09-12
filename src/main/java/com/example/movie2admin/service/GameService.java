package com.example.movie2admin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.movie2admin.dao.*;
import com.example.movie2admin.data.EPayData;
import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.entity.*;
import com.example.movie2admin.util.EPayUtil;
import com.example.movie2admin.util.TimeUtil;
import com.example.movie2admin.util.UrlUtil;
import com.example.movie2admin.util.WaLiUtil;
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
public class GameService {
    private static int INDEX_OF_SCROLL = 0;
    private static int MINI_OF_SCROLL = 12;
    private static int INDEX_OF_CASH_IN = 0;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GameDao gameDao;
    @Autowired
    private GameButtonDao gameButtonDao;
    @Autowired
    private GameCarouselDao gameCarouselDao;
    @Autowired
    private GameConfigDao gameConfigDao;
    @Autowired
    private GameFundsDao gameFundsDao;
    @Autowired
    private GameOrderDao gameOrderDao;
    @Autowired
    private GameScrollDao gameScrollDao;
    @Autowired
    private GameWaterDao gameWaterDao;
    @Autowired
    private GamePublicityDao gamePublicityDao;
    @Autowired
    private GamePublicityReportDao gamePublicityReportDao;
    @Autowired
    private CashInOrderDao cashInOrderDao;
    @Autowired
    private CashInConfigDao cashInConfigDao;
    @Autowired
    private CashInOptionDao cashInOptionDao;
    @Autowired
    private AuthDao authDao;
    @Autowired
    private GameOutConfigDao gameOutConfigDao;
    @Autowired
    private GameOutCardDao gameOutCardDao;
    @Autowired
    private GameOutOrderDao gameOutOrderDao;

    public boolean getOutConfigBool(String name) {
        return getOutConfigLong(name) > 0;
    }

    public long getOutConfigLong(String name) {
        String value = getOutConfig(name);
        if (value == null) return 0;
        return Long.parseLong(value);
    }
    public double getOutConfigDouble(String name) {
        String value = getOutConfig(name);
        if (value == null) return 0D;
        return Double.parseDouble(value);
    }

    public String getOutConfig(String name) {
        List<GameOutConfig> configs = gameOutConfigDao.findAllByName(name);
        return configs.isEmpty() ? null : configs.get(0).getVal();
    }
    public boolean getConfigBool(String name) {
        return getConfigLong(name) > 0;
    }

    public long getConfigLong(String name) {
        String value = getConfig(name);
        if (value == null) return 0;
        return Long.parseLong(value);
    }

    public String getConfig(String name) {
        List<GameConfig> configs = gameConfigDao.findAllByName(name);
        return configs.isEmpty() ? null : configs.get(0).getVal();
    }

    public JSONArray getScroll(List<GameScroll> scrolls) {
        JSONArray array = new JSONArray();
        for (GameScroll scroll : scrolls) {
            JSONObject json = new JSONObject();
            json.put("text2", scroll.getName());
            if(scroll.getGame().contains("提现")){
                json.put("text3", "兴高采烈地提走了一桶金 ");
            }else {
                json.put("text3", "在游戏【" + scroll.getGame() + "】中赢得了");
            }
            json.put("text4", String.format("%.2f", scroll.getAmount() / 100D));
            json.put("text5", "元");
            array.add(json);
        }
        return array;
    }
    public JSONObject getGame(Game game) {
        return getGame(game, true);
    }
    public JSONObject getGame(Game game, boolean small) {
        JSONObject object = new JSONObject();
        object.put("name", game.getName());
        if (small){
            object.put("image", getImageSmall(game));
        }else {
            object.put("image", getImage(game));
        }
        object.put("id", game.getId());
        return object;
    }
    public String getImageSmall(Game game) {
//        if (StringUtils.isEmpty(game.getImage())) return game.getImage();
        if (StringUtils.isNotEmpty(game.getImage()) && game.getImage().startsWith("http")) return game.getImage();
        return getConfig("ImageDomain") + "/game/gameicon-200x200/200-200-" + game.getGameId() + "." + UrlUtil.encode(game.getName()) + ".png";
    }
    public String getImage(Game game) {
//        if (StringUtils.isEmpty(game.getImage())) return game.getImage();
        if (StringUtils.isNotEmpty(game.getImage()) && game.getImage().startsWith("http")) return game.getImage();
        return getConfig("ImageDomain") + "/game/gameicon-600400-square/600-400-" + game.getGameId() + "." + UrlUtil.encode(game.getName()) + ".png";
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

    public boolean handlerOrder(String orderId){
        GameOrder order = gameOrderDao.findAllByOrderNo(orderId);
        if (order == null) return false;
        User user = userDao.findAllById(order.getUserId());
        if (user == null) return false;
        GameFunds fund = new GameFunds(user.getId(), order.getAmount() * 100, "后台补单:\n"+orderId);
        if (WaLiUtil.tranfer(user.getId(),fund.getAmount())){
            gameFundsDao.saveAndFlush(fund);
            return true;
        }
        return false;
    }

    public JSONObject getCard(GameOutCard card) {
        JSONObject object = new JSONObject();
        object.put("id", card.getId());
        object.put("name", card.getName());
        object.put("bank", card.getBank());
        object.put("card", card.getCard());
        object.put("address", card.getAddress());
        object.put("addTime", card.getAddTime());
        object.put("updateTime", card.getUpdateTime());
        return object;
    }

    public ResponseData getGameList(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"addTime"));
        Page<Game> gamePage;
        if (StringUtils.isNotEmpty(title)) {
            gamePage = gameDao.findAllByName("%"+title+"%",pageable);
        }else {
            gamePage = gameDao.findAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (Game game : gamePage.getContent()) {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(game));
            json.put("pic", getImage(game));
            array.add(json);
        }
        JSONObject object = ResponseData.object("total", gamePage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }

    public ResponseData deleteGame(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        List<Game> games = gameDao.findAllById(ids);
        for (int i = 0; i < games.size(); i++) {
            games.get(i).setStatus(-1);
        }
        gameDao.saveAllAndFlush(games);
        return ResponseData.success();
    }

    public ResponseData updateGame(long id, String name, String image, int gameId, int status, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (id < 1) return ResponseData.error("");
        if (StringUtils.isEmpty(name)) return ResponseData.error("游戏名称不可为空");
        Game game = gameDao.findAllById(id);
        if (game == null) return ResponseData.error("游戏不存在!");
        name = name.replaceAll(" ","").trim().toUpperCase();
        game.setName(name);
        game.setImage(image);
        game.setGameId(gameId);
        game.setStatus(status);
        game.setUpdateTime(System.currentTimeMillis());
        if(game.getAddTime() == 0) game.setAddTime(System.currentTimeMillis());
        gameDao.saveAndFlush(game);
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(game));
        json.put("pic", getImage(game));
        return ResponseData.success(json);
    }

    public ResponseData addGame(String name, String image, int gameId, int status, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (gameId < 1) return ResponseData.error("游戏ID不可为0");
        if (StringUtils.isEmpty(name)) return ResponseData.error("游戏名称不可为空");
        Game game = gameDao.findAllByGameId(gameId);
        if (game != null) return ResponseData.error("游戏ID已存在!");
        name = name.replaceAll(" ","").trim().toUpperCase();
        game = new Game();
        game.setName(name);
        game.setImage(image);
        game.setGameId(gameId);
        game.setStatus(status);
        game.setUpdateTime(System.currentTimeMillis());
        game.setAddTime(System.currentTimeMillis());
        gameDao.saveAndFlush(game);
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(game));
        json.put("pic", getImage(game));
        return ResponseData.success(json);
    }

    public ResponseData getButtonList(long title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.ASC,"amount"));
        Page<GameButton> buttonPage;
        if (title > 0) {
            buttonPage = gameButtonDao.findAllByAmountGreaterThanEqual(title,pageable);
        }else {
            buttonPage = gameButtonDao.findAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (GameButton button : buttonPage.getContent()) {
            array.add(getButton(button));
        }
        JSONObject object = ResponseData.object("total", buttonPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }

    public ResponseData deleteButton(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        List<GameButton> buttons = gameButtonDao.findAllById(ids);
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setStatus(-1);
        }
        gameButtonDao.saveAllAndFlush(buttons);
        return ResponseData.success();
    }

    public ResponseData updateButton(long id, long amount, Double price, int less, long cashInId, int status, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        GameButton button = gameButtonDao.findAllById(id);
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
        gameButtonDao.saveAndFlush(button);
        return ResponseData.success(getButton(button));
    }
    public JSONObject getButton(GameButton button){
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
    public ResponseData addButton(long amount, Double price, int less, long cashInId, int status, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (cashInId > 0){
            CashInConfig config = cashInConfigDao.findAllById(cashInId);
            if (config == null) return ResponseData.error("通道ID不存在");
        }else{
            cashInId = 0;
        }
        GameButton button = new GameButton();
        button.setAmount(amount);
        button.setPrice(price.longValue());
        button.setCashInId(cashInId);
        button.setLess(less);
        button.setStatus(status);
        button.setUpdateTime(System.currentTimeMillis());
        button.setAddTime(System.currentTimeMillis());
        gameButtonDao.saveAndFlush(button);
        return ResponseData.success(getButton(button));
    }

    public ResponseData getButtonConfigList(SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        List<CashInConfig> configs = cashInConfigDao.findAll();
        JSONArray array = new JSONArray();
        for (CashInConfig config: configs) {
            JSONObject object = new JSONObject();
            object.put("id", config.getId());
            object.put("title", config.getTitle());
            object.put("status", config.getStatus());
            array.add(object);
        }
        return ResponseData.success(array);
    }

    public ResponseData getButtonConfig(long id, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        CashInConfig config = null;
        if (id > 0) {
            config = cashInConfigDao.findAllById(id);
            if (config == null) return ResponseData.success(ResponseData.object("result", ""));
        }
        List<CashInOption> options = getAllowed(config);
        StringBuilder sb = new StringBuilder();
        for (CashInOption option: options) {
            sb.append(option.getName()).append(",");
        }
        return ResponseData.success(ResponseData.object("result", sb.toString()));
    }
    public List<GameConfig> getUpdateGameConfig(JSONObject data){
        List<GameConfig> configs = new ArrayList<>();
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
            GameConfig config = gameConfigDao.findByName(key);
            if (config == null){
                config = new GameConfig(key, object.getString(key));
            }else{
                config.setVal(object.getString(key));
                config.setUpdateTime(System.currentTimeMillis());
            }
            configs.add(config);
        }
        return configs;
    }
    public List<GameOutConfig> getUpdateGameWithdrawConfig(JSONObject data){
        List<GameOutConfig> configs = new ArrayList<>();
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
            GameOutConfig config = gameOutConfigDao.findByName(key);
            if (config == null){
                config = new GameOutConfig(key, object.getString(key));
            }else{
                config.setVal(object.getString(key));
                config.setUpdateTime(System.currentTimeMillis());
            }
            configs.add(config);
        }
        return configs;
    }

    public ResponseData getGameConfig(SysUser user, String ip) {
        if (user == null) return ResponseData.error("");
        List<GameConfig> configs = gameConfigDao.findAll();
        JSONObject object = new JSONObject();
        for (GameConfig config: configs) {
            object.put(config.getName(), config.getVal());
        }
        return ResponseData.success(object);
    }

    public ResponseData updateGameConfig(JSONObject data) {
        String u = data.getString("user");
        if (StringUtils.isEmpty(u)) return ResponseData.error("");
        SysUser user = SysUser.getUser(u);
        if (user == null) return ResponseData.error("");
        gameConfigDao.saveAllAndFlush(getUpdateGameConfig(data));
        return ResponseData.success();
    }

    public ResponseData getGameOrderList(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"addTime"));
        Page<GameOrder> orderPage;
        if (StringUtils.isNotEmpty(title)) {
            orderPage = gameOrderDao.findAllByOrderNoLike("%"+title+"%",pageable);
        }else {
            orderPage = gameOrderDao.findAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (GameOrder order : orderPage.getContent()) {
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

    public ResponseData deleteGameOrder(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        List<GameOrder> orders = gameOrderDao.findAllById(ids);
        List<CashInOrder> inOrders = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            CashInOrder order = cashInOrderDao.findAllByOrderNo(orders.get(i).getOrderNo());
            if (order != null){
                order.setStatus(-1);
                inOrders.add(order);
            }
        }
        gameOrderDao.saveAllAndFlush(orders);
        cashInOrderDao.saveAllAndFlush(inOrders);
        return ResponseData.success();
    }
    public ResponseData makeupGameOrder(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        List<GameOrder> orders = gameOrderDao.findAllById(ids);
        List<CashInOrder> inOrders = new ArrayList<>();
        JSONArray array = new JSONArray();
        for (GameOrder order: orders) {
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
            handlerOrder(order.getOrderNo());
        }
        return ResponseData.success(array);
    }

    public ResponseData getGameWithdrawConfig(SysUser user, String ip) {
        if (user == null) return ResponseData.error("");
        List<GameOutConfig> configs = gameOutConfigDao.findAll();
        JSONObject object = new JSONObject();
        for (GameOutConfig config: configs) {
            object.put(config.getName(), config.getVal());
        }
        return ResponseData.success(object);
    }

    public ResponseData updateGameWithdrawConfig(JSONObject data) {
        String u = data.getString("user");
        if (StringUtils.isEmpty(u)) return ResponseData.error("");
        SysUser user = SysUser.getUser(u);
        if (user == null) return ResponseData.error("");
        gameOutConfigDao.saveAllAndFlush(getUpdateGameWithdrawConfig(data));
        return ResponseData.success();
    }

    public ResponseData getGameWithdrawOrder(String title, boolean enabled, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"addTime"));
        Page<GameOutOrder> orderPage;
        if (StringUtils.isNotEmpty(title)) {
            if (enabled){
                orderPage = gameOutOrderDao.findAllByOrderNoLikeAndStatus("%"+title+"%",0,pageable);
            }else{
                orderPage = gameOutOrderDao.findAllByOrderNoLike("%"+title+"%",pageable);
            }
        }else {
            if (enabled){
                orderPage = gameOutOrderDao.findAllByStatus(0,pageable);
            }else{
                orderPage = gameOutOrderDao.findAll(pageable);
            }
        }
        JSONArray array = new JSONArray();
        for (GameOutOrder order : orderPage.getContent()) {
            array.add(getGameOutOrder(order));
        }
        JSONObject object = ResponseData.object("total", orderPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }
    public JSONObject getGameOutOrder(GameOutOrder order){
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(order));
        User u = userDao.findAllById(order.getUserId());
//            json.put("amount", String.format("%.2f", order.getAmount() / 1D));
        json.put("fee", String.format("%.2f", order.getFee()));
        json.put("totalFee", String.format("%.2f", order.getTotalFee()));
        json.put("recentIn",String.format("%.2f", gameOutOrderDao.getRecentIn(order.getId()) / 1D));
        json.put("water",String.format("%.2f", gameOutOrderDao.getRecentWater(order.getId()) / 100D));
        json.put("user","用户不存在");
        if (u!= null) {
            json.put("user", u.getNickname());
        }
        return json;
    }
    public ResponseData makeupGameWithdrawOrder(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        List<GameOutOrder> orders = gameOutOrderDao.findAllById(ids);
        for (int i = 0; i < orders.size(); i++) {
            orders.get(i).setStatus(1);
        }
        gameOutOrderDao.saveAllAndFlush(orders);
        JSONArray array = new JSONArray();
        for (GameOutOrder order : orders) {
            array.add(getGameOutOrder(order));
        }
        return ResponseData.success(array);
    }
    public boolean handlerRefund(GameOutOrder order){
        if (order.getStatus() != 0) return false;
        return WaLiUtil.tranfer(order.getUserId(), order.getAmount() * 100);
    }
    public ResponseData makeDownGameWithdrawOrder(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        List<GameOutOrder> orders = gameOutOrderDao.findAllById(ids);
        for (int i = 0; i < orders.size(); i++) {
            if (handlerRefund(orders.get(i))){
                orders.get(i).setStatus(-1);
                GameFunds fund = new GameFunds(user.getId(), orders.get(i).getAmount() * 100, "提现退回:\n"+orders.get(i).getOrderNo());
                gameFundsDao.save(fund);
            }
        }
        gameOutOrderDao.saveAllAndFlush(orders);
        JSONArray array = new JSONArray();
        for (GameOutOrder order : orders) {
            array.add(getGameOutOrder(order));
        }
        return ResponseData.success(array);
    }

    public ResponseData getGameWithdrawCard(String title, boolean enabled, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"id"));
        Page<GameOutCard> cardPage;
        if (StringUtils.isNotEmpty(title)) {
            if (enabled){
                cardPage = gameOutCardDao.getGameWithdrawCard(new Long(title),pageable);
            }else{
                cardPage = gameOutCardDao.getGameWithdrawCard("%"+title+"%",pageable);
            }
        }else {
            cardPage = gameOutCardDao.findAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (GameOutCard card : cardPage.getContent()) {
            array.add(getGameOutCard(card));
        }
        JSONObject object = ResponseData.object("total", cardPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }

    private JSONObject getGameOutCard(GameOutCard card) {
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(card));
        json.put("user","用户不存在");
        json.put("phone","用户不存在");
        User user = userDao.findAllById(card.getUserId());
        if (user!= null) {
            json.put("user", user.getNickname());
            json.put("phone", user.getPhone());
        }
        return json;
    }

    public ResponseData deleteGameWithdrawCard(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        gameOutCardDao.deleteAllById(ids);
        return ResponseData.success();
    }

    public ResponseData updateGameWithdrawCard(long id, String name, String bank, String card, String address, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (id < 1) return ResponseData.error("用户ID不正确!");
        GameOutCard outCard = gameOutCardDao.findAllById(id);
        if (outCard == null) return ResponseData.error("记录不存在");
        name = name.replaceAll(" ", "").trim();
        bank = bank.replaceAll(" ", "").trim().toUpperCase();
        card = card.replaceAll(" ", "").trim().toUpperCase();
        outCard.setName(name);
        outCard.setBank(bank);
        outCard.setCard(card);
        outCard.setAddress(address);
        outCard.setUpdateIp(ip);
        outCard.setUpdateTime(System.currentTimeMillis());
        gameOutCardDao.save(outCard);
        return ResponseData.success(getGameOutCard(outCard));
    }

    public ResponseData getGameWaterList(String title, int type, long start, long end, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"addTime"));
        Page<User> userPage;
        if (start > System.currentTimeMillis()) start = System.currentTimeMillis();
        if (end > System.currentTimeMillis()) end = System.currentTimeMillis();
        if (StringUtils.isNotEmpty(title)) {
            if (type == 0){
                userPage = userDao.findAllById(new Long(title),pageable);
            }else if (type == 1){
                userPage = userDao.findAllByNickname("%"+title+"%",pageable);
            }else if (type == 2){
                userPage = userDao.findAllByUsername("%"+title+"%",pageable);
            }else if (type == 3){
                userPage = userDao.findAllByPhone("%"+title+"%",pageable);
            }else {
                userPage = userDao.findAll(pageable);
            }
        }else {
            userPage = userDao.findAll(pageable);
        }
//        System.out.println(userPage.getContent());
        JSONArray array = new JSONArray();
        for (User profile : userPage.getContent()) {
            JSONObject object = new JSONObject();
            object.put("id",profile.getId());
            object.put("nickname",profile.getNickname());
            object.put("username",profile.getUsername());
            object.put("phone",profile.getPhone());
            object.put("addTime",profile.getAddTime());

            if (start > 0 && end > 0) {
                object.put("validBet", String.format("%.2f", gameWaterDao.getValidBet(profile.getId(),start,end) / 100D));
                object.put("profit", String.format("%.2f", gameWaterDao.getProfit(profile.getId(),start,end) / 100D));
                object.put("tax", String.format("%.2f", gameWaterDao.getTax(profile.getId(),start,end) / 100D));
            } else if (start > 0){
                object.put("validBet", String.format("%.2f", gameWaterDao.getValidBet(profile.getId(),start) / 100D));
                object.put("profit", String.format("%.2f", gameWaterDao.getProfit(profile.getId(),start) / 100D));
                object.put("tax", String.format("%.2f", gameWaterDao.getTax(profile.getId(),start) / 100D));
            } else if (end > 0){
                object.put("validBet", String.format("%.2f", gameWaterDao.getValidBetByEnd(profile.getId(),end) / 100D));
                object.put("profit", String.format("%.2f", gameWaterDao.getProfitByEnd(profile.getId(),end) / 100D));
                object.put("tax", String.format("%.2f", gameWaterDao.getTaxByEnd(profile.getId(),end) / 100D));
            }else{
                object.put("validBet", String.format("%.2f", gameWaterDao.getValidBet(profile.getId()) / 100D));
                object.put("profit", String.format("%.2f", gameWaterDao.getProfit(profile.getId()) / 100D));
                object.put("tax", String.format("%.2f", gameWaterDao.getTax(profile.getId()) / 100D));
            }
            object.put("cashIn", String.format("%.2f", gameOrderDao.getCashIn(profile.getId()) / 1D));
            object.put("cashOut", String.format("%.2f", gameOutOrderDao.getCashOut(profile.getId()) / 1D));
            object.put("firstTime",gameWaterDao.getFirstTime(profile.getId()));
            object.put("lastTime",gameWaterDao.getLastTime(profile.getId()));
//            System.out.println(object);
            array.add(object);
        }
        JSONObject josn = ResponseData.object("total", userPage.getTotalElements());
        josn.put("list", array);
        return ResponseData.success(josn);
    }
    public ResponseData getGameWater(long userId, long start, long end, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        User u = userDao.findAllById(userId);
        if (u == null) return ResponseData.error("用户不存在!");
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"record_time"));
        Page<GameWater> waterPage;
        if (start > System.currentTimeMillis()) start = System.currentTimeMillis();
        if (end > System.currentTimeMillis()) end = System.currentTimeMillis();

        if (start > 0 && end > 0) {
//            System.out.println(start);
//            System.out.println(end);
            waterPage = gameWaterDao.getGameWaterListById(userId,start,end,pageable);
        } else if (start > 0){
//            System.out.println(start);
            waterPage = gameWaterDao.getGameWaterListById(userId, start,pageable);
        } else if (end > 0){
            waterPage = gameWaterDao.getGameWaterListByIdEnd(userId, end,pageable);
        }else{
            waterPage = gameWaterDao.getGameWaterListById(userId,pageable);
        }
        JSONArray array = new JSONArray();
        for (GameWater water : waterPage.getContent()) {
            array.add(getGameWater(water));
        }
        JSONObject object = ResponseData.object("total", waterPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }
    public JSONObject getGameWater(GameWater water){
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(water));
        json.put("validBet", String.format("%.2f", water.getValidBet() / 100D));
        json.put("profit", String.format("%.2f", water.getProfit() / 100D));
        json.put("tax", String.format("%.2f", water.getTax() / 100D));
        json.put("balance", String.format("%.2f", water.getBalance() / 100D));
        json.put("game","游戏已被下加");
        Game game = gameDao.findAllById(water.getGameId());
        if (game!= null) {
            json.put("game", game.getName());
        }
        return json;
    }

    public ResponseData updateGameWater(SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        WaLiUtil.getRecords();
        return ResponseData.success();
    }

    public ResponseData getGamePublicity(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"addTime"));
        Page<GamePublicity> publicityPage;
        if (StringUtils.isNotEmpty(title)){
            publicityPage = gamePublicityDao.findAllByNameLike("%"+title+"%",pageable);
        }else{
            publicityPage = gamePublicityDao.findAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (GamePublicity publicity : publicityPage.getContent()) {
            array.add(getGamePublicity(publicity));
        }
        JSONObject object = ResponseData.object("total", publicityPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }
    public JSONObject getGamePublicity(GamePublicity publicity){
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(publicity));
        json.put("count", gamePublicityReportDao.countAllByPublicityId(publicity.getId()));
        return json;
    }
    public ResponseData deleteGamePublicity(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        for (Long id : ids){
            gamePublicityReportDao.removeAllById(id);
        }
        return ResponseData.success();
    }

    public ResponseData addGamePublicity(String name, String pic, String url1, int status, int type, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (StringUtils.isEmpty(pic)) return ResponseData.error("图片地址不可空");
        name = name.replaceAll(" ", "").trim().toUpperCase();
        if(StringUtils.isEmpty(url1)) url1=pic;
        GamePublicity publicity = new GamePublicity();
        publicity.setName(name);
        publicity.setPic(pic);
        publicity.setUrl(url1);
        publicity.setStatus(status);
        publicity.setType(type);
        publicity.setAddTime(System.currentTimeMillis());
        publicity.setUpdateTime(System.currentTimeMillis());
        gamePublicityDao.save(publicity);
        return ResponseData.success(getGamePublicity(publicity));
    }

    public ResponseData updateGamePublicity(long id, String name, String pic, String url, int status, int type, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if(id < 1) return ResponseData.error("记录不存在!");
        GamePublicity publicity = gamePublicityDao.findAllById(id);
        if (publicity == null) return ResponseData.error("记录不存在");
        if (StringUtils.isEmpty(pic)) return ResponseData.error("图片地址不可空");
        name = name.replaceAll(" ", "").trim().toUpperCase();
        if(StringUtils.isEmpty(url)) url=pic;
        publicity.setName(name);
        publicity.setPic(pic);
        publicity.setUrl(url);
        publicity.setStatus(status);
        publicity.setType(type);
        if (publicity.getAddTime() == 0)publicity.setAddTime(System.currentTimeMillis());
        publicity.setUpdateTime(System.currentTimeMillis());
        gamePublicityDao.save(publicity);
        return ResponseData.success(getGamePublicity(publicity));
    }

    public ResponseData getGameScroll(String title, int page, int limit, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        page--;
        if (page < 0) page = 0;
        if (limit < 0) limit = 20;
        Pageable pageable = PageRequest.of(page,limit, Sort.by(Sort.Direction.DESC,"addTime"));
        Page<GameScroll> scrollPage;
        if (StringUtils.isNotEmpty(title)){
            scrollPage = gameScrollDao.findAllByNameLike("%"+title+"%",pageable);
        }else{
            scrollPage = gameScrollDao.findAll(pageable);
        }
        JSONArray array = new JSONArray();
        for (GameScroll scroll : scrollPage.getContent()) {
            array.add(getGameScroll(scroll));
        }
        JSONObject object = ResponseData.object("total", scrollPage.getTotalElements());
        object.put("list", array);
        return ResponseData.success(object);
    }
    public JSONObject getGameScroll(GameScroll scroll ){
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(scroll));
        return object;
    }

    public ResponseData deleteGameScroll(List<Long> ids, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        gameScrollDao.deleteAllById(ids);
        return ResponseData.success();
    }

    public ResponseData updateGameScroll(long id, String name, long amount, String game, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        if (id < 1) return ResponseData.error("记录不存在");
        GameScroll gameScroll = gameScrollDao.findAllById(id);
        if (gameScroll == null) return ResponseData.error("记录不存在");
        name = name.replaceAll(" ", "").trim();
        game = game.replaceAll(" ","").trim();
        gameScroll.setName(name);
        gameScroll.setAmount(amount);
        gameScroll.setGame(game);
        if (gameScroll.getAddTime() == 0) gameScroll.setAddTime(System.currentTimeMillis());
        gameScrollDao.save(gameScroll);
        return ResponseData.success(getGameScroll(gameScroll));
    }

    public ResponseData addGameScroll(String name, long amount, String game, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        name = name.replaceAll(" ", "").trim();
        game = game.replaceAll(" ","").trim();
        GameScroll gameScroll = new GameScroll(name,amount,game);
        gameScrollDao.save(gameScroll);
        return ResponseData.success(getGameScroll(gameScroll));
    }

    public ResponseData automaticGameScroll(long amount, SysUser user, String ip) {
        if (user == null) return ResponseData.error(201);
        return ResponseData.error("暂未开发");
    }
}
