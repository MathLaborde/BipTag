package br.com.biptag.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.biptag.factory.RetrofitClient
import br.com.biptag.model.Inventory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun getAllInventories(): List<Inventory>{

    val apiKey = "sb_publishable_-uStjsesRzkPg7vwDmha9g_F17ohYyw"
    val token = "Bearer ${apiKey}"


    var inventories by remember {
        mutableStateOf(listOf<Inventory>())
    }

    val callInventories = RetrofitClient.getInventoryService().getAllInventories(apiKey,token)

    callInventories.enqueue(object : Callback<List<Inventory>> {
        override fun onResponse(
            call: Call<List<Inventory>?>,
            response: Response<List<Inventory>?>
        ) {
            inventories = response.body() ?: emptyList()
        }

        override fun onFailure(
            p0: Call<List<Inventory>?>,
            p1: Throwable
        ) {
            println(p1.printStackTrace())
        }

    })

    return inventories
}