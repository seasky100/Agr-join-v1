package cn.fundview.app.action.achv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.activity.expert.DetailActivity;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.model.Company;
import cn.fundview.app.domain.model.Expert;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.tool.FileTools;
import cn.fundview.app.tool.JsMethod;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.ABaseWebView;

/**
 * 成果拥有者 action
 * 拥有者可以是企业,可以是专家
 */
public class OwnerAction extends ABaseAction {

    private int ownerId;
    private int ownerType;
    private String ownerName;
    // result
    private Map<String, String> detail = null;

    public OwnerAction(Context context, ABaseWebView webView) {
        super(context, webView);
        // TODO Auto-generated constructor stub
    }

    public void execute(int ownerId, int ownerType, String ownerName) {

        this.ownerId = ownerId;
        this.ownerType = ownerType;
        this.ownerName = ownerName;

        handle(false);
    }

    @Override
    protected void doHandle() {
        super.doHandle();

        if (this.ownerType == 2) {

            //拥有者是专家
            Intent intent = new Intent(context, DetailActivity.class);
            Expert expert = DaoFactory.getInstance(context).getExpertDao().getById(ownerId);
            if (expert != null) {

                intent.putExtra("lastModify", expert.getUpdateDate());
            } else {

                intent.putExtra("lastModify", 0);
            }
            intent.putExtra("expertName", ownerName);
            intent.putExtra("expertId", ownerId);
            context.startActivity(intent);
        } else if (this.ownerType == 1) {

            Intent intent = new Intent(context, cn.fundview.app.activity.company.DetailActivity.class);
            intent.putExtra("compName", ownerName);
            intent.putExtra("compId", ownerId);
            Company company = DaoFactory.getInstance(context).getCompDao().getById(ownerId);
            if (company != null) {

                intent.putExtra("lastModify", company.getUpdateDate() + "");
            } else {

                intent.putExtra("lastModify", "0");
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void doHandleResult() {
        // TODO Auto-generated method stub
        super.doHandleResult();

//        this.closeWaitDialog();
    }


}
