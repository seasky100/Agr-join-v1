package cn.fundview.app.action.requ;

import android.content.Context;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.Favorite;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.file.FileTools;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * 技术需求详细action
 * <p/>
 * 项目名称：agr-join-v2.0.0 类名称：DetailAction 类描述： 创建人：lict 创建时间：2015年6月10日
 * 上午10:47:03 修改人：lict 修改时间：2015年6月10日 上午10:47:03 修改备注：
 */
public class DetailAction extends ABaseAction {

    private int requId;
    private String lastModify;
    private boolean isFavorite;


    //result
    private Map<String, String> detail;

    public DetailAction(Context context, ABaseWebView webView) {
        super(context, webView);
        // TODO Auto-generated constructor stub
    }

    public void execute(int requId, String lastModify) {

        this.requId = requId;
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

        // 需求详细json的存放路径
        String requSavePath = drivePath + cn.fundview.app.tool.Constants.REQU_JSON_DIR + requId + "/";//
        String fileName = lastModify + ".json";//

        //设置参数
        Map<String, String> param = new HashMap<>();
        param.put("id", String.valueOf(requId));
        if (!FileTools.isFileExist(requSavePath + fileName)) {

            // 删除就文件 指定文件夹下面的文件
            FileTools.delFile(requSavePath);
            // 需要更新下载
            ResultBean resultBean = null;
            try {
                resultBean = JSONTools.parseResult(RService.doPostSync(param, Constants.GET_REQU_DETAIL));

                if (resultBean != null && resultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                    //保存文件
                    InputStream is = new ByteArrayInputStream(resultBean.getResult().getBytes());
                    FileTools.saveDownFile(requSavePath, fileName, is);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        parseJson(requSavePath + fileName);

        isFavorite = null != DaoFactory.getInstance(context).getFavoriteDao().findFavoriteByFavoriteIdAndFavoriteType(requId, Favorite.FAVORITE_REQU_TYPE);
    }

    @Override
    protected void doHandleResult() {
        // TODO Auto-generated method stub
        super.doHandleResult();

        if (detail != null) {
            detail.put("isFavorite", isFavorite ? "1" : "0");
            //id, title, price, isFavorite, ownerName,ownerId, hzType
            String js = JsMethod.createJsWithJsonItems(
                    "javascript:Page.init(${id}, ${name}, ${finPlan}, ${isFavorite}, ${compName}, ${compId}, ${hzfs});", detail);

            webView.loadUrl(js);
            String requHj = detail.get("requHj") == null ? "" : detail.get("requHj");
            String otherRequHj = detail.get("otherRequHj") == null ? "" : detail.get("otherRequHj");
            detail.put("requHj", requHj + otherRequHj);
            // 针对性,适用性和成熟度 material, materialType, prod, prodType, hj, problem, symbal, cxDesc, other
            js = JsMethod.createJsWithJsonItems("javascript:Page.initBasic(${materialNames}, ${materialTypeNames}, ${productNames}, ${prodTypeNames}, " +
                            "${requHj}, ${target}," +
                            "${intro},${achvInnovate},${otherRequ})",
                    detail);
            webView.loadUrl(js);
        }

        this.closeWaitDialog();
    }

    /**
     * 指定下载文件的操作
     *
     * @param tools    下载文件的工具类,用于发送请求,处理响应
     * @param requId   需求id
     * @param savaPath xml 数据文件的保存路径
     * @param fileName xml 文件的名称
     */
    private boolean downJson(FileTools tools, Integer requId, String savaPath, String fileName) {

        boolean result = false;// 下载结果
        try {
            Map<String, String> textParams = new HashMap<String, String>();
            textParams.put("pid", String.valueOf(requId));
            tools.setTextParams(textParams);
            result = FileTools.saveDownFile(savaPath, fileName, tools.doGet(Constants.GET_REQU_DETAIL));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
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
