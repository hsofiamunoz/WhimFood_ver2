package com.hsofiamunoz.whimfood_ver2.ui.createProduct

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.hsofiamunoz.whimfood_ver2.R
import com.hsofiamunoz.whimfood_ver2.data.Product
import com.hsofiamunoz.whimfood_ver2.data.ProfileInfo
import com.hsofiamunoz.whimfood_ver2.data.UserProfile
import com.hsofiamunoz.whimfood_ver2.databinding.CreateProductFragmentBinding
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
private var const=100
class createProductFragment : Fragment() {

    companion object {
        fun newInstance() = createProductFragment()
    }

    private lateinit var viewModel: CreateProductViewModel

    private var _binding: CreateProductFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Variable para subir la imagen
    private var urlImage :String ?=null
    private val REQUEST_IMAGE_CAPTURE = 1000
    private lateinit var auth: FirebaseAuth;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this).get(CreateProductViewModel::class.java)

        _binding = CreateProductFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.text.observe(viewLifecycleOwner, Observer {

        })



        // Procedimiento

        with(binding) {
                    takePictrureImageView.setOnClickListener {
                        dispatchTakePictureIntent()
                    }
            addProductButton.setOnClickListener {

                savePicture()

                    }
                }

                return root
            }


    private fun savePicture() {

        val storage = FirebaseStorage.getInstance()
        const= const+1
        val pictureRef = storage.reference.child("product" + const.toString())

        binding.takePictrureImageView.isDrawingCacheEnabled = true
        binding.takePictrureImageView.buildDrawingCache()

        val bitmap = (binding.takePictrureImageView.drawable as BitmapDrawable).bitmap
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
                saveProduct(downloadUri.toString())
            } else {
                Toast.makeText(requireContext(),"Debe ingresar foto",Toast.LENGTH_SHORT)
                // Handle failures
                // ...
            }
        }




    }

    private fun saveProduct(urlPicture: String) {

        val name_product = binding.nameProductInputText.text.toString()
        val location_product = binding.locationProductInputText.text.toString()
        val description_product = binding.descrptionProductInputText.text.toString()
        val price_product = binding.priceProductInputText.text.toString()

        if(name_product.isNullOrEmpty() || location_product.isNullOrEmpty() || description_product.isNullOrEmpty() || price_product.isNullOrEmpty()){
            Toast.makeText(requireContext(),"Debe ingresar parametros",Toast.LENGTH_SHORT)
        }
        else {
            val price_product = binding.priceProductInputText.text.toString().toLong()
            var propietario = ""
            var propietario_url = ""
            val current_user = Firebase.auth.currentUser
            var id_usuario = ""
            var propietario_id = ""
            current_user?.let {
                id_usuario = current_user.uid.toString()
                Log.d("id del user", id_usuario)
            }

            val db = Firebase.firestore
            //Acceder a la BD --------------------------------------------------------------------------
            db.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        var persona1: UserProfile = document.toObject<UserProfile>()
                        if (document.id == id_usuario) {
                            propietario_id = id_usuario
                            propietario = persona1.name.toString()
                            propietario_url = persona1.urlPicture.toString()
                            Log.d("name", document.data.get("name") as String)

                        }
                    }

                    //Crear un producto de comida
                    val document = db.collection("product").document()
                    val id = document.id
                    val product1 = Product(
                        id,
                        name_product,
                        location_product,
                        description_product,
                        price_product,
                        urlPicture,
                        propietario,
                        propietario_url,
                        propietario_id
                    )
                    db.collection("product").document(id).set(product1)

                    Toast.makeText(requireContext(), "Producto añadido", Toast.LENGTH_SHORT).show()

                    val perfil = ProfileInfo(
                        url_product_pic = urlPicture,
                        id = propietario_id,
                        propietario_id = propietario_id
                    )
                    val document2 = db.collection("PerfilesIndiv").document()
                    val id2 = document2.id
                    db.collection("PerfilesIndiv").document(id2).set(perfil)

                    binding.nameProductInputText.setText("")
                    binding.priceProductInputText.setText("")
                    binding.locationProductInputText.setText("")
                    binding.descrptionProductInputText.setText("")

                }
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        if(requestCode== REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.takePictrureImageView.setImageBitmap(imageBitmap)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CreateProductViewModel::class.java)

    }

}