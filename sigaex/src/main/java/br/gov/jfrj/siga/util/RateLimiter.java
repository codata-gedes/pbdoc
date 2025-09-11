package br.gov.jfrj.siga.util;

import static org.apache.commons.lang3.StringUtils.stripToEmpty;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import br.gov.jfrj.siga.model.ContextoPersistencia;

public final class RateLimiter {

	private static final String UNABLE_TO_RATE_LIMIT_AS_REQUEST_IS_NOT_SET = "Unable to rate limit request as it is not set";

    private static final Map</* USER */String, /* LAST TIMESTAMP */Long> LAST_SESSIONS_MAP = new ConcurrentHashMap<String, Long>();

    private RateLimiter() {}

    public static boolean isWithinRateLimiterTimeout(String methodReference, long minimalTimeoutMillis) {
    	String principal = ContextoPersistencia.getUserPrincipal();
    	String key = String.join(":", stripToEmpty(methodReference), principal);
    	Objects.requireNonNull(key, UNABLE_TO_RATE_LIMIT_AS_REQUEST_IS_NOT_SET);

    	final long currentTimestamp = System.currentTimeMillis();
        final Long lastTimestamp = LAST_SESSIONS_MAP.put(key, currentTimestamp);
        if (lastTimestamp == null) {
            return true;
        }

        final long timeoutMillis = currentTimestamp - lastTimestamp.longValue();
        return timeoutMillis > minimalTimeoutMillis;
    }

}