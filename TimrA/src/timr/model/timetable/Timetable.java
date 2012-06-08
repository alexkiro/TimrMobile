package timr.model.timetable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;



public class Timetable {
	public Map<Days, LinkedList<TableItem>> map = new HashMap<Days, LinkedList<TableItem>>();
	public Timetable(){
		for (Days d : Days.values()){
			map.put(d, new LinkedList<TableItem>());
		}
	}
	public LinkedList<TableItem> getTableItems(Days d){
		return map.get(d);		
	}
	
	@Override
	public String toString(){
		StringBuilder sb =new StringBuilder();
		for (Entry<Days, LinkedList<TableItem>> e : map.entrySet()){
			sb.append(e.getKey().toString() + " \n");
			for (TableItem t : e.getValue()){
				sb.append(t+"\n");
			}			
		}
		return sb.toString();
	}
}	

