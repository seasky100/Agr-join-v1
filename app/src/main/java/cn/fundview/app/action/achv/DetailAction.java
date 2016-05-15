package cn.fundview.app.action.achv;

import android.content.Context;

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
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.Favorite;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.tool.DensityUtil;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.file.FileTools;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * 成果详细action
 * <p/>
 * 项目名称：agr-join-v2.0.0 类名称：DetailAction 类描述： 创建人：lict 创建时间：2015年6月10日
 * 上午10:39:31 修改人：lict 修改时间：2015年6月10日 上午10:39:31
 * 修改备注：
 * 返回的json
 * {
 * message: "查询成功",
 * result: {
 * acceptNo: "",
 * achvCxInfo: "333",
 * applyCompName: "未应用",
 * evaluateOrgName: "未评估",
 * gyiHjie: "清洗环节",
 * id: 735,
 * imgRes: [
 * {
 * id: 1305,
 * title: "2",
 * url: "http://192.168.1.10:8080/static/thumb/achv/img/20151025//2015102521142683958_160_120.jpg"
 * },
 * {
 * id: 1300,
 * title: "body_bg",
 * url: "http://192.168.1.10:8080/static/thumb/achv/img/20151025//201510251912449971_160_120.png"
 * }
 * ],
 * majorProblemKey: "33",
 * majorProblemValue: "333",
 * materialNames: "籼稻",
 * materialTypeNames: "稻谷",
 * name: "44444433",
 * otherGyiHjie: "清洗环节",
 * prodTypeNames: "小麦粉",
 * productNames: "小麦粉(通用、专用)",
 * publisherId: 16,
 * publisherName: "河北百丰农产品开发有限公司",
 * publisherType: "1",
 * techJd: "未填写",
 * tradeNames: "农副食品加工业",
 * updateDate: 1445853911000,
 * yjstatus: "拟研",
 * zlNo: "",
 * zrPrice: 0,
 * zrWay: "未填写"
 * },
 * status: 2
 * }
 */
public class DetailAction extends ABaseAction {

    private int achvId;
    private String lastModify;
    private boolean isFavorite;

    // result
    private Map<String, String> detail = null;
    private List<String> imgList;

    public DetailAction(Context context, ABaseWebView webView) {
        super(context, webView);
        // TODO Auto-generated constructor stub
    }

    public void execute(int achvId, String lastModify) {

        this.achvId = achvId;
        this.lastModify = lastModify;

        handle(true);

        this.showWaitDialog();
    }

    @Override
    protected void doAsynchHandle() {
        // TODO Auto-generated method stub
        super.doAsynchHandle();
        // 根据有无内存卡判断
        String drivePath = "";
        if (DeviceConfig.isExistExtendStorage()) {
            drivePath = DeviceConfig.getExtendStoragePath(context);
        } else {
            drivePath = DeviceConfig.getLocalStoragePath(context);
        }

        // 成果详细json的存放路径
        String achvSavePath = drivePath + cn.fundview.app.tool.Constants.ACHV_JSON_DIR + achvId + "/";//
        String fileName = lastModify + ".json";//

        //设置参数
        Map<String, String> param = new HashMap<String, String>();
        param.put("id", String.valueOf(achvId));

        if (!FileTools.isFileExist(achvSavePath + fileName)) {

            // 删除就文件 指定文件夹下面的文件
            FileTools.delFile(achvSavePath);
            // 需要更新下载
            ResultBean resultBean = null;
            try {
                resultBean = JSONTools.parseResult(RService.doPostSync(param, Constants.GET_ACHV_DETAIL));

                if (resultBean != null && resultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                    //保存文件
                    InputStream is = new ByteArrayInputStream(resultBean.getResult().getBytes());
                    FileTools.saveDownFile(achvSavePath, fileName, is);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        parseJson(achvSavePath + fileName);

        isFavorite = null != DaoFactory.getInstance(context).getFavoriteDao().findFavoriteByFavoriteIdAndFavoriteType(achvId, Favorite.FAVORITE_ACHV_TYPE);

        //解析成果图片
        if (detail != null) {
            String imgRes = detail.get("imgRes");
            if (imgRes != null && !imgRes.trim().equals("")) {

                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(String.valueOf(imgRes));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (imgList == null) {

                            imgList = new ArrayList<>();
                        }
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        imgList.add(jsonObject.getString("url"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void doHandleResult() {
        // TODO Auto-generated method stub
        super.doHandleResult();

        String js = "";
        if (imgList != null && imgList.size() > 0) {

            for (String path : imgList) {

                if (path != null && path.trim() != "") {
                    js = JsMethod.createJs("javascript:Page.addImg(${path});", path);
                    System.out.println(js);
                    webView.loadUrl(js);
                }
            }

            js = JsMethod.createJs("javascript:Page.initImg(${imgSize},${height});", imgList.size(), (int) (DensityUtil.px2dip(context, webView.getWidth()) * 0.75));
            System.out.println(js);
            webView.loadUrl(js);

            String jss = JsMethod.createJs("javascript:Page.initSwiper();");
            System.out.println(jss);
            webView.loadUrl(jss);
        } else {

            webView.loadUrl("javascript:Page.noImg();");
        }

        if (detail != null) {

            //id, title, no, price, researchStatus, isFavorite,ownerName,zrType
            detail.put("isFavorite", isFavorite ? "1" : "0");
            js = JsMethod.createJsWithJsonItems(
                    "javascript:Page.init(${id}, ${name}, ${no}, ${zrPrice}, ${yjstatus}, ${isFavorite}, ${publisherName}, ${publisherId}, ${publisherType}, ${zrWay});", detail);

            webView.loadUrl(js);

            String otherHj = detail.get("otherGyiHjie");
            if (otherHj != null && otherHj.trim() != "") {

                detail.put("gyiHjie", detail.get("gyiHjie") + "," + otherHj);
            }

            // 针对性,适用性和成熟度 imaterial, materialType, prod, prodType, trade, hj, problem, synbal, cxDesc, jd, apply, evaluate, patent
            js = JsMethod.createJsWithJsonItems("javascript:Page.initBasic(${materialNames}, ${materialTypeNames}, ${productNames}, ${prodTypeNames}, ${tradeNames}, ${gyiHjie}, ${majorProblemKey}," +
                            "${majorProblemValue}, ${achvCxInfo}, ${techJd}, ${applyCompName}, ${evaluateOrgName}, ${zlNo})",
                    detail);
            webView.loadUrl(js);

            // 价值体现 invest, social, hardware, gy
            js = JsMethod.createJsWithJsonItems("javascript:Page.initValueEmbodiment(${investGs}, ${economyDesc}, ${hardwareDesc}, ${gyiHjieDesc});",
                    detail);
            webView.loadUrl(js);

        }


        this.closeWaitDialog();

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
