package com.example.new_firebase

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.lang.Exception
import java.net.URI

class MainActivity : AppCompatActivity() {

    val IMAGE : Int = 0
    val VIDEO : Int = 1
    lateinit var uri : Uri
    lateinit var mStorage : StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageBtn = findViewById<View>(R.id.photoBtn) as Button
        val videoBtn = findViewById<View>(R.id.videoBtn) as Button

        mStorage = FirebaseStorage.getInstance().getReference("Uploads")


        imageBtn.setOnClickListener(View.OnClickListener{
                view : View? -> val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent,"Select IMAGE"), IMAGE)
        })
        videoBtn.setOnClickListener(View.OnClickListener{
                view : View? -> val intent = Intent()
            intent.setType("video/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent,"Select VIDEO"), VIDEO)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val uriTxt = findViewById<View>(R.id.uriTxt) as TextView
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE){
                uri = data?.data!!
                uriTxt.text = uri.toString()
                upload()
            }else if(requestCode == VIDEO){
                uri = data?.data!!
                uriTxt.text = uri.toString()
                upload()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun upload(){
        var mReference = uri.lastPathSegment?.let { mStorage.child(it) }
        try{
            if (mReference != null) {
                mReference.putFile(uri).addOnSuccessListener{
                        taskSnapshot : UploadTask.TaskSnapshot? -> var url = taskSnapshot!!.storage.downloadUrl
                    val dwnTxt = findViewById<View>(R.id.dwnTxt) as TextView
                    dwnTxt.text = url.toString()
                    Toast.makeText(this, "Successfully Uploaded :)", Toast.LENGTH_LONG).show()
                }
            }
        }catch(e : Exception){
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
        }
    }
}