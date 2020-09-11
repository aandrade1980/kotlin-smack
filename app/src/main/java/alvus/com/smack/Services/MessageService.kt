package alvus.com.smack.Services

import alvus.com.smack.Controller.App
import alvus.com.smack.Model.Channel
import alvus.com.smack.Model.Message
import alvus.com.smack.Utilities.URL_GET_CHANNELS
import alvus.com.smack.Utilities.URL_GET_MESSAGES
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONException

object MessageService {

    val channels = ArrayList<Channel>()
    val messages = ArrayList<Message>()

    fun getChannels(complete: (Boolean) -> Unit) {
        val channelsRequest = object : JsonArrayRequest(Method.GET, URL_GET_CHANNELS, null, Response.Listener { response ->
            try {
                for (x in 0 until response.length()) {
                    val channel = response.getJSONObject(x)
                    val name = channel.getString("name")
                    val desc = channel.getString("description")
                    val id = channel.getString("_id")

                    this.channels.add(Channel(name, desc, id))
                }
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC: " + e.localizedMessage)
                complete(false)
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not retrieve channels: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=urf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["authorization"] = "Bearer ${App.prefs.authToken}"

                return headers
            }
        }
        App.prefs.requestQueue.add(channelsRequest)
    }

    fun getMessages(channelId: String, complete: (Boolean) -> Unit) {
        val url = "$URL_GET_MESSAGES/$channelId"

        val messagesRequest = object: JsonArrayRequest(Method.GET, url, null, Response.Listener { response ->
            this.clearMessages()
            try {
                for (x in 0 until response.length()) {
                    val message = response.getJSONObject(x)
                    val messageBody = message.getString("messageBody")
                    val channelId = message.getString("channelId")
                    val id = message.getString("_id")
                    val userName = message.getString("userName")
                    val userAvatar = message.getString("userAvatar")
                    val userAvatarColor = message.getString("userAvatarColor")
                    val timeStamp = message.getString("timeStamp")

                    val newMessage = Message(messageBody, userName, channelId, userAvatar, userAvatarColor, id, timeStamp)
                    this.messages.add(newMessage)
                }
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC: " + e.localizedMessage)
                complete(false)
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not retrieve channels: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=urf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["authorization"] = "Bearer ${App.prefs.authToken}"

                return headers
            }
        }
        App.prefs.requestQueue.add(messagesRequest)
    }

    fun clearMessages() {
        this.messages.clear()
    }

    fun clearChannels() {
        this.channels.clear()
    }
}