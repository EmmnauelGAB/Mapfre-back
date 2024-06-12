package com.mapfre.mifel.vida.model.request;

import lombok.Data;

@Data
public class FundDistribution {
	String profile;
	String investmentType;
	String percentage;
	String initialBalance;
}
