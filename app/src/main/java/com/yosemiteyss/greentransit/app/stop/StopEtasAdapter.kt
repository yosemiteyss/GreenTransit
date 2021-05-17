package com.yosemiteyss.greentransit.app.stop

/**
 * Created by kevin on 17/5/2021asasz
 */

class StopEtasAdapter(

) {
}

sealed class StopEtasListModel {
    data class StopEtasShiftModel(
        val routeId: Long,
        val etaMin: Int
    ) : StopEtasListModel()
}