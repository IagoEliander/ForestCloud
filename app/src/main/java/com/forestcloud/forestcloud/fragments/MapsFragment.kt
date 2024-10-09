package com.forestcloud.forestcloud.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.forestcloud.forestcloud.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonMultiPolygon
import com.google.maps.android.data.geojson.GeoJsonPolygon
import org.json.JSONObject
import okhttp3.*
import org.json.JSONException
import java.io.IOException
import java.util.concurrent.TimeUnit

fun fetchUrlData(url: String, callback: (JSONObject?) -> Unit) {
    val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS) // 15 segundos para conectar
        .readTimeout(60, TimeUnit.SECONDS)    // 30 segundos para ler a resposta
        .writeTimeout(60, TimeUnit.SECONDS)   // 15 segundos para enviar a requisição (opcional)
        .build()
    val request = Request.Builder().url(url).build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            // Lidar com o erro da requisição
            Log.e("ERRO Failure", e.toString())
            callback(null) // Passa null em caso de erro
        }

        override fun onResponse(call: Call, response: Response) {
            val responseData = response.body?.string() ?: "Resposta vazia"

            try {
                val jsonObject = JSONObject(responseData)
                callback(jsonObject)
            } catch (e: JSONException) {
                Log.e("ERRO", e.toString())
                callback(null) // Passa null se o JSON for inválido
            }
        }
    })
}

class MapsFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->
        fetchUrlData("https://10.0.0.101:8080/api/random-data") { responseData ->
            Log.d("URL", responseData.toString())


            /**
             * Manipulates the map once available.
             * This callback is triggered when the map is ready to be used.
             * This is where we can add markers or lines, add listeners or move the camera.
             * In this case, we just add a marker near Sydney, Australia.
             * If Google Play services is not installed on the device, the user will be prompted to
             * install it inside the SupportMapFragment. This method will only be triggered once the
             * user has installed Google Play services and returned to the app.
             */
            requireActivity().runOnUiThread {
                var geoJsonData: JSONObject? = responseData
                var layer: GeoJsonLayer? = GeoJsonLayer(googleMap, geoJsonData)
                if (layer != null) {
                    val f = layer.features.first()
                    Log.d("Layer", f.geometry.geometryObject.toString())
                    /*
                    val coordinates = when (f.geometry.geometryType) {
                        "Polygon" -> (f.geometry.geometryObject as List<List<GeoJsonPolygon>>)[0][0].coordinates[0]
                        "MultiPolygon" -> (f.geometry.geometryObject as List<GeoJsonPolygon>)[0].coordinates[0]
                        else -> throw IllegalArgumentException("Unsupported geometry type: ${f.geometry.geometryType}")
                    }*/
                    val coordinates = when (f.geometry.geometryType) {
                        "Polygon" -> {
                            val polygon = (f.geometry.geometryObject as List<List<LatLng>>)[0]// Access LatLng directly
                            polygon
                        }
                        "MultiPolygon" -> {
                            (f.geometry.geometryObject as List<GeoJsonPolygon>)
                                .flatMap { it.coordinates } // Flatten the rings from all polygons
                                .first() // Get the first ring's coordinates from the first polygon
                        }
                        else -> throw IllegalArgumentException("Unsupported geometry type: ${f.geometry.geometryType}")
                    }

                    var latitudeSum = 0.0
                    var longitudeSum = 0.0
                    for (point in coordinates) {
                        latitudeSum += point.latitude
                        longitudeSum += point.longitude // Corrected line
                    }

                    val centerLat = latitudeSum / coordinates.size
                    val centerLng = longitudeSum / coordinates.size

                    val center = LatLng(centerLat, centerLng)
                    // googleMap.addMarker(MarkerOptions().position(center).title("ifmt"))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 17.0F))
                    val pStyle = layer.defaultPolygonStyle
                    pStyle.fillColor = Color.GREEN
                    pStyle.strokeColor = Color.RED
                }

                val sydney = LatLng(-34.0, 151.0)




                googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                if (layer != null) {
                    layer.addLayerToMap()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }
}