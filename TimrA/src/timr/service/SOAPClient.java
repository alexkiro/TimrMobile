package timr.service;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public final class SOAPClient {
	private SoapObject request;
	private String namespace;
	private String url;
	private String method;
	
	public SOAPClient(String url, String method, String namespace){
		this.namespace = namespace;
		this.url = url;
		this.method = method;
		request = new SoapObject(namespace, method);	
	}
	public SOAPClient addParameter(String tag, String value){
		PropertyInfo pu = new PropertyInfo();
        pu.setName(tag);
        pu.setValue(value);
        pu.setType(PropertyInfo.STRING_CLASS);
        request.addProperty(pu);
        return this;        
	}
	public String call(){
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        
        HttpTransportSE http = new HttpTransportSE(url);
                
        System.out.println(request.toString());
        
        try
        {
            http.call(namespace+method, envelope);
            SoapObject response = (SoapObject)envelope.bodyIn;
            //System.out.println(response.toString());
            return response.toString();                        
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
	}
}
