package cn.fundview.app.domain.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Unique;

import java.io.Serializable;

/**
 * 搜索历史
 *
 * @version 1.0
 */
@Table(name = "t_search_history")
public class SearchHistory extends EntityBase {

    public static final int EXPERT_HISTORY = 0;
    public static final int COMPANY_HISTORY = 2;
    public static final int ACHV_HISTORY = 1;
    public static final int REQU_HISTORY = 3;
    public static final int ORG_HISTORY = 4;
    public static final int PRODUCT_HISTORY = 5;

    @Unique
    @Column(column = "words")
    private String words;
    @Column(column = "search_time")
    private String searchTime;
    @Column(column = "type")
    private int type;// 历史的类型,1 搜索专家的历史 2 搜索企业的历史 3 成果的历史 4 需求的历史

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWords() {
        return words;
    }

    public String getSearchTime() {
        return searchTime;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public void setSearchTime(String searchTime) {
        this.searchTime = searchTime;
    }


}
