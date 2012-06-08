package timr.model.timetable;

public enum Days {
	
		SUNDAY,MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY;
		@Override
		public String toString(){
			return super.toString().toLowerCase();
		}
		
		public static Days fromString(String day){
			return Days.valueOf(day.toUpperCase());
		}
                
                public static int indexOf(Days d){
                    for (int i=0;i<7;i++){
                        if (values()[i] == d){
                            return i;
                        }
                    }
                    return -1;
                }
                
                public static Days get(int i){
                    return values()[i];
                }
	
}
