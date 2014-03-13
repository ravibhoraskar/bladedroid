package edu.uw.bladedroid.blademanager;

import java.io.File;

class BladeRef {
	private String caption;
	private File location;
	private long size;

	BladeRef(File location) {
		String fileName = location.getName();
		this.caption = fileName.substring(0, fileName.length() - 4);
		this.location = location;
		size = location.length();
	}
	
	public File getLocation() {
		return location;
	}
	
	public long getSize() {
		return size;
	}
	
	@Override
	public String toString() {
		return caption;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof BladeRef)) {
			return false;
		}
		BladeRef other = (BladeRef) o;
		
		return other.caption.equals(caption);
	}
}