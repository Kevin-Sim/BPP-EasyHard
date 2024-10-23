package bppModel;

public class Dataset {

	int ds_id = -1;
	String ds_name = "";
	String ds_desc = "";
	int items;
	int capacity;
	double min;
	double max;
	double avg;
	double small;
	double medium;
	double large;
	double huge;
	double free;
	double distinct;
	
	
	public Dataset(int ds_id, String ds_name, String ds_desc, int items,
			int capacity, double min, double max, double avg, double small,
			double medium, double large, double huge, double free,
			double distinct) {
		super();
		this.ds_id = ds_id;
		this.ds_name = ds_name;
		this.ds_desc = ds_desc;
		this.items = items;
		this.capacity = capacity;
		this.min = min;
		this.max = max;
		this.avg = avg;
		this.small = small;
		this.medium = medium;
		this.large = large;
		this.huge = huge;
		this.free = free;
		this.distinct = distinct;
	}
	
	public int getItems() {
		return items;
	}
	public int getCapacity() {
		return capacity;
	}
	public double getMin() {
		return min;
	}
	public double getMax() {
		return max;
	}
	public double getAvg() {
		return avg;
	}
	public double getSmall() {
		return small;
	}
	public double getMedium() {
		return medium;
	}
	public double getLarge() {
		return large;
	}
	public double getHuge() {
		return huge;
	}
	public double getFree() {
		return free;
	}
	public double getDistinct() {
		return distinct;
	}
	public Dataset() {
		
	}
	public String getDs_desc() {
		return ds_desc;
	}
	public int getDs_id() {
		return ds_id;
	}
	public String getDs_name() {
		return ds_name;
	}
	
	

	public final void setDs_id(int ds_id) {
		this.ds_id = ds_id;
	}

	public final void setDs_name(String ds_name) {
		this.ds_name = ds_name;
	}

	public final void setDs_desc(String ds_desc) {
		this.ds_desc = ds_desc;
	}

	public final void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	
	public final void setItems(int items) {
		this.items = items;
	}

	public String toString(){
		return getDs_name();
	}
}
