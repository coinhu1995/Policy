package ptit.nhunh.model;

public class Data {
	private String total;

	private String totalitem;

	private Item[] items = new Item[1000];

	private String offset;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getTotalitem() {
		return totalitem;
	}

	public void setTotalitem(String totalitem) {
		this.totalitem = totalitem;
	}

	public Item[] getItem() {
		return items;
	}

	public void setItem(Item[] items) {
		this.items = items;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	@Override
	public String toString() {
		return "ClassPojo [total = " + total + ", totalitem = " + totalitem + ", items = " + items + ", offset = "
				+ offset + "]";
	}
}