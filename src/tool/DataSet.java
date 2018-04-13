package tool;

public class DataSet {
	
	private int pageSize;
	private int currentPage;
	private int totalPages;
	private NewsList[] list;
	
	public NewsList[] getList() {
		return list;
	}

	public void setList(NewsList[] list) {
		this.list = list;
	}

	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	public int getTotalPages() {
		return totalPages;
	}
	
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	

	
}
