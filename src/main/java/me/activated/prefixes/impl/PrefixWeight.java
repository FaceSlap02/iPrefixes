package me.activated.prefixes.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PrefixWeight {

    DEFAULT("Default"), COMMON("Common"), RARE("Rare"), ULTRA_RARE("Ultra Rare"), LIMITED("Limited");

    private String name;
}
