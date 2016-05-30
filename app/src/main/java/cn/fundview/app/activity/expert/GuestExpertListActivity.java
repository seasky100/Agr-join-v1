package cn.fundview.app.activity.expert;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.fundview.R;
import cn.fundview.app.view.common.pullrefresh.PullToRefreshRecyclerView;

public class GuestExpertListActivity extends AppCompatActivity {

    @ViewInject(R.id.pullRecyclerView)
    private PullToRefreshRecyclerView pullToRefreshRecyclerView;
    private RecyclerView recyclerView;
    @ViewInject(R.id.toolBar)
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_expert_list);

        ViewUtils.inject(this);


    }
}
