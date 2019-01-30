package cast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CastEntriesProcessor {
	
	Map<String, Integer> violations;
	List<CastEntry> castEntries;

	public List<CastEntry> sumViolations(List<String> violationList){
		
		violations = new HashMap<String, Integer>();
		castEntries = new ArrayList();
		
		for(String violation : violationList){
			
			String violationClass = "";
			
			if (null != violation && violation.length() > 0 )
			{
			    int endIndex = violation.lastIndexOf(".");
			    if (endIndex != -1)  
			    {
			    	violationClass = violation.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
			    }
			} 
			
			if(!violations.containsKey(violationClass)){
				violations.put(violationClass, 0);
			}
			
			violations.put(violationClass, violations.get(violationClass) + 1);
		}
		
		for(Map.Entry<String, Integer> entry : violations.entrySet()) {
		    String key = entry.getKey();
		    Integer value = entry.getValue();
		    
		    CastEntry cEntry = new CastEntry();
		    cEntry.setPath(key);
		    cEntry.setTotalErrors(String.valueOf(value));
		    
		    castEntries.add(cEntry);
		    //System.out.println("Path : " + cEntry.getPath());
		    //System.out.println("Errors : " + cEntry.getTotalErrors());
		}
		
		return castEntries;
	}
	
	public List<CastEntry> getSortedList(List<CastEntry> castEntries){
		Collections.sort(castEntries, 
                (o1, o2) -> Integer.valueOf(o2.getTotalErrors()).compareTo(Integer.valueOf(o1.getTotalErrors())));
		return castEntries;
	}
}
