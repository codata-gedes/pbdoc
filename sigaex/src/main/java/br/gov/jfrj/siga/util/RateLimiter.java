package br.gov.jfrj.siga.util;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import br.gov.jfrj.siga.model.ContextoPersistencia;

public final class RateLimiter {

	public static final String DEFAULT_RATE_LIMITER_NOTIFICATION_ICON = "fa fa-exclamation-triangle";
	public static final String DEFAULT_RATE_LIMITER_TITLE = "Alerta de limitação de tráfego";
	public static final String DEFAULT_RATE_LIMITER_MESSAGE = "Aguarde um pouco antes de efetuar esta requisição novamente.";

	private static final String UNABLE_TO_RATE_LIMIT_AS_REQUEST_IS_NOT_PROPERLY_SET = "Unable to rate limit request as it is not properly set";

	private static final long DEFAULT_RATE_LIMITER_TIMEOUT_MILLIS = 1_000;

	private static final Map</* USER */String, /* LAST TIMESTAMP */Long> LAST_SESSIONS_MAP = new ConcurrentHashMap<>();

    private RateLimiter() {}

    public static boolean isWithinRateLimiterTimeout(String methodReference) {
    	return isWithinRateLimiterTimeout(methodReference, DEFAULT_RATE_LIMITER_TIMEOUT_MILLIS);
    }

    public static boolean isWithinRateLimiterTimeout(String methodReference, long minimalTimeoutMillis) {
    	Objects.requireNonNull(methodReference, UNABLE_TO_RATE_LIMIT_AS_REQUEST_IS_NOT_PROPERLY_SET);
    	String principal = ContextoPersistencia.getUserPrincipal();
    	String key = String.join(":", methodReference, principal);

    	final long currentTimestamp = System.currentTimeMillis();
        final Long lastTimestamp = LAST_SESSIONS_MAP.put(key, currentTimestamp);
        if (lastTimestamp == null) {
            return true;
        }

        final long timeoutMillis = currentTimestamp - lastTimestamp.longValue();
        return timeoutMillis > minimalTimeoutMillis;
    }

    public static ToasterNotification buildRateLimiterToasterNotification(String message) {
    	return new ToasterNotification(DEFAULT_RATE_LIMITER_NOTIFICATION_ICON, DEFAULT_RATE_LIMITER_TITLE, null, message);
    }

}