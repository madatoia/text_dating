package general;

import dataaccess.DataBaseConnection;

public class Utilities {
    public static boolean isTableName(String entry) {
        try {
            for (String tableName:DataBaseConnection.getTableNames()) {
                if (entry.toLowerCase().replaceAll(" ","").equals(tableName.toLowerCase().replaceAll("_",""))) {    
                    return true;
                }    
            }
        } catch (Exception exception) {
            System.out.println ("Exceptie: "+exception.getMessage());
        }
        return false;
    }
}
