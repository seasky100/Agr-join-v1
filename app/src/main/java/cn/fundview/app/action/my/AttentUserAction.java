package cn.fundview.app.action.my;

import android.content.Context;
import android.content.Intent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.activity.my.LoginActivity;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.AttentUser;
import cn.fundview.app.domain.model.Company;
import cn.fundview.app.domain.model.Expert;
import cn.fundview.app.domain.model.UserInfor;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.PreferencesUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * 关注和取消关注用户
 */
public class AttentUserAction extends ABaseAction {

    /**
     * 参数
     */
    private int attentStatus;//关注状态  1关注  2取消关注
    private int type;//被关注方 的类型
    private int beAttentId;//被关注id

    /**
     * 处理结果
     **/
    private ResultBean resultBean;//结果
    private boolean result = false;

    public AttentUserAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    /**
     * 1为关注 2为取消关注
     **/
    public void execute(int type, int beAttentId, int attentStatus) {

        this.type = type;
        this.beAttentId = beAttentId;
        this.attentStatus = attentStatus;

        handle(true);
    }

    /**
     * 执行异步处理
     */
    protected void doAsynchHandle() {

        // 先得到用户的唯一码
        Integer accountId = PreferencesUtils.getInt(context, Constants.ACCOUNT_ID);//当前登录用户的id

        if (accountId != null) {

            //attentId=4&beAttentId=3859
            Map<String, String> param = new HashMap<>();
            param.put("attentId", accountId + "");
            param.put("beAttentId", beAttentId + "");
            if (this.attentStatus == 1) {

                //取消关注,请求server
                try {
                    resultBean = JSONTools.parseResult(RService.doPostSync(param, cn.fundview.app.domain.webservice.util.Constants.CANCEL_ATTENT_URL));

                    if (resultBean != null && resultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                        DaoFactory.getInstance(context).getAttentUserDao().deleteAttentByAccoundIdAndBeAttentId(accountId, this.beAttentId);
                        result = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {


                try {
                    resultBean = JSONTools.parseResult(RService.doPostSync(param, cn.fundview.app.domain.webservice.util.Constants.ATTENT_URL));

                    if (resultBean != null && resultBean.getStatus() == cn.fundview.app.domain.webservice.util.Constants.REQUEST_SUCCESS) {

                        //添加关注
                        AttentUser attentUser = new AttentUser();
                        attentUser.setAttentType(PreferencesUtils.getInt(context, Constants.ACCOUNT_TYPE_KEY));
                        attentUser.setBeAttentType(this.type);
                        attentUser.setAttentId(accountId);
                        attentUser.setAttentTime(new Date().getTime());
                        attentUser.setBeAttentId(this.beAttentId);
                        DaoFactory.getInstance(context).getAttentUserDao().save(attentUser);
                        result = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {

            //没有登录
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }

    }

    /**
     * 处理结果
     **/
    protected void doHandleResult() {


        String js = "";
        if (this.attentStatus == 1) {

            js = JsMethod.createJs("javascript:Page.showHintDialog(${message},${result},${type});", "取消关注成功", result, this.attentStatus);
        } else {

            js = JsMethod.createJs("javascript:Page.showHintDialog(${message},${result},${type});", "关注成功", result, this.attentStatus);
        }
        webView.loadUrl(js);
    }
}
