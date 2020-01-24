package me.activated.prefixes.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Prefix {

    HEART("&4&lHeart&7: <format>", "Heart", "prefix.heart", "&7[&4&l❤&7] ", true, PrefixWeight.DEFAULT),
    CHECK_MARK("&a&lCheck Mark&7: <format> &7(<weight>)", "CheckMark", "prefix.checkmark", "&7[&a&l✔&7] ", true, PrefixWeight.COMMON),
    OG("&c&lOG&7: <format> &7(<weight>)", "Heart", "prefix.og", "&7[&c&l#OG&7] ", true, PrefixWeight.ULTRA_RARE),
    EZ("&9&lEZ&7: <format> &7(<weight>)", "Easy", "prefix.ez", "&7[&9&l#EZ&7] ", true, PrefixWeight.LIMITED),
    L("&2&lL&7: <format> &7(<weight>)", "L", "prefix.l", "&7[&b&l#L&7] ", false, PrefixWeight.ULTRA_RARE);;

    private String displayName, name, permission, format;
    private boolean purchasable;
    private PrefixWeight prefixWeight;
}
