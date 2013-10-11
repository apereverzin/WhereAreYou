package com.creek.whereareyou.android.infrastructure.sqlite;

import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractRequestResponseRepository.TIME_CREATED_FIELD_NAME;
import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractRequestResponseRepository.TIME_RECEIVED_FIELD_NAME;
import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractRequestResponseRepository.TIME_SENT_FIELD_NAME;
import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractRequestResponseRepository.PROCESSED_FIELD_NAME;
import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractRequestResponseRepository.INT_FALSE;
import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractSQLiteRepository.UNDEFINED_LONG;
import static com.creek.whereareyou.android.infrastructure.sqlite.Comparison.EQUALS;
import static com.creek.whereareyou.android.infrastructure.sqlite.Comparison.NOT_EQUALS;

/**
 * 
 * @author Andrey Pereverzin
 */
class ComparisonClause {
    private final String fieldName;
    private final Comparison comparison;
    private final String value;
    
    static final ComparisonClause CREATION_TIME_KNOWN = new ComparisonClause(TIME_CREATED_FIELD_NAME, NOT_EQUALS, UNDEFINED_LONG);
    static final ComparisonClause CREATION_TIME_UNKNOWN = new ComparisonClause(TIME_CREATED_FIELD_NAME, EQUALS, UNDEFINED_LONG);
    static final ComparisonClause SENT_TIME_KNOWN = new ComparisonClause(TIME_SENT_FIELD_NAME, NOT_EQUALS, UNDEFINED_LONG);
    static final ComparisonClause SENT_TIME_UNKNOWN = new ComparisonClause(TIME_SENT_FIELD_NAME, EQUALS, UNDEFINED_LONG);
    static final ComparisonClause RECEIVED_TIME_KNOWN = new ComparisonClause(TIME_RECEIVED_FIELD_NAME, NOT_EQUALS, UNDEFINED_LONG);
    static final ComparisonClause RECEIVED_TIME_UNKNOWN = new ComparisonClause(TIME_RECEIVED_FIELD_NAME, EQUALS, UNDEFINED_LONG);
    static final ComparisonClause NOT_PROCESSED = new ComparisonClause(PROCESSED_FIELD_NAME, EQUALS, INT_FALSE);

    public ComparisonClause(String fieldName, Comparison comparison, String value) {
        this.fieldName = fieldName;
        this.comparison = comparison;
        this.value = value;
    }
    
    public ComparisonClause(String fieldName, Comparison comparison, int value) {
        this(fieldName, comparison, Integer.toString(value));
    }
    
    public ComparisonClause(String fieldName, Comparison comparison, long value) {
        this(fieldName, comparison, Long.toString(value));
    }
    
    public void appendToStringBuilder(StringBuilder sb) {
        sb.append(fieldName);
        sb.append(comparison.getValue());
        sb.append(value);
    }
}
