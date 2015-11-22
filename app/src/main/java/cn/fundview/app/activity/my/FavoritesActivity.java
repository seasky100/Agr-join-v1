package cn.fundview.app.activity.my;

import android.content.Intent;
import android.os.Bundle;

import cn.fundview.R;
import cn.fundview.app.action.my.FavoriteAchvListAction;
import cn.fundview.app.action.my.FavoriteRequListAction;
import cn.fundview.app.activity.ABaseActivity;
import cn.fundview.app.domain.model.Favorite;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.tool.DensityUtil;
import cn.fundview.app.view.my.FavoritesView;

/**
 * 我的收藏 activity
 */
public class FavoritesActivity extends ABaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        int type = getIntent().getIntExtra("favorite-type", 1);//1我收藏的成果  2我收藏的需求
        if (type == 1) {

            this.setCommonTitleBar("我收藏的成果", R.id.webView, null, false);
        } else if (type == 2) {

            this.setCommonTitleBar("我收藏的需求", R.id.webView, null, false);
        }
    }

    ;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Favorite.FAVORITE_ACHV_TYPE) {

            //更新成果页面
            int pageSize = data.getIntExtra("pageSize", 10);
            int page = data.getIntExtra("page", 1);

            FavoriteAchvListAction action = new FavoriteAchvListAction(this, (FavoritesView) this.findViewById(R.id.webView));
            action.execute(page, pageSize, true);
        } else if (requestCode == Favorite.FAVORITE_REQU_TYPE) {

            //更新需求页面
            //更新成果页面
            int pageSize = data.getIntExtra("pageSize", 10);
            int page = data.getIntExtra("page", 1);

            FavoriteRequListAction action = new FavoriteRequListAction(this, (FavoritesView) this.findViewById(R.id.webView));
            action.execute(page, pageSize, true);
        }
    }
}
