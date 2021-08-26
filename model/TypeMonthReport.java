package sample.model;

public class TypeMonthReport {

    private String type;
    private int count;
    private String month;

    /**
     * Constructor for TypeMonthReport objects
     * @param type
     * @param count
     * @param month
     */
    public TypeMonthReport(String type, int count, String month) {
        this.type = type;
        this.count = count;
        this.month = month;
    }

    /**
     * Returns type
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Returns count
     * @return
     */
    public int getCount() {
        return count;
    }

    /**
     * Returns month
     * @return
     */
    public String getMonth() {
        return month;
    }
}
