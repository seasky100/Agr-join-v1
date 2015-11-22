package cn.fundview.app.tool.json;

/**
 * Created by Administrator on 2015/10/20 0020.
 */
public class JsonItem {

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public JsonItem() {
    }

    public JsonItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "JsonItem{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
