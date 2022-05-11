/**
 *
 */
package ffapl.java.classes;

import java.util.HashMap;

import ffapl.java.interfaces.IJavaType;
import ffapl.types.FFaplTypeCrossTable;
import org.json.JSONObject;

/**
 * @author Alexander Ortner
 * @version 1.0
 *
 */
public class Record implements IJavaType<Record> {

    public HashMap<String, HashMap<String, String>> _record;
    public String _final_record;
//    private HashMap<String, IJavaType> _record;
 //   private List<List<String>> _record;

    /**
     *
     */
    public Record() {
        _record = new HashMap<>();
    }
//    public Record() {
//        _record = new HashMap<String, IJavaType>();
//    }
//    public Record() {
//        _record = new ArrayList<List<String>>();;
//    }


    /**
     * Maps the specified id to the specified value in this record.
//     * @param id
     * @param val
     */
    public void addElement(String id, HashMap<String,String> val) {
        _record.put(id, val);
    }
//    public void addElement(String id, IJavaType val) {
//        _record.put(id, val);
//    }
//    public void addElement(List<String> val) {
//        _record.add(val);
//    }
    /**
     * Returns the value to which the specified id is mapped,
     * or null if this map contains no mapping for the key.
     * @param id
     * @return
     */
    public HashMap<String, String> getElement(String id) {
        return _record.get(id);
    }
//    public IJavaType getElement(String id) {
//        return _record.get(id);
//    }
//    public ArrayList<String> getElement(int id) {
//        return (ArrayList<String>) _record.get(id);
//    }

    @Override
    public int typeID() {
        return IJavaType.RECORD;
    }

    @Override
    public String classInfo() {
        return FFaplTypeCrossTable.TYPE_Name[FFaplTypeCrossTable.FFAPLRECORD];
    }

    @Override
    public Record clone() {
        return null;
    }

    @Override
    public String toString() {
        return _record.toString();
    }

    @Override
    public boolean equalType(Object type) {
        if (type instanceof Record) {
            return true;
        }
        return false;
    }

}
