package lk.nibm.HDSE222F006

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner: Spinner = findViewById(R.id.spn_spinner)
        val button : Button = findViewById(R.id.btn_Generate)
        val image1: ImageView = findViewById(R.id.img_Imgview)
        val data = arrayOf("Cat","Dog")
        val dogApi="https://random.dog/woof.json"
        val catApi="https://api.thecatapi.com/v1/images/search"
        var selectedItem="";
        var catData="";

        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,data)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner.adapter=adapter

        spinner.onItemSelectedListener=object: AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedItem=data[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//                TODO("Not yet implemented")
            }


        }

        fun addimage(value:String)
        {
            val imageUrl = value

            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)

            Glide.with(this)
                .load(imageUrl)
                .apply(requestOptions)
                .into(image1)
        }


        fun retriveData()
        {
            if(selectedItem=="Cat")
            {
                val request = JsonArrayRequest(Request.Method.GET, catApi, null,
                    Response.Listener { response ->
                        if (response.length() > 0) {
                            val catJsonObject = response.getJSONObject(0)
                            val catImage = catJsonObject.getString("url")
                            addimage(catImage)
                        }
                    },
                    Response.ErrorListener { error ->
                        // Handle the error
                    }
                )
                Volley.newRequestQueue(this).add(request)

            }
            else if(selectedItem=="Dog")
            {
                val request = JsonObjectRequest(Request.Method.GET, dogApi, null,
                    Response.Listener<JSONObject> { response ->
                        val dogImage=response.getString("url")
                        addimage(dogImage)

                    }

                    , Response.ErrorListener { error ->
                        Log.e("Response", error.toString())
                    })

                Volley.newRequestQueue(this).add(request)
            }
        }

        button.setOnClickListener()
        {
            retriveData();
        }

    }
}