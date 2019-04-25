import weka.core.Instances;

public class Dataset {

	private double success; // Dataset'in başarı yüzdesi
	private Instances data; // işlenmiş veri için kullanılan Instances
	private Instances data2; // işlenmemiş veri için kullanılan Instances
	
	public double getSuccess() {
		return success;
	}
	public void setSuccess(double success) {
		this.success = success;
	}
	public Instances getData() {
		return data;
	}
	public void setData(Instances data) {
		this.data = data;
	}
	public Instances getData2() {
		return data2;
	}
	public void setData2(Instances data2) {
		this.data2 = data2;
	}
	

	
	
	
}
