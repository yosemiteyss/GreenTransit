package com.yosemiteyss.greentransit.data.constants

/**
 * Created by kevin on 11/5/2021
 */

object Constants {
    const val GMB_URL = "https://data.etagmb.gov.hk"
    const val TRAFFIC_NEWS_URL = "https://resource.data.one.gov.hk/"

    const val APP_DB_NAME = "green_transit_db"

    const val GMB_RESPONSE_TYPE = "type"
    const val GMB_RESPONSE_VERSION = "version"
    const val GMB_RESPONSE_GENERATED_TIMESTAMP = "generated_timestamp"
    const val GMB_RESPONSE_DATA = "data"

    const val ROUTE_REGION_KLN = "KLN"
    const val ROUTE_REGION_HKI = "HKI"
    const val ROUTE_REGION_NT = "NT"

    const val ROUTE_CODES_LOCAL_TABLE = "route_codes"
    const val ROUTE_CODES_LOCAL_DTO_CODE = "code"
    const val ROUTE_CODES_LOCAL_DTO_REGION = "region"

    const val ROUTE_CODES_NETWORK_DTO_ROUTES = "routes"

    const val ROUTE_SEARCH_COLLECTION = "route_codes"
    const val ROUTE_SEARCH_DTO_CODE = "code"
    const val ROUTE_SEARCH_DTO_REGION = "region"
    const val ROUTE_SEARCH_DTO_ROUTE_IDS = "route_ids"

    const val NEARBY_STOP_COLLECTION = "stop_id"
    const val NEARBY_STOP_DTO_ID = "stop_id"
    const val NEARBY_STOP_DTO_ROUTE_ID = "route_id"
    const val NEARBY_STOP_DTO_LOCATION = "location"
    const val NEARBY_STOP_DTO_GEO_HASH = "geohash"

    const val NEARBY_ROUTE_COLLECTION = "routes"
    const val NEARBY_ROUTE_DTO_ID = "route_id"
    const val NEARBY_ROUTE_DTO_SEQ = "route_seq"
    const val NEARBY_ROUTE_DTO_CODE = "route_code"
    const val NEARBY_ROUTE_DTO_ORIGIN_TC = "route_orig_tc"
    const val NEARBY_ROUTE_DTO_ORIGIN_SC = "route_orig_sc"
    const val NEARBY_ROUTE_DTO_ORIGIN_EN = "route_orig_en"
    const val NEARBY_ROUTE_DTO_DEST_TC = "route_dest_tc"
    const val NEARBY_ROUTE_DTO_DEST_SC = "route_dest_sc"
    const val NEARBY_ROUTE_DTO_DEST_EN = "route_dest_en"
    const val NEARBY_ROUTE_DTO_REGION = "region"
    const val NEARBY_ROUTE_DTO_STOP_IDS = "stop_ids"

    const val STOP_ROUTE_ETA_DTO_ENABLED = "enabled"
    const val STOP_ROUTE_ETA_DTO_ROUTE_ID = "route_id"
    const val STOP_ROUTE_ETA_DTO_ROUTE_SEQ = "route_seq"
    const val STOP_ROUTE_ETA_DTO_STOP_SEQ = "stop_seq"
    const val STOP_ROUTE_ETA_DTO_ETA = "eta"

    const val SHIFT_ETA_DTO_SEQ = "eta_seq"
    const val SHIFT_ETA_DTO_DIFF = "diff"
    const val SHIFT_ETA_DTO_TIMESTAMP = "timestamp"
    const val SHIFT_ETA_DTO_REMARKS_TC = "remarks_tc"
    const val SHIFT_ETA_DTO_REMARKS_SC = "remarks_sc"
    const val SHIFT_ETA_DTO_REMARKS_EN = "remarks_en"

    const val STOP_INFO_DTO_ENABLED = "enabled"
    const val STOP_INFO_DTO_REMARKS_TC = "remarks_tc"
    const val STOP_INFO_DTO_REMARKS_SC = "remarks_sc"
    const val STOP_INFO_DTO_REMARKS_EN = "remarks_en"
    const val STOP_INFO_DTO_COORDINATES = "coordinates"
    const val STOP_COORDINATES_LIST_DTO_WGS84 = "wgs84"
    const val STOP_COORDINATES_LIST_DTO_HK80 = "hk80"
    const val STOP_COORDINATES_DTO_LATITUDE = "latitude"
    const val STOP_COORDINATES_DTO_LONGITUDE = "longitude"

    const val STOP_ROUTE_DTO_ROUTE_ID = "route_id"
    const val STOP_ROUTE_DTO_ROUTE_SEQ = "route_seq"
    const val STOP_ROUTE_DTO_STOP_SEQ = "stop_seq"
    const val STOP_ROUTE_DTO_NAME_TC = "name_tc"
    const val STOP_ROUTE_DTO_NAME_SC = "name_sc"
    const val STOP_ROUTE_DTO_NAME_EN = "name_en"

    const val ROUTE_INFO_DTO_ROUTE_ID = "route_id"
    const val ROUTE_INFO_DTO_DESCRIPTION_TC = "description_tc"
    const val ROUTE_INFO_DTO_DESCRIPTION_SC = "description_sc"
    const val ROUTE_INFO_DTO_DESCRIPTION_EN = "description_en"
    const val ROUTE_INFO_DTO_DIRECTIONS = "directions"

    const val ROUTE_DIRECTION_DTO_ROUTE_SEQ = "route_seq"
    const val ROUTE_DIRECTION_DTO_ORIG_TC = "orig_tc"
    const val ROUTE_DIRECTION_DTO_ORIG_SC = "orig_sc"
    const val ROUTE_DIRECTION_DTO_ORIG_EN = "orig_en"
    const val ROUTE_DIRECTION_DTO_DEST_TC = "dest_tc"
    const val ROUTE_DIRECTION_DTO_DEST_SC = "dest_sc"
    const val ROUTE_DIRECTION_DTO_DEST_EN = "dest_en"
    const val ROUTE_DIRECTION_DTO_REMARKS_TC = "remarks_tc"
    const val ROUTE_DIRECTION_DTO_REMARKS_SC = "remarks_sc"
    const val ROUTE_DIRECTION_DTO_REMARKS_EN = "remarks_en"

    const val ROUTE_STOP_LIST_DTO_ROUTE_STOPS = "route_stops"
    const val ROUTE_STOP_DTO_STOP_SEQ = "stop_seq"
    const val ROUTE_STOP_DTO_STOP_ID = "stop_id"
    const val ROUTE_STOP_DTO_NAME_TC = "name_tc"
    const val ROUTE_STOP_DTO_NAME_SC = "name_sc"
    const val ROUTE_STOP_DTO_NAME_EN = "name_en"

    const val ROUTE_STOP_ETA_DTO_ROUTE_SEQ = "route_seq"
    const val ROUTE_STOP_ETA_DTO_STOP_SEQ = "stop_seq"
    const val ROUTE_STOP_ETA_DTO_ENABLED = "enabled"
    const val ROUTE_STOP_ETA_DTO_DESCRIPTION_TC = "description_tc"
    const val ROUTE_STOP_ETA_DTO_DESCRIPTION_SC = "description_sc"
    const val ROUTE_STOP_ETA_DTO_DESCRIPTION_EN = "description_en"
    const val ROUTE_STOP_ETA_DTO_ETA = "eta"

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

    const val TRAFFIC_NEWS_STATUS_NEW = 2
    const val TRAFFIC_NEWS_STATUS_UPDATED = 3
}