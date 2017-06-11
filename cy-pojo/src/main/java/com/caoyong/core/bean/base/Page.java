package com.caoyong.core.bean.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 分页对象
 * @author yong.cao
 * @time 2017年6月11日下午5:47:40
 */
public class Page<E> implements Serializable{
	private static final long serialVersionUID = 2647885913495907787L;
	
	/**
	 * pageSize
	 */
	private Integer limit = 20;
	
	/**
	 * 开始行
	 */
	private Integer start = 0;
	
	/**
	 * 总记录数
	 */
	private Integer results = 0;
	
	/**
	 * 对象参数
	 */
	private E params;
	
	/**
	 * 列表参数
	 */
	private List<E> rows = new ArrayList<>();
	
	/**
	 * 是否成功
	 */
	private Boolean isSuccess = false;
	
	/**
	 * 错误信息
	 */
	private String error;
	
	/**
	 * 错误代码
	 */
	private String errorCode;
	
	/**
	 * 是否分页
	 */
	private Boolean page;
	
	/**
	 * 分页栏
	 */
	private List<String> pageView;
	
	/**
	 * 第几页
	 */
	private Integer pageNo = 1;
	
	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getResults() {
		return results;
	}

	public void setResults(Integer results) {
		this.results = results;
	}

	public E getParams() {
		return params;
	}

	public void setParams(E params) {
		this.params = params;
	}

	public List<E> getRows() {
		return rows;
	}

	public void setRows(List<E> rows) {
		this.rows = rows;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public Boolean getPage() {
		return page;
	}

	public void setPage(Boolean page) {
		this.page = page;
	}
	
	public List<String> getPageView() {
		return pageView;
	}

	public void setPageView(List<String> pageView) {
		this.pageView = pageView;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getTotalPage() {
		Integer totalPage = this.results / this.limit;
		if ((totalPage == 0) || (this.results % this.limit != 0)) {
			totalPage++;
		}
		return totalPage;
	}

	public void pageView(String url, String params) {
		this.pageView = new ArrayList<String>();
		if (this.pageNo != 1) {
			this.pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?"
					+ params + "&pageNo=1'\"><font size=2>首页</font></a>");
			this.pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?"
					+ params + "&pageNo=" + (this.pageNo - 1) + "'\"><font size=2>上一页</font></a>");
		} else {
			this.pageView.add("<font size=2>首页</font>");
			this.pageView.add("<font size=2>上一页</font>");
		}
		if (getTotalPage() <= 10) {
			for (int i = 0; i < getTotalPage(); i++) {
				if (i + 1 == this.pageNo) {
					this.pageView.add("<strong>" + this.pageNo + "</strong>");
					i++;
					if (this.pageNo == getTotalPage()) {
						break;
					}
				}
				this.pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url
						+ "?" + params + "&pageNo=" + (i + 1) + "'\">" + (i + 1) + "</a>");
			}
		} else if (getTotalPage() <= 20) {
			int l = 0;
			int r = 0;
			if (this.pageNo < 5) {
				l = this.pageNo - 1;
				r = 10 - l - 1;
			} else if (getTotalPage() - this.pageNo < 5) {
				r = getTotalPage() - this.pageNo;
				l = 9 - r;
			} else {
				l = 4;
				r = 5;
			}
			int tmp = this.pageNo - l;
			for (int i = tmp; i < tmp + 10; i++) {
				if (i == this.pageNo) {
					this.pageView.add("<strong>" + this.pageNo + "</strong>");
					i++;
					if (this.pageNo == getTotalPage()) {
						break;
					}
				}
				this.pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url
						+ "?" + params + "&pageNo=" + i + "'\">" + i + "</a>");
			}
		} else if (this.pageNo < 7) {
			for (int i = 0; i < 8; i++) {
				if (i + 1 == this.pageNo) {
					this.pageView.add("<strong>" + this.pageNo + "</strong>");
					i++;
				}
				this.pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url
						+ "?" + params + "&pageNo=" + (i + 1) + "'\">" + (i + 1) + "</a>");
			}
			this.pageView.add("...");
			this.pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?"
					+ params + "&pageNo=" + (getTotalPage() - 1) + "'\">" + (getTotalPage() - 1) + "</a>");
			this.pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?"
					+ params + "&pageNo=" + getTotalPage() + "'\">" + getTotalPage() + "</a>");
		} else if (this.pageNo > getTotalPage() - 6) {
			this.pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?"
					+ params + "&pageNo=" + 1 + "'\">" + 1 + "</a>");
			this.pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?"
					+ params + "&pageNo=" + 2 + "'\">" + 2 + "</a>");
			this.pageView.add("...");
			for (int i = getTotalPage() - 8; i < getTotalPage(); i++) {
				if (i + 1 == this.pageNo) {
					this.pageView.add("<strong>" + this.pageNo + "</strong>");
					i++;
					if (this.pageNo == getTotalPage()) {
						break;
					}
				}
				this.pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url
						+ "?" + params + "&pageNo=" + (i + 1) + "'\">" + (i + 1) + "</a>");
			}
		} else {
			this.pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?"
					+ params + "&pageNo=" + 1 + "'\">" + 1 + "</a>");
			this.pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?"
					+ params + "&pageNo=" + 2 + "'\">" + 2 + "</a>");
			this.pageView.add("...");

			this.pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?"
					+ params + "&pageNo=" + (this.pageNo - 2) + "'\">" + (this.pageNo - 2) + "</a>");
			this.pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?"
					+ params + "&pageNo=" + (this.pageNo - 1) + "'\">" + (this.pageNo - 1) + "</a>");
			this.pageView.add("<strong>" + this.pageNo + "</strong>");
			this.pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?"
					+ params + "&pageNo=" + (this.pageNo + 1) + "'\">" + (this.pageNo + 1) + "</a>");
			this.pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?"
					+ params + "&pageNo=" + (this.pageNo + 2) + "'\">" + (this.pageNo + 2) + "</a>");

			this.pageView.add("...");
			this.pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?"
					+ params + "&pageNo=" + (getTotalPage() - 1) + "'\">" + (getTotalPage() - 1) + "</a>");
			this.pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?"
					+ params + "&pageNo=" + getTotalPage() + "'\">" + getTotalPage() + "</a>");
		}
		if (this.pageNo != getTotalPage()) {
			this.pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?"
					+ params + "&pageNo=" + (this.pageNo + 1) + "'\"><font size=2>下一页</font></a>");
			this.pageView.add("<a href=\"javascript:void(0);\" onclick=\"javascript:window.location.href='" + url + "?"
					+ params + "&pageNo=" + getTotalPage() + "'\"><font size=2>尾页</font></a>");
		} else {
			this.pageView.add("<font size=2>下一页</font>");
			this.pageView.add("<font size=2>尾页</font>");
		}
		this.pageView.add("共<var>" + getTotalPage()
				+ "</var>页 到第<input type='text' id='PAGENO'  size='3' />页 <input type='button' id='skip' class='hand btn60x20' value='确定' onclick=\"javascript:window.location.href = '"
				+ url + "?" + params + "&pageNo=' + $('#PAGENO').val() \"/>");
	}

	/**
	 * rows最多显示前10条
	 */
	@Override
	public String toString() {
		final Integer maxLen = 10;
		return "Page [limit=" + limit + ", start=" + start + ", results=" + results + ", params=" + params + ", rows="
				+ (null != rows ? rows.subList(0, Math.min(rows.size(), maxLen)) : null)+ ", isSuccess=" + isSuccess + ", error=" + error + ", errorCode=" + errorCode + ", page=" + page
				+ "]";
	}
	
}
