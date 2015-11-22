package cn.fundview.app.action;

import android.content.Context;

import cn.fundview.app.activity.ABaseActivity;
import cn.fundview.app.view.ABaseWebView;

public abstract class ABaseAction {

    protected ABaseWebView webView;
    protected Context context;

    public ABaseAction(Context context, ABaseWebView webView) {

        this.context = context;
        this.webView = webView;
    }

    protected void handle(boolean asynch) {

        try {

            if (asynch) {

                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        doAsynchHandle();
                        handleResult();
                    }
                }).start();

            } else {

                doHandle();
                handleResult();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    protected void handleResult() {

        ((ABaseActivity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {

                try {

                    doHandleResult();
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 同步处理
     **/
    protected void doHandle() {

    }

    /**
     * 异步处理
     **/
    protected void doAsynchHandle() {

    }

    /**
     * 执行处理结果
     **/
    protected void doHandleResult() {

    }

    protected void showWaitDialog() {


        ((ABaseActivity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {

                webView.loadUrl("javascript:Page.showLoading()");
            }
        });

    }

    protected void closeWaitDialog() {

        ((ABaseActivity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {

                webView.loadUrl("javascript:Page.hideLoading()");
            }
        });
    }
}
