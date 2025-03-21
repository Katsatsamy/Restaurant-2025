package entity;

public class Criteria {
    private String fieldName;
    private Operator operator;
    private Object value;
    private Connector connector;
    private OrderType orderType;

    public Criteria(String fieldName, Operator operator, Object value, Connector connector, OrderType orderType) {
        this.fieldName = fieldName;
        this.operator = operator;
        this.value = value;
        this.connector = connector;
        this.orderType = orderType;
    }

    public Criteria(String fieldName, Operator operator, Object value, Connector connector) {
        this.fieldName = fieldName;
        this.operator = operator;
        this.value = value;
        this.connector = connector;
    }

    public Criteria(String fieldName, OrderType orderType) {
        this.fieldName = fieldName;
        this.orderType = orderType;
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

    public OrderType getOrder() {
        return orderType;
    }
}
