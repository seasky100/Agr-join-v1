package cn.fundview.app.view;

/**
 * 查询栏设置接口
 *
 * @author ouda
 */
public interface SearchBarListener {

    public void keyChange(String key);

    public void doSearch(String key);

    public void closeView();
}