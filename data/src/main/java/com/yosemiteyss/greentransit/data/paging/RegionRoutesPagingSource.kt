package com.yosemiteyss.greentransit.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_REGION_CODES_COLLECTION
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_REGION_CODE_DTO_CODE
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_REGION_CODE_DTO_REGION
import com.yosemiteyss.greentransit.data.dtos.RouteRegionCodeDto
import kotlinx.coroutines.tasks.await

/**
 * Created by kevin on 16/5/2021
 */

class RegionRoutesPagingSource(
    private val firestore: FirebaseFirestore,
    private val region: String
) : PagingSource<Query, RouteRegionCodeDto>() {

    private var initialPageQuery: Query? = null

    override fun getRefreshKey(state: PagingState<Query, RouteRegionCodeDto>): Query? = initialPageQuery

    override suspend fun load(params: LoadParams<Query>): LoadResult<Query, RouteRegionCodeDto> {
        return try {
            initialPageQuery = initialPageQuery ?: firestore.collection(ROUTE_REGION_CODES_COLLECTION)
                .whereEqualTo(ROUTE_REGION_CODE_DTO_REGION, region)
                .orderBy(ROUTE_REGION_CODE_DTO_CODE, Query.Direction.ASCENDING)
                .limit(params.loadSize.toLong())

            val currentPageQuery = params.key ?: initialPageQuery
            val currentPageData = currentPageQuery!!.get().await()

            val nextPageQuery = if (!currentPageData.isEmpty) {
                val lastVisibleItem = currentPageData.documents[currentPageData.size() - 1]
                initialPageQuery!!.startAfter(lastVisibleItem)
            } else {
                null
            }

            LoadResult.Page(
                data = currentPageData.toObjects(RouteRegionCodeDto::class.java),
                prevKey = null,
                nextKey = nextPageQuery
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}