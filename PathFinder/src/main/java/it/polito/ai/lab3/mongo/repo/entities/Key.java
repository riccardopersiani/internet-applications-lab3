package it.polito.ai.lab3.mongo.repo.entities;

import java.io.Serializable;
import java.util.Objects;

public class Key implements Serializable {

	private static final long serialVersionUID = 2083961456275171567L;
	private String src;
	private String dst;
	
	
	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getDst() {
		return dst;
	}

	public void setDst(String dst) {
		this.dst = dst;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Key other = (Key) obj;
		
		try {
			if (this.src.equals(other.src) && this.dst.equals(other.dst))
				return true;
			else
				return false;
		}
		catch (NullPointerException e) {
			return false;
		}		
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(src, dst);
	}
}
