package it.marta.whosings.data.vo

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import it.marta.whosings.R
import org.koin.java.KoinJavaComponent


data class ResponseWrapper<out T : Any>(
    @SerializedName("message")
    val message: Message<T>?
)

data class Message<out T : Any>(
    @SerializedName("body")
    val bodyAny: JsonElement?,
    @SerializedName("header")
    val header: Header?
) {
    val body: T?
        get() = runCatching {
            val gson = KoinJavaComponent.getKoin().get<Gson>()
            gson.fromJson(bodyAny as? JsonObject, object : TypeToken<T?>() {}.type) as T
        }.getOrNull()

    val bodyVoid: List<Any>?
        get() = runCatching {
            val gson = KoinJavaComponent.getKoin().get<Gson>()
            gson.fromJson<List<Any>>(
                bodyAny as? JsonArray,
                object : TypeToken<List<Any?>?>() {}.type
            )
        }.getOrNull()
}

data class Header(
    @SerializedName("execute_time")
    val executeTime: Double?,
    @SerializedName("status_code")
    val statusCode: Int?
) {
    private fun getContext() = KoinJavaComponent.getKoin().get<Context>()

    fun errorText() = when (handleException(statusCode)) {
        is BadSyntax -> getContext().getString(R.string.bad_syntax)
        is AuthenticationFailed -> getContext().getString(R.string.auth_failed)
        is UsageLimitReached -> getContext().getString(R.string.limit_reached)
        is NotAuthorized -> getContext().getString(R.string.not_auth)
        is ResourceNotFound -> getContext().getString(R.string.resource_not_found)
        is MethodNotFound -> getContext().getString(R.string.method_not_found)
        is SomethingWrong -> getContext().getString(R.string.something_wrong)
        is SystemBusy -> getContext().getString(R.string.system_busy)
        else -> null
    }

}


internal fun handleException(statusCode: Int?): Exception? {

    return when (statusCode) {
        200 -> null
        400 -> BadSyntax()
        401 -> AuthenticationFailed()
        402 -> UsageLimitReached()
        403 -> NotAuthorized()
        404 -> ResourceNotFound()
        405 -> MethodNotFound()
        500 -> SomethingWrong()
        503 -> SystemBusy()
        else -> SomethingWrong()
    }
}