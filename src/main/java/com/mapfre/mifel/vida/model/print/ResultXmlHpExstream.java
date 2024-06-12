package com.mapfre.mifel.vida.model.print;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResultXmlHpExstream {
	int status;
	String content;
}
