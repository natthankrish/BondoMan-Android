import com.google.gson.annotations.SerializedName

data class UploadResponse(
    @SerializedName("items")
    val items: ItemsWrapper
)

data class ItemsWrapper(
    @SerializedName("items")
    val items: List<Item>
)

data class Item(
    @SerializedName("name")
    val name: String,

    @SerializedName("qty")
    val qty: Int,

    @SerializedName("price")
    val price: Float
)
