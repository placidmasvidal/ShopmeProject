package com.shopme.admin.util.export;

import java.util.concurrent.ConcurrentHashMap;

public enum ExporterTypeEnum {
  USER_CSV(1),
  USER_EXCEL(2),
  USER_PDF(3),
  CAT_CSV(4),
  CAT_EXCEL(5);

  private int value;

  private static final ConcurrentHashMap<Integer, ExporterTypeEnum> VALUES =
      new ConcurrentHashMap<>();

  ExporterTypeEnum(int value) {
    this.value = value;
  }

  public static ExporterTypeEnum fromValue(int value) {
    ExporterTypeEnum type = VALUES.get(value);

    if (type != null) {
      return type;
    }

    throw new IllegalArgumentException("There isn't any ExporterTypeEnum with id '" + value + "'.");
  }

  public int getValue() {
    return value;
  }
}
