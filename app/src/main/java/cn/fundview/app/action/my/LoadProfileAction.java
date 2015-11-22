package cn.fundview.app.action.my;

import android.content.Context;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.UserInfor;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.StringUtils;
import cn.fundview.app.view.ABaseWebView;

/**
 * 加载当前用户的信息
 * <p/>
 * 项目名称：agr-join-v2.0.0
 * 类名称：LoadProfileAction
 * 类描述：
 * 创建人：lict
 * 创建时间：2015年7月24日 下午3:35:07
 * 修改人：lict
 * 修改时间：2015年7月24日 下午3:35:07
 * 修改备注：
 */
public class LoadProfileAction extends ABaseAction {

    /**
     * 参数
     **/
    private String attr; //对应了model 的属性名
    private String callback;
    private int uid;
    private boolean editable;//是否允许编辑

    /**
     * 处理结果
     ***/
    private String result;

    public LoadProfileAction(Context context, ABaseWebView webView) {
        super(context, webView);
    }

    public void execute(String attr, String callback, int uid, boolean editable) {

        this.attr = attr;
        this.uid = uid;
        this.callback = callback;
        this.editable = editable;
        handle(false);
    }

    /**
     * 执行同步处理
     **/
    @Override
    protected void doHandle() {

        UserInfor profile = DaoFactory.getInstance(context).getUserInforDao().getById(uid);

        if (UserInfor.NAME.equals(attr)) {

            // 取名字
            result = profile.getName();
        } else if (UserInfor.AREA.equals(attr)) {

            //取地区
            result = profile.getArea();
        } else if (UserInfor.ADDR.equals(attr)) {

            //取地址
            result = profile.getAddr();
            result = StringUtils.checkNull(result, "暂未填写");
        } else if (UserInfor.TEL.equals(attr)) {

            //取电话
            result = profile.getTel();
            result = StringUtils.checkNull(result, "暂未填写");
        } else if (UserInfor.PROFESSIONAL_TITLE.equals(attr)) {

            //专家职称
            result = profile.getProfessionalTitle();
            result = StringUtils.checkNull(result, "暂未填写");
        } else if (UserInfor.LINKMAN.equals(attr)) {

            //取电话
            result = profile.getLinkMan();
            result = StringUtils.checkNull(result, "暂未填写");
        } else if (UserInfor.SERVICE.equals(attr)) {

            result = profile.getService();
            result = StringUtils.checkNull(result, "暂未填写");
        } else if (UserInfor.WORK.equals(attr)) {

            result = profile.getWork();
            result = StringUtils.checkNull(result, "暂未填写");
        } else if (UserInfor.SPECIALTY.equals(attr)) {

            result = profile.getSpecialty();
            result = StringUtils.checkNull(result, "暂未填写");
        }

    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {
        if (callback != null && !callback.trim().equals("")) {
            String js = JsMethod.createJs("javascript:" + callback
                    + "(${result},${editable});", result, editable);
            webView.loadUrl(js);
        }
    }

}
