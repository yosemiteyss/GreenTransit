package com.yosemiteyss.greentransit.data.constants

/**
 * Created by kevin on 11/5/2021
 */

object Constants {
    const val GMB_URL = "https://data.etagmb.gov.hk"
    const val TRAFFIC_NEWS_URL = "https://resource.data.one.gov.hk/"

    const val GMB_RESPONSE_TYPE = "type"
    const val GMB_RESPONSE_VERSION = "version"
    const val GMB_RESPONSE_GENERATED_TIMESTAMP = "generated_timestamp"
    const val GMB_RESPONSE_DATA = "data"

    const val ROUTES_DTO_ROUTES = "routes"

    const val STOP_LOCATION_COLLECTION = "stop_id"
    const val STOP_LOCATION_DTO_ID = "stop_id"
    const val STOP_LOCATION_DTO_LOCATION = "location"
    const val STOP_LOCATION_GEO_HASH = "geohash"

    const val ROUTE_INFO_COLLECTION = "routes"
    const val ROUTE_INFO_DTO_ID = "route_id"
    const val ROUTE_INFO_DTO_SEQ = "route_seq"
    const val ROUTE_INFO_DTO_CODE = "route_code"
    const val ROUTE_INFO_DTO_REGION = "region"

    const val ROUTE_REGION_KLN = "KLN"
    const val ROUTE_REGION_HKI = "HKI"
    const val ROUTE_REGION_NT = "NT"

    const val STOP_ETA_ROUTE_DTO_ENABLED = "enabled"
    const val STOP_ETA_ROUTE_DTO_ROUTE_ID = "route_id"
    const val STOP_ETA_ROUTE_DTO_ROUTE_SEQ = "route_seq"
    const val STOP_ETA_ROUTE_DTO_STOP_SEQ = "stop_seq"
    const val STOP_ETA_ROUTE_DTO_ETA = "eta"

    const val STOP_ETA_ITEM_DTO_SEQ = "eta_seq"
    const val STOP_ETA_ITEM_DTO_DIFF = "diff"
    const val STOP_ETA_ITEM_DTO_TIMESTAMP = "timestamp"
    const val STOP_ETA_ITEM_DTO_REMARKS_TC = "remarks_tc"
    const val STOP_ETA_ITEM_DTO_REMARKS_SC = "remarks_sc"
    const val STOP_ETA_ITEM_DTO_REMARKS_EN = "remarks_en"

    const val TRAFFIC_NEWS_MESSAGES_DTO_ROOT = "body"
    const val TRAFFIC_NEWS_DTO_ROOT = "message"
    const val TRAFFIC_NEWS_DTO_MSG_ID = "msgID"
    const val TRAFFIC_NEWS_DTO_CURRENT_STATUS = "CurrentStatus"
    const val TRAFFIC_NEWS_DTO_CHIN_TEXT = "ChinText"
    const val TRAFFIC_NEWS_DTO_CHIN_SHORT = "ChinShort"
    const val TRAFFIC_NEWS_DTO_ENG_TEXT = "EngText"
    const val TRAFFIC_NEWS_DTO_ENG_SHORT = "EngShort"
    const val TRAFFIC_NEWS_DTO_REF_DATE = "ReferenceDate"
    const val TRAFFIC_NEWS_DTO_COUNT_DIST = "CountofDistricts"
}