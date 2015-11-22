package cn.fundview.app.action.company;

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
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.Favorite;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.tool.DensityUtil;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.FileTools;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * @author dell 企业产品详细信息action
 *         <p/>
 *         备注:
 *         返回的json
 *         {
 *         message: "查询成功",
 *         result: {
 *         compId: 16,
 *         createDate: 1445771677000,
 *         id: 411,
 *         intro: "22",
 *         logo: [
 *         "http://192.168.1.10:8080/static/thumb/comp/product/20151025//2015102519290298910.jpg",
 *         "http://192.168.1.10:8080/static/thumb/comp/product/20151025//2015102519143471348.jpg",
 *         "http://192.168.1.10:8080/static/thumb/comp/product/20151025//2015102519140179682.jpg"
 *         ],
 *         materialDesc: "22",
 *         name: "212",
 *         price: "22",
 *         techDesc: "2",
 *         unit: "22",
 *         updateDate: 1445772544000
 *         },
 *         status: 2
 *         }
 */
public class ProductDetailAction extends ABaseAction {

    /**
     * 参数
     */
    private Integer productId;
    private String lastModify;
    private List<String> imgList;
    /**
     * 处理结果
     */
    private Map<String, String> detail = null;

    public ProductDetailAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(Integer productId, String lastModify) {

        this.productId = productId;
        this.lastModify = (lastModify == null || lastModify == "") ? "0" : lastModify;
        handle(true);

        this.showWaitDialog();
    }

    /**
     * 执行异步处理
     **/
    @Override
    protected void doAsynchHandle() {

        // 根据有无内存卡判断
        String drivePath = "";
        if (DeviceConfig.isExistExtendStorage()) {
            drivePath = DeviceConfig.getExtendStoragePath(context);
        } else {
            drivePath = DeviceConfig.getLocalStoragePath(context);
        }

        // 企业产品详细json的存放路径
        String compSavePath = drivePath + cn.fundview.app.tool.Constants.COMPANY_PRODUCT_JSON_DIR + productId + "/";//
        String fileName = lastModify + ".json";//

        //设置参数
        Map<String, String> param = new HashMap<String, String>();
        param.put("id", String.valueOf(productId));

        if (!FileTools.isFileExist(compSavePath + fileName)) {

            // 删除就文件 指定文件夹下面的文件
            FileTools.delFile(compSavePath);
            // 需要更新下载
            ResultBean resultBean = null;
            try {
                resultBean = JSONTools.parseResult(RService.doPostSync(param, Constants.GET_COMP_PRODUCT_DETAIL_URL));

                if (resultBean != null && resultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                    //保存文件
                    InputStream is = new ByteArrayInputStream(resultBean.getResult().getBytes());
                    FileTools.saveDownFile(compSavePath, fileName, is);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        parseJson(compSavePath + fileName);

        //解析产品图片
        if (detail != null) {
            String imgRes = detail.get("logo");
            if (imgRes != null && !imgRes.trim().equals("")) {

                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(String.valueOf(imgRes));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (imgList == null) {

                            imgList = new ArrayList<>();
                        }
                        imgList.add((String) jsonArray.opt(i));
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

        String js = "";
        if (imgList != null && imgList.size() > 0) {


            js = JsMethod.createJs("javascript:Page.initImg(${imgSize},${height});",
                    imgList.size(), (int) (DensityUtil.px2dip(context, webView.getWidth()) * 0.75));
            webView.loadUrl(js);

            for (String path : imgList) {

                if (path != null && path.trim() != "") {
                    js = JsMethod.createJs("javascript:Page.addImg(${path});", path);
                    webView.loadUrl(js);
                }
            }
            js = JsMethod.createJs("javascript:Page.initSwiper();");
            webView.loadUrl(js);
        } else {

            js = JsMethod.createJs("javascript:Page.initImg(${imgSize},${height});",
                    0, (int) (DensityUtil.px2dip(context, webView.getWidth()) * 0.75));
            webView.loadUrl(js);
        }


        if (detail != null) {

            js = JsMethod.createJsWithJsonItems(
                    "javascript:Page.init(${id}, ${name}, ${unit}, ${price}, ${materialDesc}, ${techDesc}, ${intro});", detail);
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
