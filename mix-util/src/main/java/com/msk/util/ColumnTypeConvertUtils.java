package com.msk.util;

import com.msk.exception.OwnException;
import com.msk.result.ResultEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author MSK
 * @date 2023/7/21
 */
public class ColumnTypeConvertUtils {

    private static Map<String, Map<String, List<String>>> toType = new HashMap<>();

    static {
        Map<String, List<String>> longConvert = new HashMap<>();
        longConvert.put("ODPS", Arrays.asList("BIGINT"));
        longConvert.put("MYSQL", Arrays.asList("int", "tinyint", "smallint", "mediumint", "int", "bigint"));
        longConvert.put("OCEANBASE", Arrays.asList("int", "tinyint", "smallint", "mediumint", "int", "bigint"));
        longConvert.put("ORACLE", Arrays.asList("NUMBER", "INTEGER", "INT", "SMALLINT"));
        longConvert.put("DMDB", Arrays.asList("INTEGER", "INT", "PLS_INTEGER", "BIGINT", "TINYINT", "BYTE", "SMALLINT"));
        longConvert.put("POSTGRESQL", Arrays.asList("bigint", "bigserial", "integer", "smallint", "serial"));
        longConvert.put("SQLSERVER", Arrays.asList("bigint", "int", "smallint", "tinyint"));
        longConvert.put("KINGBASEES8", Arrays.asList("bigint", "bigserial", "integer", "smallint", "serial"));
        toType.put("Long", longConvert);


        Map<String, List<String>> doubleConvert = new HashMap<>();
        doubleConvert.put("ODPS", Arrays.asList("DOUBLE"));
        doubleConvert.put("MYSQL", Arrays.asList("float", "double", "decimal"));
        doubleConvert.put("OCEANBASE", Arrays.asList("float", "double", "decimal"));
        doubleConvert.put("ORACLE", Arrays.asList("NUMERIC", "DECIMAL", "FLOAT", "DOUBLEPRECISION", "REAL"));
        doubleConvert.put("DMDB", Arrays.asList("NUMERIC", "NUMBER", "DECIMAL", "DEC", "DOUBLE", "REAL", "FLOAT", "DOUBLE", "DOUBLE PRECISION"));
        doubleConvert.put("POSTGRESQL", Arrays.asList("double precision", "money", "numeric", "real"));
        doubleConvert.put("SQLSERVER", Arrays.asList("float", "decimal", "real", "numeric"));
        doubleConvert.put("KINGBASEES8", Arrays.asList("doubleprecision", "money", "numeric", "real"));
        toType.put("Double", doubleConvert);


        Map<String, List<String>> stringConvert = new HashMap<>();
        stringConvert.put("ODPS", Arrays.asList("STRING"));
        stringConvert.put("MYSQL", Arrays.asList("varchar", "char", "tinytext", "text", "mediumtext", "longtext", "year"));
        stringConvert.put("OCEANBASE", Arrays.asList("varchar", "char", "tinytext", "text", "mediumtext", "longtext", "year"));
        stringConvert.put("ORACLE", Arrays.asList("VARCHAR2", "LONG", "CHAR", "NCHAR", "VARCHAR", "NVARCHAR2", "CLOB", "NCLOB", "CHARACTER", "CHARACTERVARYING", "CHARVARYING", "NATIONALCHARACTER", "NATIONALCHAR", "NATIONALCHARACTERVARYING", "NATIONALCHARVARYING", "NCHARVARYING"));
        stringConvert.put("DMDB", Arrays.asList("VARCHAR", "CHAR", "CHARACTER"));
        stringConvert.put("POSTGRESQL", Arrays.asList("varchar", "char", "text", "bit", "inet"));
        stringConvert.put("SQLSERVER", Arrays.asList("nvarchar", "varchar", "char", "nchar", "ntext", "text", "nvarchar(MAX)", "varchar(MAX)"));
        stringConvert.put("KINGBASEES8", Arrays.asList("varchar", "char", "text", "bit", "inet"));
        toType.put("String", stringConvert);


        Map<String, List<String>> dateConvert = new HashMap<>();
        dateConvert.put("ODPS", Arrays.asList("DATETIME"));
        dateConvert.put("MYSQL", Arrays.asList("date", "datetime", "timestamp", "time"));
        dateConvert.put("OCEANBASE", Arrays.asList("date", "datetime", "timestamp", "time"));
        dateConvert.put("ORACLE", Arrays.asList("TIMESTAMP", "DATE"));
        dateConvert.put("DMDB", Arrays.asList("DATE", "TIME", "TIMESTAMP", "DATATIME", "TIME WITH TIME ZONE", "TIMESTAMP WITH TIME ZONE", "TIMESTAMP WITH LOCAL TIME ZONE"));
        dateConvert.put("POSTGRESQL", Arrays.asList("date", "time", "timestamp"));
        dateConvert.put("SQLSERVER", Arrays.asList("date", "datetime", "time"));
        dateConvert.put("KINGBASEES8", Arrays.asList("date", "time", "timestamp"));
        toType.put("Date", dateConvert);


        Map<String, List<String>> boolConvert = new HashMap<>();
        boolConvert.put("ODPS", Arrays.asList("Boolean"));
        boolConvert.put("MYSQL", Arrays.asList("bit", "bool"));
        boolConvert.put("OCEANBASE", Arrays.asList("bit", "bool"));
        boolConvert.put("ORACLE", Arrays.asList("bit", "bool"));
        boolConvert.put("DMDB", Arrays.asList("BOOL", "BOOLEAN"));
        boolConvert.put("POSTGRESQL", Arrays.asList("bool"));
        boolConvert.put("SQLSERVER", Arrays.asList("bit"));
        boolConvert.put("KINGBASEES8", Arrays.asList("bool"));
        toType.put("Bool", boolConvert);

        Map<String, List<String>> bytesConvert = new HashMap<>();
        bytesConvert.put("MYSQL", Arrays.asList("tinyblob", "mediumblob", "blob", "longblob", "varbinary"));
        bytesConvert.put("OCEANBASE", Arrays.asList("tinyblob", "mediumblob", "blob", "longblob", "varbinary"));
        bytesConvert.put("ORACLE", Arrays.asList("BLOB", "BFILE", "RAW", "LONG RAW"));
        bytesConvert.put("DMDB", Arrays.asList("BINARY", "VARBINARY", "BFILE"));
        bytesConvert.put("POSTGRESQL", Arrays.asList("bytea"));
        bytesConvert.put("SQLSERVER", Arrays.asList("binary", "varbinary", "varbinary(MAX)", "timestamp"));
        bytesConvert.put("KINGBASEES8", Arrays.asList("bytea"));
        toType.put("Bytes", bytesConvert);
    }

    public static String convertType(String inType, String outType, String columnType) {
        String key = columnType;
        if (StringUtils.isNotBlank(inType)) {
            for (String k : toType.keySet()) {
                Map<String, List<String>> map = toType.get(k);
                List<String> list = map.get(inType);
                if (list.contains(columnType)) {
                    key = k;
                    break;
                }
            }
        }
        if (StringUtils.isBlank(key)) {
            String msg = "数据类型不存在 {" + inType + " : " + columnType + "}";
            throw new OwnException(ResultEnum.ERROR.getCode(), msg);
        }
        List<String> list = toType.get(key).get(outType);
        if (Objects.isNull(list) || list.size() == 0) {
            return toType.get("String").get(outType).get(0);
        } else {
            return list.get(0);
        }
    }
}
