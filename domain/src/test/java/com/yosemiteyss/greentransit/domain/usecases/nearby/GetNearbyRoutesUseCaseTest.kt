//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.domain.usecases.nearby

import app.cash.turbine.test
import com.yosemiteyss.greentransit.domain.models.NearbyStop
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.testshared.repositories.FakeTransitRepositoryImpl
import com.yosemiteyss.greentransit.testshared.utils.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.utils.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetNearbyRoutesUseCaseTest {

    private lateinit var fakeTransitRepositoryImpl: FakeTransitRepositoryImpl

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Before
    fun init() {
        fakeTransitRepositoryImpl = FakeTransitRepositoryImpl()
    }

    @Test
    fun `test route sorted by code ascending`() = coroutineRule.runBlockingTest {
        fakeTransitRepositoryImpl.setNetworkError(false)

        val nearbyStops = fakeTransitRepositoryImpl.fakeNearbyStops
        val useCase = createGetNearbyRoutesUseCase()

        useCase(nearbyStops).test {
            assert(expectItem() is Resource.Loading)

            val expectItem = expectItem()
            assert(
                expectItem is Resource.Success &&
                expectItem.data == expectItem.data.sortedBy { it.code }
            )

            expectComplete()
        }
    }

    @Test
    fun `test routes has distinct id`() = coroutineRule.runBlockingTest {
        fakeTransitRepositoryImpl.setNetworkError(false)

        val nearbyStops = fakeTransitRepositoryImpl.fakeNearbyStops
        val useCase = createGetNearbyRoutesUseCase()

        useCase(nearbyStops).test {
            assert(expectItem() is Resource.Loading)
            expectItem().let { actual ->
                assert(
                    actual is Resource.Success &&
                    actual.data == actual.data.distinctBy { it.id }
                )
            }
            expectComplete()
        }
    }

    @Test
    fun `test return when nearby stops is empty`() = coroutineRule.runBlockingTest {
        fakeTransitRepositoryImpl.setNetworkError(false)

        val nearbyStops = emptyList<NearbyStop>()
        val useCase = createGetNearbyRoutesUseCase()

        useCase(nearbyStops).test {
            assert(expectItem() is Resource.Loading)
            expectComplete()
        }
    }

    private fun createGetNearbyRoutesUseCase(): GetNearbyRoutesUseCase {
        return GetNearbyRoutesUseCase(
            transitRepository = fakeTransitRepositoryImpl,
            coroutineDispatcher = coroutineRule.testDispatcher
        )
    }
}