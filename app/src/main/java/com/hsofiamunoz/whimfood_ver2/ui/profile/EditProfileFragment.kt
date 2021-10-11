package com.hsofiamunoz.whimfood_ver2.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.hsofiamunoz.whimfood_ver2.data.UserProfile
import com.hsofiamunoz.whimfood_ver2.databinding.FragmentEditProfileBinding
import java.io.ByteArrayOutputStream

class EditProfileFragment : Fragment(){


    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    // Variable para subir la imagen
    private var urlImage :String ?=null
    private val REQUEST_IMAGE_CAPTURE = 1000
    private var flagFoto= false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val current_user = Firebase.auth.currentUser
        var id = ""

        // -------------------------------------------------------

        // Obtener el id y correo
        current_user?.let {
            id = current_user.uid.toString()
            Log.d("id del user",id)

        }

        var db = Firebase.firestore
        //Acceder a la BD --------------------------------------------------------------------------
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    var persona1: UserProfile = document.toObject<UserProfile>()
                    if (document.id == id) {
                        binding.nameInputText.setText(persona1.name)

                    }
                }
            }


        //tomar captura de la imagen

        with(binding){
            takePictureProfileImageView4.setOnClickListener{
                dispatchTakePictureProfileIntent()
                flagFoto=true
            }
        }


        // Al guardar los cambios
        binding.saveChangesButton.setOnClickListener {
            //Se regresa al perfil
            if(flagFoto==true){
                savePicture()
                saveDatos()
            }
            else{
                saveDatos()
            }

        }
        return root
    }

    private fun saveDatos(){
        var newName = binding.nameInputText.text.toString()
        var newLocation= binding.descriptionInputText.text.toString()
        val db = Firebase.firestore
        val current_user= Firebase.auth.currentUser
        var id= ""
        current_user?.let {
            id = current_user.uid.toString()
            //Log.d("id del user",id)
        }

        db.collection("users").get().addOnSuccessListener { result ->
            var usersExist = false
            for(document in result){
                val user: UserProfile = document.toObject<UserProfile>()
                    if (user.id == id) {
                        newLocation= document.data.get("location") as String
                        newName= document.data.get("name") as String
                        Log.d("curren12", newName)
                        usersExist = true
                    }
            }
            if(!usersExist)
                Toast.makeText(requireContext(),"No existe",Toast.LENGTH_SHORT).show()
        }
        //actualizar información
        val documentUpdate = HashMap<String,Any>()
        documentUpdate["location"] = newLocation
        documentUpdate["name"] =  newName

        id?.let { it1 -> db.collection("users").document(it1).update(documentUpdate).addOnSuccessListener {
            Toast.makeText(requireContext(),"Usuario actualizado exitosamente",Toast.LENGTH_SHORT).show()
        } }


        findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToNavigationHome())


    }
    private fun savePicture() {
        val db = Firebase.firestore
        val current_user= Firebase.auth.currentUser
        var id_profile_pict= ""
        current_user?.let {
            id_profile_pict = current_user.uid.toString()
            //Log.d("id del user",id)

        }

        val storage = FirebaseStorage.getInstance()
        val pictureRef = storage.reference.child(id_profile_pict.toString())


        binding.takePictureProfileImageView4.isDrawingCacheEnabled = true
        binding.takePictureProfileImageView4.buildDrawingCache()

        val bitmap = (binding.takePictureProfileImageView4.drawable as BitmapDrawable).bitmap
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
                saveChangesProfile(downloadUri.toString())
            } else {
            }
        }
    }

    private fun saveChangesProfile(urlPicture: String) {
        val newName = binding.nameInputText.text.toString()
        val db = Firebase.firestore
        val current_user= Firebase.auth.currentUser
        var id= ""
        current_user?.let {
            id = current_user.uid.toString()
            //Log.d("id del user",id)

        }

        db.collection("users").get().addOnSuccessListener { result ->
            var usersExist = false
            for(document in result){
                val user: UserProfile = document.toObject<UserProfile>()
                if (user.name == document.data.get("name") as String){
                    id = user.id.toString()
                    usersExist = true
                }
            }
            Log.d("idUser", id)
            if(!usersExist)
                Toast.makeText(requireContext(),"No existe",Toast.LENGTH_SHORT).show()
        }
        //actualizar información
        val documentUpdate = HashMap<String,Any>()
        documentUpdate["urlPicture"] = urlPicture



        id?.let { it1 -> db.collection("users").document(it1).update(documentUpdate).addOnSuccessListener {
            Toast.makeText(requireContext(),"Usuario actualizado exitosamente",Toast.LENGTH_SHORT).show()
        } }



    }

    private fun dispatchTakePictureProfileIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        if(requestCode== REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.takePictureProfileImageView4.setImageBitmap(imageBitmap)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(CreateProductViewModel::class.java)

    }

}