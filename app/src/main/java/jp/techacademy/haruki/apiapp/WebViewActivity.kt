package jp.techacademy.haruki.apiapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import jp.techacademy.haruki.apiapp.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?,) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webView.loadUrl(intent.getStringExtra(KEY_URL).toString())
       //追加
        binding.fav.setOnClickListener {

            val id = intent.getStringExtra(KEY_ID).toString()
            val name = intent.getStringExtra(KEY_NAME).toString()
            val imageUrl = intent.getStringExtra(KEY_IMAGE_URL).toString()
            val url = intent.getStringExtra(KEY_URL).toString()

            // お気に入り情報を追加または削除
            if (isFavorite(id)) {
                deleteFavorite(id)
                binding.fav.apply {setImageResource( R.drawable.ic_star_border)}
            } else {
                addFavorite(id, name, imageUrl, url)
                binding.fav.apply {setImageResource( R.drawable.ic_star)}
            }
        }
    }

    private fun isFavorite(id: String): Boolean {
        return FavoriteShop.findBy(id) != null
    }
    private fun addFavorite(id: String, name: String, imageUrl: String, url: String) {
        FavoriteShop.insert(FavoriteShop().apply {
            this.id = id
            this.name = name
            this.imageUrl = imageUrl
            this.url = url
        })
    }
    private fun deleteFavorite(id: String) {
        showConfirmDeleteFavoriteDialog(id)
    }
    private fun showConfirmDeleteFavoriteDialog(id: String) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_favorite_dialog_title)
            .setMessage(R.string.delete_favorite_dialog_message)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                FavoriteShop.delete(id)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .create()
            .show()
    }

    companion object {
        private const val KEY_URL = "key_url"
        private const val KEY_ID = "key_id"
        private const val KEY_IMAGE_URL = "key_image_url"
        private const val KEY_NAME = "key_name"

        fun start(activity: Activity, url: String, id: String = "", imageUrl: String = "", name: String = "") {
            val intent = Intent(activity, WebViewActivity::class.java)
            intent.putExtra(KEY_URL, url)
            intent.putExtra(KEY_ID, id)
            intent.putExtra(KEY_IMAGE_URL, imageUrl)
            intent.putExtra(KEY_NAME, name)
            activity.startActivity(intent)
        }
    }
}