package com.cobra.forensics.db.model;

import com.cobra.forensics.util.CobraException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Generic forensic data, where the object is represented by a list of key-value pairs.
 * This is primarily for output produced by windows powershell commands.
 */
public class WindowsGenericForensicData extends  ForensicData{
    private final static String ERROR_MISSING_NAMES_VALUES = "Failed to create forensic data, missing key, names or values.";
    private final static String MISSING_KEY = "Failed to create forensic data, failed to locate key with name %s.";

    private final Map<String, String> fields = new HashMap<>();
    private final String keyName;

    public WindowsGenericForensicData(String keyName, String[] names, String[] values) throws CobraException {
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
            // Specific workaround for windows, value may not be provided if empty
            if (values.length > i) {
                fields.put(name, values[i]);
            } else {
                fields.put(name, null);
            }
        }

        if (!foundKey) {
            throw new CobraException(String.format(MISSING_KEY,keyName));
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
        if (obj instanceof WindowsGenericForensicData) {
            WindowsGenericForensicData forensicData = (WindowsGenericForensicData) obj;
            if (fields.size() == forensicData.fields.size()) {
                equals = true;
                Set<String> keys = fields.keySet();
                for (String key : keys) {
                    String value = fields.get(key);
                    String otherValue = forensicData.fields.get(key);
                    if (!value.equals(otherValue)){
                        equals = false;
                        break;
                    }
                }
            }
        }
        return equals;
    }

    @Override
    public String toString() {
        return fields.values().toString();
    }
}
