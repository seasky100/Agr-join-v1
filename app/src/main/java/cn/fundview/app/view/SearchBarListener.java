package cn.fundview.app.view;

/**
 * 查询栏设置接口
 *
 * @author ouda
 */
public interface SearchBarListener {

    void keyChange(String key);

    void doSearch(String key);

    void closeView();
}