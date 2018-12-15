package db.util;

import java.sql.ResultSet;

public interface RowParser {
    public Object convertToModel(ResultSet rs);
}
