package com.api.parkingcontroljdbc.models;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Entity implements Serializable {
	private static final long serialVersionUID = 1L;
	private UUID id;
}
