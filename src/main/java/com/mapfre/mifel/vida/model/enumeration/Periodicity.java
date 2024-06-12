package com.mapfre.mifel.vida.model.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum Periodicity {
	SEMESTRAL(2),
	TRIMESTRAL(3),
	MENSUAL(4),
	ANUAL(10);
	
	private int periodicity;
	
	Periodicity(int periodicity){
		this.periodicity = periodicity;
	}

	public int getPeriodicity() {
		return periodicity;
	}

	public static String toString(int periodicity){
		switch(periodicity) {
			case 2:
				return "SEMESTRAL";
			case 3:
				return "TRIMESTRAL";
			case 4:
				return "MENSUAL";
			case 10:
				return "ANUAL (MULTIANUAL)";
			default:
				throw new IllegalArgumentException("Invalid periodicty");
			
		}
	}
	
	@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
	public static Periodicity fromId(int periodicity) {
		return Stream.of(Periodicity.values()).filter(var -> var.periodicity == periodicity).findFirst().get();
	}
}

