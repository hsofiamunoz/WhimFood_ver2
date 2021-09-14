package com.hsofiamunoz.whimfood_ver2.ui.Profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.icu.text.BidiRun
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.hsofiamunoz.whimfood_ver2.data.profileServer.profileServer
import com.hsofiamunoz.whimfood_ver2.databinding.FragmentProfileBinding
import java.io.ByteArrayOutputStream

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Variable para subir la imagen
    private var urlImage :String ?=null
    private val REQUEST_IMAGE_CAPTURE = 1000
    var idProfile :String ?= ""



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        profileViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })

        //---------------------------------------------------------------------------
        // Procedimiento
        var flag_cambio = true
        with(binding){
            editButton.setOnClickListener{

                if(flag_cambio){
                    editButton.setText("Guardar")
                    flag_cambio = false
                    takePictureProfileImageView.setOnClickListener{
                        dispatchTakePictureIntent()
                    }
                }
                else{
                    savePicture()
                    editButton.setText("Editar")
                    flag_cambio = true
                    cleanViews()
                }
            }

        }


        return root
    }

    private fun cleanViews() {
        binding.descriptionInputText.setText("")
        binding.nameInputText.setText("")
        binding.adressInputText.setText("")
    }

    private fun savePicture() {
        val storage = FirebaseStorage.getInstance()
        val pictureRef = storage.reference.child("Foto Perfil")

        binding.takePictureProfileImageView.isDrawingCacheEnabled = true
        binding.takePictureProfileImageView.buildDrawingCache()

        val bitmap = (binding.takePictureProfileImageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        // data es la foto comprimida para subirla a la nube
        val data = baos.toByteArray()

        val uploadTask = pictureRef.putBytes(data)

        val urlTask = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            pictureRef.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                // Se guardan los cambios
                saveProfileChanges(downloadUri.toString())
            } else {
                // Handle failures
                // ...
            }
        }
    }


    private fun saveProfileChanges(downloadUri: String) {
        with(binding){
            val name =  nameInputText.text.toString()
            val descripcion = descriptionInputText.text.toString()
            val adress =  adressInputText.text.toString()

            val db = Firebase.firestore

            val adressRef = db.collection("users").document("UQI9lEEAUBQGsrttwWG1CjhAS2E3")
            adressRef.update("adress","adress").addOnSuccessListener { Log.d("Actualizado","exito") }
                .addOnFailureListener { e -> Log.w("Actualizado", "Error updating document", e) }

            val descriptionRef = db.collection("users").document("UQI9lEEAUBQGsrttwWG1CjhAS2E3")
            adressRef.update("description",descripcion).addOnSuccessListener { Log.d("Actualizado","exito") }
                .addOnFailureListener { e -> Log.w("Actualizado", "Error updating document", e) }

            val url_pictureRef = db.collection("users").document("UQI9lEEAUBQGsrttwWG1CjhAS2E3")
            adressRef.update("url_picture",downloadUri).addOnSuccessListener { Log.d("Actualizado","exito") }
                .addOnFailureListener { e -> Log.w("Actualizado", "Error updating document", e) }
        }

    }

    private fun createProfileInfo(name: String, descripcion: String, adress: String, downloadUri: String) {
        val db = Firebase.firestore
        var userExist = true
        db.collection("users").get().addOnSuccessListener { result ->
            Log.d("antes For", "name.toString()")
            for(document in result){
                val user: profileServer = document.toObject<profileServer>()
                if (user.name == name){
                    idProfile = user.id
                    userExist = true
                }
                else
                    Log.d("Register", "name.toString()")
            }
            if(!userExist)
                Toast.makeText(requireContext(),"No existe",Toast.LENGTH_SHORT).show()
        }

        val documentUpdate = HashMap<String,Any>()
        documentUpdate["name"] =  name
        documentUpdate["url_picture"] = downloadUri
        documentUpdate["adress"] = adress

        idProfile?.let { it1 -> db.collection("users").document(it1).update(documentUpdate).addOnSuccessListener {
            Toast.makeText(requireContext(),"Perfil actualizado exitosamente",Toast.LENGTH_SHORT).show()
        } }

    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        if(requestCode== REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.takePictureProfileImageView.setImageBitmap(imageBitmap)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}