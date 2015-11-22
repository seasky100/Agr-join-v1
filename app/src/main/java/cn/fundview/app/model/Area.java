package cn.fundview.app.model;

public class Area {

    private Integer id;
    private Integer parentId;// 上一级别ID
    private int levels;// 层次
    private String name;

    public Area() {
    }

    public Area(Integer id, Integer parentId, int levels, String name) {
        this.id = id;
        this.parentId = parentId;
        this.levels = levels;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public int getLevels() {
        return levels;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
