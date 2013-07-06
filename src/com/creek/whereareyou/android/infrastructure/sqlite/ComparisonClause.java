package com.creek.whereareyou.android.infrastructure.sqlite;

import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractRequestResponseRepository.TIME_CREATED_FIELD_NAME;
import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractRequestResponseRepository.TIME_RECEIVED_FIELD_NAME;
import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractRequestResponseRepository.TIME_SENT_FIELD_NAME;
import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractRequestResponseRepository.PROCESSED_FIELD_NAME;
import static com.creek.whereareyou.android.infrastructure.sqlite.AbstractRequestResponseRepository.FALSE;

/**
 * 
 * @author Andrey Pereverzin
 */
class ComparisonClause {
    private final String fieldName;
    private final Comparison comparison;
    private final String value;
    
    private static final String ZERO = "0";
    
    static final ComparisonClause CREATION_TIME_KNOWN = new ComparisonClause(TIME_CREATED_FIELD_NAME, Comparison.GREATER_THAN, ZERO);
    static final ComparisonClause CREATION_TIME_UNKNOWN = new ComparisonClause(TIME_CREATED_FIELD_NAME, Comparison.EQUALS, ZERO);
    static final ComparisonClause SENT_TIME_KNOWN = new ComparisonClause(TIME_SENT_FIELD_NAME, Comparison.GREATER_THAN, ZERO);
    static final ComparisonClause SENT_TIME_UNKNOWN = new ComparisonClause(TIME_SENT_FIELD_NAME, Comparison.EQUALS, ZERO);
    static final ComparisonClause RECEIVED_TIME_KNOWN = new ComparisonClause(TIME_RECEIVED_FIELD_NAME, Comparison.GREATER_THAN, ZERO);
    static final ComparisonClause RECEIVED_TIME_UNKNOWN = new ComparisonClause(TIME_RECEIVED_FIELD_NAME, Comparison.EQUALS, ZERO);
    static final ComparisonClause PENDING = new ComparisonClause(PROCESSED_FIELD_NAME, Comparison.EQUALS, FALSE);

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
    
    protected enum Comparison {
        EQUALS("="),
        NOT_EQUALS("!="),
        GREATER_THAN(">"),
        GREATER_THAN_OR_EQUALS(">="),
        LESS_THAN("<"),
        LESS_THAN_OR_EQUALS("<=");

        private String value;

        private Comparison(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
