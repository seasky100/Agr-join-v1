package cn.fundview.app.action.msg;

import android.content.Context;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.file.FileTools;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

public class FundviewInforDetailAction extends ABaseAction {

    /**
     * 参数
     */
    private int inforId;
    private String updateDate;
    /**
     * 处理结果
     */
    // result
    private Map<String, String> detail = null;

    public FundviewInforDetailAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    /**
     * 执行action流程
     */
    public void execute(int inforId, String updateDate) {

        this.inforId = inforId;
        this.updateDate = updateDate;
//
        handle(true);
        this.showWaitDialog();
    }

    /**
     * 执行异步处理
     */
    protected void doAsynchHandle() {

        super.doAsynchHandle();
        // 根据有无内存卡判断
        String drivePath = "";
        if (DeviceConfig.isExistExtendStorage()) {
            drivePath = DeviceConfig.getExtendStoragePath(context);
        } else {
            drivePath = DeviceConfig.getLocalStoragePath(context);
        }

        // 新闻详细json的存放路径
        String inforSavePath = drivePath + cn.fundview.app.tool.Constants.INFORS_JSON_DIR + inforId + "/";//
        String fileName = updateDate + ".json";//

        //设置参数
        Map<String, String> param = new HashMap<String, String>();
        param.put("currentId", String.valueOf(inforId));

        if (!FileTools.isFileExist(inforSavePath + fileName)) {

            // 删除就文件 指定文件夹下面的文件
            FileTools.delFile(inforSavePath);
            // 需要更新下载
            ResultBean resultBean = null;
            try {
                resultBean = JSONTools.parseResult(RService.doPostSync(param, Constants.GET_FUNDVIEW_INFOR_DETAIL_URL));

                if (resultBean != null && resultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                    //保存文件
                    InputStream is = new ByteArrayInputStream(resultBean.getResult().getBytes());
                    FileTools.saveDownFile(inforSavePath, fileName, is);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        parseJson(inforSavePath + fileName);
    }

    /**
     * 处理结果
     */
    protected void doHandleResult() {

        super.doHandleResult();
        String js = "";
        if (detail != null) {

            js = JsMethod.createJsWithJsonItems("javascript:Page.initPage(${id}, ${title}, ${updateDate}, ${content}, ${imgUrl});", detail);
            webView.loadUrl(js);
            System.out.println(js);
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
