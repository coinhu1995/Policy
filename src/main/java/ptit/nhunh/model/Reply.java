package ptit.nhunh.model;

public class Reply {
	private String total;

	private Item[] items;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public Item[] getItem() {
		return items;
	}

	public void setItem(Item[] items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "ClassPojo [total = " + total + ", items = " + items + "]";
	}
}