package com.example.nmsadminapp.utils

object HttpResponseCode
{
    // 1xx Informational
    val CONTINUE = 100
    val SWITCHING_PROTOCOLS = 101
    val PROCESSING = 102
    val EARLY_HINTS = 103

    // 2xx Success
    val OK = 200
    val CREATED = 201
    val ACCEPTED = 202
    val NON_AUTHORITATIVE_INFORMATION = 203
    val NO_CONTENT = 204
    val RESET_CONTENT = 205
    val PARTIAL_CONTENT = 206
    val MULTI_STATUS = 207
    val ALREADY_REPORTED = 208
    val IM_USED = 226

    // 3xx Redirection
    val MULTIPLE_CHOICES = 300
    val MOVED_PERMANENTLY = 301
    val FOUND = 302
    val SEE_OTHER = 303
    val NOT_MODIFIED = 304
    val USE_PROXY = 305
    val SWITCH_PROXY = 306
    val TEMPORARY_REDIRECT = 307
    val PERMANENT_REDIRECT = 308

    // 4xx Client Error
    val BAD_REQUEST = 400
    val UNAUTHORIZED = 401
    val PAYMENT_REQUIRED = 402
    val FORBIDDEN = 403
    val NOT_FOUND = 404
    val METHOD_NOT_ALLOWED = 405
    val NOT_ACCEPTABLE = 406
    val PROXY_AUTHENTICATION_REQUIRED = 407
    val REQUEST_TIMEOUT = 408
    val CONFLICT = 409
    val GONE = 410
    val LENGTH_REQUIRED = 411
    val PRECONDITION_FAILED = 412
    val PAYLOAD_TOO_LARGE = 413
    val URI_TOO_LONG = 414
    val UNSUPPORTED_MEDIA_TYPE = 415
    val RANGE_NOT_SATISFIABLE = 416
    val EXPECTATION_FAILED = 417
    val IM_A_TEAPOT = 418
    val MISDIRECTED_REQUEST = 421
    val UNPROCESSABLE_ENTITY = 422
    val LOCKED = 423
    val FAILED_DEPENDENCY = 424
    val TOO_EARLY = 425
    val UPGRADE_REQUIRED = 426
    val PRECONDITION_REQUIRED = 428
    val TOO_MANY_REQUESTS = 429
    val REQUEST_HEADER_FIELDS_TOO_LARGE = 431
    val UNAVAILABLE_FOR_LEGAL_REASONS = 451

    // 5xx Server Error
    val INTERNAL_SERVER_ERROR = 500
    val NOT_IMPLEMENTED = 501
    val BAD_GATEWAY = 502
    val SERVICE_UNAVAILABLE = 503
    val GATEWAY_TIMEOUT = 504
    val HTTP_VERSION_NOT_SUPPORTED = 505
    val VARIANT_ALSO_NEGOTIATES = 506
    val INSUFFICIENT_STORAGE = 507
    val LOOP_DETECTED = 508
    val NOT_EXTENDED = 510
    val NETWORK_AUTHENTICATION_REQUIRED = 511

    // 6xx Custom Error
    val INVALID_CREDENTIALS = 600
    val USER_ALREADY_EXISTS = 601

}