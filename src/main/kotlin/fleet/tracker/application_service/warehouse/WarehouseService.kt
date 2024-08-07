package fleet.tracker.application_service.warehouse

import fleet.tracker.dto.*
import fleet.tracker.exception.database.DatabaseException
import fleet.tracker.exception.warehouse.WarehouseNotFoundException
import fleet.tracker.infrastructure.warehouse.WarehouseRepository
import fleet.tracker.infrastructure.warehouse_area.WarehouseAreaRepository
import fleet.tracker.model.DelayState
import fleet.tracker.model.SearchSourceWarehouse
import fleet.tracker.model.SearchSourceWarehouseArea
import fleet.tracker.model.getDelayStateByWeight
import net.sf.geographiclib.Geodesic
import net.sf.geographiclib.GeodesicMask
import org.springframework.stereotype.Service
import kotlin.math.round
import kotlin.math.roundToInt

interface WarehouseService {
    fun getByWarehouseId(warehouseId: Int): WarehouseGetDTO
    fun getByWarehouseAreaId(warehouseAreaId: Int): List<WarehouseGetDTO>
    fun getSearchWarehouses(
        favoriteWarehouseIds: List<Int>?,
        userLatitude: Double,
        userLongitude: Double
    ): WarehouseSearchDTO
    fun getAllWarehouses(): List<LocalAreasDTO>
}

@Service
class WarehouseServiceImpl(
    val warehouseRepository: WarehouseRepository,
    val warehouseAreaRepository: WarehouseAreaRepository
) : WarehouseService {
    override fun getByWarehouseId(warehouseId: Int): WarehouseGetDTO {
        try {
            val warehouse = warehouseRepository.getByWarehouseIdOrNull(warehouseId)
                ?: throw WarehouseNotFoundException("Warehouse not found")

            return WarehouseGetDTO(
                warehouseId = warehouse.warehouseId,
                warehouseName = warehouse.warehouseName,
                warehouseAreaId = warehouse.warehouseAreaId,
                warehouseLatitude = warehouse.warehouseLatitude,
                warehouseLongitude = warehouse.warehouseLongitude
            )
        } catch (e: DatabaseException) {
            throw DatabaseException("Database error", e)
        }
    }

    override fun getByWarehouseAreaId(warehouseAreaId: Int): List<WarehouseGetDTO> {
        try {
            val warehouses = warehouseRepository.getByWarehouseAreaId(warehouseAreaId)

            if (warehouses.isEmpty()) throw WarehouseNotFoundException("Warehouse area not found")

            return warehouses.filterNotNull().map {
                WarehouseGetDTO(
                    warehouseId = it.warehouseId,
                    warehouseName = it.warehouseName,
                    warehouseAreaId = it.warehouseAreaId,
                    warehouseLatitude = it.warehouseLatitude,
                    warehouseLongitude = it.warehouseLongitude
                )
            }
        } catch (e: DatabaseException) {
            throw DatabaseException("Database error", e)
        }
    }

    override fun getSearchWarehouses(
        favoriteWarehouseIds: List<Int>?,
        userLatitude: Double,
        userLongitude: Double
    ): WarehouseSearchDTO {
        try {
            if (!favoriteWarehouseIds.isNullOrEmpty() && !warehouseRepository.existsByIds(favoriteWarehouseIds)) {
                throw WarehouseNotFoundException("Warehouse not found")
            }

            val invasionResult = checkInvasion(userLatitude, userLongitude)

            val favoriteWarehouses = favoriteWarehouseIds?.let { ids ->
                getWarehouseSearchResult(
                    warehouseRepository.getSearchSourceWarehousesByWarehouseIds(ids),
                    userLatitude,
                    userLongitude
                ).sortedBy { warehouse -> warehouse.distance}
            } ?: emptyList()

            val warehouses = if (invasionResult.isInvading) {
                getWarehouseSearchResult(
                    warehouseRepository.getSearchSourceWarehousesByWarehouseAreaId(invasionResult.warehouseAreaId),
                    userLatitude,
                    userLongitude
                ).sortedBy { warehouse -> warehouse.distance}
            } else {
                emptyList()
            }

            val warehouseAreas = if (!invasionResult.isInvading) {
                val warehouseAreaIds = invasionResult.nearByWarehouses.map { it.warehouseAreaId }
                getWarehouseAreaSearchResult(
                    warehouseAreaRepository.getSearchSourceWarehouseAreaByWarehouseAreaIds(warehouseAreaIds),
                    userLatitude,
                    userLongitude,
                    invasionResult.nearByWarehouses
                ).sortedBy { warehouseArea -> warehouseArea.distance}
            } else {
                emptyList()
            }

            return WarehouseSearchDTO(
                isInvading = invasionResult.isInvading,
                warehouses = warehouses,
                favoriteWarehouses = favoriteWarehouses,
                warehouseAreas = warehouseAreas
            )
        } catch (e: DatabaseException) {
            throw DatabaseException("Database error", e)
        }
    }

    override fun getAllWarehouses(): List<LocalAreasDTO> {
        try {
            val localAreas = warehouseRepository.getAllWarehouses()

            return localAreas.map { localArea ->
                LocalAreasDTO(
                    localAreaId = localArea.localAreaId,
                    localAreaName = localArea.localAreaName,
                    warehouseAreas = localArea.warehouseAreas.map { warehouseArea ->
                        LocalWarehouseAreaDTO(
                            warehouseAreaId = warehouseArea.warehouseAreaId,
                            warehouseAreaName = warehouseArea.warehouseAreaName,
                            warehouseAreaLatitude = warehouseArea.warehouseAreaLatitude,
                            warehouseAreaLongitude = warehouseArea.warehouseAreaLongitude,
                            warehouses = warehouseArea.warehouses.map { warehouse ->
                                LocalWarehouseDTO(
                                    warehouseId = warehouse.warehouseId,
                                    warehouseName = warehouse.warehouseName,
                                    warehouseLatitude = warehouse.warehouseLatitude,
                                    warehouseLongitude = warehouse.warehouseLongitude
                                )
                            }
                        )
                    }
                )
            }
        } catch (e: DatabaseException) {
            throw DatabaseException("Database error", e)
        }
    }


    fun checkInvasion(latitude: Double, longitude: Double): InvasionResult {
        val warehouseAreas = warehouseAreaRepository.getAllWarehouseArea()

        for (warehouseArea in warehouseAreas) {
            val distance = getDistance(
                latitude,
                longitude,
                warehouseArea.warehouseAreaLatitude,
                warehouseArea.warehouseAreaLongitude
            )

            if (distance <= warehouseArea.warehouseAreaRadius) {
                return InvasionResult(true, warehouseArea.warehouseAreaId)
            }
        }

        val nearByWarehouses = warehouseAreas.map { warehouseArea ->
            val distance = getDistance(
                latitude,
                longitude,
                warehouseArea.warehouseAreaLatitude,
                warehouseArea.warehouseAreaLongitude
            )

            WarehouseAreaDistance(warehouseArea.warehouseAreaId, distance)
        }.sortedBy { it.distance }.take(3)

        return InvasionResult(false, 0, nearByWarehouses)
    }

    fun getWarehouseSearchResult(
        searchSourceWarehouses: List<SearchSourceWarehouse>,
        userLatitude: Double,
        userLongitude: Double
    ): List<WarehousesSearchResultDTO> {
        val allStates = DelayState.entries
        return searchSourceWarehouses.map { searchSourceWarehouse ->

            val averageDelayState = searchSourceWarehouse.delayTimeDetail.calculateAverageDelayState()

            val existingDetails = searchSourceWarehouse.delayTimeDetail.associateBy { it?.delayState }

            val completeDetails = allStates.map { state ->
                existingDetails[state] ?: DelayTimeDetail(state, 0)
            }

            WarehousesSearchResultDTO(
                warehouseId = searchSourceWarehouse.warehouseId,
                warehouseAreaId = searchSourceWarehouse.warehouseAreaId,
                warehouseName = searchSourceWarehouse.warehouseName,
                averageDelayState = getDelayStateByWeight(averageDelayState),
                delayTimeDetail = completeDetails,
                distance = getDistance(
                    userLatitude,
                    userLongitude,
                    searchSourceWarehouse.warehouseLatitude,
                    searchSourceWarehouse.warehouseLongitude
                )
            )
        }
    }

    fun getWarehouseAreaSearchResult(
        searchSourceWarehouseAreas: List<SearchSourceWarehouseArea>,
        userLatitude: Double,
        userLongitude: Double,
        nearByWarehouses: List<WarehouseAreaDistance>
    ): List<WarehouseAreaSearchResultDTO> {
        return searchSourceWarehouseAreas.map { searchSourceWarehouseArea ->

            val averageDelayState = searchSourceWarehouseArea.delayTimeDetail.calculateAverageDelayState()

            WarehouseAreaSearchResultDTO(
                warehouseAreaId = searchSourceWarehouseArea.warehouseAreaId,
                warehouseAreaName = searchSourceWarehouseArea.warehouseAreaName,
                averageDelayState = getDelayStateByWeight(averageDelayState),
                warehouseAreaLatitude = searchSourceWarehouseArea.warehouseAreaLatitude,
                warehouseAreaLongitude = searchSourceWarehouseArea.warehouseAreaLongitude,
                distance = nearByWarehouses.find { it.warehouseAreaId == searchSourceWarehouseArea.warehouseAreaId }?.distance ?: 0.0,
            )
        }
    }

}

fun getDistance(latitude1: Double, longitude1: Double, latitude2: Double, longitude2: Double): Double {
    val geodesic = Geodesic(Constants.A_ELLIPSE, Constants.F_ELLIPSE)
    val result = geodesic.Inverse(
        latitude1,
        longitude1,
        latitude2,
        longitude2,
        GeodesicMask.DISTANCE
    )
    // 小数点以下2桁まで表示 (km)
    return (result.s12 / 1000).let { "%.2f".format(it).toDouble() }
}

object Constants {
    const val A_ELLIPSE = 6378137.0
    const val F_ELLIPSE = 1 / 298.257222101
}

data class InvasionResult(val isInvading: Boolean, val warehouseAreaId: Int, val nearByWarehouses: List<WarehouseAreaDistance> = emptyList())
data class WarehouseAreaDistance(val warehouseAreaId: Int, val distance: Double)

// 各DelayStateの重みを取得しanswerCountを掛けて合計し平均を算出。
// - 遅延情報がない場合は1を設定
// - 入庫不可が3件以上の場合は入庫不可を返す
fun List<DelayTimeDetail?>.calculateAverageDelayState(): Int {
    if (this.isEmpty()) {
        return DelayState.Normal.getWeight()
    }

    val impossibleAnswerCount = this.filterNotNull().sumOf {
        if (it.delayState == DelayState.Impossible) it.answerCount else 0
    }

    if (impossibleAnswerCount >= 3) {
        return DelayState.Impossible.getWeight()
    }

    val totalCount = this.filterNotNull().sumOf { it.answerCount }
    if (totalCount == 0) return DelayState.Normal.getWeight()

    val totalWeight = this.filterNotNull().sumOf { it.delayState.getWeight() * it.answerCount }
    return (totalWeight.toDouble() / totalCount.toDouble()).roundToInt()
}