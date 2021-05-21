//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.domain.usecases.nearby

import app.cash.turbine.test
import com.yosemiteyss.greentransit.domain.models.Coordinate
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.testshared.repositories.FakeTransitRepositoryImpl
import com.yosemiteyss.greentransit.testshared.utils.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.utils.runBlockingTest
import kotlinx.coroutines.flow.toList
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import kotlin.random.Random

class GetNearbyStopsUseCaseTest {

    private lateinit var fakeTransitRepositoryImpl: FakeTransitRepositoryImpl

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Before
    fun init() {
        fakeTransitRepositoryImpl = FakeTransitRepositoryImpl()
    }

    @Test
    fun `test stops distinct`() = coroutineRule.runBlockingTest {
        fakeTransitRepositoryImpl.setNetworkError(false)

        val useCase = createGetNearbyStopsUseCase()
        val param = createGetNearbyStopsParams()
        val result = useCase(param).toList().last()

        assert(
            result is Resource.Success &&
            result.data == result.data.distinct()
        )
    }

    @Test
    fun `test exception`() = coroutineRule.runBlockingTest {
        fakeTransitRepositoryImpl.setNetworkError(true)

        val useCase = createGetNearbyStopsUseCase()
        val param = createGetNearbyStopsParams()
        val result = useCase(param).toList().last()

        assert(result is Resource.Error)
    }

    @Test
    fun `test emit stops when last coordinate null`() = coroutineRule.runBlockingTest {
        fakeTransitRepositoryImpl.setNetworkError(false)

        val useCase = createGetNearbyStopsUseCase()
        val param = createGetNearbyStopsParams()

        useCase(param).test {
            assert(expectItem() is Resource.Loading)
            assert(expectItem() is Resource.Success)
            expectComplete()
        }
    }

    @Test
    fun `test emit stops when exceed distance`() = coroutineRule.runBlockingTest {
        fakeTransitRepositoryImpl.setNetworkError(false)

        val useCase = createGetNearbyStopsUseCase()
        val param1 = createGetNearbyStopsParams(
            Coordinate(22.336992686834407, 114.20479499004605)
        )

        // Start: 22.336992686834407, 114.20479499004605
        // End: 22.337493837461707, 114.20647405284788
        // Expect Distance: 180m

        useCase(param1).test {
            assert(expectItem() is Resource.Loading)
            assert(expectItem() is Resource.Success)
            expectComplete()
        }

        val param2 = createGetNearbyStopsParams(
            Coordinate(22.337493837461707, 114.20647405284788)
        )

        useCase(param2).test {
            assert(expectItem() is Resource.Loading)
            assert(expectItem() is Resource.Success)
            expectComplete()
        }
    }

    @Test
    fun `test emit no stops when within distance`() = coroutineRule.runBlockingTest {
        fakeTransitRepositoryImpl.setNetworkError(false)

        val useCase = createGetNearbyStopsUseCase()
        val param1 = createGetNearbyStopsParams(
            Coordinate(22.336992686834407, 114.20479499004605)
        )

        // Start: 22.336992686834407, 114.20479499004605
        // End: 22.337394599856744, 114.20613609452036
        // Expect Distance: 145m

        useCase(param1).test {
            assert(expectItem() is Resource.Loading)
            assert(expectItem() is Resource.Success)
            expectComplete()
        }

        val param2 = createGetNearbyStopsParams(
            Coordinate(22.337394599856744, 114.20613609452036)
        )

        useCase(param2).test {
            assert(expectItem() is Resource.Loading)
            expectComplete()
        }
    }

    private fun createGetNearbyStopsParams(currentCoord: Coordinate? = null): GetNearbyStopsParams {
        val coord = currentCoord ?: Coordinate(
            latitude = Random.nextDouble(),
            longitude = Random.nextDouble()
        )

        return GetNearbyStopsParams(
            currentCoord = coord,
            bounds = MutableList(4) {
                NearbyGeoBound(
                    startHash = UUID.randomUUID().toString(),
                    endHash = UUID.randomUUID().toString()
                )
            }
        )
    }

    private fun createGetNearbyStopsUseCase(): GetNearbyStopsUseCase {
        return GetNearbyStopsUseCase(
            transitRepository = fakeTransitRepositoryImpl,
            coroutineDispatcher = coroutineRule.testDispatcher
        )
    }
}