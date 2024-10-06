package io.leafage.basic.assets.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import top.leafage.common.servlet.audit.AuditMetadata;

/**
 * model class for file record.
 *
 * @author wq li
 */
@Entity
@Table(name = "file_records")
public class FileRecord extends AuditMetadata {

    @Column(name = "name", nullable = false, unique = true, length = 50)
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
