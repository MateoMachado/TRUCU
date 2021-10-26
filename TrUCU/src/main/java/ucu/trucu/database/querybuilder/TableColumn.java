package ucu.trucu.database.querybuilder;

/**
 *
 * @author NicoPuig
 */
public class TableColumn {

    private final String name;
    private final SQLType type;
    private Integer presition;
    private boolean notNull = false;
    private Object defaultValue;

    public TableColumn(String name, SQLType type) {
        this.name = name;
        this.type = type;
    }

    public TableColumn(String name, SQLType type, Integer presition) {
        this.name = name;
        this.type = type;
        this.presition = presition;
    }

    public SQLType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Integer getPresition() {
        return presition;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setPresition(Integer presition) {
        this.presition = presition;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String toTableDefinitionFormat() {
        String format = name + " " + type;
        if (presition != null) {
            format += String.format("(%s)", presition);
        }
        format += notNull ? " NOT NULL" : " NULL";
        if (defaultValue != null) {
            format += " DEFAULT " + defaultValue;
        }
        return format;
    }
}
