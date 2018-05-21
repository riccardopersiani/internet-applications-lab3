package it.polito.ai.lab3.web.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SrcDstPointsForm {
	@NotNull
	@Pattern(regexp="^[0-9]+(\\.[0-9]+)?$")
	private String srcLat;
	@NotNull
	@Pattern(regexp="^[0-9]+(\\.[0-9]+)?$")
	private String srcLng;
	@NotNull
	@Pattern(regexp="^[0-9]+(\\.[0-9]+)?$")
	private String dstLat;
	@NotNull
	@Pattern(regexp="^[0-9]+(\\.[0-9]+)?$")
	private String dstLng;
	
	
	public String getSrcLat() {
		return srcLat;
	}
	
	public void setSrcLat(String srcLat) {
		this.srcLat = srcLat;
	}
	
	public String getSrcLng() {
		return srcLng;
	}
	
	public void setSrcLng(String srcLng) {
		this.srcLng = srcLng;
	}
	public String getDstLat() {
		return dstLat;
	}
	
	public void setDstLat(String dstLat) {
		this.dstLat = dstLat;
	}
	
	public String getDstLng() {
		return dstLng;
	}
	
	public void setDstLng(String dstLng) {
		this.dstLng = dstLng;
	}
}
