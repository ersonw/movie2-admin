package com.example.movie2admin.service;

import com.example.movie2admin.dao.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.movie2admin.data.ResponseData;
import com.example.movie2admin.entity.*;
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
        GameFunds fund = new GameFunds(user.getId(), order.getAmount() * 100, "在线充值");
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
}
