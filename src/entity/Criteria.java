package entity;

public class Criteria {
    private String fieldName;
    private Operator operator;
    private Object value;
    private Connector connector;
    private Order order;

    public Criteria(String fieldName, Operator operator, Object value, Connector connector, Order order) {
        this.fieldName = fieldName;
        this.operator = operator;
        this.value = value;
        this.connector = connector;
        this.order = order;
    }

    public Criteria(String fieldName, Operator operator, Object value, Connector connector) {
        this.fieldName = fieldName;
        this.operator = operator;
        this.value = value;
        this.connector = connector;
    }

    public Criteria(String fieldName, Order order) {
        this.fieldName = fieldName;
        this.order = order;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Operator getOperator() {
        return operator;
    }

    public Object getValue() {
        return value;
    }

    public Connector getConnector() {
        return connector;
    }

    public Order getOrder() {
        return order;
    }
}
