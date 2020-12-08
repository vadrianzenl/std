package pe.gob.congreso.util;

public enum ReportType{
    FOO, BAR;
    public enum FILE{
        PDF, XLS;
    }
    public enum DATASOURCE{
        //WITH, WITHOUT; //Comentado AEP 15.08.2019	
    	WITH, WITHOUT, COLLECTION;  // Agregado AEP 15.08.2019
    	
    }
}
