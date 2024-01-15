package com.cobra.tracker.pshell;

import com.cobra.tracker.util.CobraException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ForensicDataItem {
    private final static String ERROR_MISMATCH_NAMES_VALUES = "Failed to create forensic data, %d names with %d values.";
    private final static String ERROR_MISSING_NAMES_VALUES = "Failed to create forensic data, missing key, names or values.";
    private final static String MISSING_KEY = "Failed to create forensic data, failed to locate key.";

    private final Map<String, String> fields = new HashMap<>();
    private final String keyName;

    public ForensicDataItem(String keyName, String[] names, String[] values) throws CobraException {
        this.keyName = keyName;
        boolean foundKey = false;

        if (keyName == null || names == null || values == null) {
            throw new CobraException(ERROR_MISSING_NAMES_VALUES);
        }

        for (int i = 0; i < names.length; i++) {
            String name;
            int startIndex = names[i].indexOf('"');
            int endIndex = names[i].lastIndexOf('"');
            if (startIndex != -1) {
                name = names[i].substring(startIndex + 1, endIndex);
            } else {
                name = names[i];
            }
            if (!foundKey) {
                foundKey = keyName.equals(name);
            }
            if (values.length > i) {
                fields.put(name, values[i]);
            } else {
                fields.put(name, null);
            }
        }

        if (!foundKey) {
            throw new CobraException(MISSING_KEY);
        }

    }

    public String key() {
        return fields.get(keyName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        boolean equals = false;
        if (obj != null) {
            ForensicDataItem forensicData = (ForensicDataItem) obj;
            if (fields.size() == forensicData.fields.size()) {
                Set<String> keys = fields.keySet();
                for (String key : keys) {
                    String value = fields.get(key);
                    String otherValue = forensicData.fields.get(key);
                    if (value != null || otherValue != null) {
                        if (value == null || !value.equals(otherValue)) {
                            break;
                        }
                    }
                }
                equals = true;
            }
        }
        return equals;
    }

    @Override
    public String toString() {
        return fields.values().toString();
    }
}
