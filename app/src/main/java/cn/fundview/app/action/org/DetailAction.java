package cn.fundview.app.action.org;

import android.app.Activity;
import android.content.Context;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.AchvDao;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.Achv;
import cn.fundview.app.domain.model.Expert;
import cn.fundview.app.domain.model.Org;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.FileTools;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.NetWorkConfig;
import cn.fundview.app.tool.PreferencesUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * 科研机构 json 文件存放在 /fundview/data/org/json/专家id/专家信息更新时间.json 文件中
 * <p/>
 * 项目名称：agr-join-v2.0.0 类名称：DetailAction 类描述： 创建人：lict 创建时间：2015年6月10日
 * 上午10:13:45 修改人：lict 修改时间：2015年6月10日 上午10:13:45 修改备注：
 */
public class DetailAction extends ABaseAction {

    /**
     * 参数
     */
    private Integer orgId;
    private String lastModify;

    /**
     * 处理结果
     */
    private Map<String, String> detail = null;
    private List<Expert> expertList;

    public DetailAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(Integer orgId, String lastModify) {

        this.orgId = orgId;
        this.lastModify = lastModify;
        handle(true);

        this.showWaitDialog();
    }

    /**
     * 执行异步处理
     **/
    @Override
    protected void doAsynchHandle() {

        // 根据有无内存卡判断
        String drivePath;
        if (DeviceConfig.isExistExtendStorage()) {
            drivePath = DeviceConfig.getExtendStoragePath(context);
        } else {
            drivePath = DeviceConfig.getLocalStoragePath(context);
        }

        // 专家详细json的存放路径
        String orgSavePath = drivePath + cn.fundview.app.tool.Constants.ORG_JSON_DIR + orgId + "/";//
        String fileName = lastModify + ".json";//

        //设置参数
        Map<String, String> param = new HashMap<>();
        param.put("accountId", String.valueOf(orgId));

        if (!FileTools.isFileExist(orgSavePath + fileName)) {

            // 删除就文件 指定文件夹下面的文件
            FileTools.delFile(orgSavePath);
            // 需要更新下载
            ResultBean orgResultBean;
            try {
                orgResultBean = JSONTools.parseResult(RService.doPostSync(param, Constants.GET_ORG_DETAIL));

                if (orgResultBean != null && orgResultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                    //保存文件
                    InputStream is = new ByteArrayInputStream(orgResultBean.getResult().getBytes());
                    FileTools.saveDownFile(orgSavePath, fileName, is);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        parseJson(orgSavePath + fileName);

        if (lastModify.equals("0")) {

            //该机构不再企业列表中,修改存储的json 文件名
            File file = new File(orgSavePath + fileName);   //指定文件名及路径
            String name = orgSavePath + detail.get("updateDate") + ".json";
            file.renameTo(new File(name));   //改名
        }

        //查询机构专家
        if (detail != null) {

            String experts = detail.get("experts");
            if (experts != null && !experts.trim().equals("")) {

                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(String.valueOf(experts));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (expertList == null) {

                            expertList = new ArrayList<>();
                        }
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        Expert expert = new Expert();
                        try {
                            if (jsonObject.has("accountId"))
                                expert.setId(jsonObject.getInt("accountId"));
                            if (jsonObject.has("logo"))
                                expert.setLogo(jsonObject.getString("logo"));
                            if (jsonObject.has("name"))
                                expert.setName(jsonObject.getString("name"));

                            expert.setUpdateDate(jsonObject.getLong("updateDate"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        expertList.add(expert);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {


        if (detail != null) {


            webView.loadUrl("javascript:Page.setTitlebar('"+detail.get("name")+"')");

            String js = JsMethod.createJsWithJsonItems("javascript:Page.init(${accountId}, ${logo}, ${name}, ${auth}, ${expoNo}, ${tel}, ${addr});",
                    detail);
            webView.loadUrl(js);


            // 显示机构服务范围
            js = JsMethod.createJsWithJsonItems("javascript:Page.addService(${service})", detail);
            webView.loadUrl(js);

            // 显示机构介绍
            String intro = detail.get("intro");
            boolean introIsMore = false;
            if (intro != null) {

                introIsMore = intro.length() > 60;
                if (introIsMore)
                    intro = intro.substring(0, 60);
            }
            js = JsMethod.createJs("javascript:Page.addInfo(${service}, ${isMore}, ${id}, ${name}, ${updateDate})", intro, introIsMore, orgId, detail.get("name"), detail.get("updateDate"));
            webView.loadUrl(js);
            this.closeWaitDialog();

            if (expertList != null && expertList.size() > 0) {

                for (Expert item : expertList) {

                    js = JsMethod.createJs("javascript:Page.addExpertImg(${id}, ${name}, ${lastModify}, ${path});",
                            item.getId(), item.getName(), item.getUpdateDate(), item.getLogo());
                    System.out.println(js);
                    webView.loadUrl(js);

                    js = JsMethod.createJs("javascript:Page.initSwiper();");
                    webView.loadUrl(js);
                }
            } else {

                //如果没有专家
                webView.loadUrl("javascript:Page.noExpert();");
            }
        }


    }

    // 解析Json 文件
    private void parseJson(String filePath) {

        try {

            detail = JSONTools.parseJsonFile(new File(filePath));
            System.out.println(detail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
