package com.ecar.apm;

import com.ecar.apm.model.MonitorFrequency;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MonitorFrequency mf = MonitorFrequency.valueOf("THIRTY");
		
		System.out.println(mf.getLabel());
		
	}

}
