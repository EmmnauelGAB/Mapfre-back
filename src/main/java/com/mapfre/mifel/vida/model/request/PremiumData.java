package com.mapfre.mifel.vida.model.request;

import java.util.List;

import com.mapfre.mifel.vida.model.enumeration.Periodicity;

import lombok.Data;

@Data
public class PremiumData {
	private String initial;
	private String aditional;
	private Periodicity periodicity;
}
