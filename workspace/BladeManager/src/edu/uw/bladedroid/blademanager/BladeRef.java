package edu.uw.bladedroid.blademanager;

import java.io.File;

class BladeRef {
	private String caption;
	private File location;

	BladeRef(File location) {
		String fileName = location.getName();
		this.caption = fileName.substring(0, fileName.length() - 4);
		this.location = location;
	}
	
	public File getLocation() {
		return location;
	}
	
	@Override
	public String toString() {
		return caption;
	}
}