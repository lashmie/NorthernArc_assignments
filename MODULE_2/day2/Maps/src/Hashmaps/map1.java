package Hashmaps;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class map1 {
    public static void main(String[] args) {
        //HashMap<String,String > map = new HashMap<>();
        TreeMap<String, String> map = new TreeMap<>();
        // Map<String,String> map = new LinkedHashMap<>();
        map.put("3", "mohana");
        map.put("4", "Selvi");
        map.put("1", "lavanya");
        map.put("2", "Elavarasan");
        System.out.println(map);

//        System.out.println("Printing the map values:");
//        System.out.println(map.get("1"));
//        System.out.println(map.get("2"));
//        System.out.println(map.get("3"));
//        System.out.println(map.get("4"));

        for (String key : map.keySet()) {
            System.out.println(key + " " + map.get(key));
        }

        map.keySet().stream().forEach((key) -> System.out.println(key + "" + map.get(key)));

        //values are:
        for (String val : map.values()) {
            System.out.println(val);
        }
        map.values().stream().forEach((val) -> System.out.println(val));

        for(Map.Entry<String,String> entry:map.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());

        }
        System.out.println("-----------------------------");
        map.entrySet().stream().forEach((Map.Entry<String,String> entry)-> System.out.println(entry.getKey()));

        System.out.println("-----------------------------");
        map.forEach((String key,String val)-> System.out.println(key+":"+val));

    }
}
