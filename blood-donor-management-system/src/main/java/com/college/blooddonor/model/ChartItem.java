package com.college.blooddonor.model;

/** A database-derived value used by the dashboard's lightweight CSS charts. */
public record ChartItem(String label, long value, int percentage) {
}
