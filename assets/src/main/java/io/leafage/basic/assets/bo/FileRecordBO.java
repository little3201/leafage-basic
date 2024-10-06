package io.leafage.basic.assets.bo;

/**
 * bo class for file record.
 *
 * @author wq li
 */
public abstract class FileRecordBO {

    private String name;

    private String path;

    private String type;

    private float size;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }
}
