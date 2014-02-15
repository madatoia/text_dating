package general;

public class ReferrencedTable {
    private String parentTable;
    private String attributeName;
    
    public ReferrencedTable() {
        parentTable   = new String();
        attributeName = new String();
    }
    
    public ReferrencedTable(String parentTable, String attributeName) {
        this.parentTable   = parentTable;
        this.attributeName = attributeName;
    }    
    
    public String getParentTable() {
        return parentTable;
    }
    
    public String getAttributeName() {
        return attributeName;
    }
    
    public void setParentTable(String parentTable) {
        this.parentTable = parentTable;
    }
    
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
}