package com.stathis.moviepedia.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.stathis.moviepedia.MovieInfoScreen

import com.stathis.moviepedia.R
import com.stathis.moviepedia.models.FavoriteMovies
import com.stathis.moviepedia.models.FavoriteTvSeries
import com.stathis.moviepedia.models.Movies
import com.stathis.moviepedia.recyclerviews.FavoriteClickListener
import com.stathis.moviepedia.recyclerviews.FavoriteMoviesAdapter
import com.stathis.moviepedia.recyclerviews.MoviesAdapter
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_movie_info_screen.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import java.io.ByteArrayOutputStream

class UserProfileFragment : Fragment(), FavoriteClickListener {

    private val REQUEST_IMAGE_CAPTURE = 100
    private lateinit var imageUri: Uri
    private lateinit var userPhoto:CircleImageView
    private lateinit var storage:  FirebaseStorage
    private lateinit var databaseReference: DatabaseReference
    private var userFavorites:MutableList<FavoriteMovies> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserFavorites()

        userPhoto = view.findViewById(R.id.profileImg)
        retrieveUserImg()

        userPhoto.setOnClickListener{
            takePictureIntent()
        }
    }

    private fun getUserFavorites(){
        //adds a new favorite to the favorite movie list
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .child("favoriteMovieList")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (i in p0.children) {
                            val fav = i.getValue(FavoriteMovies::class.java)
                            userFavorites.add(fav!!)
                            Log.d("i",i.toString())

                            val favMoviesRecView: RecyclerView = view!!.findViewById(R.id.movies_favRecView)
                            favMoviesRecView.adapter = FavoriteMoviesAdapter(userFavorites,this@UserProfileFragment)
                            val favoriteCounter:TextView = view!!.findViewById(R.id.favorites)
                            val size = userFavorites.size
                            favoriteCounter.text = "$size Favorites"
                        }
                    }
                }
            })
    }

    private fun retrieveUserImg(){
        storage = FirebaseStorage.getInstance()
        var imageRef:StorageReference = storage.reference.child("pics/${FirebaseAuth.getInstance().currentUser?.uid}")
        imageRef.getBytes(1024*1024).addOnSuccessListener {bytes ->
            var userPhoto:CircleImageView = view!!.findViewById(R.id.profileImg)
            val bitmap:Bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
            userPhoto.setImageBitmap(bitmap)

        }.addOnFailureListener {
            // Handle any errors
        }
        imageRef.downloadUrl.addOnSuccessListener {downloadUrl ->
            Log.d("DownloadUrl", downloadUrl.toString())
        }.addOnFailureListener {it->
            Log.d("DownloadUrl", it.toString())
        }

    }

    private fun takePictureIntent(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{ pictureIntent ->
            pictureIntent.resolveActivity(activity?.packageManager!!)?.also{
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK){
            val imgBitmap = data?.extras?.get("data") as Bitmap
            uploadAndSaveBitmapUri(imgBitmap)
        }
    }

    private fun uploadAndSaveBitmapUri(bitmap: Bitmap){
        val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance()
            .reference.child("pics/${FirebaseAuth.getInstance().currentUser?.uid}")
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val image = baos.toByteArray()
        val upload = storageRef.putBytes(image)
        upload.addOnCompleteListener{uploadTask ->
            if(uploadTask.isSuccessful){
                storageRef.downloadUrl.addOnCompleteListener{ urlTask ->
                    urlTask?.result?.let{
                        imageUri = it
                        profileImg.setImageBitmap(bitmap)
                    }
                }
            } else {
                uploadTask.exception?.let{
                    //
                }
            }
        }
    }

    override fun onFavoriteMoviesClick(favoriteMovies: FavoriteMovies) {
        val movieIntent = Intent (activity, MovieInfoScreen::class.java)
        //converting rating toString() so I can pass it. Double was throwing an error
        val rating = favoriteMovies.movie_rating.toString()
        movieIntent.putExtra("MOVIE_NAME", favoriteMovies.title)
        movieIntent.putExtra("MOVIE_PHOTO",favoriteMovies.photo)
        movieIntent.putExtra("RELEASE_DATE",favoriteMovies.releaseDate)
        movieIntent.putExtra("DESCRIPTION",favoriteMovies.description)
        movieIntent.putExtra("RATING",rating)
        startActivity(movieIntent)
    }

    override fun onFavoriteTvSeriesClick(favoriteTvSeries: FavoriteTvSeries) {
        TODO("Not yet implemented")
    }

}
