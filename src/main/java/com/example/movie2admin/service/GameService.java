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
            orderPage = gameOrderDao.findAllByOrderNo("%"+title+"%",pageable);
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
}
