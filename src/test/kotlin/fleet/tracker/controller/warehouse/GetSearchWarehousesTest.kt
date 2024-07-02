package fleet.tracker.controller.warehouse

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/delay/Insert_Delay_Test_Data.sql")
class GetSearchWarehousesTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return warehouses when user is in the Area`() {
        val userLatitude = 35.66270523445052
        val userLongitude = 139.73043579786187

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/warehouses/search")
                .param("user_latitude", userLatitude.toString())
                .param("user_longitude", userLongitude.toString())
        )

        result.andExpect(status().isOk)
        result.andExpect(content().json("""
            {
              "is_invading": true,
              "warehouses": [
                {
                  "warehouse_id": 28,
                  "warehouse_area_id": 11,
                  "warehouse_name": "エルフーズ東京工場",
                  "average_delay_state": "normal",
                  "delay_time_detail": [
                    {
                      "delay_state": "normal",
                      "answer_count": 0
                    },
                    {
                      "delay_state": "pause",
                      "answer_count": 0
                    },
                    {
                      "delay_state": "halfHour",
                      "answer_count": 0
                    },
                    {
                      "delay_state": "anHour",
                      "answer_count": 0
                    },
                    {
                      "delay_state": "impossible",
                      "answer_count": 0
                    }
                  ],
                  "distance": 3.29
                },
                {
                  "warehouse_id": 26,
                  "warehouse_area_id": 11,
                  "warehouse_name": "全農城南島営業所",
                  "average_delay_state": "normal",
                  "delay_time_detail": [
                    {
                      "delay_state": "normal",
                      "answer_count": 0
                    },
                    {
                      "delay_state": "pause",
                      "answer_count": 0
                    },
                    {
                      "delay_state": "halfHour",
                      "answer_count": 0
                    },
                    {
                      "delay_state": "anHour",
                      "answer_count": 0
                    },
                    {
                      "delay_state": "impossible",
                      "answer_count": 0
                    }
                  ],
                  "distance": 8.58
                },
                {
                  "warehouse_id": 27,
                  "warehouse_area_id": 11,
                  "warehouse_name": "東京冷蔵城南島物流センター",
                  "average_delay_state": "normal",
                  "delay_time_detail": [
                    {
                      "delay_state": "normal",
                      "answer_count": 0
                    },
                    {
                      "delay_state": "pause",
                      "answer_count": 0
                    },
                    {
                      "delay_state": "halfHour",
                      "answer_count": 0
                    },
                    {
                      "delay_state": "anHour",
                      "answer_count": 0
                    },
                    {
                      "delay_state": "impossible",
                      "answer_count": 0
                    }
                  ],
                  "distance": 9.03
                },
                {
                  "warehouse_id": 25,
                  "warehouse_area_id": 11,
                  "warehouse_name": "横浜水産平和島物流センター",
                  "average_delay_state": "normal",
                  "delay_time_detail": [
                    {
                      "delay_state": "normal",
                      "answer_count": 0
                    },
                    {
                      "delay_state": "pause",
                      "answer_count": 0
                    },
                    {
                      "delay_state": "halfHour",
                      "answer_count": 0
                    },
                    {
                      "delay_state": "anHour",
                      "answer_count": 0
                    },
                    {
                      "delay_state": "impossible",
                      "answer_count": 0
                    }
                  ],
                  "distance": 9.17
                }
              ],
              "favorite_warehouse_areas": [],
              "warehouse_areas": []
            }
        """.trimIndent()))
    }

    @Test
    fun `should return warehouseAreas when user is not in the Area`() {
        val userLatitude = 35.96420463614639
        val userLongitude = 139.5397469178333

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/warehouses/search")
                .param("user_latitude", userLatitude.toString())
                .param("user_longitude", userLongitude.toString())
        )

        result.andExpect(status().isOk)
        result.andExpect(content().json("""
            {
              "is_invading": false,
              "warehouses": [],
              "favorite_warehouse_areas": [],
              "warehouse_areas": [
                {
                  "warehouse_area_id": 12,
                  "warehouse_area_name": "埼玉県エリア",
                  "average_delay_state": "normal",
                  "distance": 13.89
                },
                {
                  "warehouse_area_id": 11,
                  "warehouse_area_name": "東京都エリア",
                  "average_delay_state": "normal",
                  "distance": 44.08
                },
                {
                  "warehouse_area_id": 10,
                  "warehouse_area_name": "千葉県エリア",
                  "average_delay_state": "normal",
                  "distance": 47.88
                }
              ]
            }
        """.trimIndent()))
    }

    @Test
    fun `should return favorite warehouses when favorite warehouse ids are provided`() {
        val userLatitude = 35.96420463614639
        val userLongitude = 139.5397469178333
        val favoriteWarehouseIds = listOf(1, 2)

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/warehouses/search")
                .param("user_latitude", userLatitude.toString())
                .param("user_longitude", userLongitude.toString())
                .param("favorite_warehouse_ids", favoriteWarehouseIds.joinToString(","))
        )

        result.andExpect(status().isOk)
        result.andExpect(content().json("""
            {
              "is_invading": false,
              "warehouses": [],
              "favorite_warehouse_areas": [
                {
                  "warehouse_id": 2,
                  "warehouse_area_id": 1,
                  "warehouse_name": "北海道倉庫2",
                  "average_delay_state": "normal",
                  "delay_time_detail": [
                    {
                      "delay_state": "normal",
                      "answer_count": 0
                    },
                    {
                      "delay_state": "pause",
                      "answer_count": 0
                    },
                    {
                      "delay_state": "halfHour",
                      "answer_count": 0
                    },
                    {
                      "delay_state": "anHour",
                      "answer_count": 0
                    },
                    {
                      "delay_state": "impossible",
                      "answer_count": 0
                    }
                  ],
                  "distance": 802.38
                },
                {
                  "warehouse_id": 1,
                  "warehouse_area_id": 1,
                  "warehouse_name": "北海道倉庫1",
                  "average_delay_state": "halfHour",
                  "delay_time_detail": [
                    {
                      "delay_state": "normal",
                      "answer_count": 2
                    },
                    {
                      "delay_state": "pause",
                      "answer_count": 1
                    },
                    {
                      "delay_state": "halfHour",
                      "answer_count": 0
                    },
                    {
                      "delay_state": "anHour",
                      "answer_count": 0
                    },
                    {
                      "delay_state": "impossible",
                      "answer_count": 1
                    }
                  ],
                  "distance": 804.4
                }
              ],
              "warehouse_areas": [
                {
                  "warehouse_area_id": 12,
                  "warehouse_area_name": "埼玉県エリア",
                  "average_delay_state": "normal",
                  "distance": 13.89
                },
                {
                  "warehouse_area_id": 11,
                  "warehouse_area_name": "東京都エリア",
                  "average_delay_state": "normal",
                  "distance": 44.08
                },
                {
                  "warehouse_area_id": 10,
                  "warehouse_area_name": "千葉県エリア",
                  "average_delay_state": "normal",
                  "distance": 47.88
                }
              ]
            }
        """.trimIndent()))
    }

    @Test
    fun `should return 400 when userLatitude is not provided`() {
        val userLongitude = 139.73043579786187

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/warehouses/search")
                .param("user_longitude", userLongitude.toString())
        )

        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when userLongitude is not provided`() {
        val userLatitude = 35.66270523445052

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/warehouses/search")
                .param("user_latitude", userLatitude.toString())
        )

        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when userLatitude and userLongitude are not provided`() {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/warehouses/search")
        )

        result.andExpect(status().isBadRequest)
    }
}